package kamisado.commonClasses;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Tobias Deprato
 */

public class SendenEmpfangen {
	
	private static final Logger logger = Logger.getLogger("");
	private static Turm[] neueTürme;
	private static String namePW;
	
	public static void Senden(Socket clientSocket, Turm[] Türme){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(Türme);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Senden(Socket clientSocket, String namePW){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(namePW);
			senden.flush();
			logger.info("Anmeldedaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static Turm[] Empfangen(Socket clientSocket){
		ObjectInputStream empfangen;
		Turm[] in; 
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			 in = (Turm[]) empfangen.readObject();
			logger.info("Neue Türme erhalten");
			setTürme(in);			
		} catch (Exception e){
			logger.info(e.toString());
		}
		return neueTürme;
	}	
	
	public static String EmpfangenString(Socket clientSocket){
		ObjectInputStream empfangen;
		String in; 
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			 in = (String) empfangen.readObject();
			logger.info("Name und PW erhalten");
			setString(in);		
		} catch (Exception e){
			logger.info(e.toString());
		}
		return namePW;
	}	
	public static void setTürme(Turm[] Türme){
		neueTürme = Türme;
	}
	
	public static void setString(String s){
		namePW = s;
	}
	

}
