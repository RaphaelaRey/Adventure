package kamisado.client;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;

public class Model {
	
	// TODO Gewinner/Verlierer definieren
	// TODO mögliche felder nur anzeigen wenn nicht besetzt
	
	protected Socket client;
	private boolean amLaufen = true;
	private String hostName;
	private int port = 444;
	
	//protected ObjectInputStream vonServer;
	//protected ObjectOutputStream anServer;
	private final Logger logger = Logger.getLogger("");
	
	public Model() {
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
			 
	// überpüfen, ob zwei Arrays gleich sind
	public boolean koordVergleich(int[] koord1, int[] koord2){
		return koord1[0]==koord2[0] && koord1[1]==koord2[1];
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
	
	// mögliche Felder der ArrayList hinzufügen
	public void möglicheFelderHinzufügen(int[] turmKoordinaten, 
			Turm[]türme, Feld[][]felder, ArrayList<int[]> möglicheFelder){
		int xKoords = turmKoordinaten[0];											
		int yKoords = turmKoordinaten[1];	
		
		// mögliche Felder falls der Turm schwarz ist				
		if((getTurmFarbe(turmKoordinaten, türme)==Color.BLACK)){
			// mögliche Felder geradeaus
			for(int i = 0; i < felder[0].length; i++){
				try {
					Feld möglichGeradeaus = felder[xKoords][yKoords-i];
//					while(möglichGeradeaus.istFeldBesetzt()==false){
					if(möglichGeradeaus.istFeldBesetzt()==false){
						möglicheFelder.add(möglichGeradeaus.getKoordinaten());
						möglichGeradeaus.setStrokeWidth(6);
						möglichGeradeaus.setStrokeType(StrokeType.INSIDE);
					} 
					
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			// mögliche Felder rechts diagonal 
			for(int i = 0; i < felder[0].length; i++){
				try {
					Feld möglichDiagRechts = felder[xKoords+i][yKoords-i];
					if(möglichDiagRechts.istFeldBesetzt()==false){
						möglicheFelder.add(möglichDiagRechts.getKoordinaten());
						möglichDiagRechts.setStrokeWidth(6);
						möglichDiagRechts.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			// mögliche Felder links diagonal 
			for(int i = 0; i < felder[0].length; i++){
				try {
					Feld möglichDiagLinks = felder[xKoords-i][yKoords-i];
					if(möglichDiagLinks.istFeldBesetzt()==false){
						möglicheFelder.add(möglichDiagLinks.getKoordinaten());
							möglichDiagLinks.setStrokeWidth(6);
							möglichDiagLinks.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
		}
		
		// mögliche Felder falls der Turm weiss ist
		if((getTurmFarbe(turmKoordinaten, türme)==Color.WHITE)){
			// mögliche Felder geradeaus
			for(int i = 0; i < felder[0].length; i++){
				try {
					Feld möglichGeradeaus = felder[xKoords][yKoords+i];
					if(möglichGeradeaus.istFeldBesetzt()==false){
						möglicheFelder.add(möglichGeradeaus.getKoordinaten());
						möglichGeradeaus.setStrokeWidth(6);
						möglichGeradeaus.setStroke(Color.WHITE);
						möglichGeradeaus.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			// mögliche Felder rechts diagonal 
			for(int i = 0; i < felder[0].length; i++){
				try {
					Feld möglichDiagRechts = felder[xKoords+i][yKoords+i];
					if(möglichDiagRechts.istFeldBesetzt()==false){
						möglicheFelder.add(möglichDiagRechts.getKoordinaten());
						möglichDiagRechts.setStrokeWidth(6);
						möglichDiagRechts.setStroke(Color.WHITE);
						möglichDiagRechts.setStrokeType(StrokeType.INSIDE);
					}
					} catch (ArrayIndexOutOfBoundsException e) {
						break;
						}
					}
			// mögliche Felder links diagonal 
			for(int i = 0; i < felder[0].length; i++){
				try {
					Feld möglichDiagLinks = felder[xKoords-i][yKoords+i];
					if(möglichDiagLinks.istFeldBesetzt()==false){
						möglicheFelder.add(möglichDiagLinks.getKoordinaten());
						möglichDiagLinks.setStrokeWidth(6);
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
