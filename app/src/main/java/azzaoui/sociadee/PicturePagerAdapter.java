package azzaoui.sociadee;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Youssef Azzaoui on 29/02/2016.
 * youssef.azzaoui@epfl.ch
 */
public class PicturePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    Drawable[] mResources ;
    public  ImageView[] mImageViews;
    public PicturePagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    public void setResources(Drawable[] mResources)
    {
        this.mResources = mResources;
        mImageViews = new ImageView[mResources.length];
    }

    public void updateResource(Drawable d , int pos)
    {
            mResources[pos] = d;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_picture_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.mainPicture);
        imageView.setImageDrawable(mResources[position]);

        if(mImageViews != null)
            mImageViews[position] = imageView;

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}
