package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;

public class HttpClient {

    private final int statusCode;
    private final HashMap<String, String> headerFields = new HashMap<>();
    private final String messageBody;

    public HttpClient(String host, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(host, port);

        socket.getOutputStream().write(
                ("GET " + requestTarget + " HTTP/1.1\r\n" +
                        "Connection: close\r\n" +
                        "Host: " + host + "\r\n" +
                        "\r\n").getBytes()
        );

        
        String statusLine = readLine(socket);
        this.statusCode = Integer.parseInt(statusLine.split(" ")[1]);
        
        
        String headerLine;
        while (!(headerLine = readLine(socket)).isBlank()) {
            int colonPos = headerLine.indexOf(':');
            String key = headerLine.substring(0, colonPos);
            String value = headerLine.substring(colonPos+1).trim();
            headerFields.put(key, value);
        }
        
        this.messageBody = readCharacters(socket, getContentLength());
    }

    private String readCharacters(Socket socket, int contentLength) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = socket.getInputStream();

        for (int i = 0; i < contentLength; i++) {
            result.append((char) in.read());
        }

        return result.toString();
    }

    private String readLine(Socket socket) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = socket.getInputStream();

        int c;
        while ((c = in.read()) != -1 && c != '\r') {
            result.append((char)c);
        }
        //noinspection ResultOfMethodCallIgnored
        in.read();
        return result.toString();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("httpbin.org", 80);
        
        socket.getOutputStream().write(
                ("GET /html HTTP/1.1\r\n" +
                        "Connection: close\r\n" +
                        "Host: httpbin.org\r\n" +
                        "\r\n").getBytes()
                
        );

        InputStream in = socket.getInputStream();
        int c;
        while ((c = in.read()) != -1) {
            System.out.print((char)c);
        }
    }

    public String getHeader(String headerName) {
        return headerFields.get(headerName);
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public String getMessageBody() {
        return messageBody;
    }
}
