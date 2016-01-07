package niklaskerlund.smartbusinesscard.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.util.Conference;
import niklaskerlund.smartbusinesscard.util.Tag;
import niklaskerlund.smartbusinesscard.util.User;

/**
 * Created by Niklas on 2015-12-30.
 */
public class ContactsAdapter extends ArrayAdapter<String> {

    private static final String FIREBASE_URL = "https://smartbusinesscard.firebaseio.com/";
    private ViewHolder viewHolder;
    private Firebase firebase, contactRef;
    private ArrayList<Tag> contactTags = new ArrayList<>();


    public ContactsAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        firebase = new Firebase(FIREBASE_URL);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String contactUid = getItem(position);

        contactRef = firebase.child("users").child(contactUid);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_contact, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.contactTags = (TextView) convertView.findViewById(R.id.contact_tags);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setContactName();
        setContactTags();
        return convertView;
    }

    public void setContactName() {
        contactRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewHolder.contactName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void setContactTags(){
        contactRef.child("tags").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewHolder.contactTags.setText(contactTags.toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private static class ViewHolder {
        private TextView contactName, contactTags;
    }
}
