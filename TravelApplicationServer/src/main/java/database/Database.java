package database;

import models.*;
import semaphoreAndLock.BinarySemaphore;
import semaphoreAndLock.Locks;
import util.WriteReadOperation;

import java.util.ArrayList;

public class Database {

    public static ArrayList<District> getDistricts(){
        String jsonFormDistricts = WriteReadOperation.getContentFromFile(Constants.DISTRICT_PATH);
        return WriteReadOperation.getDataAsArrayList(jsonFormDistricts,District.class);
    }

    public static ArrayList<LeisureTimeActivity> getLeisureTimeActivities(){
        String jsonFormActivities = WriteReadOperation.getContentFromFile(Constants.LEISURE_TIME_ACTIVITIES_PATH);
        return WriteReadOperation.getDataAsArrayList(jsonFormActivities,LeisureTimeActivity.class);
    }

    public static ArrayList<User> getUsers(){
        String jsonFormUsers = WriteReadOperation.getContentFromFile(Constants.USER_PATH);
        return WriteReadOperation.getDataAsArrayList(jsonFormUsers,User.class);
    }

    public static void saveUser(User newUser){
        Locks.userSemaphore.P();
        boolean isUserExist = false;
        ArrayList<User> userList = getUsers();
        for(int i=0;i<userList.size();i++){
            if(userList.get(i).getUsername().equals(newUser.getUsername())){
                isUserExist = true;
            }
        }
        if(!isUserExist){
            userList.add(newUser);
            WriteReadOperation.writeContentForAFile(Constants.USER_PATH,WriteReadOperation.convertToJSON(userList));
        }
        Locks.userSemaphore.V();
    }

    public static boolean authenticateUser(User user){
        ArrayList<User> userList = getUsers();
        for(int i=0;i<userList.size();i++){
            if(userList.get(i).equals(user)){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Place> getPlaces(District district,LeisureTimeActivity activity){
        String filename = Constants.PLACES_PATH.replace("[district]",district.getDistrictName().toLowerCase())
                .replace("[leisure_time_activity]",activity.getName().toLowerCase());

        String jsonFormPlaces = WriteReadOperation.getContentFromFile(filename);
        return WriteReadOperation.getDataAsArrayList(jsonFormPlaces,Place.class);
    }

    public static ArrayList<Comment> addComment(District district,LeisureTimeActivity activity, Place place, Comment comment){

        String key = district.getDistrictName().toLowerCase() + "_" + activity.getName().toLowerCase() + "_" +  place.getID();
        BinarySemaphore semaphoreToLock;

        if(Locks.commentLocks.containsKey(key)){
            semaphoreToLock = Locks.commentLocks.get(key);
        }
        else{
            BinarySemaphore semaphore = new BinarySemaphore();
            Locks.commentLocks.put(key,semaphore);
            semaphoreToLock = semaphore;
        }

        semaphoreToLock.P();;

        String filename = Constants.COMMENT_PATH.replace("[district]",district.getDistrictName().toLowerCase())
                .replace("[leisure_time_activity]",activity.getName().toLowerCase())
                .replace("[placeID]",place.getID()+"");
        String jsonFormComments = WriteReadOperation.getContentFromFile(filename);
        ArrayList<Comment> comments = WriteReadOperation.getDataAsArrayList(jsonFormComments,Comment.class);
        comments.add(comment);
        WriteReadOperation.writeContentForAFile(filename,WriteReadOperation.convertToJSON(comments));

        semaphoreToLock.V();

        return comments;
    }

    public static ArrayList<Comment> getComments(District district,LeisureTimeActivity activity, Place place){
        String filename = Constants.COMMENT_PATH.replace("[district]",district.getDistrictName().toLowerCase())
                .replace("[leisure_time_activity]",activity.getName().toLowerCase())
                .replace("[placeID]",place.getID()+"");
        String jsonFormComments = WriteReadOperation.getContentFromFile(filename);
        return WriteReadOperation.getDataAsArrayList(jsonFormComments,Comment.class);
    }

}
