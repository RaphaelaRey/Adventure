package kamisado.Server;


import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import kamisado.commonClasses.Turm;

/**
 * @author Tobias Deprato
 */

public class ServerModel extends Thread{
	
	private ServerSocket server;
	private Client client;
	private String name;
	private boolean amLaufen = true;
	private final Logger logger = Logger.getLogger("");
	
	
	public ServerModel (int port)  {
		
		try{
			//Server starten
			this.server = new ServerSocket(port, 2, null);
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
				ObjectInputStream empfangen;
				empfangen = new ObjectInputStream(clientSocket.getInputStream());
				this.name = (String) empfangen.readObject();
				logger.info("Name empfangen");
				empfangen.close();
				
				client = new Client(ServerModel.this, clientSocket, this.name);
				
				
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
	}
	
}

