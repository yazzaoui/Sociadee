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

    private final int PicNumbers = 200 ; // 4 * 7
    private int mCurrentNumber = 0;
    private int mDownloadedNumber = 0;
    private JSONArray imageArrayId;
    private BaseAdapter thisAdapter = this;

    public gridAddPicAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    // Shouldn't have been written here I know it
    public void retrievePictures()
    {
        mItems.clear();
        mCurrentNumber = 0;
        mDownloadedNumber = 0;
        Bundle parameters = new Bundle();
        parameters.putString("limit", Integer.toString(PicNumbers));
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject firstResponse = response.getJSONObject();
                        try {
                            imageArrayId = firstResponse.getJSONArray("data");
                            mCurrentNumber = imageArrayId.length();
                            for (int i = 0; i < 4 && i < mCurrentNumber; i++)
                            {
                               fetchImageUrl( imageArrayId.getJSONObject(i).getLong("id"));
                            }

                        } catch (JSONException e) {


                        }

                    }
                }
        ).executeAsync();
    }

    private void fetchImageUrl(final long id)
    {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+id,
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject firstResponse = response.getJSONObject();
                        try {
                            JSONArray imagesIds = firstResponse.getJSONArray("images");
                            String imageUrl = imagesIds.getJSONObject(imagesIds.length()-1).getString("source");
                            URL image_value = new URL(imageUrl);
                            Drawable newPic = Drawable.createFromStream(image_value.openConnection().getInputStream(), "blah");

                            mItems.add(new Item(id,newPic));
                            thisAdapter.notifyDataSetChanged();
                            addedPic();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();

    }

    private void addedPic()
    {
        mDownloadedNumber++;
        if(mDownloadedNumber%4 == 0)
        {
            int i=0;
            int mOldDownloadedNumber = mDownloadedNumber;
            while(i < 4 && mOldDownloadedNumber + i < mCurrentNumber)
            {
                try {
                fetchImageUrl(imageArrayId.getJSONObject(mOldDownloadedNumber+i).getLong("id"));
                 } catch (JSONException e) {
                e.printStackTrace();
                }

                i++;
            }
        }
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

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_addpic_item, viewGroup, false);
            v.setTag(R.id.pictureElemAddPic, v.findViewById(R.id.pictureElemAddPic));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.validatedPic,v.findViewById(R.id.validatedPic));

        }

        picture = (ImageView) v.getTag(R.id.pictureElemAddPic);
        //name = (TextView) v.getTag(R.id.text);

        Item item = getItem(i);

        picture.setImageDrawable(item.drawable);
        //name.setText(Long.toString(item.facebookId));


        return v;
    }

    private static class Item {
        public final long facebookId;
        public final Drawable drawable;

        Item(long facebookId, Drawable drawable) {
            this.facebookId = facebookId;
            this.drawable = drawable;
        }
    }
}