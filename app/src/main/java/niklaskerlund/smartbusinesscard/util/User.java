package niklaskerlund.smartbusinesscard.util;

import android.graphics.Bitmap;

import com.firebase.client.Firebase;
import com.firebase.client.core.Tag;

import java.util.ArrayList;

/**
 * Created by Niklas on 2015-12-09.
 */
public class User implements Comparable<User> {

    private Firebase firebase;
    private String name;
    private String username;
    private String description;
    private ArrayList<Tag> tags;
    private Bitmap profilePicture;

    public User(String username, String name, String description, ArrayList<Tag> tags, Bitmap profilePicture) {
        setUsername(username);
        setName(name);
        setDescription(description);
        setProfilePicture(profilePicture);
        setTags(tags);
        firebaseUserData();
    }

    public User(String username, String name, String description, ArrayList<Tag> tags) {
        this(username, name, description, tags, null);
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
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

    private void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }

    private void setProfilePicture(Bitmap profilePicture){
        if (profilePicture != null)
            this.profilePicture = profilePicture;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        } else {
            User u = (User) o;
            return name.equals(u.name) && username.equals(u.username);
        }

    }

    @Override
    public int compareTo(User another) {
        return this.name.compareTo(another.name);
    }

    public void firebaseUserData() {
        Firebase newUserFirebase = firebase.child("users").child("username");
        newUserFirebase.child("name").setValue(name);
        newUserFirebase.child("description").setValue(description);
        newUserFirebase.child("tags").setValue(tags);
    }
}
