package azzaoui.sociadee;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class gridAddPicAdapter  extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;

    public final int FACEBOOKID = 1;
    private BaseAdapter thisAdapter = this;

    public gridAddPicAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    public void addItem(Item obj)
    {
        mItems.add(obj);
    }

    public  void clearItem()
    {
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
        TextView name;
        Item item = getItem(i);

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_addpic_item, viewGroup, false);
            v.setTag(R.id.pictureElemAddPic, v.findViewById(R.id.pictureElemAddPic));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.validatedPic,v.findViewById(R.id.validatedPic));
            v.setTag(R.id.FACEBOOK_ID,item.facebookId);
        }

        picture = (ImageView) v.getTag(R.id.pictureElemAddPic);
        //name = (TextView) v.getTag(R.id.text);


        picture.setImageDrawable(item.drawable);
        if(item.selected)
            ((ImageView) v.getTag(R.id.validatedPic)).setVisibility(View.VISIBLE);
        //name.setText(Long.toString(item.facebookId));


        return v;
    }

    public static class Item {
        public final long facebookId;
        public final Drawable drawable;
        public boolean selected;

        Item(long facebookId, Drawable drawable, boolean selected) {
            this.facebookId = facebookId;
            this.drawable = drawable;
            this.selected = selected;
        }
    }
}