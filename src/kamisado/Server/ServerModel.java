package kamisado.Server;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Tobias Deprato
 */

public class ServerModel extends Thread{
	
	private ServerSocket server;
	private Client client;
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
				
				client = new Client(ServerModel.this, clientSocket);
				
				
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
	}
	
}

