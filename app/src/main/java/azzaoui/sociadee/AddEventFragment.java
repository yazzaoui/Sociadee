package azzaoui.sociadee;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment implements SociadeeFragment {


    TimePicker mTimePicker;
    DatePicker mDatePicker;
    TextView mTVLocalisation;
    ImageView mPicture;


    public AddEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppThemeCustom);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        //inflater sinon a la place du local
        View v = localInflater.inflate(R.layout.fragment_add_event, container, false);

        mTimePicker = (TimePicker)v.findViewById(R.id.timePicker);
        mDatePicker = (DatePicker)v.findViewById(R.id.datePicker);
        mTVLocalisation = (TextView) v.findViewById(R.id.localText);
        mPicture = (ImageView)v.findViewById(R.id.selectedPic);

        mTimePicker.setIs24HourView(true);
        return v;
    }

    public void setLocation(String loc)
    {
        mTVLocalisation.setText(loc);
    }

    public void setPicture(Drawable d)
    {

        mPicture.setImageDrawable(d);
        mPicture.setVisibility(View.VISIBLE);
    }
    public void pickPicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1337);
    }
    @Override
    public void setButtonCallback(MainActivity.CallBackTopButton myCallback) {

    }

    @Override
    public void onFragmentEnter() {

    }

    @Override
    public void onFragmentLeave() {

    }

    @Override
    public void onTopMenuMenuButtonClick() {

    }
}
