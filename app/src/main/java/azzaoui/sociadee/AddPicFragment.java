package azzaoui.sociadee;


import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPicFragment extends Fragment implements SociadeeFragment {

    private gridAddPicAdapter mGridAddPicAdapter;
    private LinkedList<Long> SelectedPic ;
    private final int PicNumbers = 20 ; // 4 * 7
    private int mCurrentNumber = 0;
    private int mDownloadedNumber = 0;
    private JSONArray imageArrayId;
    private boolean mSaveButton = false;
    private ImageButton saveButton;

    public CallBackSwitch goBackCallback;
    private MainActivity.CallBackTopButton mTopButtonCallback;

    private NetworkUserInfo mNetworkUserInfo;

    private static final String LOG = "ADDPIC";
    public AddPicFragment() {
        // Required empty public constructor
        SelectedPic = new LinkedList<>();
        mNetworkUserInfo = new NetworkUserInfo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View v = inflater.inflate(R.layout.fragment_add_pic, container, false);

        SelectedPic.clear();
        Iterator<FacebookImage> iter = Parameters.getFacebookImages().listIterator();
        while (iter.hasNext() ) {
           long myId=  iter.next().getId();
           SelectedPic.add(myId);
           Log.d(LOG,"Selected Pic : " + myId);
        }


        GridView gridView = (GridView)v.findViewById(R.id.GridLayoutAddPic);
        mGridAddPicAdapter = new gridAddPicAdapter(getContext());
        gridView.setAdapter(mGridAddPicAdapter);
        return v;
    }


    // TODO: A tester
    public void validatePic(View v)
    {
        ImageView isAdded = (ImageView) v.getTag(R.id.validatedPic);
        long imId = (long) v.getTag(R.id.FACEBOOK_ID);
        //printLogList();
        if(isAdded.getVisibility() == View.VISIBLE)
        {
            isAdded.setVisibility(View.INVISIBLE);
            SelectedPic.remove(imId);
        }
        else
        {
            SelectedPic.add(imId);
            isAdded.setVisibility(View.VISIBLE );
        }
        //printLogList();
        if(!mSaveButton)
            showSaveButton();
    }

    private void printLogList()
    {
        String res = "SelectedList : ";
        Iterator<Long> iter = SelectedPic.listIterator();
        while (iter.hasNext() ) {
            long next = iter.next();
            res += next+"-";
        }
        Log.d(LOG,res);
    }
    private boolean isSelected(long id)
    {
        boolean res = false;

        Iterator<Long> iter = SelectedPic.listIterator();
        while (iter.hasNext() && !res) {
            long next = iter.next();
            res = next == id ;
            if(res)
                Log.d(LOG,"isSelected : " + id);
        }
        return res;
    }

    public void retrievePictures()
    {
        mGridAddPicAdapter.clearItem();
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
                                Log.d("ADDPIC", "GO Fetch " + imageArrayId.getJSONObject(i).getLong("id"));
                            }

                        } catch (JSONException e) {


                        }

                    }
                }
        ).executeAsync();
    }
    private void showSaveButton()
    {
        mTopButtonCallback.fadeIn(R.drawable.saveicon);
        mSaveButton = true;
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
                            Log.d("ADDPIC", "Fetched " + id);
                            mGridAddPicAdapter.addItem(new gridAddPicAdapter.Item(id, newPic,isSelected(id)));
                            mGridAddPicAdapter.notifyDataSetChanged();
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
    public void setButtonCallback(MainActivity.CallBackTopButton myCallback) {
        mTopButtonCallback = myCallback;
    }

    @Override
    public void onFragmentEnter() {

    }

    @Override
    public void onFragmentLeave() {

    }

    @Override
    public void onTopMenuMenuButtonClick() {
        UpdatePicture newA = new UpdatePicture();
        newA.execute();
    }

    private class UpdatePicture extends AsyncTask<Void, Void, Void> {

        private String toSend ="";
        @Override
        protected void onPreExecute()
        {
            Iterator<Long> iter = SelectedPic.listIterator();
            int i = 1; int s = SelectedPic.size();
            while (iter.hasNext() ) {
                if(i < s)
                    toSend += String.valueOf(iter.next()) + "-" ;
                else
                    toSend += String.valueOf(iter.next());
                i++;
            }
            Log.d("ADDPIC", "SENDING " + toSend);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                mNetworkUserInfo.updatePictures(toSend);

                ListIterator<NetworkLogin.myImage> listIterator = mNetworkUserInfo.getMyImages().listIterator();
                Parameters.getFacebookImages().clear();
                while (listIterator.hasNext()) {
                    NetworkLogin.myImage curIm = listIterator.next();
                    Parameters.addImage(curIm.id,new BitmapDrawable(getResources(), curIm.im));
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                toSend = "";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            mSaveButton = false;
            mTopButtonCallback.fadeOut();
            goBackCallback.goBack();
            // fadeStuffIn();
        }


    }
    public interface CallBackSwitch
    {
        public void goBack();
    }
}
