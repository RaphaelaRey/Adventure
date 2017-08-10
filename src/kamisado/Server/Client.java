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
					EmpfangenServer();
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
		
		Object neuEmpfangen = (Object) empfangen.readObject();
		
	if( neuEmpfangen instanceof Turm[]){
		Turm[] tmpTürme = (Turm[])neuEmpfangen;
		logger.info("Türme erhalten");
		
		for (Client c : clients) {
			SendenEmpfangen.Senden(c.clientSocket, tmpTürme);
			logger.info("neue Türme gesendet an" + c.clientSocket.getInetAddress().getHostName());
		}
	} else if (neuEmpfangen instanceof String){
		String tmpMeldung = (String)neuEmpfangen;
		logger.info("String eerhalten");
		
		String[] teile = tmpMeldung.split(",");
		String namePW = teile[1] +"," + teile[2];
		String meldung;
		logger.info(teile[0] + " wird erfolgen");
		if(teile[0].equals("anmelden") ){
			logger.info("Anmeldung");
			meldung = model.AnmeldungPrüfen(namePW);
			SendenEmpfangen.Senden(clientSocket, meldung);
			logger.info(meldung);
		} else if (teile[0].equals("registrieren")){
		logger.info("Registrierung");
			meldung = model.RegistrierungPrüfen(namePW);
			SendenEmpfangen.Senden(clientSocket, meldung);
		} else if (teile[0].equals("löschen")){
			logger.info("Löschung");
			meldung = model.LöschenPrüfen(namePW);
			SendenEmpfangen.Senden(clientSocket, meldung);
		} else if (teile[0].equals("gewinnerWeiss")){
//			TODO Meldung anzeigen
			
			for (Client c : clients) {
				SendenEmpfangen.Senden(c.clientSocket, tmpMeldung);
				logger.info("neuer String " + tmpMeldung + " gesendet an " + c.clientSocket.getInetAddress().getHostName());
			}
		} else if (teile[0].equals("gewinnerSchwarz")){
//			TODO Meldung anzeigen
			
			for (Client c : clients) {
				SendenEmpfangen.Senden(c.clientSocket, tmpMeldung);
				logger.info("neuer String " + tmpMeldung + " gesendet an " + c.clientSocket.getInetAddress().getHostName());
			}
		} else if (teile[0].equals("stillstand")){
//			TODO Meldung anzeigen
			
			for (Client c : clients) {
				SendenEmpfangen.Senden(c.clientSocket, tmpMeldung);
				logger.info("neuer String " + tmpMeldung + " gesendet an " + c.clientSocket.getInetAddress().getHostName());
			}
		}
		
		
		
	} else if (neuEmpfangen instanceof Boolean){
		boolean tmpBol = (boolean)neuEmpfangen;
		 
		for (Client c : clients) {
			SendenEmpfangen.Senden(c.clientSocket, tmpBol);
			logger.info("neue boolean gesendet an" + c.clientSocket.getInetAddress().getHostName());						}
	} else{
		logger.info("Objecttype: " + empfangen.getClass());
	}
	
	logger.info("Daten Empfangen von Client ");
	} catch (Exception e) {
		e.printStackTrace();
		
	}
		
	
	}

        
}

