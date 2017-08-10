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
	private String füllFarbe; // Farbe des Turms
	private String strokeFarbe; // Randfarbe des Turms
	
	private boolean turmBewegt; // wenn turmBewegt true ist, wurde bereits ein Zug durchgeführt
	private boolean aktiverTurm;
	private boolean ersterBlockierenderTurm; // dieser Turm hat die Blockade provoziert
	private boolean zweiterBlockierenderTurm; // dieser Turm hat die zweite Blockade, die zum völligen Stillstand führt provoziert
	private boolean gewinnerTurm;

	// Konstruktor: Der Radius und die Koordinaten des Kreises (x- und y-Koordinate in Array) werden mitgegeben
	public Turm(int radius, int[] koord) {
		super(radius);
		setKoordinaten(koord);
	}
	
	// Getter und setter
	public int[] getKoordinaten() {
		return koordinaten;
	}
	public void setKoordinaten(int[] koordinaten) {
		this.koordinaten = koordinaten;
	}

	public int getTurmRadius() {
		return turmRadius;
	}
	public void setTurmRadius(int turmRadius) {
		this.turmRadius = turmRadius;
	}

	public String getFüllFarbe() {
		return füllFarbe;
	}
	public void setFüllFarbe(String füllFarbe) {
		this.füllFarbe = füllFarbe;
	}

	public String getStrokeFarbe() {
		return strokeFarbe;
	}
	public void setStrokeFarbe(String strokeFarbe) {
		this.strokeFarbe = strokeFarbe;
	}

	public boolean isTurmBewegt() {
		return turmBewegt;
	}
	public void setTurmBewegt(boolean turmBewegt) {
		this.turmBewegt = turmBewegt;
	}

	public boolean isAktiverTurm() {
		return aktiverTurm;
	}
	public void setAktiverTurm(boolean aktiverTurm) {
		this.aktiverTurm = aktiverTurm;
	}

	public boolean isErsterBlockierenderTurm() {
		return ersterBlockierenderTurm;
	}
	public void setErsterBlockierenderTurm(boolean ersterBlockierenderTurm) {
		this.ersterBlockierenderTurm = ersterBlockierenderTurm;
	}

	public boolean isZweiterBlockierenderTurm() {
		return zweiterBlockierenderTurm;
	}
	public void setZweiterBlockierenderTurm(boolean zweiterBlockierenderTurm) {
		this.zweiterBlockierenderTurm = zweiterBlockierenderTurm;
	}

	public boolean isGewinnerTurm() {
		return gewinnerTurm;
	}
	public void setGewinnerTurm(boolean gewinnerTurm) {
		this.gewinnerTurm = gewinnerTurm;
	}
}
