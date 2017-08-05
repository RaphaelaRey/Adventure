package kamisado.Server;

import java.net.Socket;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kamisado.commonClasses.SendenEmpfangen;

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

		
				
		
					
				
		
		
	}
	
	public void senden(){
		for (Client c : clients) {
			SendenEmpfangen.Senden(clientSocket);
			logger.info("neue Daten gesendet an" + clientSocket.getInetAddress().getHostName());
		}
	}
        
}

