package kamisado.Server;

import java.net.Socket;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Spielbrett;

public class Client {
	
	private Socket clientSocket;
	private ServerModel model;
	protected final static ObservableList<Client> clients = FXCollections.observableArrayList();
	private final Logger logger = Logger.getLogger("");

	protected Client(ServerModel model, Socket socket) {
		this.model = model;
		this.clientSocket = socket;
		this.clients.add(Client.this);
		logger.info("Neuer Client zu Liste hinzugefügt " + clientSocket);
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while(true) {
					Spielbrett tmpSpielbrett = SendenEmpfangen.Empfangen(clientSocket);
					logger.info("Daten Empfangen von Client ");
							
					for (Client c : clients) {
						SendenEmpfangen.Senden(c.clientSocket, tmpSpielbrett);
						logger.info("neue Daten gesendet an" + clientSocket.getInetAddress().getHostName());
					}
				}
			}
		};	
		Thread t = new Thread(r);
		t.start();
				
		
		
	}
        
}

