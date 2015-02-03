package in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.adapters;

public class Contact {

    private int _id = 0;

    private String mGroupName = "";
    private String mName = "";
    private String mContactImage = "";
    private String mPhoneNumber = "";
    private boolean isChecked;

    public Contact() {
    }

    //private static contact singleton = new contact( );

    public Contact(int id, String mGroupName, String name, String _phone_number) {
        this._id = id;
        this.mGroupName = mGroupName;
        this.mName = name;
        this.mPhoneNumber = _phone_number;
    }
/*
    TODO need in feature
    public Contact(String name, String _phone_number) {
        this.mName = name;
        this.mPhoneNumber = _phone_number;
    }*/

    public boolean isChecked() {
        return isChecked;
    }
/*TODO need in feature
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }*/

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getmGroupName() {
        return mGroupName;
    }

    public void setmGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public String getmName() {
        return this.mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    /*
        TODO need in feature
     public String getmContactImage() {
        return this.mContactImage;
    }
*/
    public void setmContactImage(String mContactImage) {
        this.mContactImage = mContactImage;
    }

    public String getmPhoneNumber() {
        return this.mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }
}