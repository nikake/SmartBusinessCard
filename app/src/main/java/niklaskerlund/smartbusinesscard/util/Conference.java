package niklaskerlund.smartbusinesscard.util;

import android.location.Location;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Niklas on 2015-12-28.
 */
public class Conference {

    private String name, description, date, startTime, endTime, owner;
    private Double latitude, longitude;
    private ArrayList<User> invitedUsers;
    private ArrayList<User> attendingUsers;
    private ArrayList<User> nonAttendingUsers;

    public Conference() {

    }

    public Conference(String name, String description, String date, String startTime, double latitude, double longitude, String uid){
        setName(name);
        setDescription(description);
        setDate(date);
        setStartTime(startTime);
        setEndTime(endTime);
        setLatitude(latitude);
        setLongitude(longitude);
        setOwner(uid);
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    private void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    private void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private void setOwner(String uid) {
        this.owner = uid;
    }

    public String getOwner() {
        return owner;
    }
}
