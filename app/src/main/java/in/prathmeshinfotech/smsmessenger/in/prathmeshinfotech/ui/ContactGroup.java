package in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.ui;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import in.prathmeshinfotech.smsmessenger.R;
import in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.adapters.Contact;
import in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.adapters.CustomAdapter;
import in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.in.prathmeshinfotech.db.DatabaseHandler;

public class ContactGroup extends ActionBarActivity {
    private CustomAdapter adapter;
    private ContactGroup mGroupContact = null;
    private ArrayList<Contact> contactArrayList = new ArrayList<Contact>();

    private EditText edtGroup;
    private Button btnAddGroup;
    private ListView mList;

    private boolean isAvaGroupName;

    private DatabaseHandler db_GroupContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtGroup = (EditText) findViewById(R.id.editGroupName);
        btnAddGroup = (Button) findViewById(R.id.btnAddGroup);

        mGroupContact = this;
        /**
         * Initialize database handler and connection for db and table.
         */
        Resources res = getResources();
        mList = (ListView) findViewById(R.id.contactlist);  // List defined in XML ( See Below )
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = (Contact) parent.getAdapter().getItem(position);
                mt("Details of Selected Item: " + contact.getmName() + contact.getmPhoneNumber());
                CheckBox chk = (CheckBox) view.findViewById(R.id.chkSelect);
                contact.setmGroupName(edtGroup.getText().toString());
                if (chk.isChecked()) {
                    mt("Checked status => unchecked ");
                    chk.setChecked(false);
                    db_GroupContact.deleteContact(contact);
                } else {
                    mt("unchecked stats => checked");
                    chk.setChecked(true);
                    db_GroupContact.addContact(contact);
                }
                //HashMap<String, String> nameGroup=db_GroupContact.getContactGroup();
            }
        });


        /**************** Create Custom Adapter *********/
        adapter = new CustomAdapter(this, contactArrayList, res);
        mList.setAdapter(adapter);

        new ContactData().execute();

        btnAddGroup.setOnClickListener(new ClickListener());
        db_GroupContact = new DatabaseHandler(this);
    }

    private void mt(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public class ClickListener implements View.OnClickListener {
        public ClickListener() {
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnAddGroup:
                    if (edtGroup.getText().toString().length() <= 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ContactGroup.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Group Name Required,\n Please enter group name");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        isAvaGroupName = false;
                    } else {

                        mt(edtGroup.getText().toString() + " " + adapter.getCount());
                        for (int i = 0; i < adapter.getCount(); i++) {
                            Contact contact = adapter.getItem(i);
                            mt("contacts " + i + Boolean.toString(contact.isChecked()));
                            if (contact.isChecked()) {
                                mt("contacts " + i + contact.getmName() + contact.getmPhoneNumber());
                            }
                        }
                        isAvaGroupName = true;
                    }
                    break;
            }
        }
    }


   /* private void populate() {

		*//*
         * String [] columns = { "pname", "pid", "pcity"}; String selection =
		 * "city = ? and pname = ?"; String [] selectionArgs = { "pune",
		 * "laptop" }; String groupBy = "city"; String having =
		 * "sum(price) <= 10000"; String orderBy = "price asc, name desc";
		 * Cursor c = db.query("products", columns, selection, selectionArgs,
		 * groupBy, having, orderBy);
		 *//*

        // db.rawQuery("select * from products", selectionArgs)

        data.clear();

        Cursor c = db.query("products", null, null, null, null, null,
                "pid desc");

        while (c.moveToNext()) {
            String info = c.getInt(0) + " - " + c.getString(1) + " - "
                    + c.getString(2);

            data.add(info);
        }
        adapter.notifyDataSetChanged();
    }*/

    public class ContactData extends AsyncTask<String, Void, ArrayList<Contact>> {

        private ProgressBar spinner;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mList.setVisibility(View.GONE);
            spinner = (ProgressBar) findViewById(R.id.progressSpinner);
            spinner.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayList<Contact> doInBackground(String... params) {
            String phoneNumber = "";
            String email = "";

            ArrayList<Contact> listContact = new ArrayList<Contact>();

            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String PHOTO_THUMBNAIL_DATA = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

            Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;

            ContentResolver contentResolver = getContentResolver();

            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            //cursor.moveToFirst();
            // Loop for every contact in the phone
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    String photouri = cursor.getString(cursor.getColumnIndex(PHOTO_THUMBNAIL_DATA));
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {

                        // Query and loop for every phone number of the contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (phoneCursor.moveToNext()) {

                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                            final Contact contact = new Contact();
                            contact.set_id(Integer.parseInt(contact_id));
                            contact.setmName(name);

                            contact.setmPhoneNumber(phoneNumber);
                            if (photouri != null) {
                                contact.setmContactImage(photouri);
                            }
                            listContact.add(contact);

                            Log.e("Msg: ID", contact_id);
                            Log.e("Msg: Name", name);
                            Log.e("Msg: Phone", phoneNumber);
                        }
                        phoneCursor.close();
                    /*// Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        output.append("\nEmail:" + email);
                    }
                    emailCursor.close();*/

                    }

                }
            }
            return listContact;
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            contactArrayList.clear();
            contactArrayList.addAll(contacts);
            adapter.notifyDataSetChanged();
            spinner.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
        }
    }
}