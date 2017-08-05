package kamisado.commonClasses;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import kamisado.Server.ServerModel;
import kamisado.client.ClientModel;
import kamisado.client.ClientView;

public class SendenEmpfangen {
	
	private static final Logger logger = Logger.getLogger("");
	private static ClientModel model;
	private static Spielbrett spielbrett;
	
	public static void Senden(Socket clientSocket){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");

			//neueKoordinaten an Client senden
			spielbrett = model.getSpielbrett();
			senden.writeObject(spielbrett);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
			spielbrett = null;
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Empfangen(Socket clientSocket){
		ObjectInputStream empfangen;
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("Streams erstellt");
		
			//neueKoordinaten von Client empfangen
			Spielbrett in = (Spielbrett) empfangen.readObject();
			logger.info("Neue Koordinaten erhalten");
			
			if(spielbrett != in){
				spielbrett = in;
				model.setSpielbrett(spielbrett);
				logger.info("Koordinaten ersetzt");
				}
				//else do nothing
				
			spielbrett = null;
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	

}
