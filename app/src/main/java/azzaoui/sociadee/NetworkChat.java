package azzaoui.sociadee;

import android.graphics.Bitmap;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Youssef Azzaoui on 17/04/2016.
 * youssef.azzaoui@epfl.ch
 */
public class NetworkChat extends NetworkBase {


    public NetworkChat() {
        super();
    }

    public boolean registerToken(String token) throws IOException, JSONException {
        String toSend = "/gcmtoken";
        String postData = "token=" + token;
        JSONObject response = sendPOSTRequest(toSend,postData,true);
        if (response == null) {
            return false;
        } else {
            return true;
        }
    }
    public boolean postPublicMessage(String message) throws IOException, JSONException {
        String toSend = "/postpublicmessage";
        String postData = "message=" + message;
        JSONObject response = sendPOSTRequest(toSend,postData,true);
        if (response == null) {
            return false;
        } else {
            return true;
        }
    }






}

