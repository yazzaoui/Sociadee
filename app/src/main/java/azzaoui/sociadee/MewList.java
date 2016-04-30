package azzaoui.sociadee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Youssef Azzaoui on 30/04/2016.
 * youssef.azzaoui@epfl.ch
 */
public class MewList extends ListView {

    public MewList(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
    }

    public MewList(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
    }

    public MewList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap m = this.getDrawingCache();
        Bitmap newB = m.copy(m.getConfig(),true);



    }
}
