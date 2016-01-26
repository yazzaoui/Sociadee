package azzaoui.sociadee;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
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

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppThemeCustom);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        //inflater sinon a la place du local
        View v = localInflater.inflate(R.layout.fragment_profile, container, false);


        BitmapDrawable bd=(BitmapDrawable)Parameters.getProfilePicture();
        ((ImageView)v.findViewById(R.id.profilePicture)).setImageBitmap(bd.getBitmap());
        ((ImageView)v.findViewById(R.id.profilePicture)).setScaleType(ImageView.ScaleType.FIT_XY);


        return v;
    }

}