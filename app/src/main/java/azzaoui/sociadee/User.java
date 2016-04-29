package azzaoui.sociadee;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.BitSet;

/**
 * Created by Youssef Azzaoui on 28/01/2016.
 * youssef.azzaoui@epfl.ch
 *
 *  No usage ATM
 *
 */
public class User {

    private long mId;
    private String mFirstName;
    private String mLastName;
    private Drawable mProfilePicture;

    public User(long id) {
        this.mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public Drawable getmProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(Drawable mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }


    public long getmId() {
        return mId;
    }
}
