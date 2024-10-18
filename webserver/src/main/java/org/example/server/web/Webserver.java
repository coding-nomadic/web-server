package org.example.server.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.server.client.ClientHandler;
import org.example.server.config.ConfigLoader;
import org.example.server.constants.Constants;
import org.example.server.service.ServerExecutor;
import org.example.server.utils.ConfigParseUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Webserver implements ServerExecutor {

    private final int port;
    private final int maxThreads;
    private final ExecutorService executorService;
    private static final Logger logger = LogManager.getLogger(Webserver.class);


    public Webserver() {
        Map<String, String> configs = loadConfigs();
        this.port = ConfigParseUtils.parsePort(configs);
        this.maxThreads = ConfigParseUtils.parseMaxThreads(configs);
        this.executorService = Executors.newFixedThreadPool(maxThreads);
    }


    private Map<String, String> loadConfigs() {
        ConfigLoader configLoader = ConfigLoader.getInstance();
        return configLoader.getJsonData();
    }


    @Override
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server is listening on port {}", port);
            acceptClients(serverSocket);
        } catch (IOException ex) {
            logger.error("Error starting the server: {}", ex.getMessage());
        } finally {
            shutdownExecutorService();
        }
    }


    private void acceptClients(ServerSocket serverSocket) throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            logger.info("New client connected from {}", clientSocket.getRemoteSocketAddress());
            executorService.submit(new ClientHandler(clientSocket));
        }
    }


    private void shutdownExecutorService() {
        logger.info("Shutting down server...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
                logger.warn("Forcing shutdown due to timeout.");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("Error while shutting down executor service: {}", e.getMessage());
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("Server shutdown complete.");
    }
}
