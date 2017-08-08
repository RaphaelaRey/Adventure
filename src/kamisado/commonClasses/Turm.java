package kamisado.commonClasses;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * @author Raphaela Rey
 */

public class Turm extends Circle implements Serializable {
		
	private int[] koordinaten = new int[2];
	private int turmRadius;
	private Paint füllFarbe;
	private Color strokeFarbe;

	// Konstruktor: Der Radius und die Koordinaten des Kreises (x- und y-Koordinate in Array) werden mitgegeben
	public Turm(int radius, int[] koord) {
		super(radius);
		setKoordinaten(koord);
	}
	
	// Getter und setter für die Turmkoordinaten
	public int[] getKoordinaten() {
		return koordinaten;
	}
	public void setKoordinaten(int[] koordinaten) {
		this.koordinaten = koordinaten;
	}

	public Paint getFüllFarbe() {
		return füllFarbe;
	}

	public void setFüllFarbe(Paint füllFarbe) {
		this.füllFarbe = füllFarbe;
	}

	public Color getStrokeFarbe() {
		return strokeFarbe;
	}

	public void setStrokeFarbe(Color strokeFarbe) {
		this.strokeFarbe = strokeFarbe;
	}

	public int getTurmRadius() {
		return turmRadius;
	}

	public void setTurmRadius(int turmRadius) {
		this.turmRadius = turmRadius;
	}
}
