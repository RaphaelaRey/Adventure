package kamisado.client;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;

public class ClientModel {
	
	protected static Spielbrett spielbrett;

	protected Socket clientSocket;
	private boolean amLaufen = true;
	private static String meldung;
	private static String ipAdresse;
	private int port = 444;
	
	private final static Logger logger = Logger.getLogger("");
	
	/** verbindet den Client mit dem Server, sendet die Anmeldedaten 
	 * und wartet auf Daten zum empfangen
	 * @param String ipAdresse, String Name, String Passwort und String Anmeldeart 
	 * @author Tobias Deprato 
	 */
	public void Verbinden(String ipAdresse, String name, String pw, String art) {
		 
		 
		try{
			//Verbindung mit Server herstellen
			this.clientSocket = new Socket(ipAdresse, port);
			logger.info(ipAdresse + " über Port " + port + " verbunden");
			
			AnmeldungSenden(art, name, pw);
			
			//Thread erstellen
			Runnable a = new Runnable() {
				@Override
				public void run() {
					try{
						while(amLaufen == true){
							EmpfangenClient(clientSocket);
							logger.info("Daten empfangen auf Client");						
						}
					}catch (Exception e){
						logger.info(e.toString());
					}
				}
			}; 
			Thread b = new Thread(a);
			b.start();
			logger.info("Thread gestartet");
			
		} catch (Exception e){
			logger.info(e.toString());
		}
	}	
	
