package com.multideproject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class DataTransferHandler implements Runnable {
	
	private boolean running = false;
	private Thread run, send, sendFile, recieve;
	private Socket clientSocket;
	
	public DataTransferHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		run = new Thread(this, "Run");
		run.start();
	}

	@Override
	public void run() {
		running = true;
		recieve();
	}
	
	public void setRunning(boolean b) {
		running = b;
	}
	
	public void recieve() {
		recieve = new Thread("Recieve") {
			public void run() {
                BufferedReader fromServer;
				while(running) {
					String input = "";
					try {
						fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						input = fromServer.readLine();
					} catch (IOException e) {
						MultIDE.establishConnection(MultIDE.getIp(), MultIDE.getPort());
					}
					if (!(input.equals(""))) {
						manageInput(input);
					}
				}
                fromServer.close();
			}
		};
		recieve.start();
	}
	
	public void manageInput(String input) {
		Thread manageInput = new Thread("ManageInputFromServer") {
			public void run() {
				if (input.equals("TESTING_CONNECTION")) {
					send("STILL_CONNECTED");
				}
				else {
					MultIDE.txtArea.setText(input);
				}
			}
		};
		manageInput.start();
	}
	
	
	public void send(String toSend) {
		send = new Thread("Send") {
			public void run() {
		        PrintWriter outToServer;
				try {
					outToServer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			        outToServer.print(toSend + '\n');
			        outToServer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public void sendFile(File file) {
		sendFile = new Thread("FileSender") {
			FileInputStream fis;
			BufferedInputStream bis;
			OutputStream os;
			public void run() {
				try {
					byte [] mybytearray  = new byte [(int)file.length()];
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					bis.read(mybytearray,0,mybytearray.length);
					os = clientSocket.getOutputStream();
					os.write(mybytearray,0,mybytearray.length);
					os.flush();
		        } catch (IOException e) {
					e.printStackTrace();
				}
			}
					if (fis != null) fis.close();
					if (bis != null) bis.close();
		};
		sendFile.start();
	}
	
}
