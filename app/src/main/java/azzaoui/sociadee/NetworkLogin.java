package azzaoui.sociadee;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Extends the NetworkBase class, used to handle login and register
 */
public class NetworkLogin extends NetworkBase {


    private String SociadeeToken = null;
    private String firstName = null;
    private String lastName = null;
    private String status = null;
    private Bitmap profilePic = null;
    private boolean available = false;
    private String aboutme = null;
    private long faceBookid = 0;

    /**
     * This constructor takes no parameter because the server will always be the same. Calls
     * it's superclass' constructor.
     */
    public NetworkLogin() {
        super();
    }


    /**
     * Called when the user tries to log in. Returns true if the server accepted the login attempt
     * or false if it didn't.
     *
     * @return True if the login attempt was succesful, false otherwise.
     */
    public boolean Login(String facebookToken) throws IOException, JSONException {
        String toSend = "/login";
        String postData = "FBtoken=" + facebookToken;
        JSONObject response = sendPOSTRequest(toSend,postData);
        if (response == null) {
            return false;
        } else {
            status = response.getString("status");
            profilePic = decodeBase64(response.getString("picture"));
            firstName = response.getString("firstname");
            lastName = response.getString("lastname");
            SociadeeToken = response.getString("token");
            faceBookid = response.getLong("facebookId");
            aboutme = response.getString("aboutMe");
            available = response.getBoolean("available");
            return true;
        }
    }



    /**
     * Returns the value of the token that has been gotten when logging in.
     *
     * @return The token
     * @throws Exception
     */
    public String getSociadeeToken() throws IllegalStateException {
        if (SociadeeToken == null) {
            throw new IllegalStateException("Tried to access a token with value 0. " +
                    "The token has probably not been initlizaed yet or the connection has failed");
        }
        return SociadeeToken;
    }

    public long getFaceBookid() throws IllegalStateException {
        if (faceBookid == 0) {
            throw new IllegalStateException("Tried to access a token with value 0. " +
                    "The token has probably not been initlizaed yet or the connection has failed");
        }
        return faceBookid;
    }

    public Bitmap getProfilePicture() throws IllegalStateException {
        if (profilePic == null) {
            throw new IllegalStateException("Tried to access a token with value 0. " +
                    "The token has probably not been initlizaed yet or the connection has failed");
        }
        return profilePic;
    }
    /**
     * A getter for the username (accessible after logging in)
     *
     * @return The username
     * @throws Exception in case the username is still null (never logged in)
     */
    public String getFirstName() throws IllegalStateException {
        if (firstName == null) {
            throw new IllegalStateException("Tried to access the firstname before logging in");
        }
        return firstName;
    }

    public String getLastName() throws IllegalStateException {
        if (lastName == null) {
            throw new IllegalStateException("Tried to access the lastname before logging in");
        }
        return lastName;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getAboutme() {
        return aboutme;
    }
}
