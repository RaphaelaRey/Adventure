package kamisado.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ServerModel extends Thread{
	
	private ServerSocket server;
	private int[] neueKoordinaten;
	private boolean amLaufen = true;
	protected List<Client> clients;
	private final Logger logger = Logger.getLogger("");
	
	ObjectOutputStream anClient;
	ObjectInputStream vonClient;
	
	
	public ServerModel (int port)  {
		
		try{
			//Verbindung mit Client herstellen
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
				Socket clientSocket = server.accept();
				logger.info(clientSocket.getInetAddress().getHostName() + " verbunden");
				Client neuerClient = new Client(clientSocket);
				this.clients.add(neuerClient);
				//Streams erstellen
				anClient = new ObjectOutputStream(clientSocket.getOutputStream());
				vonClient = new ObjectInputStream(clientSocket.getInputStream());

				//neueKoordinaten von Client empfangen
				int[] in = (int[]) vonClient.readObject();
				this.neueKoordinaten = in;

				//neueKoordinaten an Client senden
				anClient.writeObject(neueKoordinaten);
				anClient.flush();
				
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
	}
}

