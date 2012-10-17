import java.io.*;
import java.net.*;

public class Client {
	private static final int _serverPort = 9999;
	private String _serverHostname = "localhost";
	
	private Socket clientSocket;
	private BufferedReader inFromUser;
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	
	private String _username;
	private String _recipient;
	
	public ClientGUI _gui;
	
	public Client(){
		_gui = null;
	}
	
	public Client(ClientGUI gui)
	{
		this._gui = gui;
	}
	
	public void run() {
		System.out.println("Client started.");
		
		try {
			clientSocket = new Socket(_serverHostname, _serverPort);
			if(this._gui == null){
				System.out.println("Connection to server established.");	
			}
			else{
				this._gui.display("Connection to server established.\n");
			}
		}
		catch (Exception e){
			System.out.println("Connection to server failed.");
			return;
		}
		
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// Register username with server
			if(this._gui == null){
				System.out.print("Please enter a username: ");
				registerUsername(inFromUser.readLine());		
			}
		}
		catch(IOException e) {
			System.out.println("Creation of Input/Output streams failed.");
		}
		
		new MessagingThread().start();
		new ListeningThread().start();
	}
	
	public void registerUsername(String username){
		try {
			this._username = username;
			System.out.println(username);
			outToServer.writeBytes("/registerUsername/ " + this._username + " \n");
		} catch (IOException e) {
			System.out.println("Registration of username failed.");
		}
	}
	
	private class ListeningThread extends Thread {	
		@Override
		public void run() {
			while (true) {
				try {
					if(_gui == null){
						System.out.println(inFromServer.readLine() + "\n");
					}
					else{
						_gui.display(inFromServer.readLine() + "\n");
					}
				} catch (IOException e) {
					System.out.println("Listening thread has been terminated.");
					break;
				}
			}
		}
	}

	private class MessagingThread extends Thread{
		@Override
		public void run() {
			while (true) {
				try {
					String message = inFromUser.readLine(); 
					if(message.contains("RECIPIENT")){
						_recipient = message.replaceAll(".*RECIPIENT *=", "").trim();
						continue;
					}
					outToServer.writeBytes(_recipient + " " + message + "\n");						
					if(message.contains("LOGOUT")){
						disconnect();
					}
					
				} catch (IOException e) {
					System.out.println("Messaging thread has been terminated.");
					break;
				}
			}
		}

	}

	private void disconnect() {
		try {
			outToServer.close();
			inFromServer.close();
			inFromUser.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Connection failed to close.");
		}
	}
	
	public void whoIsOnline(){
		try {
			outToServer.writeBytes("WHOISONLINE\n");
		} catch (IOException e) {
			e.printStackTrace();
		}						
	}
	
	public void message(String message){
		try {
			outToServer.writeBytes(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Client client = new Client();
		client.run();
	}
}
