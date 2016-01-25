package azzaoui.sociadee;


import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);


        BitmapDrawable bd=(BitmapDrawable)Parameters.getProfilePicture();
        ((ImageView)v.findViewById(R.id.profilePicture)).setImageBitmap(bd.getBitmap());
        ((ImageView)v.findViewById(R.id.profilePicture)).setScaleType(ImageView.ScaleType.FIT_XY);


        return v;
    }

}