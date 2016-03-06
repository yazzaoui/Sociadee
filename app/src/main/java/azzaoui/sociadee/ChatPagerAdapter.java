package azzaoui.sociadee;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Youssef Azzaoui on 06/03/2016.
 * youssef.azzaoui@epfl.ch
 */
public class ChatPagerAdapter extends FragmentPagerAdapter {

    private final List fragments;

    //On fournit à l'adapter la liste des fragments à afficher
    public ChatPagerAdapter(FragmentManager fm, List fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
