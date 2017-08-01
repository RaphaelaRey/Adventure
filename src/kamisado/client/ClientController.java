package kamisado.client;

import java.util.Iterator;

import javafx.event.EventHandler; 
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Turm;

public class ClientController {
	
	final private Model model;
	final private ClientView view;
	
	Spielbrett spielbrett;		
		
	public ClientController(Model model, ClientView view) {
		this.model = model;
		this.view = view;
		spielbrett = view.spielbrett;
		// Turm bewegen					
		for(int i = 0; i < spielbrett.getFelder().length; i++){
			for (int j = 0; j < spielbrett.getFelder().length; j++){
				Feld f = spielbrett.getFelder()[i][j];
				int xKoords = i;
				int yKoords = j;
				f.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event){
						if (spielbrett.getMöglicheFelder().contains(f.getKoordinaten())){
							//dem Turm seine neuen Koordinaten mitteilen, ihn löschen und am neuen Ort der Gridpane hinzufügen
							for (int i = 0; i < spielbrett.getTürme().length; i++){
								if(model.koordVergleich(spielbrett.getTürme()[i].getKoordinaten(), spielbrett.getAktiverTurmKoordinaten())){
									// alte Koordinaten des Turms setzen, den Turm von der Gridpane entfernen und das Feld freigeben
									spielbrett.getTürme()[i].setKoordinaten(f.getKoordinaten());
									spielbrett.getPane().getChildren().remove(spielbrett.getTürme()[i]);
									spielbrett.getFelder()[spielbrett.getAktiverTurmKoordinaten()[0]][spielbrett.getAktiverTurmKoordinaten()[1]].setFeldBesetzt(false);

									// neue Koordinaten des Turms setzen, den Turm der Gridpane hinzufügen und das Feld besetzen
									spielbrett.setAktiverTurmKoordinaten(f.getKoordinaten());
									spielbrett.getPane().add(spielbrett.getTürme()[i], xKoords, yKoords);
									spielbrett.getFelder()[spielbrett.getAktiverTurmKoordinaten()[0]][spielbrett.getAktiverTurmKoordinaten()[1]].setFeldBesetzt(true);
									
//									// neuer aktiver Turm der gegnerischen Farbe setzen: 		(feldfarbe wird zur neuen Turmfarbe) NICHT HIER
									if(spielbrett.getTürme()[i].getStroke()==Color.BLACK){
									for (int k = 0; k < spielbrett.getTürme().length; k++){
										spielbrett.getTürme()[k].setStrokeWidth(3);		// Formatierung zurücksetzen
										
										if(spielbrett.getTürme()[k].getStroke()==Color.WHITE 
												&& spielbrett.getTürme()[k].getFill()==spielbrett.getFelder()[xKoords][yKoords].getFill()){
											spielbrett.getTürme()[k].setStrokeWidth(10);
//											view.setAktiverTurmKoordinaten(view.türme[k].getKoordinaten());
//											view.möglicheFelderAnzeigen(view.türme[k].getKoordinaten());  // TODO Verzögerung?
										}
										
//									if(spielbrett.türme[i].getStroke()==Color.WHITE){
//										for (int m = 0; m < spielbrett.türme.length; m++){
//											spielbrett.türme[m].setStrokeWidth(3);		// Formatierung zurücksetzen
//											
//											if(spielbrett.türme[m].getStroke()==Color.BLACK 
//													&& spielbrett.türme[m].getFill()==spielbrett.felder[xKoords][yKoords].getFill()){
//												spielbrett.türme[m].setStrokeWidth(10);
////												view.setAktiverTurmKoordinaten(view.türme[k].getKoordinaten());
////												view.möglicheFelderAnzeigen(view.türme[k].getKoordinaten());  
//									}

											}
										}

								}
							}

						}
					}
				});
			}
		}
		
		
		 
		// Türme aktivieren
		for (int i = 0; i < spielbrett.getTürme().length; i++){
			Turm t = spielbrett.getTürme()[i];				// wenn array aktiverturmkoord leer, denn setonmouseclicked, süsch eifach so türm aktiviere
			t.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event){
					for (int i = 0; i < spielbrett.getTürme().length; i++){
						spielbrett.getTürme()[i].setStrokeWidth(3);		// Formatierung zurücksetzen
					}
//					if (view.getAktiverTurmKoordinaten().length==0){			// wieso ist die länge hier nicht null?
//						// TODO darf nur ausgeführt werden wenn getaktiverturmkoord null iyf
//					}
					t.setStrokeWidth(10);
					spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());
					spielbrett.möglicheFelderAnzeigen(spielbrett.getAktiverTurmKoordinaten());
  				}
			});
		}
		

	}
}