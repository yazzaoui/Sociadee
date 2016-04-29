package azzaoui.sociadee;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocalChatFragment extends ListFragment {

    private BroadcastReceiver mMessageBroadcastReceiver;
    private NetworkChat mNetworkChat;

    private MessagesListAdapter mMessagesListAdapter;
   private List<PublicMessageItem> mMessageList;
    private EditText mMainText;

    public LocalChatFragment() {
        mNetworkChat = new NetworkChat();
        mMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mess = intent.getStringExtra("MESSAGE");
                Toast.makeText(getActivity(),  mess, Toast.LENGTH_LONG).show();
                fetchMessages();
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageBroadcastReceiver,
                new IntentFilter(Parameters.PUBLIC_MESSAGE_RECEIVED));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_local_chat, container, false);
        mMainText = (EditText)v.findViewById(R.id.MainLocalText);
        fetchMessages();

        return v;
    }

    public void sendMessage()
    {
        if(!mMainText.getText().toString().isEmpty())
        {
            mNetworkChat.postPublicMessage(mMainText.getText().toString());
        }
    }


    private void fetchMessages()
    {
        GetMessagesTask task = new GetMessagesTask();
        task.execute();
    }

    static class PublicMessageItem {

        public User author;
        public String message;

        public PublicMessageItem(String message, User author) {
            this.message = message;
            this.author = author;
        }
    }
    private class GetMessagesTask extends AsyncTask<Void, Void, Void> {

        private boolean mNoError;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            mNoError = mNetworkChat.getPublicMessage(getContext());

            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            if(mNoError) {
                mMessageList = mNetworkChat.getmMessageList();
                mMessagesListAdapter = new MessagesListAdapter(getActivity(), mMessageList);
                setListAdapter(mMessagesListAdapter);
            }

        }
    }
    static class MessagesListAdapter  extends ArrayAdapter<PublicMessageItem> {

        public int selectedView=0;
        public boolean animate=false;
        public MessagesListAdapter(Context context, List<PublicMessageItem> items) {
            super(context, R.layout.public_message_list_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null) {
                // inflate the GridView item layout
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.public_message_list_item, parent, false);

                // initialize the view holder
                viewHolder = new ViewHolder();
                viewHolder.liPicture = (ImageView) convertView.findViewById(R.id.profileUser);
                viewHolder.liName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.liText = (TextView) convertView.findViewById(R.id.text);
                viewHolder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);

                convertView.setTag(viewHolder);
            } else {
                // recycle the already inflated view
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // update the item view
            PublicMessageItem item = getItem(position);
            viewHolder.liPicture.setImageDrawable(item.author.getmProfilePicture());
            viewHolder.liName.setText(item.author.getFirstName());
            viewHolder.liText.setText(item.message);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.layout.getLayoutParams();
            params.setMargins(5, 5, 0, 0); //substitute parameters for left, top, right, bottom
            viewHolder.layout.setLayoutParams(params);


            return convertView;
        }


        private static class ViewHolder {
            RelativeLayout layout;
            ImageView liPicture;
            TextView liName;
            TextView liText;
        }
    }
}
