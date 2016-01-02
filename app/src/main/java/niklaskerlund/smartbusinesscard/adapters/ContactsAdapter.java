package niklaskerlund.smartbusinesscard.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.util.User;

/**
 * Created by Niklas on 2015-12-30.
 */
public class ContactsAdapter extends ArrayAdapter<User> {

    public ContactsAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource);
    }
}
