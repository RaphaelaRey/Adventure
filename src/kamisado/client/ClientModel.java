package kamisado.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientModel {
	
		private int port = 444;
		private String spielerName;
		private boolean amLaufen = true;
		
		private int[] neueKoordinaten; //= kamisado.commonClasses.Spielbrett.getAktiverTurmKoordinaten() --- nicht static
		
		ObjectOutputStream anServer;
		ObjectInputStream vonServer;
		
			
		public void mitServerVerbinden() {
			
			
			
			try{
				//Verbindung mit ServerClient herstellen
				Socket clientSocket = new Socket(spielerName, port);
				
				//Streams erstellen
				anServer = new ObjectOutputStream(clientSocket.getOutputStream());
				vonServer = new ObjectInputStream(clientSocket.getInputStream());
				
				while(amLaufen == true){
					
					anServer.writeObject(neueKoordinaten);
				
					clientSocket.close();	
				}
			} 	catch (IOException e){
					System.out.println(e);
				}
		}

}
