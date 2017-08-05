package kamisado.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kamisado.commonClasses.SendenEmpfangen;

public class ServerModel extends Thread{
	
	private ServerSocket server;
	private boolean amLaufen = true;
	protected final static ObservableList<Client> clients = FXCollections.observableArrayList();;
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
				this.clients.add(neuerClient);
				logger.info("Neuer Client zu Liste hinzugefügt");
				
				SendenEmpfangen.Senden(clientSocket);				
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
	}
	
	public static void printClients(){
		System.out.println(clients);
	}
}

