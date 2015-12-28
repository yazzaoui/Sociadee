package azzaoui.sociadee;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * This class is meant to be used by most classes that need to communicate with the server, it
 * contains the base functions required for this purpose.
 */
public abstract class NetworkBase {

    private String stringURL = "https://picture-contest.appspot.com/_ah/api/picarena/v1";
    private int latestErrorCode = 0;

    public NetworkBase() {
    }

    /**
     * Used for HTTP requests, sends the request and returns the server's answer.
     *
     * @param requestType    POST or GET
     * @param requestMessage the details of the message to send (for example login and password)
     * @return A string containing the server response or an error message in case of 4xx code
     * @throws IOException
     */
    public JSONObject sendRequest(String requestType, String requestMessage) throws IOException, JSONException {
        return sendRequest(requestType, requestMessage, null);
    }


    /**
     * Used for HTTP requests, sends the request and returns the server's answer.
     *
     * @param requestType    POST or GET
     * @param requestMessage the details of the message to send (for example login and password)
     * @param token          the authentification token (null if not needed)
     * @return A string containing the server response or an error message in case of 4xx code
     * @throws IOException
     */
    public JSONObject sendRequest(String requestType, String requestMessage, String token) throws IOException, JSONException {
        try {
            String toSend = stringURL + requestMessage;
            URL url = new URL(toSend);
            HttpsURLConnection HUC = (HttpsURLConnection) url.openConnection();
            HUC.setReadTimeout(10000);
            HUC.setConnectTimeout(15000);
            HUC.setRequestMethod(requestType);
            HUC.setRequestProperty("Authorization", token);
            HUC.setDoOutput(true);

            int code = HUC.getResponseCode();

            if (code >= 400 && code <= 499) {
                latestErrorCode = 9000;
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

            int JSONCode = JSONresponse.getInt("code");

            if (JSONCode == 400) {  // an error occured and is returned in the JSON object
                JSONObject JSONError = JSONresponse.getJSONObject("data");
                latestErrorCode = JSONError.getInt("code");
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
    protected void setLatestErrorCode(int code){
        latestErrorCode = code;
    }

    /**
     * Gives the ID of the last error received from server.
     *
     * @return the last error's code
     */
    public int getLastError() throws IllegalStateException {
        if (latestErrorCode == 0) {
            throw new IllegalStateException("No error this far");
        }
        return latestErrorCode;
    }


    /**
     * Used by some subclasses to get the base URL String
     *
     * @return The base URL String
     */
    public String getURLString(){
        return stringURL;
    }

}
