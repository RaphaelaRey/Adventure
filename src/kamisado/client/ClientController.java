package kamisado.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kamisado.client.infofenster.InfofensterController;
import kamisado.client.infofenster.InfofensterView;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Turm;

//TODO Team Wie wird definiert, wer die schwarzen Türme hat? Derjenige, der am wenigsten oft gespielt hat? Und wenn gleich dann zufällig?

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
					if(Spielbrett.istTurmBewegt()==false && t.getStroke().equals(Color.BLACK)){	
						clientModel.turmStrokeWidthZurücksetzen();
						t.setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
						Spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());	// TODO Raphaela weiss das der andere Client?
						clientModel.möglicheFelderAnzeigen(Spielbrett.getAktiverTurmKoordinaten());
						
					}				
  				}
			});
		}
		
		// Turm bewegen, überprüfen ob jemand die Grundlinie des Gegners erreicht hat, ob jemand gewonnen hat und das Spiel zurücksetzen			
		for(int i = 0; i < Spielbrett.getFelder().length; i++){
			for (int j = 0; j < Spielbrett.getFelder().length; j++){
				Feld ausgewähltesFeld = Spielbrett.getFelder()[i][j];
				ausgewähltesFeld.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event){
						if (Spielbrett.getMöglicheFelder().contains(ausgewähltesFeld.getKoordinaten())){
							int[] nächsterAktiverTurm = new int[2];
							for (int k = 0; k < Spielbrett.getTürme().length; k++){
								if(clientModel.koordVergleich(Spielbrett.getTürme()[k].getKoordinaten(), Spielbrett.getAktiverTurmKoordinaten())){ //aktiver Turm herausfinden
									clientModel.turmBewegen(ausgewähltesFeld, k);
									// Überprüfen, ob es einen Gewinner gibt 
									spielbrett.setGewinner(clientModel.gewinnerDefinieren(ausgewähltesFeld));
									// Zukünftiger gegnerischer Turm definieren und mögliche Felder anzeigen (sofern das Spiel nicht schon beendet ist)
									if(spielbrett.getGewinner()==null){
										nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurm(k, ausgewähltesFeld, nächsterAktiverTurm);		
										System.out.println("nächsterAktiverturm=clientmodel.setnächstergegnerischerturm ausgeführt");
									}
								}	 
							}
							// Zukünftiger gegnerischer Turm definieren im Fall einer Blockade 			
							if(spielbrett.getGewinner()==null && Spielbrett.isBlockiert()==true){
								nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurmBlockade(nächsterAktiverTurm);	 
								if(Spielbrett.getBlockadenVerursacher().equals(Color.BLACK)){
									//Blockadenmeldung
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.BlockadeMeldungSchwarz);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								} else if (Spielbrett.getBlockadenVerursacher().equals(Color.WHITE)){
									//Blockadenmeldung
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.BlockadeMeldungWeiss);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								}
							} 			
							// Völliger Stillstand TODO Raphaela kontrollieren
							if(Spielbrett.getBlockadenCounter()==2){
								if(Spielbrett.getBlockadenVerursacher().equals(Color.BLACK)){
									spielbrett.setGewinner(Color.WHITE);
									System.out.println("Schwarz hat totalen Stillstand verursacht, weiss gewinnt"); 
									// Gewinnermeldung bei völligem Stillstand
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungStillstandWeiss);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								} else{
									spielbrett.setGewinner(Color.BLACK);
									System.out.println("Weiss hat totalen Stillstand verursacht, schwarz gewinnt");
									//Gewinnermeldung bei völligem Stillstand
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungStillstandSchwarz);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								}
							}
							 
							// Überprüfen, wer gewonnen hat und die entsprechende Meldung anzeigen
							if(spielbrett.getGewinner()==Color.BLACK){
								//Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur das Spielbrett zurückgesetzt)
								Stage stage = new Stage();
								InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungSchwarz);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
							} else if(spielbrett.getGewinner()==Color.WHITE){
								//Gewinnermeldung 
								Stage stage = new Stage();
								InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungWeiss);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
							}
							
							// Spiel zurücksetzen nach Gewinn 
							if(spielbrett.getGewinner()!=null){
								clientModel.spielZurücksetzen();	
							}
							// Koordinaten des nächsten aktiven Turms ausserhalb der for-Schleife definieren
							
//							Spielbrett.setAktiverTurmKoordinaten(nächsterAktiverTurm);	
							
							clientModel.getTurm(nächsterAktiverTurm).setAktiverTurm(true);
							clientModel.TürmeSenden();

						}

					}					
				});				
			}
		}
		
		view.menuDateiAbmelden.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				clientModel.clientAnhalten();
				
			}
			
		});
	}
	
	
}