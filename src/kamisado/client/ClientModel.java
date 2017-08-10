package kamisado.client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import kamisado.client.anmeldefenster.AnmeldefensterController;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;

public class ClientModel {
	
	protected Spielbrett spielbrett;

	protected Socket clientSocket;
	private boolean amLaufen = true;
	private static String ip;
	private String meldung;
	private static String ipAdresse;
	private int port = 444;
	private Feld f;
	
	private final Logger logger = Logger.getLogger("");
	
//	public void Verbinden(String name, String pw, String art){
//		Verbinden(ip, name, pw, art);
//	}

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
							TürmeEmpfangen();
							logger.info("Türme empfangen auf Client");							
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
	public void TürmeEmpfangen(){
		Turm[] Türme = Spielbrett.getTürme();
		Turm[] tmpTürme = SendenEmpfangen.Empfangen(clientSocket);
		if(tmpTürme != null && tmpTürme[0] != null) {
			logger.info("Türme empfangen");
			UpdateSpielfeld(Türme, tmpTürme);
			logger.info("Spielfeld aktualisiert");
			
			for (int i = 0; i < tmpTürme.length; i++){
				if(tmpTürme[i].isAktiverTurm()==true){
					Spielbrett.setAktiverTurmKoordinaten(tmpTürme[i].getKoordinaten());
				}
			}
		}
	}	
	
	public void TürmeSenden(){
		SendenEmpfangen.Senden(clientSocket, Spielbrett.getTürme());
		logger.info("Daten gesendet");
	}
	
	public void AnmeldungSenden(String art, String name, String pw){
		String namePW = art + "," + name + ","+ pw;
		 setIp(ipAdresse);
		SendenEmpfangen.Senden(clientSocket, namePW);
		this.setMeldung(SendenEmpfangen.EmpfangenString(clientSocket));
		logger.info(this.meldung);
	}
	
