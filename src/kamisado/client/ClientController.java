package kamisado.client;

import javafx.event.EventHandler; 
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Turm;


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
		for (int i = 0; i < spielbrett.getTürme().length; i++){				
			Turm t = spielbrett.getTürme()[i];				
			t.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event){
					spielbrett.setSpielBeendet(false);
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
							for (int k = 0; k < spielbrett.getTürme().length; k++){
								if(clientModel.koordVergleich(spielbrett.getTürme()[k].getKoordinaten(), spielbrett.getAktiverTurmKoordinaten())){ //aktiver Turm herausfinden
									clientModel.turmBewegen(ausgewähltesFeld, k);
									// Gewinner? 
									for(int l = 0; l < Spielbrett.GEWINNERFELDERSCHWARZ.length; l++){
										int [] koordGewinnerFeld = {Spielbrett.GEWINNERFELDERSCHWARZ[l][0], Spielbrett.GEWINNERFELDERSCHWARZ[l][1]};
										if(clientModel.koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
											// schwarz gewinnt -> TODO Carmen Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur das Spielbrett zurückgesetzt)
											spielbrett.setSpielBeendet(true);		
										}
									}	
									for(int m = 0; m < Spielbrett.GEWINNERFELDERWEISS.length; m++){
										int [] koordGewinnerFeld = {Spielbrett.GEWINNERFELDERWEISS[m][0], Spielbrett.GEWINNERFELDERWEISS[m][1]};
										if(clientModel.koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
											// weiss gewinnt -> TODO Carmen Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur das Spielbrett zurückgesetzt)
											spielbrett.setSpielBeendet(true);		
										}
									}
									// Spiel zurücksetzen nach Gewinn // TODO Wie wird definiert, wer die schwarzen Türme hat?
																			// Derjenige, der am wenigsten oft gespielt hat? Und wenn gleich dann zufällig?
									if(spielbrett.istSpielBeendet()==true){
										clientModel.spielZurücksetzen(spielbrett.getMöglicheFelder(), spielbrett.getFelder(), spielbrett.getTürme());	
									}
									
									// Zukünftiger gegnerischer Turm definieren und mögliche Felder anzeigen (sofern das Spiel nicht schon beendet ist)
									if(spielbrett.istSpielBeendet()==false){
										nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurm(k, ausgewähltesFeld, nächsterAktiverTurm);							
									}
								}	
							}
							spielbrett.setAktiverTurmKoordinaten(nächsterAktiverTurm);							
						}
					}					
				});				
			}
		}
					
		// TODO Totaler Stillstand, Gewinner definieren (+ Carmen Gewinnermeldung sobald gelöst)	
		if(spielbrett.getMöglicheFelder().size()==0 && spielbrett.istTurmBewegt()==true){
		
		}
	}
}