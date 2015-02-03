package in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.prathmeshinfotech.smsmessenger.R;

public class CustomAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    public Resources res;
    Contact contactDetails = null;
    /**
     * ******** Declare Used Variables ********
     */
    private Activity activity;
    private ArrayList data;

    /**
     * **********  CustomAdapter Constructor ****************
     */
    public CustomAdapter(Activity activity, ArrayList contactList, Resources imageResouce) {

        /********** Take passed values **********/
        this.activity = activity;
        data = contactList;
        res = imageResouce;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) this.activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * ***** What is the size of Passed Arraylist Size ***********
     */
    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Contact getItem(int position) {
        return (Contact) data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * *** Depends upon data size called for each row , Create each ListView row ****
     */
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.contact_custom_list, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.name = (TextView) vi.findViewById(R.id.txtFullName);
            holder.mobileNumber = (TextView) vi.findViewById(R.id.txtDefaultMobileNumber);
            holder.image = (ImageView) vi.findViewById(R.id.imgContactPerson);
            holder.chkSelect = (CheckBox) vi.findViewById(R.id.chkSelect);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.name.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/
            contactDetails = null;
            contactDetails = (Contact) data.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.name.setText(contactDetails.getmName());
            holder.mobileNumber.setText(contactDetails.getmPhoneNumber());
            holder.image.setImageResource(R.drawable.ic_launcher);

            //TODO Need to add the exact image from contact PHOTO_URI and set image using view.post image/bitmap.
           /* holder.image.setImageResource(
                    res.getIdentifier(
                            "com.androidexample.customlistview:drawable/" + contactDetails.getmContactImage()
                            , null, null));*/

        }
        return vi;
    }

    /**
     * ****** Create a holder Class to contain inflated xml file elements ********
     */
    public static class ViewHolder {
        public TextView name;
        public TextView mobileNumber;
        public ImageView image;
        public CheckBox chkSelect;
    }
}