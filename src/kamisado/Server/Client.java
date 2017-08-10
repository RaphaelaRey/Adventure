package kamisado.Server;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;
import kamisado.commonClasses.Spielbrett;

/**
 * @author Tobias Deprato
 */

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
		
		Runnable a = new Runnable() {
			@Override
			public void run() {
				try{
			
				while(true) {
					//neu
					EmpfangenServer();
					
					//alt
//					Turm[] tmpTürme = SendenEmpfangen.Empfangen(clientSocket);
//					
//					logger.info("Daten Empfangen von Client ");
					
					// wenn clientsocket.inputstream.readobject instanceOf türme
					//else instanceof String
							
//					for (Client c : clients) {
//						SendenEmpfangen.Senden(c.clientSocket, tmpTürme);
//						SendenEmpfangen.Senden(c.clientSocket, tmpKoord);
//						SendenEmpfangen.Senden(c.clientSocket, tmpMFelder);
//						logger.info("neue Daten gesendet an" + clientSocket.getInetAddress().getHostName());
//					}
				}
				} catch (Exception e){
					e.toString();
				}
			}
		}; 
		Thread b = new Thread(a);
		b.start();
		logger.info("Thread gestartet");
			
	}
	//neue klasse
public void EmpfangenServer (){
	
		
		try{
			ObjectInputStream empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("available is: " + empfangen.available());
			
//			Object neuEmpfangen = empfangen.readObject();
			
//		if( neuEmpfangen instanceof Turm[]){
			Turm[] tmpTürme = (Turm[]) empfangen.readObject();
			logger.info("Türme erhalten");
			
			for (Client c : clients) {
				SendenEmpfangen.Senden(c.clientSocket, tmpTürme);
				logger.info("neue Türme gesendet an" + c.clientSocket.getInetAddress().getHostName());
			}
//		} else if (neuEmpfangen instanceof String){
//			String tmpMeldung = (String) empfangen.readObject();
//			
//			for (Client c : clients) {
//				SendenEmpfangen.Senden(c.clientSocket, tmpMeldung);
//				logger.info("neuer String " + tmpMeldung + " gesendet an " + c.clientSocket.getInetAddress().getHostName());
//			}
//		} else if (neuEmpfangen instanceof Boolean){
//			boolean tmpBol = (boolean) empfangen.readObject();
//			
//			for (Client c : clients) {
//				SendenEmpfangen.Senden(c.clientSocket, tmpBol);
//				logger.info("neue boolean gesendet an" + c.clientSocket.getInetAddress().getHostName());						}
//		} else{
//			logger.info("hat nicht funktioniert so");
//		}
//		
		logger.info("Daten Empfangen von Client ");
		} catch (Exception e) {
			logger.info(e.toString());
		}
		
	
	}

        
}

