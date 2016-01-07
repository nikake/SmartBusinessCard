package niklaskerlund.smartbusinesscard.util;



import java.util.ArrayList;

/**
 * Created by Niklas on 2015-12-09.
 */
public class User implements Comparable<User> {

    private String name;
    private String description;
    private ArrayList<Interest> interests;
    private ArrayList<String> contacts;

    public User() {

    }

    public User(String name, String description, ArrayList<Interest> interests, ArrayList<String> contacts) {
        setName(name);
        setDescription(description);
        setInterests(interests);
        setContacts(contacts);
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

    private void setInterests(ArrayList<Interest> interests) {
        if (interests != null)
            this.interests = interests;
        else
            this.interests = new ArrayList<>();
    }

    public ArrayList<Interest> getInterests(){
        return interests;
    }

    private void setContacts(ArrayList<String> contacts) {
        if (contacts != null)
            this.contacts = contacts;
        else
            this.contacts = new ArrayList<>();
    }

    public ArrayList<String> getContacts() {
        return contacts;
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
