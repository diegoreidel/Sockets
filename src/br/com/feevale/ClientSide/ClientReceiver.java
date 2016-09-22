package br.com.feevale.ClientSide;


import java.io.BufferedReader;
import java.io.IOException;

public class ClientReceiver implements Runnable {
    BufferedReader socketInput;

    public ClientReceiver(BufferedReader socketInput){
        this.socketInput = socketInput;
    }

    @Override
    public void run() {
        String message;
        try {
            while (true) {
                message = socketInput.readLine();
                System.out.println("Mensagem recebida: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
