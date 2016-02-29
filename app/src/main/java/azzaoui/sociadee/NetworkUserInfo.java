package azzaoui.sociadee;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Youssef Azzaoui on 29/01/2016.
 * youssef.azzaoui@epfl.ch
 */
public class NetworkUserInfo extends NetworkBase {


    private Bitmap mUserpicture = null;
    private String mFirstname = null;
    private String mAboutme = null;
    private boolean mAvailable = false;
    private String mCity = null;


    public NetworkUserInfo() {
        super();
    }

    /*
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
    }*/

    public Boolean fetchUserInfo(long userId) throws IOException, JSONException {
        String toSend = "/getuserinfo";
        String postData = "id=" + userId;
        JSONObject response = sendPOSTRequest(toSend,postData,true);
        if (response == null) {
            return false;
        } else {
            String data = response.getString("pic");
            mUserpicture = decodeBase64(data);
            mFirstname =  response.getString("firstname");
            mAvailable =  response.getBoolean("available");
            mCity = response.getString("city");
            mAboutme = response.getString("aboutMe");
            return true;
        }
    }

    public Boolean setUserInfo(String aboutme, Boolean available) throws IOException, JSONException {
        String toSend = "/setuserinfo";
        String av = available? "True":"False";
        String postData = "aboutme=" + aboutme + "&available=" + av;
        JSONObject response = sendPOSTRequest(toSend,postData,true);
        if (response == null) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updatePictures(String pic)throws IOException, JSONException {
        String toSend = "/updateuserimage";
        String postData = "images=" + pic;
        JSONObject response = sendPOSTRequest(toSend,postData,true);
        if (response == null) {
            return false;
        } else {
            return true;
        }
    }

    public Bitmap getmUserpicture() {
        return mUserpicture;
    }

    public String getmFirstname() {
        return mFirstname;
    }

    public String getmAboutme() {
        return mAboutme;
    }

    public boolean ismAvailable() {
        return mAvailable;
    }

    public String getmCity() {
        return mCity;
    }


}
