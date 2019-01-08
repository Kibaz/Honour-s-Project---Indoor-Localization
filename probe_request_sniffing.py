# Importing required python libraries
from scapy.all import * # Scapy library for Network WiFi Sniffing
import csv # for writing the captured data to a csv file
import os # checking status of files/directories

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

CSV_PATH = "/home/pi/Desktop/CSV_Files/"

EXCLUSIONS = ['b8:27:eb:35:2b:60']

# List of access points on WLAN network - List by MAC addresses
access_points = []

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
                print("Client with MAC: %s probing for SSID %s at RSSI %s"
                  % (sender_MAC, SSID, signal_str))
            
                # Write probe request results to a CSV file
                # Open file - if exists, append to, otherwise create new file
                if not os.path.isfile(CSV_PATH + "probe_requests.csv"):
                    with open(CSV_PATH + "probe_requests.csv", mode="w") as csv_file:
                        fields = ['sender_address', 'ssid', 'timestamp', 'rssi'] # list of attribute names
                        writer = csv.DictWriter(csv_file, fieldnames=fields) # init writer for writing to csv file

                        writer.writeheader() # construct header for csv file
                
                        # Create row data using attributes and data collected in probe requests
                        row_data = {'sender_address' : sender_MAC, 'ssid': SSID, 'timestamp': time, 'rssi': signal_str}
                        writer.writerow(row_data) # Write row to csv file
                else:
                    with open(CSV_PATH + "probe_requests.csv", mode="a") as csv_file:
                        fields = ['sender_address', 'ssid', 'timestamp', 'rssi'] # list of attribute names
                        writer = csv.DictWriter(csv_file, fieldnames=fields) # init writer for writing to csv file
                
                        # Create row data using attributes and data collected in probe requests
                        row_data = {'sender_address' : sender_MAC, 'ssid': SSID, 'timestamp': time, 'rssi': signal_str}
                        writer.writerow(row_data) # Write row to csv file
            
        # sniffing probe responses
        if packet.type == PROBE_REQUEST_TYPE and packet.subtype == PROBE_RESPONSE:
            destination_MAC = packet.addr1 # MAC address of original probing device
            ap_MAC = packet.addr3 # Addr2 is Sender address, also stored in Addr3 as AP MAC
            SSID = packet.info # SSID of network
            time = packet.time # time of response


            # Filter MAC addresses used in localisation system
            if destination_MAC not in EXCLUSIONS:
            
                # Filter by specifc SSID WLAN1
                if str(SSID) == "b'WLAN1'" or str(SSID) == "b'DTEC'":
                    print("Access Point with MAC: %s responding to Client with MAC: %s on SSID %s"
                      %(ap_MAC, destination_MAC, SSID))
                    # Write probe response results to a CSV file
                    # Open file - if exists, append to, otherwise create new file
                    if not os.path.isfile(CSV_PATH + "probe_responses.csv"):
                        with open(CSV_PATH + "probe_responses.csv", mode="w") as csv_file:
                            fields = ['ap_address', 'receiver_address', 'ssid', 'timestamp'] # list of attribute names
                            writer = csv.DictWriter(csv_file, fieldnames=fields) # init writer for writing to csv file

                            writer.writeheader() # construct header for csv file
                
                            # Create row data using attributes and data collected in probe responses
                            row_data = {'ap_address' : ap_MAC, 'receiver_address': destination_MAC , 'ssid': SSID, 'timestamp': time}
                            writer.writerow(row_data) # Write row to csv file
                    else:
                        with open(CSV_PATH + "probe_responses.csv", mode="a") as csv_file:
                            fields = ['ap_address', 'receiver_address', 'ssid', 'timestamp'] # list of attribute names
                            writer = csv.DictWriter(csv_file, fieldnames=fields) # init writer for writing to csv file
                
                            # Create row data using attributes and data collected in probe requests
                            row_data = {'ap_address' : ap_MAC, 'receiver_address': destination_MAC , 'ssid': SSID, 'timestamp': time}
                            writer.writerow(row_data) # Write row to csv file
                            
        # Uncomment for sniffing Beacon Frames emitted from Access Points
        # This section was used to test channel configurations on APs and Cisco WLC
        # Similar tests were carried out using Wireshark
        '''if packet.type == PROBE_REQUEST_TYPE and packet.subtype == ACCESS_POINT_TYPE:
            dest_MAC = packet.addr1
            ap_MAC = packet.addr2
            SSID = packet.info
            time = packet.time
            rssi = -(256 - ord(packet.notdecoded[-4:-3]))
            print("Access Point with MAC: %s responding to Client with MAC: %s on SSID %s"
                      %(ap_MAC, dest_MAC, SSID))'''

            
def main():
    sniff(iface="wlan1",prn=sniff_probes)

if __name__ == '__main__':
    main()
