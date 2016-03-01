package azzaoui.sociadee;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Youssef Azzaoui on 28/02/2016.
 * youssef.azzaoui@epfl.ch
 */
public class gridShowPicAdapter  extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;


    private BaseAdapter thisAdapter = this;

    public gridShowPicAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    public void addItem(Item obj) {
        mItems.add(obj);
    }

    public void clearItem() {
        mItems.clear();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).facebookId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        Item item = getItem(i);

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_showpic_item, viewGroup, false);
            v.setTag(R.id.pictureElemShowPic, v.findViewById(R.id.pictureElemShowPic));

        }
        v.setTag(R.id.FACEBOOK_ID, item.facebookId);
        v.setTag(R.integer.ITEM_NUM,i);
        picture = (ImageView) v.getTag(R.id.pictureElemShowPic);
        //name = (TextView) v.getTag(R.id.text);


        picture.setImageDrawable(item.drawable);
        //name.setText(Long.toString(item.facebookId));


        return v;
    }

    public static class Item {
        public final long facebookId;
        public final Drawable drawable;

        Item(long facebookId, Drawable drawable) {
            this.facebookId = facebookId;
            this.drawable = drawable;
        }
    }
}