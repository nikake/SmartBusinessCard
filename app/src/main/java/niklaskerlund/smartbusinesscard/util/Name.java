package niklaskerlund.smartbusinesscard.util;

import java.util.IllegalFormatException;

/**
 * Created by Niklas on 2015-12-09.
 */
public class Name implements Comparable<Name> {
    String firstName, lastName;

    public Name(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }

    private void setFirstName(String firstName) {
        check(firstName);
        this.firstName = firstName;
    }

    private void setLastName(String lastName) {
        check(lastName);
        this.lastName = lastName;
    }

    private void check(String data){
        if (data.equals("") || data == null)
            throw new IllegalArgumentException("Input error.");
    }

    @Override
    public int compareTo(Name another) {
        if(firstName.compareTo(another.firstName) == 0)
            return lastName.compareTo(another.lastName);
        else
            return firstName.compareTo(another.firstName);
    }
}
