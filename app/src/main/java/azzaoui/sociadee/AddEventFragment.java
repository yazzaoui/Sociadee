package azzaoui.sociadee;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment implements SociadeeFragment {


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
        return v;
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
