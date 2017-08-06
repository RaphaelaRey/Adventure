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
	private static Spielbrett spielbrett;
	
	public static void Senden(Socket clientSocket, Spielbrett spielbrett){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");

			//neueKoordinaten an Client senden
			senden.writeObject(spielbrett);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static Spielbrett Empfangen(Socket clientSocket){
		ObjectInputStream empfangen;
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("Streams erstellt");
		
			//neueKoordinaten von Client empfangen
			Spielbrett in = (Spielbrett) empfangen.readObject();
			logger.info("Neue Koordinaten erhalten");
			
			if(spielbrett != in){
				spielbrett = in;
				logger.info("Koordinaten ersetzt");
				}
				//else do nothing
			
		} catch (Exception e){
			logger.info(e.toString());
		}
		return spielbrett;
	}
	
	public static void setSpielbrett(Spielbrett spielfeld){
		spielbrett = spielfeld;
	}
	

}