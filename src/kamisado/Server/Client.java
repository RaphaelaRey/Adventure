package kamisado.Server;

import java.net.Socket;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kamisado.commonClasses.SendenEmpfangen;

public class Client {
	
	private Socket ClientSocket;
	private ServerModel model;
	protected final static ObservableList<Client> clients = FXCollections.observableArrayList();
	private final Logger logger = Logger.getLogger("");

	protected Client(ServerModel model, Socket socket) {
		this.model = model;
		this.ClientSocket = socket;
		this.clients.add(Client.this);
		logger.info("Neuer Client zu Liste hinzugefügt " + ClientSocket);

		
				
		SendenEmpfangen.Empfangen(ClientSocket);
		logger.info("Daten Empfangen von Client ");
					
		for (Client c : clients) {
			SendenEmpfangen.Senden(ClientSocket);
			logger.info("neue Daten gesendet an" + ClientSocket.getInetAddress().getHostName());
					}
				
		
		
	}
        
}

