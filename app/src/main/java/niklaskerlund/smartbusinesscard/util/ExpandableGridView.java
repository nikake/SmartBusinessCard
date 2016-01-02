package niklaskerlund.smartbusinesscard.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Niklas on 2015-12-22.
 */
public class ExpandableGridView extends GridView {

    boolean expanded = false;

    public ExpandableGridView(Context context) {
        super(context);
    }

    public ExpandableGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ExpandableGridView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeeasureSpec){
        if(expanded) {
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeeasureSpec);
        }
    }

    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }
}
