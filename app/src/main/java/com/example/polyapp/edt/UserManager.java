package com.example.polyapp.edt;

import android.database.Cursor;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    DBManager m_db;

    /**
     * Default UserManager constructor
     * @param db database already initialized
     */
    public UserManager(DBManager db){
        m_db = db;
    }

    /**
     * Get all users with specific promo ID
     * @param resourceID promo ID
     * @return List of UserStruct
     */
    public List<UserStruct> getUsersByPromoID(int resourceID){

        String selectionKeys = null;
        String[] selectionArgs = null;

        if(resourceID >= 0){
            selectionKeys = "( " + DBSyntax.RID + " = ? )";
            selectionArgs = new String[]{String.valueOf(resourceID)};
        }

        Cursor DBEvents =  m_db.selectFromUsers(selectionKeys,selectionArgs,null);

        List<UserStruct> userStructList = new ArrayList<UserStruct>();

        if(!DBEvents.isAfterLast()){
            userStructList.add(new UserStruct(DBEvents));
            while (DBEvents.moveToNext()) {
                userStructList.add(new UserStruct(DBEvents));
            }
        }

        DBEvents.close();

        return userStructList;
    }

    public List<UserStruct> getFriends(){

        String selectionKeys = null;
        String[] selectionArgs = null;

        Cursor DBEvents =  m_db.selectFromUsers(null,null,null);

        List<UserStruct> userStructList = new ArrayList<UserStruct>();

        if(!DBEvents.isAfterLast()){
            while (DBEvents.moveToNext()) {
                userStructList.add(new UserStruct(DBEvents));
            }
        }

        DBEvents.close();

        return userStructList;
    }

    public UserStruct getUserById(int userID){

        String selectionKeys = "( " + DBSyntax.UID + " = ? )";
        String[] selectionArgs = new String[]{String.valueOf(userID)};

        Cursor DBEvents =  m_db.selectFromUsers(selectionKeys,selectionArgs,null);

        UserStruct userStruct = null;

        if(!DBEvents.isAfterLast()){
            userStruct = new UserStruct(DBEvents);
        }

        DBEvents.close();

        return userStruct;
    }

    public UserStruct getMainUser(){
        return getUserById(1);
    }

    public void createUserByPromoID(String first_name, String last_name, int promoID){
        m_db.insertUser(first_name,last_name,promoID);
    }

    public void createUserByPromoName(String first_name, String last_name, String promoName){
        Specialities s = new Specialities();
        int promoID = s.getIDBySpeciality(promoName);
        createUserByPromoID(first_name,last_name,promoID);
    }

    public void deleteUser(int userID){
        String selectionKeys = "( " + DBSyntax.UID + " = ? )";
        String[] selectionArgs = new String[]{String.valueOf(userID)};

        m_db.deleteUser(selectionKeys,selectionArgs);
    }

    public List<UserStruct> getAllUsers(){
        String selectionKeys = null;
        String[] selectionArgs = null;

        Cursor DBEvents = m_db.selectFromUsers(null,null,null);

        List<UserStruct> userStructList = new ArrayList<UserStruct>();

        if(!DBEvents.isAfterLast()){
            userStructList.add(new UserStruct(DBEvents));
            while (DBEvents.moveToNext()) {
                userStructList.add(new UserStruct(DBEvents));
            }
        }

        DBEvents.close();

        return userStructList;
    }

    /**
     * Convert all users table to byte array
     * @return OutputStream of List<UserStruct> cast in byte array
     * @throws IOException
     */
    public byte[] usersToBytes(){

        // Get all users
        List<UserStruct> users = getAllUsers();

        String usersInString = "";

        for(int i=0; i<users.size();i++){
            // Add user in String format
            usersInString += users.get(i).first_name + "\t" + users.get(i).last_name + "\t" + users.get(i).promo_ID + "\n";
        }

        //s.substring(0, s.length() - 1);
        return usersInString.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Convert byte array received to List<UserStruct> object and replace it in database
     * @param data
     * @return return 0 if it's ok, else it returns 1
     */
    public int addUsersWithByteArray( byte[] data ){
        // If data is empty, return an error
        if (data == null) return 1;

        String s = new String(data, StandardCharsets.UTF_8);

        String usersString[] = s.split("\n");

        m_db.deleteAllUsers();

        for(int i=0; i<usersString.length-1; i++){
            try {
                String userParams[] = usersString[i].split("\t");
                Log.d("FIRSTNAME",userParams[0]);
                Log.d("LASTNAME",userParams[1]);
                Log.d("PROMOID",userParams[2]);
                createUserByPromoID(userParams[0],userParams[1],Integer.parseInt(userParams[2]));
            }catch (Exception e){
                e.printStackTrace();
                return 1;
            }
        }
        return 0;
    }

}
