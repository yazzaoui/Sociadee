package azzaoui.sociadee;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Extends the NetworkBase class, used to handle login and register
 */
public class NetworkLogin extends NetworkBase {


    private String token = null;
    private String userName = null;


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
     * @param email    The user's login (email)
     * @param password The user's password
     * @return True if the login attempt was succesful, false otherwise.
     */
    public boolean Login(String email, String password) throws IOException, JSONException {
        String toSend = "/loginUser/" + email + "/" + password;
        JSONObject response = sendRequest("POST", toSend);
        if (response == null) {
            return false;
        } else {
            JSONObject data = response.getJSONObject("data");
            token = data.getString("authentication-token");
            userName = data.getString("username");
            Parameters.setToken(token);
            return true;
        }
    }

    /**
     * Sends a registration request to the server
     *
     * @param email    the email you want to register with
     * @param login    the username
     * @param password the password of the account you want to register
     * @return true if the registration was succesful, false otherwise
     * @throws IOException
     * @throws JSONException
     */
    public boolean Register(String email, String login, String password) throws IOException, JSONException {
        String toSend = "/createUser/" + email + "/" + password + "/" + login;
        JSONObject response = sendRequest("POST", toSend);
        return response != null;
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

}
