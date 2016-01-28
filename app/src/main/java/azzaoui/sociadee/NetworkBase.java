package azzaoui.sociadee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * This class is meant to be used by most classes that need to communicate with the server, it
 * contains the base functions required for this purpose.
 */
public abstract class NetworkBase {

    private String stringURL = "https://sociadee.appspot.com/";
    //private String stringURL = "http://192.168.1.107:8080/";
    private boolean debug = false;
    private String latestError = "";

    public NetworkBase() {
    }

    /**
     * Used for HTTP requests, sends the request and returns the server's answer.
     *
     * @param requestAction    login/..
     * @param requestData the details of the message to send (for example login and password)
     * @return A string containing the server response or an error message in case of 4xx code
     * @throws IOException
     */
    public JSONObject sendPOSTRequest( String requestAction, String requestData) throws IOException, JSONException {
        return sendPOSTRequest(requestAction,requestData, false);
    }


    /**
     * Used for HTTP requests, sends the request and returns the server's answer.
     *
     * @param token          the authentification token (null if not needed)
     * @return A string containing the server response or an error message in case of 4xx code
     * @throws IOException
     */
    public JSONObject sendPOSTRequest(String requestAction, String requestData, boolean token) throws IOException, JSONException {
        try {
            String toSend = stringURL + requestAction;
            if(token)
                requestData = "userKey=" + Parameters.getSociadeeToken() +  "&"+ requestData;

            byte[] postData = requestData.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            URL url = new URL(toSend);
            HttpsURLConnection HUC = (HttpsURLConnection) url.openConnection();
            HUC.setReadTimeout(10000);
            HUC.setConnectTimeout(15000);
            HUC.setDoOutput(true);


            HUC.setInstanceFollowRedirects(false);
            HUC.setRequestMethod("POST");
            HUC.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            HUC.setRequestProperty("charset", "utf-8");
            HUC.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            HUC.setUseCaches(false );
            try( DataOutputStream wr = new DataOutputStream( HUC.getOutputStream())) {
                wr.write( postData );
            }



            int code = HUC.getResponseCode();

            if (code >= 400 && code <= 499) {
                latestError = "Unknown error :(";
                return null; // there are no 400 to 499 errors on the server side for now. Just putting this to be sure.
            }


            //succesful response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(HUC.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject JSONresponse = new JSONObject(response.toString());

            String ReceivedError = JSONresponse.getString("error");
            latestError = ReceivedError;

            if (!ReceivedError.equals("none")) {
                return null;
            }

            return JSONresponse;

        } catch (IOException e) {
            throw new IOException();
        }
    }


    /**
     *  Used by some subclasses to set the last error's code
     * @param code The last error's code
     */
    protected void setLatestError(String code){
        latestError = code;
    }

    /**
     * Gives the ID of the last error received from server.
     *
     * @return the last error's code
     */
    public String getLastError() throws IllegalStateException {
        if (latestError == "none") {
            throw new IllegalStateException("No error this far");
        }
        return latestError;
    }


    /**
     * Used by some subclasses to get the base URL String
     *
     * @return The base URL String
     */
    public String getURLString(){
        return stringURL;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);

        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
