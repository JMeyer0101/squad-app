package com.example.mars.httpapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mars on 3/23/16.
 */
public class StudyGroup implements Parcelable {

        public int id;
        public String department;
        public int classnumber;
        public String time;
        public String date;
        public String description;
        private int mData;


        public StudyGroup(int ID, String dept, int classnum, String t, String descr, String datetime){
            this.id = ID;
            this.department = dept;
            this.classnumber = classnum;
            this.time = t;
            this.description = descr;
            this.date = datetime;
        }
    
    /* everything below here is for implementing Parcelable */


    @Override
    public int describeContents() {
        return 0;
    }

    // write object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[] {
                String.valueOf(this.id),
                this.department,
                String.valueOf(this.classnumber),
                this.date,
                this.time,
                this.description});
    }

    // this is used to regenerate the object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<StudyGroup> CREATOR = new Parcelable.Creator<StudyGroup>() {
        public StudyGroup createFromParcel(Parcel in) {
            return new StudyGroup(in);
        }

        public StudyGroup[] newArray(int size) {
            return new StudyGroup[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private StudyGroup(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.department= data[1];
        this.classnumber = Integer.parseInt(data[2]);
        this.date= data[3];
        this.time = data[4];
        this.description = data[5];
    }
}