	public void UpdateSpielfeld(Turm[] alteTürme, Turm[]neueTürme){
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
							// Felder-Formatierung zurücksetzen
							for (int i = 0; i < Spielbrett.getFelder().length; i++){
								for (int j = 0; j < Spielbrett.getFelder().length; j++){
						    			Spielbrett.getFelder()[i][j].setStroke(Color.BLACK);
						    			Spielbrett.getFelder()[i][j].setStrokeWidth(spielbrett.STROKEWIDTHFELDERSTANDARD);
						    			Spielbrett.getFelder()[i][j].setStrokeType(StrokeType.CENTERED);	
						    	}
							}
							// TODO Raphaela zurücksetzen, damit wieder mit schwarzen Türmen gefahren werden kann
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
	
	public void setName(String name){
	}
	
	public String getIP(){
		return ClientModel.ipAdresse;
	}
	public void setIP(String ipAdresse){
		ClientModel.ipAdresse = ipAdresse;
	}
	
	public Turm getTurm(int[] turmKoordinaten){
		Turm[] türme = Spielbrett.getTürme();
		for(int i = 0; i < türme.length; i++){
				if(koordVergleich(türme[i].getKoordinaten(), turmKoordinaten)){
					return türme[i];
				}
		}
		return null;
	}
	
	public Feld getFeld(int[] feldKoordinaten){
		
		Feld[][] felder = Spielbrett.getFelder();
		for(int i = 0; i < felder.length; i++){
			for(int j = 0; j < felder.length; j++){
				if(felder[i][j].getKoordinaten() == feldKoordinaten){
					this.f = felder[i][j];
				}
			}
		}
		return f;
	}
	
	/** Spielbrett initialisieren
	 * @param spielbrett
	 */
	public void setSpielbrett(Spielbrett Spielbrett){
		this.spielbrett = Spielbrett;
	}
	
	public Spielbrett getSpielbrett (){
		return this.spielbrett; 
	}
	
	/** Überprüfen, ob zwei int-Arrays gleich sind
	 * @param Koordinaten des ersten Arrays
	 * @param Koordinaten des zweiten Arrays
	 * @return true oder false
	 * @author Raphaela Rey
	 */
	public boolean koordVergleich(int[] koord1, int[] koord2){
		if(koord1[0]==koord2[0] && koord1[1]==koord2[1]){
			return true;
		}
		return false;
	}
	/** Randbreite aller Türme zurücksetzen
	 * @author Raphaela Rey
	 */
	public void turmStrokeWidthZurücksetzen(){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
		}	
	}
	/** Turmfarbe (schwarz/weiss) basierend auf dessen Koordinaten herausfinden
	 * @param turmKoordinaten
	 * @return Turmfarbe
	 * @author Raphaela Rey
	 */
	public Color getTurmFarbe(int[]turmKoordinaten){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			if(koordVergleich(Spielbrett.getTürme()[i].getKoordinaten(), turmKoordinaten)==true // ausgewählter Turm herausfinden
					&& (Spielbrett.getTürme()[i].getStroke().equals(Color.BLACK))){				// herausfinden ob der Turm schwarz ist
				return Color.BLACK;
			}
		}
		return Color.WHITE;
	}
	
	/** Gewinner herausfinden (überprüfen, ob ein Turm als Gewinnerturm definiert ist)
	 * @return Gewinnerfarbe oder null, falls niemand gewonnen hat
	 * @author Raphaela Rey
	 */
	public Color getGewinner(){
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
	
	public Turm[] gewinnerZurücksetzen(){
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
		
		Spielbrett.setTurmBewegt(true);
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
//		if(getTurmFarbe(Spielbrett.getAktiverTurmKoordinaten())==Color.BLACK){
			for (int i = 0; i < Spielbrett.getTürme().length; i++){
				Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);	//Formatierung aller Türme zurücksetzen	
				if (Spielbrett.getTürme()[i].getStroke().equals(Color.WHITE)
						&& Spielbrett.getTürme()[i].getFill().equals(ausgewähltesFeld.getFill())){
					nächsterAktiverTurm = Spielbrett.getTürme()[i].getKoordinaten();
					Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					möglicheFelderAnzeigen(nächsterAktiverTurm);
					if(Spielbrett.getMöglicheFelder().size()==0){	
						Spielbrett.getTürme()[k].setErsterBlockierenderTurm(true);
					} else{
						blockierendeTürmeZurücksetzen();
					}
					getErsterBlockierenderTurm();
				}
			}	
		}else{		// Nächster gegnerischer Turm falls der vorherige Turm weiss war 
//		if(getTurmFarbe(Spielbrett.getAktiverTurmKoordinaten()).equals(Color.WHITE)){	
			for (int i = 0; i < Spielbrett.getTürme().length; i++){ 
//				Spielbrett.getTürme()[i].setAktiverTurm(false);
				Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);	//Formatierung aller Türme zurücksetzen
				if (Spielbrett.getTürme()[i].getStroke().equals(Color.BLACK) 
						&& Spielbrett.getTürme()[i].getFill().equals(ausgewähltesFeld.getFill())){		
					nächsterAktiverTurm = Spielbrett.getTürme()[i].getKoordinaten();
					möglicheFelderAnzeigen(nächsterAktiverTurm);
					Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					if(Spielbrett.getMöglicheFelder().size()==0){	
						Spielbrett.getTürme()[k].setErsterBlockierenderTurm(true);
					} else{
						blockierendeTürmeZurücksetzen();
					}
					getErsterBlockierenderTurm();
				}
			}	
		}
		logger.info("Koordinaten nächster aktiver Turm: "+nächsterAktiverTurm[0]+nächsterAktiverTurm[1]);
		return nächsterAktiverTurm;
	}
		
	/** Nächster gegnerischer Turm definieren im Fall einer Blockade
	 * @param aktueller nächsterAktiverTurm
	 * @return Koordinaten des nächsten gegnerischen Turms
	 * @author Raphaela Rey
	 */
	public int[] setNächsterGegnerischerTurmBlockade(int[]nächsterAktiverTurm){ 
		turmStrokeWidthZurücksetzen();
		System.out.println("setnächstergegnerischerturmblockade ausgeführt");
		if(getTurmFarbe(nächsterAktiverTurm).equals(Color.BLACK)){
			for (int m = 0; m < Spielbrett.getTürme().length; m++){
				Feld aktivesFeld = Spielbrett.getFelder()[nächsterAktiverTurm[0]][nächsterAktiverTurm[1]];
				if (Spielbrett.getTürme()[m].getStroke().equals(Color.WHITE)
						&& (Spielbrett.getTürme()[m].getFill().equals(aktivesFeld.getFill()))){
					nächsterAktiverTurm = Spielbrett.getTürme()[m].getKoordinaten(); 
//					möglicheFelderAnzeigen(nächsterAktiverTurm);
					Spielbrett.getTürme()[m].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
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
//					möglicheFelderAnzeigen(nächsterAktiverTurm);
					Spielbrett.getTürme()[m].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
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
	public void spielZurücksetzen(){ 
		möglicheFelderLeeren(); 
		// Gewinner löschen, alle Türme vom Spielbrett entfernen und die Felder freigeben
		gewinnerZurücksetzen();
		spielbrett.getPane().getChildren().removeAll(Spielbrett.getTürme());
		for (int i = 0; i < Spielbrett.getFelder().length; i++){
    		for (int j = 0; j < Spielbrett.getFelder().length; j++){
    			Spielbrett.getFelder()[i][j].setFeldBesetzt(false);
    		}
		}	
		Spielbrett.setTurmBewegt(false);
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
	}
	
	/** ArrayList mögliche Felder leeren
	 * @return geleerte ArrayList
	 * @author Raphaela Rey
	 */
	public ArrayList<int[]> möglicheFelderLeeren(){
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
	public ArrayList<int[]> möglicheFelderAnzeigen(int[] turmKoordinaten){		
		möglicheFelderLeeren();
		möglicheFelderHinzufügen(turmKoordinaten);
		return Spielbrett.getMöglicheFelder();
	}
	
	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		ClientModel.ip = ip;
	}

	/** Mögliche Felder der ArrayList hinzufügen (Supportmethode)
	 * @param turmKoordinaten
	 * @author Raphaela Rey
	 */
	private void möglicheFelderHinzufügen(int[] turmKoordinaten){
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
	public String getMeldung() {
		return meldung;
	}
	public void setMeldung(String meldung) {
		this.meldung = meldung;
	}

}
