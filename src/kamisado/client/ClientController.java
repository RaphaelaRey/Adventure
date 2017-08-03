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
		
		// Turm bewegen					
		for(int i = 0; i < spielbrett.getFelder().length; i++){
			for (int j = 0; j < spielbrett.getFelder().length; j++){
				Feld ausgewähltesFeld = spielbrett.getFelder()[i][j];
				int xKoords = i;
				int yKoords = j;
				ausgewähltesFeld.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event){
						if (spielbrett.getMöglicheFelder().contains(ausgewähltesFeld.getKoordinaten())){
							//dem Turm seine neuen Koordinaten mitteilen, ihn löschen und am neuen Ort der Gridpane hinzufügen
							int[] nächsterAktiverTurm = new int[2];
							for (int k = 0; k < spielbrett.getTürme().length; k++){
								if(clientModel.koordVergleich(spielbrett.getTürme()[k].getKoordinaten(), spielbrett.getAktiverTurmKoordinaten())){ //aktiver Turm herausfinden
									// den zu bewegenden Turm von der Gridpane entfernen und das Feld freigeben
									spielbrett.getPane().getChildren().remove(spielbrett.getTürme()[k]);
									spielbrett.getFelder()[spielbrett.getAktiverTurmKoordinaten()[0]][spielbrett.getAktiverTurmKoordinaten()[1]].setFeldBesetzt(false);

									// neue Koordinaten des Turms setzen, den Turm der Gridpane hinzufügen und das Feld besetzen
									spielbrett.getTürme()[k].setKoordinaten(ausgewähltesFeld.getKoordinaten());
									spielbrett.setAktiverTurmKoordinaten(ausgewähltesFeld.getKoordinaten());
									spielbrett.getPane().add(spielbrett.getTürme()[k], xKoords, yKoords);
									spielbrett.getFelder()[xKoords][yKoords].setFeldBesetzt(true);
									
									spielbrett.setTurmBewegt(true);											
									
									// Gewinner? 
									for(int l = 0; l < spielbrett.GEWINNERFELDERSCHWARZ.length; l++){
										int [] koordGewinnerFeld = {spielbrett.GEWINNERFELDERSCHWARZ[l][0], spielbrett.GEWINNERFELDERSCHWARZ[l][1]};
										if(clientModel.koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
											ausgewähltesFeld.setFill(Color.ALICEBLUE); 	
											// schwarz gewinnt -> TODO Carmen Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur ein Feld eingefärbt)
											spielbrett.setSpielBeendet(true);		
										}
									}	
									for(int m = 0; m < spielbrett.GEWINNERFELDERWEISS.length; m++){
										int [] koordGewinnerFeld = {spielbrett.GEWINNERFELDERWEISS[m][0], spielbrett.GEWINNERFELDERWEISS[m][1]};
										if(clientModel.koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
											ausgewähltesFeld.setFill(Color.ALICEBLUE);
											// weiss gewinnt -> TODO Carmen Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur ein Feld eingefärbt)
											spielbrett.setSpielBeendet(true);		
										}
									}	
									
									// Spiel zurücksetzen nach Gewinn TODO Überprüfen
									if(spielbrett.istSpielBeendet()==true){
										clientModel.spielZurücksetzen(spielbrett.getMöglicheFelder(), spielbrett.getFelder(), spielbrett.getTürme());
										
									}
									
									// Zukünftiger gegnerischer Turm definieren und mögliche Felder anzeigen (sofern das Spiel nicht schon beendet ist)
									if(spielbrett.istTurmBewegt()==true){
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
				
		// Türme aktivieren
		for (int i = 0; i < spielbrett.getTürme().length; i++){				
			Turm t = spielbrett.getTürme()[i];				
			t.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event){
					if(spielbrett.istTurmBewegt()==false){	
						for (int i = 0; i < spielbrett.getTürme().length; i++){
							spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
						}	
						t.setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
						spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());
						clientModel.möglicheFelderAnzeigen(spielbrett.getAktiverTurmKoordinaten());
					}				
  				}
			});
		}
		
		// TODO Totaler Stillstand, Gewinner definieren (+ Carmen Gewinnermeldung)	
		if(spielbrett.getMöglicheFelder().size()==0 && spielbrett.istTurmBewegt()==true){
		
		}
	}
}