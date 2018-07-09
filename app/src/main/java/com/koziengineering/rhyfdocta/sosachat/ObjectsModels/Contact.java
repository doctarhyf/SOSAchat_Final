package com.koziengineering.rhyfdocta.sosachat.ObjectsModels;

import android.os.Bundle;

/**
 * Created by rhyfdocta on 11/18/17.
 */

public class Contact {

    private int contactID;
    private String contactName;
    private String lastMsgDate;
    private String lastMsg;

    public static final String KEY_CONTACT_ID = "contactID";
    public static final String KEY_CONTACT_NAME = "contactName";
    public static final String KEY_LAST_MESSAGE_DATE = "lastMsgDate";
    public static final String KEY_LAST_MESSAGE = "lastMsg";

    public Contact(int contactID, String contactName, String lastMsg, String lastMsgDate){
        this.contactID = contactID;
        this.contactName = contactName;
        this.lastMsg = lastMsg;
        this.lastMsgDate = lastMsgDate;

    }

    public Bundle toBundle(){
        Bundle b = new Bundle();

        b.putInt(KEY_CONTACT_ID, contactID);
        b.putString(KEY_CONTACT_NAME, contactName);

        return b;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLastMsgDate() {
        return lastMsgDate;
    }

    public void setLastMsgDate(String lastMsgDate) {
        this.lastMsgDate = lastMsgDate;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
}
