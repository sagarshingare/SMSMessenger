package in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.ui;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.prathmeshinfotech.smsmessenger.R;
import in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.adapters.Contact;
import in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.in.prathmeshinfotech.db.DatabaseHandler;

public class GroupSms extends ActionBarActivity {

    DatabaseHandler db_GroupContact;
    Button sendgsms;
    EditText smsbody;
    SimpleAdapter adapter;
    private MultiAutoCompleteTextView autoComplete;
    private ArrayList<HashMap<String, String>> recieptList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_group_sms);

        sendgsms = (Button) findViewById(R.id.btnSendsms);
        smsbody = (EditText) findViewById(R.id.editText);
        sendgsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSendsms:
                        if (autoComplete.getText().toString().length() > 0 || smsbody.getText().toString().length() > 0) {
                            String[] list = autoComplete.getText().toString().split(",");
                            String bodyText = smsbody.getText().toString().trim();

                            for (String name : list) {
                                Log.e("Reciept splited ", name);
                                if (name != " " || name.length() > 0) {
                                    for (HashMap<String, String> entry : recieptList) {
                                        //Log.e("Matching ", entry.get("txtGroupName") + " with " + name + " and result is " + entry.get("txtGroupName").trim().equals(name.trim()));
                                        String recieptName = entry.get("txtGroupName").trim();
                                        String recieptPhone = entry.get("txtPhoneNumber").trim();


                                        if (recieptName.equals(name.trim())) {
                                            if (recieptPhone.equals("Group")) {
                                                //sending group sms.
                                                ArrayList<String> groupPhoneNumber = db_GroupContact.getGroupContact(recieptName);
                                                for (String phoneNumber : groupPhoneNumber) {
                                                    Log.e("Group SMS sent to Phone ", phoneNumber);
                                                    sentSMS(phoneNumber, bodyText);
                                               /* Toast.makeText(getApplicationContext(), "SMS sent." + name + "=>" + entry.get("txtPhoneNumber"),
                                                        Toast.LENGTH_LONG).show();
                                                Log.e("Sent sms to " + name, entry.get("txtPhoneNumber"));*/
                                                }
                                            } else {

                                                Log.e("SMS sent to Phone ", recieptName + recieptPhone);
                                                sentSMS(recieptPhone, bodyText);
                                           /* Toast.makeText(getApplicationContext(), "SMS sent." + name + "=>" + entry.get("txtPhoneNumber"),
                                                    Toast.LENGTH_LONG).show();
                                            Log.e("Sent sms to " + name, entry.get("txtPhoneNumber"));*/
                                            }

                                        } else {
                                            Log.e("TEST", "");//Exceptional Case occured", name + " " + recieptName + " " + recieptPhone);
                                        }
                                    }
                                }
                            }
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(GroupSms.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Group Name Required,\n Please enter group name");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        break;
                }

            }

            private void sentSMS(String phoneNumber, String bodyText) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, bodyText, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent to " + phoneNumber,
                        Toast.LENGTH_LONG).show();
                //Log.e("Sent sms to " + name, entry.get("txtPhoneNumber"));
            }
        });


        autoComplete = (MultiAutoCompleteTextView) findViewById(R.id.autoComplete);
        autoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hm = (HashMap<String, String>) parent.getAdapter().getItem(position);
            }
        };


        /******* Key used in hash-map *******/
        String[] keyInHashMap = {"txtGroupName", "txtPhoneNumber", "txtImage"};
        int[] listViewId = {R.id.txtName, R.id.txtPhoneNumber, R.id.imgContact};

        //adapter = new SimpleAdapter(GroupSms.this, recieptList, R.layout.layout, keyInHashMap, listViewId);

        adapter = new SimpleAdapter(GroupSms.this, recieptList, R.layout.layout, keyInHashMap, listViewId);
        autoComplete.setAdapter(adapter);
        new RecieptData().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //mt("onCreateOptionsMenu");

        MenuInflater inflater =
                getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


    		/*
            //TODO we need this in future when app will published
    		MenuItem item;
    		item = menu.add(0, O_HELP, 0, "Help");
    		item.setIcon(R.drawable.ic_launcher);
    		item.setAlphabeticShortcut('h');
    		item.setNumericShortcut('9');
    		item.setCheckable(true);

    		menu.add(0, O_INFO, 0, "Info");
    		SubMenu sMenu =
    		menu.addSubMenu(0, O_SETTINGS, 0, "Settings");
    		item = sMenu.add(1, S_SYS, 0, "System Settings");
    		item.setIcon(R.drawable.ic_launcher);
    		item.setCheckable(true);
    		item.setChecked(true);

    		sMenu.add(1, S_PHONE, 0, "Phone Settings");
    		*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendGrouSMS:
                Intent intent = new Intent(GroupSms.this, ContactGroup.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class RecieptData extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

        private ProgressBar spinner;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mList.setVisibility(View.GONE);
            spinner = (ProgressBar) findViewById(R.id.progressSpinner2);
            spinner.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            String phoneNumber = null;
            String email = null;

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

            //////****************Getting group list*************************----*/
            ArrayList<HashMap<String, String>> finalAdapterData = new ArrayList<HashMap<String, String>>();
            db_GroupContact = new DatabaseHandler(getBaseContext());

            List<Contact> groupList = db_GroupContact.getAllContacts();
            List<Contact> contactsPhone;


            for (int i = 0; i < groupList.size(); i++) {
                HashMap<String, String> nameGroup = new HashMap<String, String>();
                nameGroup.put("txtGroupName", groupList.get(i).getmGroupName());
                nameGroup.put("txtPhoneNumber", "Group");
                nameGroup.put("txtImage", Integer.toString(R.drawable.ic_launcher));
                finalAdapterData.add(nameGroup);
                // Log.e("\n DB Name", groupList.get(i).getmName());
                Log.e("\n group name", groupList.get(i).getmGroupName());
                // Log.e("\n phonenumber", groupList.get(i).getmPhoneNumber());
            }

            //*********************************************

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
                            HashMap<String, String> nameGroup = new HashMap<String, String>();
                           /* contact.set_id(Integer.parseInt(contact_id));
                            contact.setmNAdapter Valuesame(name);
                            contact.setmPhoneNumber(phoneNumber);*/
                            nameGroup.put("txtGroupName", name);
                            nameGroup.put("txtPhoneNumber", phoneNumber);
                            nameGroup.put("txtImage", Integer.toString(R.drawable.ic_launcher));


                            if (photouri != null) {
                                contact.setmContactImage(photouri);
                            }
                            //listContact.add(contact);
                            finalAdapterData.add(nameGroup);

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
            for (int i = 0; i < finalAdapterData.size(); i++) {
                HashMap<String, String> nameGroup = finalAdapterData.get(i);
                Log.e("Adapter Values Name", nameGroup.get("txtGroupName"));
                Log.e("Adapter Values phone", nameGroup.get("txtPhoneNumber"));
                Log.e("Adapter Values img", nameGroup.get("txtImage"));
            }
            return finalAdapterData;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> resultd) {
            recieptList.clear();
            recieptList.addAll(resultd);

            adapter.notifyDataSetChanged();
            spinner.setVisibility(View.GONE);
            //mList.setVisibility(View.VISIBLE);
        }
    }
}
