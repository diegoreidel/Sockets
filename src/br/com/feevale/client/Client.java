package br.com.feevale.client;

import br.com.feevale.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
    private String CLIENT_ERROR = "Um erro ocorreu no cliente!";

    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Thread thread;
    private String id;

    @Override
    public void run() {
        while (true) {
            receiveMessage();
        }
    }

    public Client(Socket socket) {
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());
            start();
        } catch (IOException e){
            System.out.println(CLIENT_ERROR);
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        output.println(message);
        output.flush();
    }

    public String receiveMessage() {
        String message;
        try {
            message = input.readLine();
            System.out.println("Li mensagem: " + message);
            String[] params = message.split("_");
            this.id = params[0];
            message = params[1];
            for(Client client : Server.getClients()){
                if(!id.equals(client.getId()))
                    client.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getId(){
        return this.id;
    }


    private void start(){
        thread = new Thread(this);
        thread.start();
    }
}
