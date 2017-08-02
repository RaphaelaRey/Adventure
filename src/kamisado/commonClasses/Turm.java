package kamisado.commonClasses;

import java.io.Serializable;

import javafx.scene.shape.Circle;

/**
 * @author Raphaela Rey
 */

public class Turm extends Circle implements Serializable {
		
	private int[] koordinaten = new int[2];

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
}
