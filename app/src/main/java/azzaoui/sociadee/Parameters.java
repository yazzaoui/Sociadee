package azzaoui.sociadee;

import android.graphics.drawable.Drawable;

/**
 * Created by Youssef Azzaoui on 10/11/15.
 *
 *  Singleton class for storing various parameters
 */
public class Parameters {

    private static String FBToken;
    private static String SociadeeToken;
    private static long facebookId;
    private static String firstname;
    private static Drawable profilePicture;


    public static String getFBToken() {
        return FBToken;
    }

    public static String getSociadeeToken() {
        return SociadeeToken;
    }

    public static void setSociadeeToken(String sociadeeToken) {
        SociadeeToken = sociadeeToken;
    }
    public static void setFBToken(String token) {
        FBToken = token;
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
