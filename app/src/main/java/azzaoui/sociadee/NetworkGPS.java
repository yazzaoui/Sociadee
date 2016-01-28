package azzaoui.sociadee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Youssef Azzaoui on 28/01/2016.
 * youssef.azzaoui@epfl.ch
 */

public class NetworkGPS extends NetworkBase {

    private ArrayList<UserFetchedGPS> fetchedUser = null;
    private Bitmap lastPicture = null;

    public NetworkGPS() {
        super();
    }


    /**
     * Called when the user tries to log in. Returns true if the server accepted the login attempt
     * or false if it didn't.
     *
     * @return True if the login attempt was succesful, false otherwise.
     */
    public boolean UpdateLoc(double lat, double lon, String City) throws IOException, JSONException {
        String toSend = "/updateloc";
        String postData = "longitude=" + lon + "&latitude=" + lat + "&city=" + City;
        JSONObject response = sendPOSTRequest(toSend,postData,true);
        if (response == null) {
            return false;
        } else {
            JSONArray data = response.getJSONArray("data");
            fetchedUser = new ArrayList<>();
            for(int i = 0; i < data.length(); i++)
            {
                UserFetchedGPS newUser = new UserFetchedGPS();
                newUser.facebookId = data.getJSONObject(i).getLong("id");
                newUser.latitude = data.getJSONObject(i).getDouble("lat");
                newUser.longitude = data.getJSONObject(i).getDouble("lon");

                fetchedUser.add(newUser);
            }
            return true;
        }
    }

    public Boolean fetchUserPicture(long userId) throws IOException, JSONException {
        String toSend = "/getuserpicture";
        String postData = "id=" + userId;
        JSONObject response = sendPOSTRequest(toSend,postData,true);
        if (response == null) {
            return false;
        } else {
            String data = response.getString("data");
            lastPicture = decodeBase64(data);
            return true;
        }
    }

    public ArrayList<UserFetchedGPS> getFetchedUser()
    {
        return fetchedUser;
    }

    public Bitmap getLastPicture() {
        return lastPicture;
    }

    public static class UserFetchedGPS
    {

        public long facebookId;
        public double latitude;
        public double longitude;

    }

}
