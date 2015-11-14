package com.multideproject.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
	
	private Socket socket;
	private int ID;
	private int serverID;
	private Thread recieve;
	private BufferedReader inFromClient;
	private ArrayList<File> openFiles;
	
	public Client(Socket socket, int ID, int serverID) {
		this.socket = socket;
		this.ID = ID;
		this.serverID = serverID;
		recieve();
	}
	
	public void recieve() {
		recieve = new Thread("Recieve") {
			public void run() {
				String dataFromClient = new String("");
				while (ServerHost.getServers()[serverID].isRunning()) {
	                try {
		                inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		                dataFromClient = inFromClient.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
	                if(!dataFromClient.equals("")) {
	                	manageInput(dataFromClient);
	                }
				}
			}
		};
		recieve.start();
	}
	public void manageInput(String input) {
		Thread manageInput = new Thread("manageInput") {
			public void run() {
				if (input.contains("RECIEVE")) {
					System.out.println("Ready to recieve a file");
				}
				else {
					ServerHost.getServers()[serverID].manageInput(input, ID);
				}
			}
		};
		manageInput.start();
	}
	
    public File[] getEditingFileSet() {
		return (File[]) openFiles.toArray();
    }
    
    public void setEditingFileSet(File[] files) {
    	openFiles.addAll(Arrays.asList(files));
    }
    
    public void addFileToEditingSet(File file) {
    	openFiles.add(file);
    }
    
    public void removeFileFromEditingSet(File file) {
    	int index = openFiles.indexOf(file);
    	openFiles.remove(index);
    }
	
	public int getID() {
		return ID;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
