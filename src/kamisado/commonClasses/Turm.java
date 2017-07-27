package kamisado.commonClasses;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Turm extends Circle {
	
	private int[] koordinaten = new int[2];

	// Turm-Konstruktor: Der Radius und die Koordinaten des Kreises (x- und y-Koordinate in Array) werden mitgegeben
	public Turm(int radius, int[] koord) {
		super(radius);
		setKoordinaten(koord);
	}

	// Farbe des Turms herausfinden
	public Color getTurmFarbe()	{
		return (Color) super.getFill();
	}
	
	// Getter und setter für die Turmkoordinaten
	public int[] getKoordinaten() {
		return koordinaten;
	}
	public void setKoordinaten(int[] koordinaten) {
		this.koordinaten = koordinaten;
	}
}
