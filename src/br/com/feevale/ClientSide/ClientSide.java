package br.com.feevale.ClientSide;

import java.io.*;
import java.net.Socket;

public class ClientSide {
    private String CLIENT_ERROR_MESSAGE = "Ops! Something went wrong!";
    private String REGEX_TOKEN = "_";

    private Socket socket;
    private ClientReceiver receiver;
    private PrintWriter socketOutPut;
    private BufferedReader terminalInput;
    private String clientId;

    public ClientSide (String serverAddress, int serverPort, String clientId){
        try {
            this.clientId = clientId;
            socket = new Socket(serverAddress, serverPort);
            socketOutPut = new PrintWriter(socket.getOutputStream(), true);
            terminalInput = new BufferedReader(new InputStreamReader(System.in));
            receiver = new ClientReceiver(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            Thread receiverThread = new Thread(receiver);
            receiverThread.start();

            sendMessage();
        } catch (IOException e) {
            System.out.println(CLIENT_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void sendMessage() throws IOException{
        String userInput;
        while ((userInput = terminalInput.readLine()) != null) {
            socketOutPut.println(clientId + REGEX_TOKEN + userInput);
        }
    }
    public static void main(String[] args) {
        String clientId = "1";
        ClientSide client = new ClientSide("127.0.0.1", 8081, clientId);
    }
}
