package kamisado.Server;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;

/**
 * @author Tobias Deprato
 */

public class ServerModel extends Thread{
	
	private ServerSocket server;
	private Client client;
	private String name;
	private String namePW;
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
				namePW = SendenEmpfangen.EmpfangenString(clientSocket);
				logger.info("Anmeldedaten erhalten: " + namePW);
				String meldung = AnmeldungPrüfen(namePW);
				SendenEmpfangen.Senden(clientSocket, meldung);
				
				client = new Client(ServerModel.this, clientSocket, this.name);
				
				
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
	}
	
	//TODO Carmen clientController hierhin auslagern
	public String AnmeldungPrüfen(String AnmeldeInfos){
		String meldung = "";
		String[] prüfen = AnmeldeInfos.split(",");
		logger.info("Name: " + prüfen[0] + " PW: " + prüfen[1]);
		try {
			FileReader fr = new FileReader("src/kamisado/registrierungen.txt");
			BufferedReader reader = new BufferedReader(fr);
			boolean benutzerExistiert = false;
			String zeile;
			while((zeile = reader.readLine())!=null){
				String[] parts = zeile.split(",");
				//Überprüfung, ob Name und Passwort übereinstimmen
				if(parts[0].equals(prüfen[0])&&parts[1].equals(prüfen[1])){
					benutzerExistiert = true;
					meldung = "startMeldung";
				//Überprüfen, ob Name stimmt und Passwort falsch ist
				}else if(parts[0].equals(prüfen[0])&&!parts[1].equals(prüfen[1]))
					benutzerExistiert=true;
				meldung = "PasswortFalsch";
			}
			//Der Benutzer ist nicht gespeichert
			if(benutzerExistiert==false){
				meldung = "BenutzerExistiertNicht";
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return meldung;
	}
	
	public String RegistrierungPrüfen(String RegistrierInfos){
		String meldung = "";
		String[] prüfen = RegistrierInfos.split(",");
		try {
			FileReader fr = new FileReader("src/kamisado/registrierungen.txt");
			BufferedReader reader = new BufferedReader(fr);
			String zeile;
			boolean benutzerVergeben = false;
			while((zeile=reader.readLine())!=null){
				String[] parts = zeile.split(",");
				if(parts[0].equals(prüfen[0])){
					benutzerVergeben = true;
					meldung = "BenutzernameVergeben";
				}
			}
			
			if(benutzerVergeben==false){
				if(prüfen[1].length()>=5){
					FileWriter fw = new FileWriter("src/kamisado/registrierungen.txt",true);
					fw.write(prüfen[0]+",");
					fw.write(prüfen[1]);
					fw.write("\n");
					fw.close();
					meldung="RegistrierMeldung";
				}else{
					meldung ="PasswortZuKurz";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return meldung;
	}
	
	public String LöschenPrüfen(String LöschInfos){
		String meldung = "";
		
		return meldung;
		
	}
	
	
}

