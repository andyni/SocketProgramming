import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.*;

public class Server {
	private Map<String, Integer> _usersOnline = new HashMap<String, Integer>();
	private static final int _serverPort = 9000;
	private ServerSocket serverSocket;
	
	private void listen(){
		try {
			serverSocket = new ServerSocket(_serverPort);
			System.out.println("Server started.");
			
			while(true){
				Socket connectionSocket = serverSocket.accept();
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				
			}
		} 
		catch (IOException e) {
			System.out.println("IO error when opening socket at port " + _serverPort + ".");
		}
	}
	
	public static void main(String[] args){
		Server server = new Server();
		server.listen();
	}
}
