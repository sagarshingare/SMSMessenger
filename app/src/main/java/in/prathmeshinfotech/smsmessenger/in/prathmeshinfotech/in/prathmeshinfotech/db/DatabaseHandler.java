package in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.in.prathmeshinfotech.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.adapters.Contact;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Group_Contanct";
    private static final String TABLE_CONTACTS = "group_list";
    private static final String KEY_ID = "_id";
    private static final String GROUP_NAME = "group_name";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";


    private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + "  TEXT, "
            + GROUP_NAME + " TEXT, "
            + KEY_PH_NO + " TEXT " + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
/* //TODO need in feature
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATETABLE);
        Log.e("Create DB ", CREATETABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addContact(Contact contact) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getmName());  //Contact group name
            values.put(GROUP_NAME, contact.getmGroupName()); // Contact Name
            values.put(KEY_PH_NO, contact.getmPhoneNumber()); // Contact Phone

            // Inserting Row
            long result = db.insert(TABLE_CONTACTS, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
            Log.e("DB Ops: ", "Result is " + result + "\nValues are  " + values.toString());
        } catch (Exception e) {
            Log.e("DB Ops: ", e.getMessage());
        }
    }

   /*  TODO need in feature
   // code to get the single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, GROUP_NAME, KEY_PH_NO}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        Log.e("DB Ops: ", "Contact has been fetched");
        return contact;
    }*/

    /* // code to get the group contacts
     public HashMap<String, String> getContactGroup() {
         SQLiteDatabase db = this.getReadableDatabase();

         Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,KEY_NAME, GROUP_NAME,
                         KEY_PH_NO}, null,
                 null, null, null, null, null);
         if (cursor != null)
             cursor.moveToFirst();

         Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                 cursor.getString(1), cursor.getString(2), cursor.getString(3));
         HashMap<String, String> nameGroup = new HashMap<String, String>();
         nameGroup.put("txtGroupName", cursor.getString(1));
         nameGroup.put("txtImage", Integer.toString(R.drawable.ic_launcher));

         // return contact
         Log.e("DB Ops: ", "All contact group has been fetched");
         return nameGroup;
     }
 */
    // code to get all contacts in a list view
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  distinct " + GROUP_NAME + " FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                //contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setmGroupName(cursor.getString(0));
                // contact.setmPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        Log.e("DB Ops: ", "All contact has been fetched");
        // return contact list
        return contactList;
    }
/*
    // code to update the single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getmName());
        values.put(KEY_PH_NO, contact.getmPhoneNumber());
        Log.e("DB Ops: ", "Updated given contact");
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.get_id())});

    }*/

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.get_id())});
        db.close();
        Log.e("DB Ops: ", "Deleted the given contact");
    }

    /*// Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        Log.e("DB Ops: ", "Count of contact in db");
        // return count
        return cursor.getCount();
    }
*/
    public ArrayList<String> getGroupContact(String recieptName) {

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + KEY_PH_NO + " FROM " + TABLE_CONTACTS + " where " + GROUP_NAME + " LIKE '" + recieptName + "' ";
        ArrayList<String> contactList = new ArrayList<>();
        Log.e(selectQuery, "");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.e("DB Ops:PH: ", cursor.getString(0));
                contactList.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        Log.e("DB Ops: ", "All contact has been fetched");
        // return contact list
        cursor.close();
        return contactList;
    }
}

