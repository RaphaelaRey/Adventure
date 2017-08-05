package kamisado.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerModel extends Thread{
	
	private ServerSocket server;
	private boolean amLaufen = true;
	private final Logger logger = Logger.getLogger("");
	
	ObjectOutputStream anClient;
	ObjectInputStream vonClient;
	
	
	public ServerModel (int port)  {
		
		try{
			//Server starten
			this.server = new ServerSocket(port);
			logger.info("Neuer Server gestartet");
			
			this.start();
			
		} catch(Exception e){
			logger.info(e.toString());
		}
	}
	
	public void run() {
		while(amLaufen == true){
			try{
				//Verbindung mit Client herstellen
				Socket clientSocket = server.accept();
				logger.info(clientSocket.getInetAddress().getHostName() + " verbunden");
				
				Client neuerClient = new Client(ServerModel.this, clientSocket);
						
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
	}
	
}

