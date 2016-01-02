package niklaskerlund.smartbusinesscard.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.util.Conference;

/**
 * Created by Niklas on 2015-12-30.
 */
public class ConferenceAdapter extends ArrayAdapter<Conference> {

    public ConferenceAdapter(Context context, int resource, ArrayList<Conference> objects) {
        super(context, resource);
    }
}
