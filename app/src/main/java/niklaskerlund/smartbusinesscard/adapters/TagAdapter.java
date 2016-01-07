package niklaskerlund.smartbusinesscard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import niklaskerlund.smartbusinesscard.R;
import niklaskerlund.smartbusinesscard.util.Tag;

/**
 * Created by Niklas on 2015-12-28.
 */
public class TagAdapter extends ArrayAdapter<Tag> {

    private ViewHolder viewHolder;

    public TagAdapter(Context context, int resource, ArrayList<Tag> objects) {
        super(context, resource, objects);
    }

    public TagAdapter(Context context, int resource, Tag[] objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Tag tag = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_tag, parent, false);
        }

        TextView tagName = (TextView) convertView.findViewById(R.id.tag_text);
        tagName.setText(tag.getTagName());

        return convertView;
    }

    private static class ViewHolder {
        private TextView itemView;
    }
}
