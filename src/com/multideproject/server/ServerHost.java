package com.multideproject.server;

public class ServerHost {
	
	private static Server[] servers = new Server[4];
	
	public static void main(String[] args) {
		createServer(6225);
	}
	
	public static boolean createServer(int port) {
		for(int i = 0; i < servers.length; i++) {
			if (servers[i] == null) {
				servers[i] = new Server(i, port, "/ServerData");
				return true;
			}
		}
		System.out.println("Unable to start server");
		return false;
	}

	public static Server[] getServers() {
		return servers;
	}
}
