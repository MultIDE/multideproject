package com.multideproject.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
	
	private Socket socket = null;
	private int ID;
	private int serverID;
	private Thread recieve, testConnection, send;
	private BufferedReader inFromClient;
	private ArrayList<File> openFiles;
    private boolean connected, responded;
	
	public Client(Socket socket, int ID, int serverID) {
		this.socket = socket;
        if (this.socket != null)
            connected = true;
		this.ID = ID;
		this.serverID = serverID;
		recieve();
	}
	
	public void recieve() {
		recieve = new Thread("Recieve") {
			public void run() {
				String dataFromClient = new String("");
				while (connected) {
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
    
    public void testConnection() {
        testConnection = new Thread("TestClientConnection") {
            public void run() {
                while(connected) {
                    send("TESTING_CONNECTION");
                    long start = System.currentTimeMillis();
                    while (!responded) {
                        if (System.currentTimeMillis - start > 5000) {
                            ServerHost.getServers()[serverID].disconnectClient(ID);
                            break;
                        }
                    }
                    long total = System.currentTimeMillis() - start;
                    Thread.sleep(1000);
                }
            }
        }
    }
    
    public void send(String s) {
        send = new Thread("Client Send") {
            public void run() {
                PrintWriter outToClient = null;
                try {
                    outToClient = new PrintWriter(socket.getOutputStream());
				    outToClient.println(s);
				    outToClient.flush();
                } catch(IOException e) {
				    e.printStackTrace();
				}
            }
        }
        send.start();
    }
    
	public void manageInput(String input) {
		Thread manageInput = new Thread("manageInput") {
			public void run() {
                if (input.equals("STILL_CONNECTED"))
                    responded = true;
				else if (input.contains("RECIEVE")) {
					System.out.println("Ready to recieve a file");
				}
				else {
					ServerHost.getServers()[serverID].manageInput(input, ID);
				}
			}
		};
		manageInput.start();
	}
    
    public void disconnect() {
        connected = false;
        socket.close();
    }
    
    public boolean isConnected() {
        return connected;
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
    
    public int getHostServerID() {
        return serverID;
    }
	
	public int getID() {
		return ID;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
