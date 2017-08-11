package kamisado.Server;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;

/**
 * @author Tobias Deprato
 */

public class Client {
	
	private Socket clientSocket;
	private ServerModel model;
	protected final static ObservableList<Client> clients = FXCollections.observableArrayList();
	private final Logger logger = Logger.getLogger("");
	
	/** stellt eine Instanz des mit dem Server verbundenen Clients dar, 
	 * pro verbundener Client gibt es einen Client-Thread auf dem Server.
	 * Dieser wartet auf Daten zum empfangen
	 * @param String ipAdresse, String Name, String Passwort und String Anmeldeart 
	 * @author Tobias Deprato 
	 */
	protected Client(ServerModel model, Socket socket) {
		this.model = model;
		this.clientSocket = socket;
		
		//neuer Client zu Clientliste hinzugefügt
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
	
	/** empfängt ein Objekt und verarbeitet es je nach Objekttyp weiter
	 * @param Turm[] oder String
	 * @author Tobias Deprato 
	 */	
public void EmpfangenServer (){
	
		
	try{
		ObjectInputStream empfangen = new ObjectInputStream(clientSocket.getInputStream());
		
		Object neuEmpfangen = (Object) empfangen.readObject();
		
		if( neuEmpfangen instanceof Turm[]){
			Turm[] tmpTürme = (Turm[])neuEmpfangen;
			
			for (Client c : clients) {
				SendenEmpfangen.Senden(c.clientSocket, tmpTürme);
			}
		} else if (neuEmpfangen instanceof String){
			String tmpMeldung = (String)neuEmpfangen;
			
			String[] teile = tmpMeldung.split(",");
			String namePW = teile[1] +"," + teile[2];
			String meldung;
			
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
			}
		} 
	}catch (Exception e) {
		e.printStackTrace();
		
		}
	}
	

        
}

