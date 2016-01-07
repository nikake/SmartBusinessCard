package niklaskerlund.smartbusinesscard.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.util.Interest;
import niklaskerlund.smartbusinesscard.util.User;

/**
 * Created by Niklas on 2015-12-30.
 */
public class ContactsAdapter extends ArrayAdapter<User> {

    private static final String TAG = ContactsAdapter.class.getSimpleName();
    private ViewHolder viewHolder;

    public ContactsAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        User contact = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_contact, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.contactTags = (TextView) convertView.findViewById(R.id.contact_tags);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.contactName.setText(contact.getName());
        if(contact.getInterests() != null )
            viewHolder.contactTags.setText(listToString(contact.getInterests()));
        Log.d(TAG, "InterestArrayList - Name: " + contact.getName() + " Interests: " + contact.getInterests());

        return convertView;
    }

    private String listToString(ArrayList<Interest> list){
        String interestString = "";
        int index = 0;
        while(index < list.size() - 1) {
            interestString += list.get(index).toString() + ", ";
            index++;
        }
        interestString += list.get(index);
        return interestString;
    }

    private static class ViewHolder {
        private TextView contactName, contactTags;
    }
}
