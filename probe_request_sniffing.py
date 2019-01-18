# Importing required python libraries
from scapy.all import * # Scapy library for Network WiFi Sniffing
import csv # for writing the captured data to a csv file
import os # checking status of files/directories
import subprocess # for configuring monitor mode
import threading # allow operations to run concurrently
import time # Managing threads

# For identifying the underlying host's MAC Address
from uuid import getnode as mac_address
# Converting 48 bit integer into MAC Address format
import re

import socket # for transmitting data to the localisation server
from socket import * # Required for catching timeouts/exceptions

'''
    For this script to work, the specified iface for packet sniffing
    must support "Monitor Mode" and be configured in this mode.

    From the Raspberry Pi, ensure that the WiFi Adapter is plugged
    in and operating in Monitor Mode.

    iface = wlan1
'''


# Constants
PROBE_REQUEST_TYPE = 0
PACKET_REQUEST_SUBTYPE = 4
SERVER_ADDRESS = "192.168.9.23" # IP Address of localisation server - subject to change upon location
SERVER_PORT = 8128 # Configured port for localisation server

MAC_ADDRESS = (':'.join(re.findall('..', '%012x' % mac_address()))) # Extract MAC address from underlying host

# Exclude the MAC addresses of all Raspberry PIs used in monitoring system
EXCLUSIONS = ['b8:27:eb:35:2b:60','5c:aa:fd:16:d2:6a',
              'b8:27:eb:f7:b8:27','b8:27:eb:01:d1:9d']

class Sniffer(threading.Thread):
    def __init__(self,interface='wlan1'):
        super().__init__()

        self.interface = interface

    def run(self):
        global packet_data
        sniff(iface=self.interface, prn=self.sniff_probes)

    # Method to be invoked in sniff() method available from scapy library
    # Scan packets across a network interface, retrieve required data
    # Scan for RSSI, SSID, MAC Addresses etc...
    def sniff_probes(self,packet):
        if packet.haslayer(Dot11):
            # sniffing for probe requests
            if packet.type == PROBE_REQUEST_TYPE and packet.subtype == PACKET_REQUEST_SUBTYPE:
                sender_MAC = packet.addr2 # MAC address of device sendig probes
                SSID = packet.info # SSID of network
                signal_str = -(256 - ord(packet.notdecoded[-4:-3])) # Signal strength received
                time = packet.time # time packet was received

                # Filter MAC addresses used in localisation system
                if sender_MAC not in EXCLUSIONS:
                    # Output request information to console
                    print("Client with MAC: %s probing for SSID %s at RSSI %s"
                      % (sender_MAC, SSID, signal_str))
                    # Construct message to be sent to localisation server
                    msg = ("REQUEST INFORMATION,%s,%s,%s,%s" %(sender_MAC,MAC_ADDRESS,signal_str,time)) # Message containing probe request information
                    send_message(msg)

sniffer = Sniffer()
                

# Method to carry out configuration of monitor mode
def configure_monitor_mode():
    # Check if correct interface is operating in monitor mode
    interface_conf = subprocess.Popen(['iwconfig wlan1'], stdout=subprocess.PIPE, shell=True)
    output = interface_conf.communicate()
    if not str(output).__contains__("Monitor"):
        print("Configuring monitor mode...")
        subprocess.call(['sudo', 'ifconfig', 'wlan1', 'down']) # Bring interface of WiFi adapter down
        subprocess.call(['sudo', 'iwconfig', 'wlan1', 'mode', 'monitor']) # Configure monitor mode with iwconfig
        subprocess.call(['sudo', 'ifconfig', 'wlan1', 'up']) # Bring interface of WiFi adapter back up
        print("Monitor mode configured successfully!")

# Method for sending packets of data over TCP socket
def send_message(data):
    # Create TCP Socket for sending data
    # UDP proved to not have enough reliability on sending packets
    # TCP will not allow packets to drop, however produces more overhead
    sock = socket(AF_INET, SOCK_STREAM) # Use Internet protocol
    data = bytes(data,"utf-8") # Convert string message to byte array
    sock.connect((SERVER_ADDRESS,SERVER_PORT)) # Establish connection with localisation server
    # Attempt to send message / capture timeouts if they occur
    try:
        sock.sendall(data) # Send the data in stream to server
    except timeout as e:
        print(e)
    sock.close() # Close connection / close stream
    

            
def main():
    configure_monitor_mode()
    print("Commencing probe request sniffing...")
    sniffer.start() # Start sniffing probe requests


if __name__ == '__main__':
    main()
