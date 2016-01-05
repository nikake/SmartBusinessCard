package niklaskerlund.smartbusinesscard.util;

import android.graphics.Bitmap;

import com.firebase.client.Firebase;
import com.firebase.client.core.Tag;

import java.util.ArrayList;

/**
 * Created by Niklas on 2015-12-09.
 */
public class User implements Comparable<User> {

    private String name;
    private String description;
    private ArrayList<Tag> tags;

    public User() {

    }

    public User(String name, String description, ArrayList<Tag> tags) {
        setName(name);
        setDescription(description);
        setTags(tags);
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

    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        } else {
            User u = (User) o;
            return name.equals(u.name);
        }

    }

    @Override
    public int compareTo(User another) {
        return this.name.compareTo(another.name);
    }
}