	/** empfängt ein Objekt und verarbeitet es je nach Objekttyp weiter
	 * @param Turm[] oder String
	 * @author Tobias Deprato 
	 */	
	public static void EmpfangenClient(Socket clientSocket){
		try{
			ObjectInputStream empfangen = new ObjectInputStream(clientSocket.getInputStream());
			
			Object neuEmpfangen = (Object) empfangen.readObject();
			
			//Turm Empfangen
			if( neuEmpfangen instanceof Turm[]){
				Turm[] tmpTürme = (Turm[]) neuEmpfangen;
				TürmeEmpfangen(tmpTürme);
				
			// String Empfangen
			} else if (neuEmpfangen instanceof String){
			String tmpMeldung = (String) neuEmpfangen;
				
				String[] teile = tmpMeldung.split(",");
				String meldung = teile[1];
				
				
				if(teile[0].equals("anmelden") ){
					setMeldung(meldung);
				} else if (teile[0].equals("registrieren")) {
					setMeldung(meldung);
				} else if (teile[0].equals("löschen")){
					setMeldung(meldung);
				}		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** erhält die Türme, nachdem sie im Client eingetroffen sind 
	 * und startet das Update
	 * @param neueTürme
	 * @author Tobias Deprato 
	 */
	public static void TürmeEmpfangen(Turm[] tmpTürme){
		Turm[] Türme = Spielbrett.getTürme();
		if(tmpTürme != null && tmpTürme[0] != null) {
			UpdateSpielfeld(Türme, tmpTürme);
			logger.info("Spielfeld aktualisiert");
			
			for (int i = 0; i < tmpTürme.length; i++){
				if(tmpTürme[i].isAktiverTurm()==true){
					Spielbrett.setAktiverTurmKoordinaten(tmpTürme[i].getKoordinaten());
				}
			}
		}
	}	
	
	/** leitet die neuen Türme an die SendenEmpfangen klasse weiter zum senden
	 * @param neueTürme
	 * @author Tobias Deprato 
	 */
	public void TürmeSenden(){
		SendenEmpfangen.Senden(clientSocket, Spielbrett.getTürme());
		logger.info("Daten gesendet");
	}
	
	/** leitet die Anmelde-, Registrierungs- oder Löschen-Daten 
	 * an die SendenEmpfangen klasse weiter zum senden
	 * @param String anmeldung, Registrierung oder Löschung
	 * @author Tobias Deprato 
	 */
	public void AnmeldungSenden(String art, String name, String pw){
		String namePW = art + "," + name + ","+ pw;
		SendenEmpfangen.Senden(clientSocket, namePW);
	}
	
	/** Spielfeld updaten
	 * @param alteTürme
	 * @param neueTürme
	 * @author Tobias Deprato (Kommunikation) und Raphaela Rey (Spiellogik)
	 */
	public static void UpdateSpielfeld(Turm[] alteTürme, Turm[]neueTürme){
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				if(neueTürme!=null && alteTürme!=null &&  neueTürme.length>0){
					
					//alte Türme von der Gridpane löschen und Felder zurücksetzen
					spielbrett.getPane().getChildren().removeAll(Spielbrett.getTürme());
					for (int i = 0; i < Spielbrett.getFelder().length; i++){
						for (int j = 0; j < Spielbrett.getFelder().length; j++){
				    			Spielbrett.getFelder()[i][j].setFeldBesetzt(false);
				    	}
					}	
			
					for(int i = 0; i < neueTürme.length; i++){
						int xKoords = neueTürme[i].getKoordinaten()[0];
						int yKoords = neueTürme[i].getKoordinaten()[1];
						
						// Turmdurchmesser definieren
						neueTürme[i].setRadius(spielbrett.TURMDURCHMESSER);
						
						// Randbreite der Türme definieren (breit beim aktiven Turm, standard bei den anderen)
						if(koordVergleich(neueTürme[i].getKoordinaten(), Spielbrett.getAktiverTurmKoordinaten())){
							neueTürme[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
						} else{
							neueTürme[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
						}
						
						// Türme bei den Koordinaten platzieren, Rand- und Füllfarbe definieren und Felder besetzen
						spielbrett.getPane().add(neueTürme[i], xKoords, yKoords);
						neueTürme[i].setFill(Color.valueOf(neueTürme[i].getFüllFarbe()));
						neueTürme[i].setStroke(Color.valueOf(neueTürme[i].getStrokeFarbe()));
						
						Spielbrett.getFelder()[xKoords][yKoords].setFeldBesetzt(true);
					}	
					
					// Mögliche Felder anzeigen
					möglicheFelderAnzeigen(Spielbrett.getAktiverTurmKoordinaten());
				}
				Spielbrett.setTürme(neueTürme);
				
				if(getGewinner()!=null){
					spielZurücksetzen();
				}
			}
		});
	}
					
	
	public void clientAnhalten(){
		if(clientSocket != null){
			try{
				clientSocket.close();
				logger.info("client Thread beendet");
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
	}
	
	public String getIP(){
		return ClientModel.ipAdresse;
	}
	public void setIP(String ipAdresse){
		ClientModel.ipAdresse = ipAdresse;
	}
	
	public String getMeldung() {
		return meldung;
	}

	public static void setMeldung(String neueMeldung) {
		meldung = neueMeldung;
	}

	public void setSpielbrett(Spielbrett Spielbrett){
		ClientModel.spielbrett = Spielbrett;
	}
	
	public Spielbrett getSpielbrett (){
		return ClientModel.spielbrett; 
	}
	
	/** Überprüfen, ob zwei int-Arrays gleich sind
	 * @param Koordinaten des ersten Arrays
	 * @param Koordinaten des zweiten Arrays
	 * @return true oder false
	 * @author Raphaela Rey
	 */
	public static boolean koordVergleich(int[] koord1, int[] koord2){
		if(koord1[0]==koord2[0] && koord1[1]==koord2[1]){
			return true;
		}
		return false;
	}

	/** Randbreite aller Türme zurücksetzen
	 * @author Raphaela Rey
	 */
	public static void turmStrokeWidthZurücksetzen(){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
		}	
	}

	/** Turmfarbe (schwarz/weiss) basierend auf dessen Koordinaten herausfinden
	 * @param turmKoordinaten
	 * @return Turmfarbe
	 * @author Raphaela Rey
	 */
	public static Color getTurmFarbe(int[]turmKoordinaten){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			if(koordVergleich(Spielbrett.getTürme()[i].getKoordinaten(), turmKoordinaten)==true // ausgewählter Turm herausfinden
					&& (Spielbrett.getTürme()[i].getStroke().equals(Color.BLACK))){				// herausfinden ob der Turm schwarz ist
				return Color.BLACK;
			}
		}
		return Color.WHITE;
	}
	
	/** Turm aufgrund von Koordinaten herausfinden
	 * @param Koordinaten
	 * @return Turm oder null, falls kein Turm auffindbar ist
	 * @author Raphaela Rey
	 */
	public Turm getTurm(int[] turmKoordinaten){
		for(int i = 0; i < Spielbrett.getTürme().length; i++){
				if(koordVergleich(Spielbrett.getTürme()[i].getKoordinaten(), turmKoordinaten)){
					return Spielbrett.getTürme()[i];
				}
		}
		return null;
	}
	/** Überprüft, ob bereits ein Turm bewegt wurde (also ob es sich um den ersten Spielzug handelt oder nicht
	 * @return true falls ein Turm bewegt wurde
	 * @author Raphaela Rey
	 */
	public static boolean bereitsEinTurmBewegt(){
		for(int i = 0; i < Spielbrett.getTürme().length; i++){
			if(Spielbrett.getTürme()[i].isTurmBewegt()){
				return true;
			}
		}
		return false;
	}

	/** Setzt die boolean-Variable turmbewegt zurück, damit wieder ein Turm ausgewählt werden kann
	 * @return zurückgesetztes Turm-Array
	 * @author Raphaela Rey
	 */
	public static Turm[] bereitsEinTurmBewegtZurücksetzen(){
		for(int i = 0; i < Spielbrett.getTürme().length; i++){
			Spielbrett.getTürme()[i].setTurmBewegt(false);
		}
		return Spielbrett.getTürme();
	}

	/** Gewinner herausfinden (überprüfen, ob ein Turm als Gewinnerturm definiert ist)
	 * @return Gewinnerfarbe oder null, falls niemand gewonnen hat
	 * @author Raphaela Rey
	 */
	public static Color getGewinner(){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			if(Spielbrett.getTürme()[i].isGewinnerTurm() && Spielbrett.getTürme()[i].getStroke().equals(Color.BLACK)){
				return Color.BLACK;
			}
			if(Spielbrett.getTürme()[i].isGewinnerTurm() && Spielbrett.getTürme()[i].getStroke().equals(Color.WHITE)){
				return Color.WHITE;
			}
		}
		return null;
	}
	
	/** Gewinnervarible in den Türmen zurücksetzen
	 * @return Turm-Array
	 * @author Raphaela Rey
	 */
	public static Turm[] gewinnerZurücksetzen(){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			Spielbrett.getTürme()[i].setGewinnerTurm(false);
		}
		return Spielbrett.getTürme();
	}
	
