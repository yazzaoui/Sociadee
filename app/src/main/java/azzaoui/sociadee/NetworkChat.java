package azzaoui.sociadee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import azzaoui.sociadee.LocalChatFragment.PublicMessageItem;
import azzaoui.sociadee.InboxFragment.PrivateDiscussionItem;

/**
 * Created by Youssef Azzaoui on 17/04/2016.
 * youssef.azzaoui@epfl.ch
 */
public class NetworkChat extends NetworkBase {


    public NetworkChat() {
        super();
    }

    private LinkedList<PublicMessageItem> mMessageList;
    private LinkedList<PrivateDiscussionItem> mDiscussionList;

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

    private boolean fetchUserData(long id,Context ctxt)
    {
        try {
            if(fetchUserPicture(id))
            {
                User newUser = new User(id);
                newUser.setFirstName(getLastName());
                newUser.setProfilePicture(new BitmapDrawable(ctxt.getResources(), getLastPicture()));
                MainChatFragment.addUser(newUser);
                return true;
            }
            else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getPublicMessage(Context ctxt){
        String toSend = "/getroommessages";
        JSONObject response;
        try {
            response = sendPOSTRequest(toSend,"",true);
            if (response == null) {
                return false;
            } else {
                JSONArray messageList = response.getJSONArray("data");
                mMessageList = new LinkedList<>();
                for(int i = 0; i < messageList.length(); i++)
                {
                    JSONObject m = messageList.getJSONObject(i);
                    long imId =  m.getLong("sender");
                    String message = m.getString("message");
                    String id = m.getString("id");
                    boolean present = MainChatFragment.isUserPresent(imId);

                    if(!present)
                        if(!fetchUserData(imId,ctxt))
                            return false;

                    User user = MainChatFragment.getUserById(imId);

                    PublicMessageItem curMes = new PublicMessageItem(id,message,user);
                    mMessageList.add(curMes);

                }
                return true;
            }
        } catch (JSONException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }


    public boolean getDiscussionList(Context ctxt){
        String toSend = "/getprivatediscussions";
        JSONObject response;
        try {
            response = sendPOSTRequest(toSend,"",true);
            if (response == null) {
                return false;
            } else {
                JSONArray messageList = response.getJSONArray("data");
                mDiscussionList = new LinkedList<>();
                for(int i = 0; i < messageList.length(); i++)
                {
                    JSONObject m = messageList.getJSONObject(i);
                    JSONArray participants = m.getJSONArray("participants");
                    User contact = null;
                    for(int j = 0; j < participants.length(); j++)
                    {
                        long uid = participants.getLong(j);
                        if(uid != Parameters.getFacebookId())
                        {
                            boolean present = MainChatFragment.isUserPresent(uid);
                            if(!present)
                                if(!fetchUserData(uid,ctxt))
                                    return false;

                            contact = MainChatFragment.getUserById(uid);
                        }
                    }
                    if(contact == null)
                        return false;


                    long imId =  m.getLong("lastsender");
                    String message = m.getString("text");
                    String id = m.getString("id");
                    User lastsender = MainChatFragment.getUserById(imId);

                    PrivateDiscussionItem newItem = new PrivateDiscussionItem(contact,message,lastsender,id);

                    mDiscussionList.add(newItem);

                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }





    public boolean postPublicMessage(String message){
        String toSend = "/postpublicmessage";
        String postData = "message=" + message;
        JSONObject response;
        try {
            response = sendPOSTRequest(toSend,postData,true);
            if (response == null) {
                return false;
            } else {
                return true;
            }
        } catch (JSONException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public LinkedList<PublicMessageItem> getmMessageList() {
        return mMessageList;
    }
    public LinkedList<PrivateDiscussionItem> getmDiscussionList() {
        return mDiscussionList;
    }

}

