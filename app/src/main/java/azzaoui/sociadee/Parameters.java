package azzaoui.sociadee;

import android.graphics.drawable.Drawable;

/**
 * Created by Youssef Azzaoui on 10/11/15.
 */
public class Parameters {

    private static String Token;

    private static long facebookId;
    private static String firstname;
    private static Drawable profilePicture;

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static String getFirstname() {
        return firstname;
    }

    public static void setFirstname(String fname) {
        Parameters.firstname = fname;
    }

    public static long getFacebookId() {
        return facebookId;
    }

    public static void setFacebookId(long facebookId) {
        Parameters.facebookId = facebookId;
    }

    public static Drawable getProfilePicture() {
        return profilePicture;
    }

    public static void setProfilePicture(Drawable profilePicture) {
        Parameters.profilePicture = profilePicture;
    }
}
