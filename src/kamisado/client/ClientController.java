package kamisado.client;

import javafx.event.EventHandler; 
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Turm;

//TODO Wie wird definiert, wer die schwarzen Türme hat? Derjenige, der am wenigsten oft gespielt hat? Und wenn gleich dann zufällig?

public class ClientController {
	 	
	final private ClientModel clientModel;
	final private ClientView view;
	final private Spielbrett spielbrett;	
	
	// Konstruktor
	public ClientController(ClientModel clientModel, ClientView view) {
		this.clientModel = clientModel;
		this.view = view;
		spielbrett = view.spielbrett;
		
		// Schwarze Türme für Spielbeginn aktivieren
		for (int i = 0; i < Spielbrett.getTürme().length; i++){				
			Turm t = Spielbrett.getTürme()[i];				
			t.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event){
					if(spielbrett.istTurmBewegt()==false && t.getStroke()==Color.BLACK){	
						clientModel.turmStrokeWidthZurücksetzen();
						t.setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
						spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());
						clientModel.möglicheFelderAnzeigen(spielbrett.getAktiverTurmKoordinaten());
					}				
  				}
			});
		}
		
		// Turm bewegen, überprüfen ob jemand die Grundlinie des Gegners erreicht hat, ob jemand gewonnen hat und das Spiel zurücksetzen			
		for(int i = 0; i < spielbrett.getFelder().length; i++){
			for (int j = 0; j < spielbrett.getFelder().length; j++){
				Feld ausgewähltesFeld = spielbrett.getFelder()[i][j];
				ausgewähltesFeld.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event){
						if (spielbrett.getMöglicheFelder().contains(ausgewähltesFeld.getKoordinaten())){
							int[] nächsterAktiverTurm = new int[2];
							for (int k = 0; k < Spielbrett.getTürme().length; k++){
								if(clientModel.koordVergleich(Spielbrett.getTürme()[k].getKoordinaten(), spielbrett.getAktiverTurmKoordinaten())){ //aktiver Turm herausfinden
									clientModel.turmBewegen(ausgewähltesFeld, k);
									Spielbrett.setBlockadenCounter(0); // TODO am richtigen ort??
									// Überprüfen, ob es einen Gewinner gibt 
									spielbrett.setGewinner(clientModel.gewinnerDefinieren(ausgewähltesFeld));
									// Zukünftiger gegnerischer Turm definieren und mögliche Felder anzeigen (sofern das Spiel nicht schon beendet ist)
									if(spielbrett.getGewinner()==null){
										nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurm(k, ausgewähltesFeld, nächsterAktiverTurm);							
									}
								}	 
							}
							// Zukünftiger gegnerischer Turm definieren im Fall einer Blockade 			
							if(spielbrett.getGewinner()==null && Spielbrett.isBlockiert()==true){
								nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurmBlockade(nächsterAktiverTurm);	 
								if(Spielbrett.getBlockadenVerursacher()==Color.BLACK){
									System.out.println("Schwarz hat Blockade verursacht, schwarz ist wieder am Zug"); // TODO Carmen Blockadenmeldung
								} else if (Spielbrett.getBlockadenVerursacher()==Color.WHITE){
									System.out.println("Weiss hat Blockade verursacht, weiss ist wieder am Zug");// TODO Carmen Blockadenmeldung
								}
							} 			
							// Völliger Stillstand				// TODO Testen
							if(Spielbrett.getBlockadenCounter()==2){
								System.out.println("Totale Blockade");
							}
							
							// Überprüfen, wer gewonnen hat und die entsprechende Meldung anzeigen
							if(spielbrett.getGewinner() == Color.BLACK){
								System.out.println("schwarz gewinnt"); 
								// TODO Carmen Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur das Spielbrett zurückgesetzt)
							} else if(spielbrett.getGewinner() == Color.WHITE){
								System.out.println("Weiss gewinnt"); 
								// TODO Carmen Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur das Spielbrett zurückgesetzt)
							}
							
							// Spiel zurücksetzen nach Gewinn 
							if(spielbrett.getGewinner()!=null){
								clientModel.spielZurücksetzen(spielbrett.getMöglicheFelder(), spielbrett.getFelder(), Spielbrett.getTürme());	
							}
							// Koordinaten des nächsten aktiven Turms ausserhalb der for-Schleife definieren
							spielbrett.setAktiverTurmKoordinaten(nächsterAktiverTurm);							
						}
					}					
				});				
			}
		}
	}
}