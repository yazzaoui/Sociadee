package azzaoui.sociadee;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


public class MainChatFragment extends Fragment implements SociadeeFragment {

private ChatPagerAdapter mPagerAdapter;

    public LocalChatFragment mLocalChatFragment;
    public InboxFragment mInboxFragment;
    public PrivateDiscussionFragment mPrivateDiscussionFragment;

    protected static HashMap<Long,User> usersMap = new HashMap<>();

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
        mLocalChatFragment = (LocalChatFragment)Fragment.instantiate(getActivity(),LocalChatFragment.class.getName());
        fragments.add(mLocalChatFragment);
        mInboxFragment = (InboxFragment)Fragment.instantiate(getActivity(),InboxFragment.class.getName());
        fragments.add(mInboxFragment);
        mPrivateDiscussionFragment = (PrivateDiscussionFragment)Fragment.instantiate(getActivity(),PrivateDiscussionFragment.class.getName());
        fragments.add(mPrivateDiscussionFragment);

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new ChatPagerAdapter(getChildFragmentManager(), fragments);

       DynamicViewPager pager = (DynamicViewPager) v.findViewById(R.id.pagerChat);
        pager.setMaxPages(3);
        pager.setBackgroundAsset(R.drawable.wallpaper3);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);

        return v;
    }


    public static boolean isUserPresent(long userId)
    {
        return usersMap.containsKey(userId);
    }

    public static User getUserById(long id)
    {
        return usersMap.get(id);
    }

    public static void addUser(User user)
    {
        usersMap.put(user.getmId(),user);
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
    public void sendPublicMessage()
    {
        mLocalChatFragment.sendMessage();
    }
}
