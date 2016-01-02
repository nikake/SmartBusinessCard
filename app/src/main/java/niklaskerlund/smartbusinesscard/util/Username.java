package niklaskerlund.smartbusinesscard.util;

/**
 * Created by Niklas on 2015-12-09.
 */
public class Username implements Comparable<Username> {

    private String username;

    public Username(String username) {
        setUsername(username);
    }


    private void setUsername(String username) throws IllegalArgumentException {
        if (username.equals("") || username == null)
            throw new IllegalArgumentException("Username must contain at least 1 character.");
        this.username = username;
    }

    @Override
    public int compareTo(Username another) {
        return username.compareTo(another.username);
    }

    public String toString(){
        return username;
    }
}
