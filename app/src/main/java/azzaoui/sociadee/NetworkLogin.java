package azzaoui.sociadee;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Extends the NetworkBase class, used to handle login and register
 */
public class NetworkLogin extends NetworkBase {


    private String token = null;
    private String userName = null;
    private Bitmap profilePic = null;

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
            String status = response.getString("status");
            //token = data.getString("authentication-token");
            //userName = data.getString("username");

            //Parameters.setSociadeeToken();
            return true;
        }
    }



    /**
     * Returns the value of the token that has been gotten when logging in.
     *
     * @return The token
     * @throws Exception
     */
    public String getToken() throws IllegalStateException {
        if (token == null) {
            throw new IllegalStateException("Tried to access a token with value 0. " +
                    "The token has probably not been initlizaed yet or the connection has failed");
        }
        return token;
    }

    /**
     * A getter for the username (accessible after logging in)
     *
     * @return The username
     * @throws Exception in case the username is still null (never logged in)
     */
    public String getUserName() throws IllegalStateException {
        if (userName == null) {
            throw new IllegalStateException("Tried to access the username before logging in");
        }
        return userName;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);

        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
