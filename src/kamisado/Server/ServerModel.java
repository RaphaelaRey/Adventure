package kamisado.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerModel {
	
	private int port = 444;
	ObjectOutputStream anClient;
	ObjectInputStream vonClient;
	
	
	private void StartServer (int port) throws IOException {
		
		try{
			//Verbindung mit Client herstellen
			ServerSocket serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();
			
			//Strems erstellen
			anClient = new ObjectOutputStream(clientSocket.getOutputStream());
			vonClient = new ObjectInputStream(clientSocket.getInputStream());
			
			
			
			
			clientSocket.close();
			serverSocket.close();
		} catch (IOException e){
			System.out.println(e);
		}
	}

}
