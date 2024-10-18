package org.example.server;


import org.example.server.web.Webserver;

public class StartServer {

    public static void main(String[] args) {
        Webserver webserver = new Webserver();
        webserver.execute();
    }
}