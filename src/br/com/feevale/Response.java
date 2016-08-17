package br.com.feevale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by i851463 on 8/13/16.
 */
public class Response {
    private String SUCCESS_HEADER = "HTTP/1.1 200 OK";
    private String CONTENT_TYPE = "Content-Type: ";
    private String SERVER = "Server: Xulapa 7.2";
    private String CONNECTION = "Connection: close";
    private String CONTENT_LENGTH = "Content-Length: ";

    private int contentLength;
    private String contentType;
    private byte[] responseBody;

    public Response(ContentType contentType, byte[] responseBody) {
        processContentType(contentType);
        this.contentLength = responseBody.length;
        this.responseBody = responseBody;
    }

    private void processContentType(ContentType contentType) {
        switch (contentType) {
            case JPEG: case JPG:
                this.contentType = "image/jpeg;";
                break;
            default:
                this.contentType = "text/html;";
                break;
        }
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    private int getContentLength(){
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    private ContentType getContentTypeForUri(String uri) {
        return  null;
    }

    public byte[] getResponse() throws IOException{
        String response = SUCCESS_HEADER + System.getProperty("line.separator") +
                CONTENT_TYPE + getContentType() + System.getProperty("line.separator") +
                SERVER + System.getProperty("line.separator") +
                CONNECTION + System.getProperty("line.separator") +
                CONTENT_LENGTH + getContentLength() + System.getProperty("line.separator") + System.getProperty("line.separator");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write(response.getBytes());
        outputStream.write(getResponseBody());

        return outputStream.toByteArray( );
    }
}
