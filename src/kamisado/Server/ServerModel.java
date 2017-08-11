package kamisado.Server;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Tobias Deprato
 */

public class ServerModel extends Thread{
	
	private ServerSocket server;
	private boolean amLaufen = true;
	private static final Logger logger = Logger.getLogger("");
	/** startet den Server und wartet bis sich ein Client verbinden möchte,
	 * bei einer Verbindung erstellt er ein Clientobjekt
	 * @author Tobias Deprato 
	 */	
	public ServerModel (int port)  {
		
		try{
			//Server starten
			this.server = new ServerSocket(port, 2, null);
			logger.info("Neuer Server gestartet");
			
			this.start();
			
		} catch(Exception e){
			logger.info(e.toString());
		}
	
	
	Runnable a = new Runnable() {
		public void run() {
			while(amLaufen == true){
				try{
					//Verbindung mit Client herstellen
					Socket clientSocket = server.accept();
					logger.info(clientSocket.getInetAddress().getHostName() + " verbunden");
					
					new Client(ServerModel.this, clientSocket);
					
					
				} catch (Exception e){
					logger.info(e.toString());
				}
			}
		}
	}; 
	Thread b = new Thread(a);
	b.start();
}
	
	/**
	 * @param AnmeldeInfos
	 * @return String mit der Meldung, welche auf dem Client angezeigt wird
	 * @author carmen walser
	 */
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
					meldung = "anmelden,startMeldung";
				//Überprüfen, ob Name stimmt und Passwort falsch ist
				}else if(parts[0].equals(prüfen[0])&&!parts[1].equals(prüfen[1])){
					benutzerExistiert=true;
					meldung = "anmelden,PasswortFalsch";
				}
				
			}
			//Der Benutzer ist nicht gespeichert
			if(benutzerExistiert==false){
				meldung = "anmelden,BenutzerExistiertNicht";
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return meldung;
	}
	
	/**
	 * @param RegistrierInfos
	 * @return String mit der Meldung, welche auf dem Client angezeigt wird
	 * @author carmen walser
	 */
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
					meldung = "registrieren,BenutzernameVergeben";
				}
			}
			
			if(benutzerVergeben==false){
				if(prüfen[1].length()>=5){
					FileWriter fw = new FileWriter("src/kamisado/registrierungen.txt",true);
					fw.write(prüfen[0]+",");
					fw.write(prüfen[1]);
					fw.write("\n");
					fw.close();
					meldung="registrieren,RegistrierMeldung";
				}else{
					meldung ="registrieren,PasswortZuKurz";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return meldung;
	}
	
	
	/**
	 * @param LöschInfos
	 * @return String mit der Meldung, welche auf dem Client angezeigt wird
	 * @author carmen walser
	 */
	public String LöschenPrüfen(String LöschInfos){
		String meldung = "";
		String[] prüfen = LöschInfos.split(",");
		try {
			FileReader fr = new FileReader("src/kamisado/registrierungen.txt");
			BufferedReader reader = new BufferedReader(fr);
			FileReader fr2 = new FileReader("src/kamisado/registrierungen.txt");
			BufferedReader reader2 = new BufferedReader(fr2);
			FileReader fr3 = new FileReader("src/kamisado/registrierungen.txt");
			BufferedReader reader3 = new BufferedReader(fr3);
			
			String zeile;
			boolean benutzerExistiert = false;
			while ((zeile = reader.readLine())!=null){
				String[] parts = zeile.split(",");
				if(parts[0].equals(prüfen[0])&&parts[1].equals(prüfen[1])){
					benutzerExistiert = true;
					String neueZeile;
					int counter=0;
					while((neueZeile = reader2.readLine())!=null){
						counter++;
					}reader2.close();
					
					String[] alle = new String[counter];
					
					for(int i = 0; i<counter;i++){
						while ((neueZeile = reader3.readLine())!= null){
							if(neueZeile.equals(LöschInfos)){
								alle[i]="";
							}else{							
								alle[i]=neueZeile;
								
								}break;
						}	
					}reader3.close();
					FileWriter fw = new FileWriter("src/kamisado/registrierungen.txt");
					BufferedWriter löschen = new BufferedWriter(fw);
					löschen.write("");
					löschen.close();
					fw.close();
					
					for(int i=0; i<alle.length;i++){
						FileWriter schreiben = new FileWriter("src/kamisado/registrierungen.txt",true);
						String s = alle[i];
						schreiben.write(s);
						schreiben.write("\n");
						schreiben.close();
					}meldung="löschen,ErfolgreichMeldung";
					break;
				}else if(parts[0].equals(prüfen[0])&&!parts[1].equals(prüfen[1])){
					benutzerExistiert=true;
					meldung = "löschen,PasswortFalschMeldung";
				}
			}
			if(benutzerExistiert==false){
				meldung="löschen,BenutzerExistiertNichtMeldung";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return meldung;
	}
	
	
	
}

