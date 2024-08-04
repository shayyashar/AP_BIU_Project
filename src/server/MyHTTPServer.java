package server;

import servlets.Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyHTTPServer extends Thread implements HTTPServer {

    private final int port;
    private final ExecutorService threadPool;
    private final Map<String, Map<String, Servlet>> servlets;
    private volatile boolean running;
    private ServerSocket serverSocket;

    public MyHTTPServer(int port,int nThreads){
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(nThreads);
        // creating Hash Map with key for every method
        this.servlets = new ConcurrentHashMap<>();
        servlets.put("GET", new ConcurrentHashMap<>());
        servlets.put("POST", new ConcurrentHashMap<>());
        servlets.put("DELETE", new ConcurrentHashMap<>());
    }

    public void addServlet(String httpCommand, String uri, Servlet s) {
        // put in the correct Hash Map method
        this.servlets.get(httpCommand).put(uri, s);

    }

    public void removeServlet(String httpCommand, String uri) {
        // remove from the correct Hash Map method
        this.servlets.get(httpCommand).remove(uri);
    }

    public void run() {
        try {
            // run every 1 sec
            serverSocket = new ServerSocket(port);
            running = true;
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(() -> handleClient(clientSocket));
                } catch (SocketTimeoutException e) {
                    sleep(1000);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        // crete in and out from the socket
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            // parse the request
            RequestParser.RequestInfo ri = RequestParser.parseRequest(in);
            if (ri != null) {
                // find the correct serverlet with 'findServlet' function
                Servlet servlet = findServlet(ri.getHttpCommand(), ri.getUri());
                if (servlet != null) {
                    servlet.handle(ri, out);
                } else {
                    // Handle 404 Not Found
                    String response = "HTTP/1.1 404 Not Found\r\n\r\n";
                    out.write(response.getBytes());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Servlet findServlet(String httpCommand, String uri) {
        Map<String, Servlet> commandServlets = servlets.get(httpCommand);
        if (commandServlets == null) {
            return null;
        }

        String longestMatch = "";
        Servlet matchedServlet = null;
        for (Map.Entry<String, Servlet> entry : commandServlets.entrySet()) {
            String registeredUri = entry.getKey();
            // check if the request starts with the request we get
            // and check the maximum match
            if (uri.startsWith(registeredUri) && registeredUri.length() > longestMatch.length()) {
                longestMatch = registeredUri;
                matchedServlet = entry.getValue();
            }
        }
        return matchedServlet;
    }


    public void close() {
        //  close the socket
        this.running = false;
        this.threadPool.shutdown();
        try {
            if (this.serverSocket != null && !this.serverSocket.isClosed()) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

