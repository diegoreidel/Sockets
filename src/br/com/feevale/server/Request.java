package br.com.feevale.server;

public class Request {

    private String method;
    private String uri;

    public Request(String requestHeader) {
        String[] header = requestHeader.split(" ");
        method = header[0];
        uri = header[1].substring(1);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
