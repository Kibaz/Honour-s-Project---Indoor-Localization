package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class LocalisationSystem {
	
	private static final int SSH_PORT = 22;
	
	private List<Session> sshSessions;
	private List<Channel> channels;
	
	public LocalisationSystem()
	{
		sshSessions = new ArrayList<>();
		channels = new ArrayList<>();
	}
	
	public void startSessions()
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter remote user:");
		String user = input.nextLine();
		
		System.out.println("Please enter remote password:");
		String password = input.nextLine();
		
		input.close();
		
		// Store array of monitor devices' IP Addresses
		String[] hosts = new String[] {"192.168.1.4", "192.168.1.9", "192.168.1.15"};
		
		// For each host - connect via SSH and execute python script
		for(int i = 0; i < hosts.length; i++)
		{
			
			// Configure session configuration for SSH session
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			
			// Using JSch Package to open SSH sessions
			JSch jsch = new JSch();
			try {
				// Initialise session
				Session session = jsch.getSession(user,hosts[i],SSH_PORT);
				sshSessions.add(session);
				// Set password for remote user
				session.setPassword(password);
				// Set session config
				session.setConfig(config);
				// Connect to SSH session
				session.connect();
				
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startScripts()
	{
		// Linux command to run the python sniffing script on the RPis
		String command = "sudo python3 Desktop/probe_request_sniffing.py";
		
		for(Session session: sshSessions)
		{
			// Issue command to remote host
			Channel channel;
			try {
				channel = session.openChannel("exec"); // Allow for excution of commands
				channels.add(channel);
				((ChannelExec) channel).setCommand(command);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(null);
				
				channel.connect();
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public void stop()
	{
		for(Channel channel: channels)
		{
			channel.disconnect();
		}
		
		for(Session session: sshSessions)
		{
			session.disconnect();
		}
	}

}
