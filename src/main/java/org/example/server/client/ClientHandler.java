package org.example.server.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void handleRequest() {
        try (InputStream input = socket.getInputStream(); OutputStream output = socket.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                logger.info("Request: {}", line);
            }
            String[] requestParts = line.split(" ");
            if (requestParts.length >= 3) {
                String method = requestParts[0];
                switch (method) {
                    case "GET":
                        handleGet(output);
                        break;
                    case "POST":
                        handlePost(output);
                        break;
                    case "PUT":
                        handlePut(output);
                        break;
                    case "DELETE":
                        handleDelete(output);
                        break;
                    default:
                        handleUnsupportedMethod(output, method);
                        break;
                }
            } else {
                logger.warn("Malformed request line: {}", line);
            }
        } catch (IOException e) {
            logger.info("Error handling client request : {}", e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.info("Error closing socket {}", e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        handleRequest();
    }

    private void handleGet(OutputStream output) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "Connection: close\r\n\r\n" + "<html><body><h1>GET Response</h1><p>" + new Date().toString() + "</p></body></html>";
        output.write(httpResponse.getBytes());
        output.flush();
    }

    private void handlePost(OutputStream output) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "Connection: close\r\n\r\n" + "<html><body><h1>POST Response</h1><p>" + new Date().toString() + "</p></body></html>";
        output.write(httpResponse.getBytes());
        output.flush();
    }

    private void handlePut(OutputStream output) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "Connection: close\r\n\r\n" + "<html><body><h1>PUT Response</h1><p>" + new Date().toString() + "</p></body></html>";
        output.write(httpResponse.getBytes());
        output.flush();
    }

    private void handleDelete(OutputStream output) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "Connection: close\r\n\r\n" + "<html><body><h1>DELETE Response</h1><p>" + new Date().toString() + "</p></body></html>";
        output.write(httpResponse.getBytes());
        output.flush();
    }

    private void handleUnsupportedMethod(OutputStream output, String method) throws IOException {
        String httpResponse = "HTTP/1.1 405 Method Not Allowed\r\n" + "Content-Type: text/html\r\n" + "Connection: close\r\n\r\n" + "<html><body><h1>405 Method Not Allowed</h1><p>The method " + method + " is not supported.</p></body></html>";
        output.write(httpResponse.getBytes());
        output.flush();
    }
}
