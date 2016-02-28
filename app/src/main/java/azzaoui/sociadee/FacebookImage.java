package azzaoui.sociadee;

import android.graphics.drawable.Drawable;

/**
 * Created by Youssef Azzaoui on 28/02/2016.
 * youssef.azzaoui@epfl.ch
 */
public class FacebookImage {

    private long id;
    private Drawable lowResImage;

    public FacebookImage(long id, Drawable lowResImage)
    {
        this.id  = id ;
        this.lowResImage = lowResImage;
    }

    public Drawable getLowResImage() {
        return lowResImage;
    }

    public void setLowResImage(Drawable lowResImage) {
        this.lowResImage = lowResImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
