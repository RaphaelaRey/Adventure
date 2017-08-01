package kamisado.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientModel extends Thread{
	
		protected Socket client;
		private boolean amLaufen = true;
		private static int[] neueKoordinaten;
		protected ObjectInputStream vonServer;
		protected ObjectOutputStream anServer;
		private final Logger logger = Logger.getLogger("");
		String hostName;
		int port = 444;
		
		public static void setNeueKoordinaten(int[] aktiverTurmKoordinaten) {
	 		neueKoordinaten = aktiverTurmKoordinaten;
	 	}
			
		public ClientModel(){
			
			try{
				this.hostName= InetAddress.getLocalHost().getHostName();
				
				//Verbindung mit Server herstellen
				this.client = new Socket(hostName, port);
				
				while(amLaufen == true){
				
					//Streams erstellen
					this.vonServer = new ObjectInputStream(client.getInputStream());
					this.anServer = new ObjectOutputStream(client.getOutputStream());
	
					//neueKoordinaten an Server senden
					anServer.writeObject(neueKoordinaten);
					anServer.flush();
					
					//neueKoordinaten von Client empfangen
					int[] in = (int[]) vonServer.readObject();
					
					if(neueKoordinaten != in){
						setNeueKoordinaten(in);
					}
					//else do nothing
										
				} 
			} catch (Exception e){
				logger.info(e.toString());
			}
			
		}
		
		public void clientAnhalten(){
			this.amLaufen = false;
			
			
		}
		
		

}
