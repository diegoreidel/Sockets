package br.com.feevale.server;

import br.com.feevale.client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by i851463 on 9/8/16.
 */
public class Server {
    private int port;
    private ServerSocket serverSocket;
    public static ArrayList<Client> clients;

    public Server (int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForClient() {
        Socket socket;
        Client client;

        try {
            while (true){
                socket = serverSocket.accept();
                System.out.println("Cliente conectou!");
                client = new Client(socket);
                clients.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ArrayList<Client> getClients(){
       return Server.clients;
    }

    public static void main(String[] args) {
        Server server = new Server(8081);
        server.waitForClient();
    }
}
