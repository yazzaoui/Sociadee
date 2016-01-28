package azzaoui.sociadee;

/**
 * Created by Youssef Azzaoui on 27/01/2016.
 * youssef.azzaoui@epfl.ch
 */
public interface SociadeeFragment {

    public void setButtonCallback(MainActivity.CallBackTopButton myCallback);
    public void onFragmentEnter();
    public void onFragmentLeave();
    public void onTopMenuMenuButtonClick();
}
