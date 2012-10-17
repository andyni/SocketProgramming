import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.*;

public class Server {
	private Map<String, DataOutputStream> _usersOnline = new HashMap<String, DataOutputStream>();
	private static final int _serverPort = 9999;
	private ServerSocket serverSocket;
	
	private void listen(){
		try {
			serverSocket = new ServerSocket(_serverPort);
			System.out.println("Server started.");
					
			while(true){
				Socket connectionSocket = serverSocket.accept();
				new ClientHandlerThread(connectionSocket).start();
			}
		} 
		catch (IOException e) {
			System.out.println("IO error when opening socket at port " + _serverPort + ".");
		}
	}
	
	private class ClientHandlerThread extends Thread{
		private Socket _connectionSocket;
		private String _clientName;
		
		public ClientHandlerThread(Socket connectionSocket){
			this._connectionSocket = connectionSocket;
		}
		
		@Override
		public void run() {
			try {
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(_connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(_connectionSocket.getOutputStream());
				while (true) {
					String clientMessage = inFromClient.readLine();
					String messageType = clientMessage.split(" ")[0];
					String message = clientMessage.replaceFirst(messageType, "").trim();
					System.out.println(clientMessage);
					if(messageType.equals("/registerUsername/"))
					{
						_usersOnline.put(message, outToClient);
						this._clientName = message;
						outToClient.writeBytes("'" + message + "' has been registered. Type 'WHOISONLINE' to choose a user to talk to. Type 'LOGOUT' to disconnect. Type 'RECIPIENT = <username>' to start chatting with another user.\n");
						System.out.println("'" + message + "' has been registered. \n");
					}
					else if (clientMessage.contains("LOGOUT"))
					{
						_usersOnline.remove(this._clientName);
						System.out.println(this._clientName + " has logged off.\n");
					}
					else if (clientMessage.contains("WHOISONLINE"))
					{
						String userList = "";
						for(String user : _usersOnline.keySet())
						{
							userList += user + " ";
						}
						outToClient.writeBytes("Users Online: " + userList + "\n");
					}
					else
					{
						if(_usersOnline.containsKey(messageType)){
							DataOutputStream socket = _usersOnline.get(messageType);
							socket.writeBytes(this._clientName + ">> " + message + "\n");
						}
					}
				}
			} 
			catch (Exception e) {

			}		
		}
	}
	
	public static void main(String[] args){
		Server server = new Server();
		server.listen();
	}
}