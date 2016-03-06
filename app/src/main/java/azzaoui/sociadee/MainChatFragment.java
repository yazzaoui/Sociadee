package azzaoui.sociadee;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Vector;


public class MainChatFragment extends Fragment implements SociadeeFragment {

private ChatPagerAdapter mPagerAdapter;

    public MainChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_main_chat, container, false);
        List fragments = new Vector();

        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(getActivity(),LocalChatFragment.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(),InboxFragment.class.getName()));


        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new ChatPagerAdapter(getChildFragmentManager(), fragments);

        ViewPager pager = (ViewPager) v.findViewById(R.id.pagerChat);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);

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
