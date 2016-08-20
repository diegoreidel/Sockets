package br.com.feevale;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class WebServer {
    private String RUNTIME_EXCEPTION_MESSAGE = "Socket Error!";
    private String RESOURCE_NOT_FOUND = "Could not find file ";
    private String HOMEPAGE = "home.html";
    private String ERROR_PAGE = "error.html";
    private ServerSocket ss;

    public void setUp() {
        try {
            ss = new ServerSocket(8081);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE);
        }
    }

    public Socket waitForConnection() throws IOException { return ss.accept(); }

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

        String extension = uri.contains(".") ? uri.split("\\.")[1] : "";

        switch (extension.toUpperCase()) {
            case "JPEG": case "JPG":
                return ContentType.JPEG;
            default:
                return ContentType.HTML;
        }
    }

    private byte[] getResource(String fileName){
        ClassLoader classLoader = getClass().getClassLoader();

        try {
            File file = Optional<File> (classLoader.getResource(fileName).getFile());
            Path path = file.toPath();

            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println(RESOURCE_NOT_FOUND + fileName);
        }
        return getResource(ERROR_PAGE);
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
                Socket socket = ws.waitForConnection();
                Request request = ws.receiveRequest(socket);
                ws.sendResponse(request, socket);
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
