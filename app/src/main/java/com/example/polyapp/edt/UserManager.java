package com.example.polyapp.edt;

import android.database.Cursor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        return getUserById(0);
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
    public byte[] usersToBytes() throws IOException {

        // Get all users
        List<UserStruct> users = getAllUsers();

        // Stream data in ByteArrayOutputStream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(users);
        oos.flush();

        // Convert ByteArrayOutputStream to byte array
        byte [] data = bos.toByteArray();
        return data;
    }

    /**
     * Convert byte array received to List<UserStruct> object and replace it in database
     * @param data
     * @return
     */
    public int changeUsersWithByteArray( byte[] data ){
        // If data is empty, return an error
        if (data == null) return 1;

        // Create the new object
        List<UserStruct> users = null;
        try {
            // Create a ByteArrayInputStream to convert the byte array to List<UserStruct>
            ByteArrayInputStream bi = new ByteArrayInputStream(data);
            ObjectInputStream oi = new ObjectInputStream(bi);

            // Try to cast the data into List<UserStruct> object
            users = (List<UserStruct>) oi.readObject();

            // Close stream
            bi.close();
            oi.close();
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        // Remove old users
        m_db.deleteAllUsers();

        // For each user in List<UserStruct>
        for(int i=0;i<users.size();i++){
            m_db.insertUser(users.get(i).first_name,users.get(i).last_name,users.get(i).getPromoID());
        }

        return 0;
    }

}
