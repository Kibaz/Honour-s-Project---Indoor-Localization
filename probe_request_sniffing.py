# Importing required python libraries
from scapy.all import * # Scapy library for Network WiFi Sniffing

'''
    For this script to work, the specified iface for packet sniffing
    must support "Monitor Mode" and be configured in this mode.

    From the Raspberry Pi, ensure that the WiFi Adapter is plugged
    in and operating in Monitor Mode.

    iface = wlan1
'''


# Constants
PROBE_REQUEST_TYPE = 0
ACCESS_POINT_TYPE = 8
PACKET_REQUEST_SUBTYPE = 4
PROBE_RESPONSE = 5

# List of access points on WLAN network - List by MAC addresses
access_points = []



def sniff_probe_requests(packet):
    if packet.haslayer(Dot11):
        if packet.type == PROBE_REQUEST_TYPE and packet.subtype == PACKET_REQUEST_SUBTYPE:
            sender_MAC = packet.addr2
            SSID = packet.info
            print("Client with MAC: %s probing for SSID %s"
                  % (sender_MAC, SSID))
            signal_str = -(256 - ord(packet.notdecoded[-4:-3]))
            print("Signal Strength %s" %(signal_str))

"""
For monitoring probe responses
This can be useful in identifying
the access points receiving particular
requests
"""
def sniff_probe_responses(packet):
    if packet.haslayer(Dot11):
        if packet.type == PROBE_REQUEST_TYPE and packet.subtype == PROBE_RESPONSE:
            SSID = packet.info
            ap_MAC = packet.addr2 # Addr2 is Sender address, also stored in Addr3 as AP MAC
            device_MAC = packet.addr1 # MAC address of original probing device
            signal_str = -(256 - ord(packet.notdecoded[-4:-3])) # Retrieve signal strength
            print("Access Point with MAC: %s responding to probe request for SSIS %s. Request sent from MAC: %s"
                  %(ap_MAC,SSID,device_MAC))
            print("Signal Strength: %s" %(signal_str))
def main():
    sniff(iface="wlan1", prn=sniff_probe_requests)

if __name__ == '__main__':
    main()
