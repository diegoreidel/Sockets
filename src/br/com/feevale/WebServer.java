package br.com.feevale;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebServer {
    private String RUNTIME_EXCEPTION_MESSAGE = "Socket Error!";
    private String HOMEPAGE = "home.html";
    private ServerSocket ss;

    public void setUp() {
        try {
            ss = new ServerSocket(8081);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE);
        }
    }

    public Socket waitForConnections() throws IOException { return ss.accept(); }

    public Request receiveRequest(Socket s) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        String line = in.readLine();
        Request request = new Request(line);

        StringBuilder stBuilder = new StringBuilder(line).append(System.getProperty("line.separator"));
        while ((line = in.readLine()).length() > 0) {
            stBuilder.append(line).append(System.getProperty("line.separator"));
        }
        registerRequest(stBuilder.toString());
        return request;
    }

    public void registerRequest(String request) {
        System.out.println(request);
    }

    public void sendResponse(Request request, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        String uri = request.getUri().isEmpty() ? HOMEPAGE : request.getUri();

        Response response = new Response(getContentTypeForURI(uri), getResource(uri));
        byte[] preparedResponse = response.getResponse();
        out.write(preparedResponse);
        out.flush();

        registerResponse(preparedResponse.toString());
    }

    private ContentType getContentTypeForURI(String uri){
        String extension = uri.split("\\.")[1];

        switch (extension) {
            case "JPEG": case "JPG": case "jpeg": case "jpg":
                return ContentType.JPEG;
            default:
                return ContentType.HTML;
        }
    }

    private byte[] getResource(String fileName) throws IOException{
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource(fileName).getFile());
        Path path = file.toPath();

        return Files.readAllBytes(path);
    }

    public void registerResponse(String response) {
        System.out.println(response);
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WebServer ws = new WebServer();
        ws.setUp();
        while(true) {
            try {
                Socket socket = ws.waitForConnections();
                Request request = ws.receiveRequest(socket);
                ws.sendResponse(request, socket);
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
