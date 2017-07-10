package server;

import java.net.ServerSocket;
import java.util.logging.Logger;

import com.sun.security.ntlm.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	
	private Logger logger = Logger.getLogger("");
	private ServerSocket listener;
	protected final ObservableList<Client> clients = FXCollections.observableArrayList();
}
