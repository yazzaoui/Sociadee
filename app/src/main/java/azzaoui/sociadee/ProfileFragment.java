package azzaoui.sociadee;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements SociadeeFragment {


    private TextView mLocationTextView ;
    private ImageView mLocationIcon;
    private EditText mEditMyAnnounce;
    private Boolean mCitySetUp = false;
    private Boolean mSaveButton = false;
    private MainActivity.CallBackTopButton mTopButtonCallback;

    private gridShowPicAdapter mGridShowPicAdapter;
    private NetworkUserInfo mNetworkUserInfo;

    public ProfileFragment() {
        // Required empty public constructor
        mNetworkUserInfo = new NetworkUserInfo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppThemeCustom);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        //inflater sinon a la place du local
        View v = localInflater.inflate(R.layout.fragment_profile, container, false);


        BitmapDrawable bd=(BitmapDrawable)Parameters.getHDprofilePicture();

        ImageView profilePictureView =  ( ImageView)v.findViewById(R.id.profilePicture);
        profilePictureView.setImageBitmap(bd.getBitmap());

       //((ImageView)v.findViewById(R.id.profilePicture)).setScaleType(ImageView.ScaleType.FIT_XY);
        mEditMyAnnounce = (EditText)v.findViewById(R.id.aboutMe);
        mEditMyAnnounce.setText(Parameters.getAboutme());


        ToggleButton toggle = (ToggleButton) v.findViewById(R.id.toggleDispo);
        toggle.setChecked(Parameters.isAvailable());
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Parameters.setAvailable(true);
                } else {
                    Parameters.setAvailable(false);
                }
            }
        });


        mEditMyAnnounce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mSaveButton) {
                    showSaveButton();

                }
            }
        });

        ((TextView)v.findViewById(R.id.textName)).setText(Parameters.getFirstname());
/*
        RelativeLayout nonBlurred = (RelativeLayout)v.findViewById(R.id.nonBlurred);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        profilePictureView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int bottom = (int)(profilePictureView.getMeasuredHeight() *0.3);
        params.setMargins(0, 0, 0,bottom);
        nonBlurred.setLayoutParams(params);*/


        mLocationTextView =  ((TextView)v.findViewById(R.id.locationText));
        mLocationIcon = (ImageView)v.findViewById(R.id.locationIcon);
        ((TextView)v.findViewById(R.id.profileAge)).setText(String.valueOf(Parameters.getAge()));


        GridView gridView = (GridView)v.findViewById(R.id.GridLayoutProfilePic);
        mGridShowPicAdapter = new gridShowPicAdapter(getContext());
        gridView.setAdapter(mGridShowPicAdapter);


        // Resize profile image to take 3/4 of total screen height
        FrameLayout layout = ( FrameLayout)v.findViewById(R.id.pictureWrapper);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        params.height = (metrics.heightPixels *3)/4;


        setImages();
        return v;
    }


    private void showSaveButton()
    {
        mTopButtonCallback.fadeIn(R.drawable.saveicon);
        mSaveButton = true;
    }
    public void setNewCity(String city)
    {
        if(!mCitySetUp && city != null)
        {
            mLocationIcon.setVisibility(View.VISIBLE);
            mLocationTextView.setVisibility(View.VISIBLE);
            mCitySetUp = true;
            mLocationTextView.setText(city);
        }
        else if(city != null) {
            mLocationTextView.setText(city);
        }
    }

    @Override
    public void setButtonCallback(MainActivity.CallBackTopButton myCallback) {
        mTopButtonCallback = myCallback;
    }

    @Override
    public void onFragmentEnter() {
        if(mSaveButton)
            showSaveButton();
    }

    @Override
    public void onFragmentLeave() {
        if(mSaveButton)
            mTopButtonCallback.fadeOut();
    }

    @Override
    public void onTopMenuMenuButtonClick() {
        Parameters.setAboutme(mEditMyAnnounce.getText().toString());
        setUserInfo gUI = new setUserInfo();
        gUI.execute();
    }

    public void setImages()
    {
        Iterator<FacebookImage> iter = Parameters.getFacebookImages().listIterator();
        mGridShowPicAdapter.clearItem();
        while (iter.hasNext() ) {
            FacebookImage im = iter.next();
            mGridShowPicAdapter.addItem(new gridShowPicAdapter.Item(im.getId(),im.getLowResImage()));
        }
        mGridShowPicAdapter.notifyDataSetChanged();
    }

    private class setUserInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                mNetworkUserInfo.setUserInfo(Parameters.getAboutme(),Parameters.isAvailable());

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            mSaveButton = false;
            mTopButtonCallback.fadeOut();
            // fadeStuffIn();
        }


    }

}