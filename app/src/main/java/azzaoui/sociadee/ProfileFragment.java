package azzaoui.sociadee;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


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


    public ProfileFragment() {
        // Required empty public constructor
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


        BitmapDrawable bd=(BitmapDrawable)Parameters.getProfilePicture();
        ((ImageView)v.findViewById(R.id.profilePicture)).setImageBitmap(bd.getBitmap());
        ((ImageView)v.findViewById(R.id.profilePicture)).setScaleType(ImageView.ScaleType.FIT_XY);
        mEditMyAnnounce = (EditText)v.findViewById(R.id.aboutMe);
        mEditMyAnnounce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(!mSaveButton)
                    {
                        showSaveButton();
                    }
            }
        });

        ((TextView)v.findViewById(R.id.textName)).setText(Parameters.getFirstname());

        mLocationTextView =  ((TextView)v.findViewById(R.id.locationText));
        mLocationIcon = (ImageView)v.findViewById(R.id.locationIcon);
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

    }
}