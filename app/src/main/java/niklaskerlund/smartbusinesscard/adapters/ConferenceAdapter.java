package niklaskerlund.smartbusinesscard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.util.Conference;

/**
 * Created by Niklas on 2015-12-30.
 */
public class ConferenceAdapter extends ArrayAdapter<Conference> {

    private ViewHolder viewHolder;
    TextView conferenceName, conferenceDescription, conferenceDate;

    public ConferenceAdapter(Context context, int resource, ArrayList<Conference> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Conference conference = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_conference, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.confName = (TextView) convertView.findViewById(R.id.conference_name);
            viewHolder.confDescription = (TextView) convertView.findViewById(R.id.conference_description);
            viewHolder.confDate = (TextView) convertView.findViewById(R.id.conference_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.confName.setText(conference.getName());
        viewHolder.confDescription.setText(conference.getDescription());
        viewHolder.confDate.setText(conference.getDate());
//        conferenceName = (TextView) convertView.findViewById(R.id.conference_name);
//        conferenceName.setText(conference.getName());
//
//        conferenceDescription = (TextView) convertView.findViewById(R.id.conference_description);
//        conferenceDescription.setText(conference.getDescription());
//
//        conferenceDate = (TextView) convertView.findViewById(R.id.conference_date);
//        conferenceDate.setText(conference.getDate());

        return convertView;
    }

    private static class ViewHolder {
        private TextView confName, confDescription, confDate;
    }
}
