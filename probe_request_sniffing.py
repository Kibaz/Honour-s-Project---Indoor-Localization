# Importing required python libraries
from scapy.all import * # Scapy library for Network WiFi Sniffing
import csv # for writing the captured data to a csv file
import os # checking status of files/directories
import subprocess # for configuring monitor mode

import socket # for transmitting data to the localisation server

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

# Exclude the MAC addresses of all Raspberry PIs used in monitoring system
EXCLUSIONS = ['b8:27:eb:35:2b:60']

# List of access points on WLAN network - List by MAC addresses
access_points = []

# Create UDP Socket for sending data
msg_SOCK = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # Use Internet protocol and UDP

# Method to be invoked in sniff() method available from scapy library
# Scan packets across a network interface, retrieve required data
# Scan for RSSI, SSID, MAC Addresses etc...
def sniff_probes(packet):
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
                
                # Forward requests to localisation server
                msg = ("REQUEST INFORMATION: sender - %s SSID - %s RSSI - %s Time - %s" %(sender_MAC,SSID,signal_str,time)) # Message containing probe request informatio
                send_packet(msg) # Send packet to localisation server
                

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

# Method for sending packets of data over UDP socket
def send_packet(data):
    data = bytes(data, "utf-8") # Convert string to bytes
    msg_SOCK.send(data)
    

            
def main():
    configure_monitor_mode()
    msg_SOCK.connect((SERVER_ADDRESS,SERVER_PORT))
    print("Commencing probe request sniffing...")
    sniff(iface="wlan1",prn=sniff_probes)
    msg_SOCK.close()

if __name__ == '__main__':
    main()
