package com.frostweepgames.apptracker.controllers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.frostweepgames.apptracker.settings.Constants;
import com.frostweepgames.apptracker.models.ContactsModel;
import com.frostweepgames.apptracker.Helper;
import com.frostweepgames.apptracker.core.IController;

import static android.Manifest.permission.READ_CONTACTS;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class GetContactsController implements IController {

    private Activity _currentActivity;

    private Context _currentContext;

    private ArrayList<ContactsModel> contactList;
    private Cursor cursor;


    public GetContactsController(Activity currentActivity) {

        _currentActivity = currentActivity;
        _currentContext = currentActivity.getApplicationContext();
        contactList = new ArrayList<ContactsModel>();
    }

    public GetContactsController(Context context) {

        _currentContext = context;
        contactList = new ArrayList<ContactsModel>();
    }

    public Object getContent() {

        getContacts();

        return contactList;
    }

    public void getContacts() {

        if(_currentActivity != null) {
            if (!Helper.mayRequestPermission(_currentActivity, READ_CONTACTS, Constants.REQUEST_PERMISSION_VALUE)) {
                return;
            }
        }

        contactList.clear();

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        ContentResolver contentResolver = _currentContext.getContentResolver();

        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);


        if (cursor.getCount() > 0) {

            ContactsModel contact = null;

            while (cursor.moveToNext()) {

                contact = new ContactsModel();

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                contact.id = contact_id;

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if(name.trim().equals("") || hasPhoneNumber <= 0)
                    continue;

                if (hasPhoneNumber > 0) {

                    contact.name = name;

                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    contact.phone = new ArrayList<>();

                    ContactsModel.PhoneNumber phoneNumberLocal;
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phoneNumberLocal = new ContactsModel.PhoneNumber();
                        phoneNumberLocal.phone = phoneNumber;
                        contact.phone.add(phoneNumberLocal);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        contact.email += email + ";";
                    }

                    emailCursor.close();

                    String columns[] = {
                            ContactsContract.CommonDataKinds.Event.START_DATE,
                            ContactsContract.CommonDataKinds.Event.TYPE,
                            ContactsContract.CommonDataKinds.Event.MIMETYPE,
                    };

                    String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                            " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and " + ContactsContract.Data.CONTACT_ID + " = " + contact_id;

                    String[] selectionArgs = null;
                    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

                    Cursor birthdayCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder);

                    if (birthdayCur.getCount() > 0) {
                        while (birthdayCur.moveToNext()) {
                            String birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));

                            contact.note += "Birthday :" + birthday;
                        }
                    }
                    birthdayCur.close();
                }

                contactList.add(contact);
            }
        }
    }
}
