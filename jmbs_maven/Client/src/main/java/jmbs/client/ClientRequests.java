
/**
 * JMBS: Java Micro Blogging System
 *
 * Copyright (C) 2012  
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * @author Younes CHEIKH http://cyounes.com
 * @author Benjamin Babic http://bbabic.com
 * 
 */


package jmbs.client;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import jmbs.client.Graphics.SayToUser;
import jmbs.common.RemoteServer;

public class ClientRequests {
	private static RemoteServer server = null;
	private static String addressIP ;

	private static int port ;
	private static String name ;
	
	public static int maxReceivedMsgs = 30;
	public int getMaxReceivedMsgs() {
		return maxReceivedMsgs;
	}

	public void setMaxReceivedMsgs(int maxReceivedMsgs) {
		ClientRequests.maxReceivedMsgs = maxReceivedMsgs;
	}
	
	public ClientRequests() {
		if (server == null) {
			
			System.setSecurityManager(new RMISecurityManager());
			try {
				ClientRequests.addressIP = "127.0.0.1";
				ClientRequests.name = "serverjmbs";
				ClientRequests.port = 1099;
				Registry registry = LocateRegistry
						.getRegistry(addressIP,port);
				server = (RemoteServer) registry
						.lookup(ClientRequests.name);

			} catch (Exception e) {
				// Something wrong here
				new SayToUser("coucou can't connect to server", true);
			}
		}
	}
	
	public void setNewConfiguration(String ip, int port, String name) {
		ClientRequests.addressIP = ip;
		ClientRequests.port = port;
		ClientRequests.name = name;
	}
	
	public RemoteServer getConnection() {
		return ClientRequests.server;
	}
	
	public String getAddressIP() {
		return addressIP;
	}

	public void setAddressIP(String addressIP) {
		ClientRequests.addressIP = addressIP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		ClientRequests.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		ClientRequests.name = name;
	}
}