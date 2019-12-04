package server;

import java.io.IOException;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;

public class ApplicationServer {
    public static ArrayList<ServerThread> serverThreads;
    private static final int PORT_NUMBER = 7777;

    public static void main(String[] args) throws IOException {
        ApplicationServer.serverThreads = new ArrayList<ServerThread>();
        ApplicationServer server = new ApplicationServer();
        server.start();
    }

    public static void start() throws IOException {

        ServerSocket serverSocket = new ServerSocket(ApplicationServer.PORT_NUMBER);

        while (true){
            Socket clientSocket = serverSocket.accept();
            ServerThread newThread = new ServerThread(clientSocket);
            serverThreads.add(newThread);
            newThread.start();
        }
    }

}
