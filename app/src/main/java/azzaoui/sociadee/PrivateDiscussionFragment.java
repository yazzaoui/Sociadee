package azzaoui.sociadee;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateDiscussionFragment extends ListFragment {


    private NetworkChat mNetworkChat;
    private BroadcastReceiver mMessageBroadcastReceiver;
    private LinkedList<MessageItem> mMessageList, mNewMessageList;
    private MessageListAdapter mMessageListAdapter;

    private String mConversationId = "";
    private String lastMessageId= "";
    private User contact;

    public PrivateDiscussionFragment() {
        mNetworkChat = new NetworkChat();
        mMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String convid = intent.getStringExtra("CONVID");
                if(mConversationId.equals(convid)) {
                    fetchConversation();
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageBroadcastReceiver,
                new IntentFilter(Parameters.PRIVATE_MESSAGE_RECEIVED));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_private_discussion, container, false);

        mMessageList = new LinkedList<>();
        mMessageListAdapter = new MessageListAdapter(getActivity(), mMessageList);
        setListAdapter(mMessageListAdapter);

        return v;
    }

    public void setConversation(String convId)
    {
        lastMessageId="";
        mMessageList.clear();
        fetchConversation();
    }

    private void fetchConversation()
    {
        GetDiscussionsTask task = new GetDiscussionsTask();
        task.execute();
    }

    private class GetDiscussionsTask extends AsyncTask<Void, Void, Void> {

        private boolean mNoError;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            mNoError = mNetworkChat.getPrivateMessageList(mConversationId);

            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            if(mNoError) {
                mNewMessageList = mNetworkChat.getmPrivateMessageList();

                ListIterator iter = mNewMessageList.listIterator();
                boolean add = lastMessageId.equals("");
                while(iter.hasNext())
                {
                    MessageItem curItem = (MessageItem)iter.next();
                    if(curItem.id.equals(lastMessageId))
                    {
                        add = true;
                    }
                    else if(add)
                    {
                        mMessageList.add(curItem);
                    }
                }
                mMessageListAdapter.notifyDataSetChanged();
                //getListView().smoothScrollToPosition(mMessagesListAdapter.getCount());
            }

        }
    }
    static class MessageItem {

        public User sender;
        public String id;
        public String text;
        public MessageItem(String id, User sender, String text) {
            this.sender = sender;
            this.text = text;
            this.id = id;
        }

        public boolean isMe()
        {
            return sender.getmId() == Parameters.getFacebookId();
        }
    }
    static class MessageListAdapter  extends ArrayAdapter<MessageItem> {


        public MessageListAdapter(Context context, List<MessageItem> items) {
            super(context, 0, items);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        //isMe is 1
        //not me is 0
        @Override
        public int getItemViewType(int position) {
            return getItem(position).isMe()? 1:0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null) {
                // inflate the GridView item layout
                LayoutInflater inflater = LayoutInflater.from(getContext());
                if(getItem(position).isMe())
                    convertView = inflater.inflate(R.layout.private_me_message_list_item, parent, false);
                else
                    convertView = inflater.inflate(R.layout.private_contact_message_list_item, parent, false);
                // initialize the view holder
                viewHolder = new ViewHolder();
                viewHolder.liPicture = (ImageView) convertView.findViewById(R.id.profileUser);
                viewHolder.liText = (TextView) convertView.findViewById(R.id.text);

                convertView.setTag(viewHolder);
            } else {
                // recycle the already inflated view
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // update the item view
            MessageItem item = getItem(position);
            viewHolder.liPicture.setImageDrawable(item.sender.getmProfilePicture());
            viewHolder.liText.setText(item.text);

            return convertView;
        }


        private static class ViewHolder {
            ImageView liPicture;
            TextView liName;
            TextView liText;
        }
    }
}