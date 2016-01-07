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

/**
 * Created by Niklas on 2015-12-28.
 */
public class ProfileInterestAdapter extends ArrayAdapter<Interest> {

    private static final String TAG = ProfileInterestAdapter.class.getSimpleName();
    private ViewHolder viewHolder;

    public ProfileInterestAdapter(Context context, int resource, ArrayList<Interest> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Interest interest = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_tag_profile, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.itemView = (TextView) convertView.findViewById(R.id.profile_tag);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.d(TAG, "Interest: " + position + " Name: " + interest.getTagName());
        viewHolder.itemView.setText(interest.getTagName());
        return convertView;
    }

    private static class ViewHolder {
        private TextView itemView;
    }
}
