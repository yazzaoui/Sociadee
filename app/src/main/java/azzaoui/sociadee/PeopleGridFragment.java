package azzaoui.sociadee;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PeopleGridFragment extends Fragment implements SociadeeFragment {


    public PeopleGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people_grid, container, false);
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
