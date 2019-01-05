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

# List responses received
probe_responses = [{}]

# List requests received
probe_requests = [{}]

# Method to be invoked in sniff() method available from scapy library
# Scan packets across a network interface, retrieve required data
# Scan for RSSI, SSID, MAC Addresses etc...
def sniff_probes(packet):
    if packet.haslayer(Dot11):
        # sniffing for probe requests
        if packet.type == PROBE_REQUEST_TYPE and packet.subtype == PACKET_REQUEST_SUBTYPE:
            sender_MAC = packet.addr2 # MAC address of device sendig probes
            SSID = packet.info # SSID of network
            print("Client with MAC: %s probing for SSID %s"
                  % (sender_MAC, SSID))
            signal_str = -(256 - ord(packet.notdecoded[-4:-3])) # Signal strength received
            print("Signal Strength %s" %(signal_str))
        # sniffing probe responses
        if packet.type == PROBE_REQUEST_TYPE and packet.subtype == PROBE_RESPONSE:
            destination_MAC = packet.addr1 # MAC address of original probing device
            ap_MAC = packet.addr2 # Addr2 is Sender address, also stored in Addr3 as AP MAC
            
def main():
    sniff(iface="wlan1",prn=sniff_probes)

if __name__ == '__main__':
    main()
