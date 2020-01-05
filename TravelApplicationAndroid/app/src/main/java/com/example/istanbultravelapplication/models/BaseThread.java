package com.example.istanbultravelapplication.models;

import com.example.istanbultravelapplication.util.JsonHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class BaseThread extends Thread{
    private static final String IP_ADDRESS = "35.159.52.52";
    private static final int PORT_NUMBER = 7777;
    public Socket clientSocket;
    public BufferedReader inputFromClient;
    public DataOutputStream outputForClient;


    @Override
    public void run() {
        try {
            clientSocket = new Socket(BaseThread.IP_ADDRESS, BaseThread.PORT_NUMBER);
            inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputForClient = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer(Object object){
        try {
            outputForClient.writeBytes(JsonHelper.convertToJSON(object) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
