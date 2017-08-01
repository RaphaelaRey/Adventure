package kamisado.commonClasses;

import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import kamisado.client.Model;

public class Spielbrett extends GridPane {
	
	protected Model model;
		
	protected int [] aktiverTurmKoordinaten = new int [2];			// TODO aktiver Turm definieren -> man darf nur mit dem Turm fahren, der vorher als Feldfarbe definiert wurde
	protected int [] ausgewähltesFeldKoordinaten = new int [2];				// wenn aktiverturmkoordinaten nicht leer, dann....
    
	
    private Feld [][] felder = new Feld [8][8];
	
	// Array mit Koordinaten der farbigen Felder erstellen
    protected int [][] orangeFelder = {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}};
    protected int [][] brauneFelder = {{0,7},{1,6},{2,5},{3,4},{4,3},{5,2},{6,1},{7,0}};
    protected int [][] pinkeFelder = {{0,3},{1,2},{2,1},{3,0},{4,7},{5,6},{6,5},{7,4}};
    protected int [][] gelbeFelder = {{0,4},{1,5},{2,6},{3,7},{4,0},{5,1},{6,2},{7,3}};
    protected int [][] blaueFelder = {{0,5},{1,0},{2,3},{3,6},{4,1},{5,4},{6,7},{7,2}};
    protected int [][] violetteFelder = {{0,6},{1,3},{2,0},{3,5},{4,2},{5,7},{6,4},{7,1}};
    protected int [][] roteFelder = {{0,1},{1,4},{2,7},{3,2},{4,5},{5,0},{6,3},{7,6}};
    protected int [][] grüneFelder = {{0,2},{1,7},{2,4},{3,1},{4,6},{5,3},{6,0},{7,5}};
    
    // Je ein Array für die beiden Turmfarben und alle Türme erstellen 
    protected Turm [] schwarzeTürme = new Turm [8];					
    protected Turm [] weisseTürme = new Turm [8];						
    private static Turm [] türme = new Turm [16];
    
    // Array für die Koordinaten der möglichen Felder erstellen
	private ArrayList<int[]> möglicheFelder = new ArrayList<int[]>();
    
	private GridPane pane = new GridPane();
    
    public Spielbrett(Model cmodel) {
		
    	model = cmodel;
		// Felder in einem zweidimensionalen Array erstellen und der Gridpane hinzufügen
    	for (int i = 0; i < getFelder().length; i++){
    		for (int j = 0; j < getFelder().length; j++){
    			getFelder()[i][j] = new Feld(100, new int[]{i, j});
    			getPane().add(getFelder()[i][j], i, j);
    			getFelder()[i][j].setStroke(Color.BLACK);
    		}
    	}
		
    	// orange Felder einfärben
    	for (int i = 0; i < orangeFelder.length; i++){
    		getFelder()[orangeFelder[i][0]][orangeFelder[i][1]].setFill(Color.ORANGE);
    	}
    	// braune Felder einfärben
    	for (int i = 0; i < brauneFelder.length; i++){
    		getFelder()[brauneFelder[i][0]][brauneFelder[i][1]].setFill(Color.BROWN);
    	}
    	// pinke Felder einfärben
    	for (int i = 0; i < pinkeFelder.length; i++){
    		getFelder()[pinkeFelder[i][0]][pinkeFelder[i][1]].setFill(Color.HOTPINK);
    	}
    	// gelbe Felder einfärben
    	for (int i = 0; i < gelbeFelder.length; i++){
    		getFelder()[gelbeFelder[i][0]][gelbeFelder[i][1]].setFill(Color.YELLOW);
    	}
    	// blaue Felder einfärben
    	for (int i = 0; i < blaueFelder.length; i++){
    		getFelder()[blaueFelder[i][0]][blaueFelder[i][1]].setFill(Color.MEDIUMBLUE);
    	}
    	// violette Felder einfärben
    	for (int i = 0; i < violetteFelder.length; i++){
    		getFelder()[violetteFelder[i][0]][violetteFelder[i][1]].setFill(Color.DARKVIOLET);
    	}
    	// rote Felder einfärben
    	for (int i = 0; i < roteFelder.length; i++){
    		getFelder()[roteFelder[i][0]][roteFelder[i][1]].setFill(Color.RED);
    	}
    	// grüne Felder einfärben
    	for (int i = 0; i < grüneFelder.length; i++){
    		getFelder()[grüneFelder[i][0]][grüneFelder[i][1]].setFill(Color.GREEN);
    	}
		
    	// schwarze Türme in Array erstellen, der Gridpane hinzufügen und einfärben gemäss den unterliegenden Feldern
    	for (int i = 0; i < schwarzeTürme.length; i++){
    		schwarzeTürme[i] = new Turm(40, new int[] {i, 7}); // vorher 49
			getPane().add(schwarzeTürme[i], i, 7);
			schwarzeTürme[i].setStroke(Color.BLACK);
			schwarzeTürme[i].setStrokeWidth(3);
			schwarzeTürme[i].setFill(getFelder()[i][7].getFill());
			getFelder()[i][7].setFeldBesetzt(true);
			
    	}
    	// weisse Türme in Array erstellen, der Gridpane hinzufügen und einfärben gemäss den unterliegenden Feldern
    	for (int i = 0; i < weisseTürme.length; i++){
    		weisseTürme[i] = new Turm(40, new int[] {i, 0});
			getPane().add(weisseTürme[i], i, 0);
			weisseTürme[i].setStroke(Color.WHITE);
			weisseTürme[i].setStrokeWidth(3);
			weisseTürme[i].setFill(getFelder()[i][0].getFill());
			getFelder()[i][0].setFeldBesetzt(true);
    	}
    	// weisse und schwarze Türme in gemeinsames Array kopieren
    	System.arraycopy(schwarzeTürme, 0, getTürme(), 0, 8);
    	System.arraycopy(weisseTürme, 0, getTürme(), 8, 8);
 
    	// Elemente der GridPane zentrieren  
    	getPane().setAlignment(Pos.CENTER);
    	ColumnConstraints[] constraints = new ColumnConstraints[schwarzeTürme.length];
    	for(int i = 0; i < schwarzeTürme.length; i++){
    		constraints[i] = new ColumnConstraints();
    		constraints[i].setHalignment(HPos.CENTER);
    	}
    	getPane().getColumnConstraints().addAll(constraints);
    	
    }
    
 // Getter und setter für Koordinaten des aktiven Turms
 	public int[] getAktiverTurmKoordinaten() {
 		return aktiverTurmKoordinaten;
 	}
 	public void setAktiverTurmKoordinaten(int[] neueTurmKoordinaten) {
 		aktiverTurmKoordinaten = neueTurmKoordinaten;
 	}
 	
 	// Getter und setter für Koordinaten des ausgewählten Felds
 	public int[] getAusgewähltesFeldKoordinaten() {
 		return ausgewähltesFeldKoordinaten;
 	}
 	public void setAusgewähltesFeldKoordinaten(int[] ausgewaehltesFeldKoordinaten) {
 		this.ausgewähltesFeldKoordinaten = ausgewaehltesFeldKoordinaten;
 	}
 	
 	
 	// mögliche Felder (geradeaus, diagonal rechts und diagonal links) anzeigen
 	public ArrayList<int[]> möglicheFelderAnzeigen(int[] turmKoordinaten){		
 		
 		// ArrayList der bestehenden Liste löschen, damit nur die aktuellen möglichen Felder gespeichert sind
 		model.möglicheFelderLeeren(getMöglicheFelder(), getFelder());
 		
 		// mögliche gerade/diagonale Felder für schwarze und weisse Türme der ArrayList hinzufügen
 		model.möglicheFelderHinzufügen(turmKoordinaten, getTürme(), getFelder(), getMöglicheFelder());

 		return getMöglicheFelder();
 	}

 	// Getter und setter für die GridPane
	public GridPane getPane() {
		return pane;
	}
	public void setPane(GridPane pane) {
		this.pane = pane;
	}

	// Getter und setter für das Turm-Array
	public static Turm [] getTürme() {
		return türme;
	}
	public static void setTürme(Turm [] neutürme) {
		türme = neutürme;
	}

	// Getter und setter für die Arrayliste mögliche Felder
	public ArrayList<int[]> getMöglicheFelder() {
		return möglicheFelder;
	}
	public void setMöglicheFelder(ArrayList<int[]> möglicheFelder) {
		this.möglicheFelder = möglicheFelder;
	}

	// Getter und settre für das Felder-Array
	public Feld [][] getFelder() {
		return felder;
	}
	public void setFelder(Feld [][] felder) {
		this.felder = felder;
	}
    	

}