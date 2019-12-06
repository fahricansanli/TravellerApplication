package server;

import database.Constants;
import database.Database;
import models.*;
import util.GsonBuilderHelper;
import util.WriteReadOperation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private Socket clientSocket;
    private BufferedReader inputFromClient;
    private DataOutputStream outputForClient;
    private Message inputMessage;
    //public String placeToAddComment = "";
    public boolean shouldWorkInfinitely = false;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputForClient = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            executeMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeMessages() throws IOException {

        String input = inputFromClient.readLine();
        try {
            inputMessage = GsonBuilderHelper.getJsonBuilder().fromJson(input, Message.class);
            switch (inputMessage.getType()) {
                case GET_DISTRICT:
                    ArrayList<District> districts = Database.getDistricts();
                    Message outputMessage = new Message(Type.GET_DISTRICT, WriteReadOperation.convertToJSON(districts), "", "", "");
                    outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessage) + '\n');
                    break;

                case GET_LEISURE_ACTIVITY:
                    ArrayList<LeisureTimeActivity> leisureTimeActivities = Database.getLeisureTimeActivities();
                    Message outputMessage2 = new Message(Type.GET_LEISURE_ACTIVITY, WriteReadOperation.convertToJSON(leisureTimeActivities), "", "", "");
                    outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessage2) + '\n');
                    break;

                case LOGIN:
                    User loginUser = WriteReadOperation.convertToObject(inputMessage.getJsonData(), User.class);
                    boolean isUserAuthenticated = Database.authenticateUser(loginUser);
                    Message outputMessageForAuthentication = new Message(Type.LOGIN, WriteReadOperation.convertToJSON(isUserAuthenticated), "", "", "");
                    outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForAuthentication) + '\n');
                    break;
                case REGISTER:
                    User userToSave = WriteReadOperation.convertToObject(inputMessage.getJsonData(), User.class);
                    Database.saveUser(userToSave);
                    Message outputMessageForSave = new Message(Type.REGISTER, WriteReadOperation.convertToJSON(true), "", "", "");
                    outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForSave) + '\n');
                    break;
                case GET_PLACES:
                    District selectedDistrict = WriteReadOperation.convertToObject(inputMessage.getJsonData(), District.class);
                    LeisureTimeActivity selectedActivity = WriteReadOperation.convertToObject(inputMessage.getOptionalSecondData(), LeisureTimeActivity.class);
                    ArrayList<Place> places = Database.getPlaces(selectedDistrict, selectedActivity);
                    Message outputMessageForGetPlaces = new Message(Type.GET_PLACES, WriteReadOperation.convertToJSON(places), "", "", "");
                    outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForGetPlaces) + '\n');
                    break;
                case UPDATE_COMMENT_LIST:
                    District district = WriteReadOperation.convertToObject(inputMessage.getJsonData(), District.class);
                    LeisureTimeActivity leisureTimeActivity = WriteReadOperation.convertToObject(inputMessage.getOptionalSecondData(), LeisureTimeActivity.class);
                    Place place = WriteReadOperation.convertToObject(inputMessage.getOptionalThirdData(), Place.class);
                    Comment comment = WriteReadOperation.convertToObject(inputMessage.getOptionalFourthData(), Comment.class);
                    ArrayList<Comment> modifiedComments = Database.addComment(district, leisureTimeActivity, place, comment);
                    Message outputMessageForAddComment = new Message(Type.UPDATE_COMMENT_LIST, WriteReadOperation.convertToJSON(modifiedComments), "", "", "");
                    outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForAddComment) + '\n');
                    //shouldWorkInfinitely = true;
                    //updateAllThreads(outputMessageForAddComment);
                    break;
                case PLACE_DETAIL:
                    District district2 = WriteReadOperation.convertToObject(inputMessage.getJsonData(), District.class);
                    LeisureTimeActivity leisureTimeActivity2 = WriteReadOperation.convertToObject(inputMessage.getOptionalSecondData(), LeisureTimeActivity.class);
                    Place place2 = WriteReadOperation.convertToObject(inputMessage.getOptionalThirdData(), Place.class);
                    ArrayList<Comment> comments = Database.getComments(district2, leisureTimeActivity2, place2);
                    Message outputMessageForPlaceDetail = new Message(Type.PLACE_DETAIL, WriteReadOperation.convertToJSON(comments), "", "", "");
                    outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForPlaceDetail) + '\n');
                    break;
                        /*case NOTIFY_SERVER_TO_LISTEN:
                            District districtToListen = WriteReadOperation.convertToObject(inputMessage.getJsonData(), District.class);
                            LeisureTimeActivity leisureTimeActivityToListen  = WriteReadOperation.convertToObject(inputMessage.getOptionalSecondData(), LeisureTimeActivity.class);
                            Place placeToListen  = WriteReadOperation.convertToObject(inputMessage.getOptionalThirdData(), Place.class);
                            placeToAddComment = Constants.COMMENT_PATH.replace("[district]", districtToListen.getDistrictName().toLowerCase())
                                    .replace("[leisure_time_activity]", leisureTimeActivityToListen.getName().toLowerCase())
                                    .replace("[placeID]", placeToListen.getID() + "");
                            shouldWorkInfinitely = true;
                            break;
                        case STOP_LISTENING_SERVER:
                            shouldWorkInfinitely = false;
                            break;*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

        /*String input;
        while (true) {
            input = inputFromClient.readLine();
            if (input != null) {
                try {
                    inputMessage = GsonBuilderHelper.getJsonBuilder().fromJson(input, Message.class);
                    switch (inputMessage.getType()) {
                        case GET_DISTRICT:
                            ArrayList<District> districts = Database.getDistricts();
                            Message outputMessage = new Message(Type.GET_DISTRICT, WriteReadOperation.convertToJSON(districts), "", "", "");
                            outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessage) + '\n');
                            break;

                        case GET_LEISURE_ACTIVITY:
                            ArrayList<LeisureTimeActivity> leisureTimeActivities = Database.getLeisureTimeActivities();
                            Message outputMessage2 = new Message(Type.GET_LEISURE_ACTIVITY, WriteReadOperation.convertToJSON(leisureTimeActivities), "", "", "");
                            outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessage2) + '\n');
                            break;

                        case LOGIN:
                            User loginUser = WriteReadOperation.convertToObject(inputMessage.getJsonData(), User.class);
                            boolean isUserAuthenticated = Database.authenticateUser(loginUser);
                            Message outputMessageForAuthentication = new Message(Type.LOGIN, WriteReadOperation.convertToJSON(isUserAuthenticated), "", "", "");
                            outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForAuthentication) + '\n');
                            break;
                        case REGISTER:
                            User userToSave = WriteReadOperation.convertToObject(inputMessage.getJsonData(), User.class);
                            Database.saveUser(userToSave);
                            Message outputMessageForSave = new Message(Type.REGISTER, WriteReadOperation.convertToJSON(true), "", "", "");
                            outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForSave) + '\n');
                            break;
                        case GET_PLACES:
                            District selectedDistrict = WriteReadOperation.convertToObject(inputMessage.getJsonData(), District.class);
                            LeisureTimeActivity selectedActivity = WriteReadOperation.convertToObject(inputMessage.getOptionalSecondData(), LeisureTimeActivity.class);
                            ArrayList<Place> places = Database.getPlaces(selectedDistrict, selectedActivity);
                            Message outputMessageForGetPlaces = new Message(Type.GET_PLACES, WriteReadOperation.convertToJSON(places), "", "", "");
                            outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForGetPlaces) + '\n');
                            break;
                        case UPDATE_COMMENT_LIST:
                            District district = WriteReadOperation.convertToObject(inputMessage.getJsonData(), District.class);
                            LeisureTimeActivity leisureTimeActivity = WriteReadOperation.convertToObject(inputMessage.getOptionalSecondData(), LeisureTimeActivity.class);
                            Place place = WriteReadOperation.convertToObject(inputMessage.getOptionalThirdData(), Place.class);
                            Comment comment = WriteReadOperation.convertToObject(inputMessage.getOptionalFourthData(), Comment.class);
                            ArrayList<Comment> modifiedComments = Database.addComment(district, leisureTimeActivity, place, comment);
                            Message outputMessageForAddComment = new Message(Type.UPDATE_COMMENT_LIST, WriteReadOperation.convertToJSON(modifiedComments), "", "", "");
                            outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForAddComment) + '\n');
                            //shouldWorkInfinitely = true;
                            //updateAllThreads(outputMessageForAddComment);
                            break;
                        case PLACE_DETAIL:
                            District district2 = WriteReadOperation.convertToObject(inputMessage.getJsonData(), District.class);
                            LeisureTimeActivity leisureTimeActivity2 = WriteReadOperation.convertToObject(inputMessage.getOptionalSecondData(), LeisureTimeActivity.class);
                            Place place2 = WriteReadOperation.convertToObject(inputMessage.getOptionalThirdData(), Place.class);
                            ArrayList<Comment> comments = Database.getComments(district2, leisureTimeActivity2, place2);
                            Message outputMessageForPlaceDetail = new Message(Type.PLACE_DETAIL, WriteReadOperation.convertToJSON(comments), "", "", "");
                            outputForClient.writeBytes(WriteReadOperation.convertToJSON(outputMessageForPlaceDetail) + '\n');
                            break;
                        case NOTIFY_SERVER_TO_LISTEN:
                            District districtToListen = WriteReadOperation.convertToObject(inputMessage.getJsonData(), District.class);
                            LeisureTimeActivity leisureTimeActivityToListen  = WriteReadOperation.convertToObject(inputMessage.getOptionalSecondData(), LeisureTimeActivity.class);
                            Place placeToListen  = WriteReadOperation.convertToObject(inputMessage.getOptionalThirdData(), Place.class);
                            placeToAddComment = Constants.COMMENT_PATH.replace("[district]", districtToListen.getDistrictName().toLowerCase())
                                    .replace("[leisure_time_activity]", leisureTimeActivityToListen.getName().toLowerCase())
                                    .replace("[placeID]", placeToListen.getID() + "");
                            shouldWorkInfinitely = true;
                            break;
                        case STOP_LISTENING_SERVER:
                            shouldWorkInfinitely = false;
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if(!shouldWorkInfinitely){
                break;
            }
        }*/

}

    /*public void updateAllThreads(Message message) throws IOException {
        for (ServerThread thread : ApplicationServer.serverThreads) {
            if (thread.isAlive() && thread.placeToAddComment.equals(this.placeToAddComment)) {
                thread.outputForClient.writeBytes(WriteReadOperation.convertToJSON(message) + '\n');
                thread.outputForClient.flush();
            }
        }
    }*/

