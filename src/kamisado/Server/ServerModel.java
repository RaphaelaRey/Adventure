package kamisado.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import kamisado.client.ClientModel;


public class ServerModel {
	
	private final Logger logger = Logger.getLogger("");
	private ServerSocket SSocket;
	private volatile boolean stop = false;
	
	public void startServer(int port) {
		logger.info("Start server");
		try {
			SSocket = new ServerSocket(port, 10, null);
			Runnable r = new Runnable() {
				@Override
				public void run() {
					while (!stop) {
						try {
							Socket socket = SSocket.accept();
							ClientModel client = new ClientModel(ServerModel.this, socket);
							
						} catch (Exception e) {
							logger.info(e.toString());
						}
					}
				}
			};
			Thread t = new Thread(r, "ServerSocket");
			t.start();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}
	
	public void stopServer() {
		
		logger.info("Stop server");
		stop = true;
		if (SSocket != null) {
			try {
				SSocket.close();
			} catch (IOException e) {
				
			}
		}
	}

}