	/** Erster blockierender Turm herausfinden (überprüfen, ob ein Turm als erster blockierender Turm definiert ist)
	 * @return Farbe des blockierenden Turms oder null, wenn kein Turm blockiert
	 * @author Raphaela Rey
	 */
	public Color getErsterBlockierenderTurm(){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			if(Spielbrett.getTürme()[i].isErsterBlockierenderTurm() && Spielbrett.getTürme()[i].getStroke().equals(Color.BLACK)){
				logger.info("Schwarz ist blockierender Turm");
				return Color.BLACK;
			}
			if(Spielbrett.getTürme()[i].isErsterBlockierenderTurm() && Spielbrett.getTürme()[i].getStroke().equals(Color.WHITE)){
				logger.info("Weiss ist blockierender Turm");
				return Color.WHITE;
			}
		}
		logger.info("Kein blockierender Turm");
		return null;
	}
	
	/** Zweiter blockierender Turm herausfinden (überprüfen, ob ein totaler Stillstand besteht)
	 * @return Farbe des blockierenden Turms oder null, wenn kein zweiter Turm blockiert
	 * @author Raphaela Rey
	 */
	public Color getZweiterBlockierenderTurm(){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			if(Spielbrett.getTürme()[i].isZweiterBlockierenderTurm() && Spielbrett.getTürme()[i].getStroke().equals(Color.BLACK)){
				logger.info("Schwarz ist zweiter blockierender Turm");
				return Color.BLACK;
			}
			if(Spielbrett.getTürme()[i].isZweiterBlockierenderTurm() && Spielbrett.getTürme()[i].getStroke().equals(Color.WHITE)){
				logger.info("Weiss ist zweiter blockierender Turm");
				return Color.WHITE;
			}
		}
		logger.info("Kein totaler Stillstand");
		return null;
	}
	
	/** Bei allen Türmen Eigenschaft blockiert zurücksetzen
	 * @return Turm-Array ohne blockierende Türme
	 * @author Raphaela Rey
	 */
	public Turm[] blockierendeTürmeZurücksetzen(){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			Spielbrett.getTürme()[i].setErsterBlockierenderTurm(false);
			Spielbrett.getTürme()[i].setZweiterBlockierenderTurm(false);
		}
		return Spielbrett.getTürme();
	}
	
	/** Den ausgewählten Turm bewegen
	 * @param ausgewähltesFeld
	 * @param k (Position des betroffenen Turms im Turm-Array
	 * @author Raphaela Rey
	 */
	public void turmBewegen(Feld ausgewähltesFeld, int k){
		int xKoords = ausgewähltesFeld.getKoordinaten()[0];
		int yKoords = ausgewähltesFeld.getKoordinaten()[1];
		// den zu bewegenden Turm von der Gridpane entfernen und das Feld freigeben
		spielbrett.getPane().getChildren().remove(Spielbrett.getTürme()[k]);
		Spielbrett.getFelder()[Spielbrett.getAktiverTurmKoordinaten()[0]][Spielbrett.getAktiverTurmKoordinaten()[1]].setFeldBesetzt(false);

		// neue Koordinaten des Turms setzen, den Turm der Gridpane hinzufügen und das Feld besetzen
		Spielbrett.getTürme()[k].setKoordinaten(ausgewähltesFeld.getKoordinaten());
		Spielbrett.setAktiverTurmKoordinaten(ausgewähltesFeld.getKoordinaten());
		spielbrett.getPane().add(Spielbrett.getTürme()[k], xKoords, yKoords);
		Spielbrett.getFelder()[xKoords][yKoords].setFeldBesetzt(true);
		
		Spielbrett.getTürme()[k].setTurmBewegt(true);
	}

	/** Zukünftiger gegnerischer Turm definieren
	 * @param k (Position des betroffenen Turms im Turm-Array)
	 * @param ausgewähltesFeld
	 * @param nächsterAktiverTurm
	 * @return aktualisierter nächsterAktiverTurm
	 * @author Raphaela Rey
	 */
	public int[] setNächsterGegnerischerTurm(int k, Feld ausgewähltesFeld, int[]nächsterAktiverTurm){
		// Nächster gegnerischer Turm falls der vorherige Turm schwarz war
		if(Spielbrett.getTürme()[k].getStroke().equals(Color.BLACK)){
			for (int i = 0; i < Spielbrett.getTürme().length; i++){
				Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);	
				if (Spielbrett.getTürme()[i].getStroke().equals(Color.WHITE)
						&& Spielbrett.getTürme()[i].getFill().equals(ausgewähltesFeld.getFill())){
					nächsterAktiverTurm = Spielbrett.getTürme()[i].getKoordinaten();
					Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					möglicheFelderAnzeigen(nächsterAktiverTurm);
					// Überprüfen ob Blockade
					if(Spielbrett.getMöglicheFelder().size()==0){	
						Spielbrett.getTürme()[k].setErsterBlockierenderTurm(true);
					} else{		
						blockierendeTürmeZurücksetzen();
					}
				}
			}	
		}else{		// Nächster gegnerischer Turm falls der vorherige Turm weiss war 
			for (int i = 0; i < Spielbrett.getTürme().length; i++){ 
				Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
				if (Spielbrett.getTürme()[i].getStroke().equals(Color.BLACK) 
						&& Spielbrett.getTürme()[i].getFill().equals(ausgewähltesFeld.getFill())){		
					nächsterAktiverTurm = Spielbrett.getTürme()[i].getKoordinaten();
					möglicheFelderAnzeigen(nächsterAktiverTurm);
					Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					// Überprüfen ob Blockade
					if(Spielbrett.getMöglicheFelder().size()==0){	
						Spielbrett.getTürme()[k].setErsterBlockierenderTurm(true);
					} else{
						blockierendeTürmeZurücksetzen();
					}
				}
			}	
		}
		return nächsterAktiverTurm;
	}
		
	/** Nächster gegnerischer Turm definieren im Fall einer Blockade
	 * @param aktueller nächsterAktiverTurm
	 * @return Koordinaten des nächsten gegnerischen Turms
	 * @author Raphaela Rey
	 */
	public int[] setNächsterGegnerischerTurmBlockade(int[]nächsterAktiverTurm){ 
		turmStrokeWidthZurücksetzen();
		if(getTurmFarbe(nächsterAktiverTurm).equals(Color.BLACK)){
			for (int m = 0; m < Spielbrett.getTürme().length; m++){
				Feld aktivesFeld = Spielbrett.getFelder()[nächsterAktiverTurm[0]][nächsterAktiverTurm[1]];
				if (Spielbrett.getTürme()[m].getStroke().equals(Color.WHITE)
						&& (Spielbrett.getTürme()[m].getFill().equals(aktivesFeld.getFill()))){
					nächsterAktiverTurm = Spielbrett.getTürme()[m].getKoordinaten(); 
					möglicheFelderAnzeigen(nächsterAktiverTurm);
					Spielbrett.getTürme()[m].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					// Überprüfen ob es einen völligen Stillstand gibt
					if(Spielbrett.getMöglicheFelder().size()==0) {
						Spielbrett.getTürme()[m].setZweiterBlockierenderTurm(true);
					}
					break;
				}
			} 
		} 
		if(getTurmFarbe(nächsterAktiverTurm).equals(Color.WHITE) && getErsterBlockierenderTurm().equals(Color.BLACK)){// && Spielbrett.isBlockiert()==true){
			for (int m = 0; m < Spielbrett.getTürme().length; m++){
				Spielbrett.getTürme()[m].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
				if (Spielbrett.getTürme()[m].getStroke().equals(Color.BLACK)
						&& Spielbrett.getTürme()[m].getFill().equals(Spielbrett.getFelder()[nächsterAktiverTurm[0]][nächsterAktiverTurm[1]].getFill())){
					nächsterAktiverTurm = Spielbrett.getTürme()[m].getKoordinaten(); 
					möglicheFelderAnzeigen(nächsterAktiverTurm);
					Spielbrett.getTürme()[m].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					// Überprüfen ob es einen völligen Stillstand gibt
					if(Spielbrett.getMöglicheFelder().size()==0) {
						Spielbrett.getTürme()[m].setZweiterBlockierenderTurm(true);
					}
					break;
				} 
			}
		}										
		return nächsterAktiverTurm;
	}
	
	/** Gewinner definieren
	 * @param ausgewähltesFeld
	 * @return die Gewinnerfarbe oder null, falls niemand gewonnen hat
	 * @author Raphaela Rey
	 */
	public Color gewinnerDefinieren(Feld ausgewähltesFeld){
		for(int l = 0; l < Spielbrett.GEWINNERFELDERSCHWARZ.length; l++){
			int [] koordGewinnerFeld = {Spielbrett.GEWINNERFELDERSCHWARZ[l][0], Spielbrett.GEWINNERFELDERSCHWARZ[l][1]};
			if(koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
				getTurm(Spielbrett.getAktiverTurmKoordinaten()).setGewinnerTurm(true);
				return Color.BLACK;
			}
		}	
		for(int m = 0; m < Spielbrett.GEWINNERFELDERWEISS.length; m++){
			int [] koordGewinnerFeld = {Spielbrett.GEWINNERFELDERWEISS[m][0], Spielbrett.GEWINNERFELDERWEISS[m][1]};
			if(koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
				getTurm(Spielbrett.getAktiverTurmKoordinaten()).setGewinnerTurm(true);
				return Color.WHITE;
			}
		}
		return null;
	}
	
	/** Ganzes Spielbrett zurücksetzen, nachdem jemand gewonnen hat, was folgendes beinhaltet:
	 * - mögliche Felder leeren - Gewinner löschen - turmBesetzt zurücksetzen 
	 * - alle Türme von der Gridpane entfernen und an den ursprünglichen Platz setzen
	 * @author Raphaela Rey
	 */
	public static void spielZurücksetzen(){ 
		möglicheFelderLeeren(); 
		// Gewinner löschen, alle Türme vom Spielbrett entfernen und die Felder freigeben
		gewinnerZurücksetzen();
		spielbrett.getPane().getChildren().removeAll(Spielbrett.getTürme());
		for (int i = 0; i < Spielbrett.getFelder().length; i++){
    		for (int j = 0; j < Spielbrett.getFelder().length; j++){
    			Spielbrett.getFelder()[i][j].setFeldBesetzt(false);
    		}
		}	
		bereitsEinTurmBewegtZurücksetzen();
		// Die Türme an ihren ursprünglichen Platz setzen
		for(int p = 0; p < Spielbrett.getTürme().length; p++){
			if(Spielbrett.getTürme()[p].getStroke().equals(Color.BLACK)){
				for(int l = 0; l < Spielbrett.getFelder()[7].length; l++){
					if(Spielbrett.getTürme()[p].getFill().equals(Spielbrett.getFelder()[l][7].getFill())){
						Spielbrett.getTürme()[p].setKoordinaten(Spielbrett.getFelder()[l][7].getKoordinaten());
						Spielbrett.getTürme()[p].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
						spielbrett.getPane().add(Spielbrett.getTürme()[p], l, 7);	
						Spielbrett.getFelder()[l][7].setFeldBesetzt(true);
					}
				}
			}else{
				for(int l = 0; l < Spielbrett.getFelder()[0].length; l++){
					if(Spielbrett.getTürme()[p].getFill().equals(Spielbrett.getFelder()[l][0].getFill())){
						Spielbrett.getTürme()[p].setKoordinaten(Spielbrett.getFelder()[l][0].getKoordinaten());
						Spielbrett.getTürme()[p].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
						spielbrett.getPane().add(Spielbrett.getTürme()[p], l, 0);	
						Spielbrett.getFelder()[l][0].setFeldBesetzt(true);
					}
				}
			}
		}
		// Türme wieder aktivieren (Notlösung, da die Türme nach dem Zurücksetzen nicht mehr bewegt werden können)
		for (int i = 0; i < Spielbrett.getTürme().length; i++){				
			Turm t = Spielbrett.getTürme()[i];				
			t.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event){
					if(bereitsEinTurmBewegt()==false && t.getStroke().equals(Color.BLACK)){	
						turmStrokeWidthZurücksetzen();
						t.setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
						Spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());	
						möglicheFelderAnzeigen(Spielbrett.getAktiverTurmKoordinaten());						
					}				
 				}
			});
		}
	}
	
	/** ArrayList mögliche Felder leeren
	 * @return geleerte ArrayList
	 * @author Raphaela Rey
	 */
	public static ArrayList<int[]> möglicheFelderLeeren(){
		ArrayList<int[]> toRemove = new ArrayList<>();
		Iterator<int[]> iter = Spielbrett.getMöglicheFelder().iterator();
		while (iter.hasNext()){
			int[]koords = iter.next();
			int xKoord = koords[0];
			int yKoord = koords[1];
			Spielbrett.getFelder()[xKoord][yKoord].setStroke(Color.BLACK);
			Spielbrett.getFelder()[xKoord][yKoord].setStrokeWidth(spielbrett.STROKEWIDTHFELDERSTANDARD);
			Spielbrett.getFelder()[xKoord][yKoord].setStrokeType(StrokeType.CENTERED);	
			toRemove.add(koords);
		}
		Spielbrett.getMöglicheFelder().removeAll(toRemove);
		
		return Spielbrett.getMöglicheFelder();		
	}
	
	/** Mögliche Felder (geradeaus, diagonal rechts und diagonal links) anzeigen
	 * Dazu wird zuerst die bestehende liste gelöscht, damit nur keine alten möglichen Felder gespeichert sind. 
	 * Dann werden die aktuellen möglichen Feder mit der Suppportmethode "möglicheFelderHinzufügen" hinzugefügt. 
	 * @param turmKoordinaten
	 * @author Raphaela Rey
	 */
	public static ArrayList<int[]> möglicheFelderAnzeigen(int[] turmKoordinaten){		
		möglicheFelderLeeren();
		möglicheFelderHinzufügen(turmKoordinaten);
		return Spielbrett.getMöglicheFelder();
	}
	
	/** Mögliche Felder der ArrayList hinzufügen (Supportmethode)
	 * @param turmKoordinaten
	 * @author Raphaela Rey
	 */
	private static void möglicheFelderHinzufügen(int[] turmKoordinaten){
		int xKoords = turmKoordinaten[0];											
		int yKoords = turmKoordinaten[1];	
		
		// Mögliche Felder falls der Turm schwarz ist				
		if((getTurmFarbe(turmKoordinaten).equals(Color.BLACK))){
			// Mögliche Felder geradeaus
			boolean bereitsEinFeldBesetzt = false;
	
			for(int i = 1; i < Spielbrett.getFelder()[0].length; i++){
				try {
					Feld möglichGeradeaus = Spielbrett.getFelder()[xKoords][yKoords-i];
					if(möglichGeradeaus.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichGeradeaus.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						Spielbrett.getMöglicheFelder().add(möglichGeradeaus.getKoordinaten());
						möglichGeradeaus.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichGeradeaus.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;
			
			// Mögliche Felder rechts diagonal
			for(int i = 1; i < Spielbrett.getFelder()[0].length; i++){
				try {
					Feld möglichDiagRechts = Spielbrett.getFelder()[xKoords+i][yKoords-i];
					if(möglichDiagRechts.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagRechts.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						Spielbrett.getMöglicheFelder().add(möglichDiagRechts.getKoordinaten());
						möglichDiagRechts.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagRechts.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;
	
			// Mögliche Felder links diagonal 
			for(int i = 1; i < Spielbrett.getFelder()[0].length; i++){
				try {
					Feld möglichDiagLinks = Spielbrett.getFelder()[xKoords-i][yKoords-i];
					if(möglichDiagLinks.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagLinks.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						Spielbrett.getMöglicheFelder().add(möglichDiagLinks.getKoordinaten());
						möglichDiagLinks.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagLinks.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
		}
		
		// Mögliche Felder falls der Turm weiss ist
		if((getTurmFarbe(turmKoordinaten).equals(Color.WHITE))){
			boolean bereitsEinFeldBesetzt = false;
			// Mögliche Felder geradeaus
			for(int i = 1; i < Spielbrett.getFelder()[0].length; i++){
				try {
					Feld möglichGeradeaus = Spielbrett.getFelder()[xKoords][yKoords+i];
					if(möglichGeradeaus.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichGeradeaus.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						Spielbrett.getMöglicheFelder().add(möglichGeradeaus.getKoordinaten());
						möglichGeradeaus.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichGeradeaus.setStroke(Color.WHITE);
						möglichGeradeaus.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;
			// Mögliche Felder rechts diagonal 
			for(int i = 1; i < Spielbrett.getFelder()[0].length; i++){
				try {
					Feld möglichDiagRechts = Spielbrett.getFelder()[xKoords+i][yKoords+i];
					if(möglichDiagRechts.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagRechts.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						Spielbrett.getMöglicheFelder().add(möglichDiagRechts.getKoordinaten());
						möglichDiagRechts.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagRechts.setStroke(Color.WHITE);
						möglichDiagRechts.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;
			// Mögliche Felder links diagonal 
			for(int i = 1; i < Spielbrett.getFelder()[0].length; i++){
				try {
					Feld möglichDiagLinks = Spielbrett.getFelder()[xKoords-i][yKoords+i];
					if(möglichDiagLinks.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagLinks.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						Spielbrett.getMöglicheFelder().add(möglichDiagLinks.getKoordinaten());
						möglichDiagLinks.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagLinks.setStroke(Color.WHITE);
						möglichDiagLinks.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}				
		}
	}
}
