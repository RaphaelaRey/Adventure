package kamisado.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import kamisado.commonClasses.SendenEmpfangen;

public class ServerModel extends Thread{
	
	private ServerSocket server;
	private boolean amLaufen = true;
	protected static List<Client> clients;
	private final Logger logger = Logger.getLogger("");
	
	ObjectOutputStream anClient;
	ObjectInputStream vonClient;
	
	
	public ServerModel (int port)  {
		
		try{
			//Server starten
			this.server = new ServerSocket(port);
			logger.info("Neuer Server gestartet");
			
			clients = Collections.synchronizedList(new ArrayList<Client>());
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
				
				Client neuerClient = new Client(clientSocket);
				this.clients.add(neuerClient);
				logger.info("Neuer Client zu Liste hinzugefügt");
				
				SendenEmpfangen.Senden(clientSocket);
				SendenEmpfangen.Empfangen(clientSocket);
				
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
	}
	
	public static void printClients(){
		System.out.println(clients);
	}
}

