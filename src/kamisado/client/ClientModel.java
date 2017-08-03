package kamisado.client;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;

public class ClientModel {
	
	protected Spielbrett spielbrett;

	
	protected Socket client;
	private boolean amLaufen = true;
	private String hostName;
	private int port = 444;
	
	//protected ObjectInputStream vonServer;
	//protected ObjectOutputStream anServer;
	private final Logger logger = Logger.getLogger("");
	
	public ClientModel() {
		try{
			this.hostName= InetAddress.getLocalHost().getHostName();
			Verbinden(hostName, port);
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public void Verbinden(String hostName, int port) {
		
		try{
			this.hostName= InetAddress.getLocalHost().getHostName();
			
			//Verbindung mit Server herstellen
			this.client = new Socket(hostName, port);
			logger.info(hostName + " über Port" + port + " verbunden");
			
			//Thread erstellen
			Runnable a = new Runnable() {
				@Override
				public void run() {
					try{
						while(amLaufen == true){
							SendenEmpfangen.Empfangen(client);
						}
					}catch (Exception e){
						logger.info(e.toString());
					}
				}
			}; 
			Thread b = new Thread(a);
			b.start();
			logger.info("thread gestartet");
		 
		}catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public void clientAnhalten(){
		if(client != null){
			try{
				client.close();
				logger.info("client Thread beendet");
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
		
		
	}
	
	// Spielbrett erstellen
	public void setSpielbrett(Spielbrett spielbrett){
		this.spielbrett = spielbrett;
	}
	
	// Farbe (schwarz/weiss) eines Turms (basierend auf Koordinaten) herausfinden
	public Color getTurmFarbe(int[]turmKoordinaten, Turm[]türme){
		for (int i = 0; i < türme.length; i++){
			if(koordVergleich(türme[i].getKoordinaten(), turmKoordinaten)==true // ausgewählter Turm herausfinden
					&& (türme[i].getStroke()==Color.BLACK)){				// herausfinden ob der Turm schwarz ist
				return Color.BLACK;
			}
		}
		return Color.WHITE;
	}
	
	// Zukünftiger gegnerischer Turm definieren
	public int[] setNächsterGegnerischerTurm(int k, Feld ausgewähltesFeld, int[]nächsterAktiverTurm){
		if(spielbrett.getMöglicheFelder().size()==0){		
			ausgewähltesFeld = spielbrett.getFelder()[spielbrett.getTürme()[k].getKoordinaten()[0]][spielbrett.getTürme()[k].getKoordinaten()[1]];
		}
		if(spielbrett.getTürme()[k].getStroke()==Color.BLACK){
			for (int i = 0; i < spielbrett.getTürme().length; i++){
				spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);	//Formatierung aller Türme zurücksetzen
				if (spielbrett.getTürme()[i].getStroke()==Color.WHITE
						&& spielbrett.getTürme()[i].getFill()==ausgewähltesFeld.getFill()){
					möglicheFelderAnzeigen(spielbrett.getTürme()[i].getKoordinaten());
					nächsterAktiverTurm = spielbrett.getTürme()[i].getKoordinaten();
					spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
				}
			}	
		}else{
			for (int i = 0; i < spielbrett.getTürme().length; i++){
				spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);	//Formatierung aller Türme zurücksetzen
				if (spielbrett.getTürme()[i].getStroke()==Color.BLACK
						&& spielbrett.getTürme()[i].getFill()==ausgewähltesFeld.getFill()){
					möglicheFelderAnzeigen(spielbrett.getTürme()[i].getKoordinaten());
					nächsterAktiverTurm = spielbrett.getTürme()[i].getKoordinaten();
					spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
				}
			}	
		}
		
		return nächsterAktiverTurm;
	}
		
	// Ganzes Spiel zurücksetzen, nachdem jemand gewonnen hat
	public void spielZurücksetzen(ArrayList<int[]> möglicheFelder, Feld[][]felder, Turm[] türme){ 
		möglicheFelderLeeren(möglicheFelder, felder);
		// Alle Türme vom Spielbrett entfernen und die Felder freigeben
		spielbrett.getPane().getChildren().removeAll(türme);
		for (int i = 0; i < spielbrett.getFelder().length; i++){
    		for (int j = 0; j < spielbrett.getFelder().length; j++){
    			spielbrett.getFelder()[i][j].setFeldBesetzt(false);
    		}
		}	
		spielbrett.setTurmBewegt(false);
		// Die Türme an ihren ursprünglichen Platz setzen
		for(int p = 0; p < spielbrett.getTürme().length; p++){
			if(spielbrett.getTürme()[p].getStroke()==Color.BLACK){
				for(int l = 0; l < spielbrett.getFelder()[7].length; l++){
					if(spielbrett.getTürme()[p].getFill()==spielbrett.getFelder()[l][7].getFill()){
						spielbrett.getTürme()[p].setKoordinaten(spielbrett.getFelder()[l][7].getKoordinaten());
						spielbrett.getTürme()[p].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
						spielbrett.getPane().add(spielbrett.getTürme()[p], l, 7);	
						spielbrett.getFelder()[l][7].setFeldBesetzt(true);
					}
				}
			}else{
				for(int l = 0; l < spielbrett.getFelder()[0].length; l++){
					if(spielbrett.getTürme()[p].getFill()==spielbrett.getFelder()[l][0].getFill()){
						spielbrett.getTürme()[p].setKoordinaten(spielbrett.getFelder()[l][0].getKoordinaten());
						spielbrett.getTürme()[p].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
						spielbrett.getPane().add(spielbrett.getTürme()[p], l, 0);	
						spielbrett.getFelder()[l][0].setFeldBesetzt(true);
					}
				}
			}
		}	
	}
	
	// Überpüfen, ob zwei Arrays gleich sind
	public boolean koordVergleich(int[] koord1, int[] koord2){
		if(koord1[0]==koord2[0] == true && koord1[1]==koord2[1]== true){
			return true;
		}
		return false;
	}
	
	// ArrayList mögliche Felder leeren 
	public ArrayList<int[]> möglicheFelderLeeren(ArrayList<int[]> möglicheFelder, Feld[][]felder){
		ArrayList<int[]> toRemove = new ArrayList<>();
		Iterator<int[]> iter = möglicheFelder.iterator();
		while (iter.hasNext()){
			int[]koords = iter.next();
			int xKoord = koords[0];
			int yKoord = koords[1];
			felder[xKoord][yKoord].setStroke(Color.BLACK);
			felder[xKoord][yKoord].setStrokeWidth(1);
			felder[xKoord][yKoord].setStrokeType(StrokeType.CENTERED);	
			toRemove.add(koords);
		}
		möglicheFelder.removeAll(toRemove);
		return möglicheFelder;		
	}
	
	// Mögliche Felder (geradeaus, diagonal rechts und diagonal links) anzeigen
	 	public ArrayList<int[]> möglicheFelderAnzeigen(int[] turmKoordinaten){		
	 		// ArrayList der bestehenden Liste löschen, damit nur die aktuellen möglichen Felder gespeichert sind
	 		möglicheFelderLeeren(spielbrett.getMöglicheFelder(), spielbrett.getFelder());
	 		// Mögliche gerade/diagonale Felder für schwarze und weisse Türme der ArrayList hinzufügen
	 		möglicheFelderHinzufügen(turmKoordinaten, spielbrett.getTürme(), spielbrett.getFelder(), spielbrett.getMöglicheFelder());
	 		return spielbrett.getMöglicheFelder();
	 	}
	
	// Mögliche Felder der ArrayList hinzufügen			
	private void möglicheFelderHinzufügen(int[] turmKoordinaten, 
			Turm[]türme, Feld[][]felder, ArrayList<int[]> möglicheFelder){
		int xKoords = turmKoordinaten[0];											
		int yKoords = turmKoordinaten[1];	
		// TODO Löschen wenn nicht funktioniert
//		for(int i = 1; i < felder[0].length; i++){
//			Feld möglichGeradeaus;
//			Feld möglichDiagRechts;
//			Feld möglichDiagLinks;
//			try {
//				möglichGeradeaus = felder[xKoords][yKoords-i];
//			} catch (ArrayIndexOutOfBoundsException e) {
//				break;
//			}
//			try {
//				möglichDiagRechts = felder[xKoords+i][yKoords-i];
//			} catch (ArrayIndexOutOfBoundsException e) {
//				break;
//			}
//			try {
//				möglichDiagLinks = felder[xKoords-i][yKoords-i];
//			} catch (ArrayIndexOutOfBoundsException e) {
//				break;
//			}
//			
//			Feld[] möglich = {möglichGeradeaus, möglichDiagRechts, möglichDiagLinks};
//			
//			for(int j = 0; j < möglich.length; j++){
//				if(möglich[j].istFeldBesetzt()==true){
//					bereitsEinFeldBesetzt = true;
//				}
//			}
//			for(int k = 0; k < möglich.length; k++){
//				if(möglich[k].istFeldBesetzt()==false
//					&& bereitsEinFeldBesetzt==false){
//					möglicheFelder.add(möglich[k].getKoordinaten());
//					möglich[k].setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
//					möglich[k].setStrokeType(StrokeType.INSIDE);
//				}
//			}	
//		}
		
		
		// Mögliche Felder falls der Turm schwarz ist				
		if((getTurmFarbe(turmKoordinaten, türme)==Color.BLACK)){
			// Mögliche Felder geradeaus
			boolean bereitsEinFeldBesetzt = false;

			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichGeradeaus = felder[xKoords][yKoords-i];
					if(möglichGeradeaus.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichGeradeaus.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichGeradeaus.getKoordinaten());
						möglichGeradeaus.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichGeradeaus.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;
			
			// Mögliche Felder rechts diagonal
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichDiagRechts = felder[xKoords+i][yKoords-i];
					if(möglichDiagRechts.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagRechts.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichDiagRechts.getKoordinaten());
						möglichDiagRechts.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagRechts.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;

			// Mögliche Felder links diagonal 
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichDiagLinks = felder[xKoords-i][yKoords-i];
					if(möglichDiagLinks.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagLinks.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichDiagLinks.getKoordinaten());
						möglichDiagLinks.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagLinks.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
		}
		
		// Mögliche Felder falls der Turm weiss ist
		if((getTurmFarbe(turmKoordinaten, türme)==Color.WHITE)){
			boolean bereitsEinFeldBesetzt = false;
			// Mögliche Felder geradeaus
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichGeradeaus = felder[xKoords][yKoords+i];
					if(möglichGeradeaus.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichGeradeaus.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichGeradeaus.getKoordinaten());
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
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichDiagRechts = felder[xKoords+i][yKoords+i];
					if(möglichDiagRechts.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagRechts.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichDiagRechts.getKoordinaten());
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
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichDiagLinks = felder[xKoords-i][yKoords+i];
					if(möglichDiagLinks.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagLinks.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichDiagLinks.getKoordinaten());
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

	
		

