package kamisado.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javafx.event.EventHandler; 
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Turm;

/**
 * @author Raphaela Rey
 */

public class ClientController {
	 	
	final private ClientModel clientModel;
	final private ClientView view;
	
	Spielbrett spielbrett;	
	
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
									
									// Gewinner? // TODO Gewinnermeldung inkl. Frage ob nochmals gespielt werden will
																		//von Carmen einbauen, im Moment wird nur ein Feld eingefärbt
									for(int l = 0; l < spielbrett.getGewinnerFelderSchwarz().length; l++){
										int [] koordGewinnerFeld = {spielbrett.getGewinnerFelderSchwarz()[l][0], spielbrett.getGewinnerFelderSchwarz()[l][1]};
										if(clientModel.koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
											ausgewähltesFeld.setFill(Color.ALICEBLUE); 			
											spielbrett.setSpielBeendet(true);		
										}
									}	
									for(int m = 0; m < spielbrett.getGewinnerFelderWeiss().length; m++){
										int [] koordGewinnerFeld = {spielbrett.getGewinnerFelderWeiss()[m][0], spielbrett.getGewinnerFelderWeiss()[m][1]};
										if(clientModel.koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
											ausgewähltesFeld.setFill(Color.ALICEBLUE);
											spielbrett.setSpielBeendet(true);		
										}
									}	
									
									// Spiel zurücksetzen nach Gewinn
									if(spielbrett.istSpielBeendet()==true){
										clientModel.spielZurücksetzen(spielbrett.getMöglicheFelder(), spielbrett.getFelder(), spielbrett.getTürme());
										
									}
									
									// zukünftiger gegnerischer Turm definieren und mögliche Felder anzeigen (sofern das Spiel nicht schon beendet ist)
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
					for (int i = 0; i < spielbrett.getTürme().length; i++){
						spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
					}
					if(spielbrett.istTurmBewegt()==false){		// TODO kommentare weg 
						t.setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
						spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());
						clientModel.möglicheFelderAnzeigen(spielbrett.getAktiverTurmKoordinaten());
					}				
  				}
			});
		}
		
		// TODO  Totaler Stillstand, Gewinner definieren (+ Carmen Gewinnermeldung)
		
		
		if(spielbrett.getMöglicheFelder().size()==0 && spielbrett.istTurmBewegt()==true){
		
		}
	}
	
	
	
	public void propertyChange(PropertyChangeEvent event){
		String property = event.getPropertyName();
		if(spielbrett.istSpielBeendet()==true){
			System.out.println("spielbeendet");
		}
	}
}