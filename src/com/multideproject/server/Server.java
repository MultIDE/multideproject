package com.multideproject.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;

public class Server implements Runnable {
	
	private int port = -1;
	private ServerSocket server;
	private Client[] clients = new Client[10];
	private final int ID;
	private Thread run, send, manageClients, connectClients;
	private boolean running = false;
	private String databasePath;
	private File[] database;
	
	public Server(int ID, int port, String databasePath) {
		this.ID = ID;
		this.port = port;
		this.databasePath = databasePath;
		run = new Thread(this, "Server");
		System.out.println("Server " + ID + " was started successfully.");
		console("Loading Database");
		loadFiles(databasePath);
		run.start();
	}
	
	private void loadFiles(String databasePath) {
		database = new File[1];
		database[0] = new File(databasePath + "/docuement.txt");
		console(database[0].getPath());
	}
	
	@Override
	public void run() {
		running = true;
		try {
			server = new ServerSocket(port);
			System.out.println("ServerSocket Successfully instantiated on " + port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to isntantiate server on specified port.");
			return;
		}
		connectClients();
		manageClients();
	}
	
	public void connectClients() {
		connectClients = new Thread("Connect") {
			public void run() {
				while (running) {
					for (int i = 0; i < clients.length; i++) {
						if (clients[i] == null) {
							try {
								clients[i] = new Client(server.accept(), i, ID);
								console("Connected new client, ID: " + i);
							} catch (IOException e) {
								e.printStackTrace();
								console("Failed to connect client to server.");
							}
						}
					}
				}
			}
		};
		connectClients.start();
	}
	public void manageInput(String input, int clientID, File file) {
		updateDatabase(file, input);
		sendToAll(input);
	}
	
	public void manageInput(String input, int clientID) {
		console("Client " + clientID + ": " + input);
		sendToAll(input);
	}
	public void updateDatabase(File file, String input) {
		database[0].setWritable(true);
		try {
			PrintWriter pw = new PrintWriter(file, "UTF-8");
			pw.write(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void manageClients() {
		manageClients = new Thread("Manage") {
			public void run() {
				while (running) {
					/*
					 * 
					 * Code for checking if clients are still connected,
					 * and if not, will remove them from client list.
					 * 
					 */
				}
			}
		};
		manageClients.start();
	}
	
	public void sendToAll(String s) {
		send = new Thread("Send") {
			public void run() {
				PrintWriter outToClient = null;
				for(int i = 0; i < clients.length; i++) {
					if (!(clients[i] == null)) {
						try {
							outToClient = new PrintWriter(clients[i].getSocket().getOutputStream());
							outToClient.println(s);
							outToClient.flush();
							console("Updated Client " + i);
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
                outToClient.close();
			}
		};
		send.start();
	}
	
	public File[] getClientEditingFileSet(int clientID) {
		return clients[clientID].getEditingFileSet();
	}
	public void setClientEditingFileSet(int clientID, File[] files) {
		clients[clientID].setEditingFileSet(files);
	}
	public void addFiletoClientEditingFileSet(int clientID, File file) {
		clients[clientID].addFileToEditingSet(file);
	}
	public void removeFileFromClientEditingFileSet(int clientID, File file) {
		clients[clientID].removeFileFromEditingSet(file);
	}
    
    public String getClientInfo(int clientID) {
    	if (clients[clientID] == null) {
    		return "Client does not exist";
    	}
    	return new String("Client ID " + clientID + " on Server " + ID + "\n"
    			+ "Port:\t\t" + clients[clientID].getSocket().getLocalPort()
    			+ "\nIP Address:\t" + clients[clientID].getSocket().getLocalAddress());
    }
    
	
	public int getPort() {
		return port;
	}
	
	public boolean isRunning() {
		if (running)
			return true;
		return false;
	}

	public void console(String message) {
		System.out.println("Server "+ ID + ": " + message);
	}
	
	public void close() {
		
	}
	
	
	
}
