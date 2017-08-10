package kamisado.client;

import com.sun.media.jfxmedia.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kamisado.client.anmeldefenster.AnmeldefensterController;
import kamisado.client.anmeldefenster.AnmeldefensterView;
import kamisado.client.infofenster.InfofensterController;
import kamisado.client.infofenster.InfofensterView;
import kamisado.client.löschenfenster.LöschenfensterController;
import kamisado.client.löschenfenster.LöschenfensterView;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Turm;

//TODO Team Wie wird definiert, wer die schwarzen Türme hat? Derjenige, der am wenigsten oft gespielt hat? Und wenn gleich dann zufällig?
//TODO Team Spiel kann erst beginnen, wenn zwei Clients eingeloggt sind
public class ClientController {
	 	
	final private ClientModel clientModel;
	final private ClientView view;
	final private Spielbrett spielbrett;
	private AnmeldefensterController anmeldeController;
	
	// Konstruktor
	public ClientController(ClientModel clientModel, ClientView view, AnmeldefensterController anmeldeController) {
		this.clientModel = clientModel;
		this.view = view;
		this.anmeldeController=anmeldeController;
		spielbrett = view.spielbrett;  
				
		// Schwarze Türme für Spielbeginn aktivieren
		for (int i = 0; i < Spielbrett.getTürme().length; i++){				
			Turm t = Spielbrett.getTürme()[i];				
			t.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event){
//					if(clientModel.bereitsEinTurmBewegt()==false && t.getStroke().equals(Color.BLACK)){	
						clientModel.turmStrokeWidthZurücksetzen();
						t.setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
						Spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());	
						clientModel.möglicheFelderAnzeigen(Spielbrett.getAktiverTurmKoordinaten());						
//					}				
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
								Spielbrett.getTürme()[k].setAktiverTurm(false);
								if(clientModel.koordVergleich(Spielbrett.getTürme()[k].getKoordinaten(), Spielbrett.getAktiverTurmKoordinaten())){ //aktiver Turm herausfinden
									clientModel.turmBewegen(ausgewähltesFeld, k);
									// Überprüfen, ob es einen Gewinner gibt 
									clientModel.gewinnerDefinieren(ausgewähltesFeld);
									// Zukünftiger gegnerischer Turm definieren und mögliche Felder anzeigen (sofern das Spiel nicht schon beendet ist)
									if(clientModel.getGewinner()==null){
										nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurm(k, ausgewähltesFeld, nächsterAktiverTurm);		
										System.out.println("nächsterAktiverturm=clientmodel.setnächstergegnerischerturm ausgeführt");
									}
								}	 
							}
							// Zukünftiger gegnerischer Turm definieren im Fall einer Blockade 	
							if(clientModel.getGewinner()==null && clientModel.getErsterBlockierenderTurm()!=null
									&& clientModel.getZweiterBlockierenderTurm()==null){
								System.out.println("Blockade");
								nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurmBlockade(nächsterAktiverTurm);	 
								if(clientModel.getErsterBlockierenderTurm().equals(Color.BLACK)){
									//Blockadenmeldung
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.BlockadeMeldungSchwarz);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								} else if (clientModel.getErsterBlockierenderTurm().equals(Color.WHITE)){
									//Blockadenmeldung
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.BlockadeMeldungWeiss);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								}
							} 		

							// Völliger Stillstand TODO Raphaela kontrollieren
							if(clientModel.getZweiterBlockierenderTurm()!=null){
								System.out.println("Völliger Stillstand");
								if(clientModel.getZweiterBlockierenderTurm().equals(Color.BLACK)){
									System.out.println("Schwarz hat totalen Stillstand verursacht, weiss gewinnt"); 
									// TODO gewinner setzen
									Spielbrett.getTürme()[15].setGewinnerTurm(true);
									// Gewinnermeldung bei völligem Stillstand
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungStillstandWeiss);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								}else{
									System.out.println("Weiss hat totalen Stillstand verursacht, schwarz gewinnt");
									Spielbrett.getTürme()[0].setGewinnerTurm(true);
									//Gewinnermeldung bei völligem Stillstand
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungStillstandSchwarz);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								}
								
							}
							 
							// Überprüfen, wer gewonnen hat und die entsprechende Meldung anzeigen
							if(clientModel.getGewinner()==Color.BLACK){
								//Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur das Spielbrett zurückgesetzt)
								Stage stage = new Stage();
								InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungSchwarz);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
							} else if(clientModel.getGewinner()==Color.WHITE){
								//Gewinnermeldung 
								Stage stage = new Stage();
								InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungWeiss);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
							}
							
							// Spiel zurücksetzen nach Gewinn 
							if(clientModel.getGewinner()!=null){
								clientModel.TürmeSenden();
								// String senden
							}							
//							Spielbrett.setAktiverTurmKoordinaten(nächsterAktiverTurm);	
							
							if (clientModel.getGewinner()==null){
								clientModel.getTurm(nächsterAktiverTurm).setAktiverTurm(true); 
								clientModel.TürmeSenden();
							}
							clientModel.TürmeSenden();
							

						}

					}					
				});				
			}
		}
		
		//Nachdem sich der Benutzer abgemeldet hat, erscheint wieder das Anmeldefenster
		view.menuDateiAbmelden.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Stage stage = new Stage();
				InfofensterView iview = new InfofensterView(stage, view.abmelden);
				InfofensterController icontroller = new InfofensterController(iview);
				iview.start();
				stage.setAlwaysOnTop(true);
				view.stop();
				
//				Stage stage = new Stage();
//				AnmeldefensterView anmeldeView = new AnmeldefensterView(stage,anmeldeController, clientModel, view);
//				AnmeldefensterController anmeldeController = new AnmeldefensterController(anmeldeView, view, clientModel);
//				clientModel.clientAnhalten();
//				anmeldeView.start();
//				stage.setAlwaysOnTop(true);
//				clientModel.clientAnhalten();
				
			}
			
		});
		
		//Wenn der Benutzer auf löschen klickt, popt ein neues Fenster für die Eingaben zur Löschung eines Accoutns auf
		view.menuDateiLöschen.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Stage stage = new Stage();
				LöschenfensterView lview = new LöschenfensterView(stage);
				LöschenfensterController lcontroller = new LöschenfensterController(lview, view);
				lview.start();
				stage.setAlwaysOnTop(true);
			}
			
		});
	}
	
	
}