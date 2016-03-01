package azzaoui.sociadee;

import android.graphics.drawable.Drawable;

import java.util.LinkedList;

/**
 * Created by Youssef Azzaoui on 29/02/2016.
 * youssef.azzaoui@epfl.ch
 */
public class PictureList {

    public long userId;
    public LinkedList<FacebookImage> imageList;
    public PictureList( LinkedList<FacebookImage> imageList)
    {
        this.imageList = imageList;
    }

    
    public static class Picture{
        public Drawable lowRes;
        public Drawable highRes;
        public boolean isHighres;
        public long id;

    }
}
