package niklaskerlund.smartbusinesscard.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Niklas on 2015-12-31.
 */
public class CircularImageView extends ImageView {

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Bitmap circularShape(Bitmap image) {
        int height = image.getHeight();
        int width = image.getWidth();
        Bitmap newImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newImage);
        Path path = new Path();
        path.addCircle(((float) width / 2), ((float) height / 2), (Math.min(((float)width),((float) height)) / 2), Path.Direction.CCW);

        canvas.clipPath(path);

        Bitmap source = image;
        canvas.drawBitmap(source, new Rect(0, 0, source.getWidth(), source.getHeight()), new Rect(0, 0, width, height), null);
        
        return newImage;
    }
}
