package azzaoui.sociadee;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateDiscussionFragment extends ListFragment {


    private NetworkChat mNetworkChat;
    private BroadcastReceiver mMessageBroadcastReceiver;
    private LinkedList<MessageItem> mMessageList;
    private DiscussionListAdapter mDiscussionListAdapter;

    public PrivateDiscussionFragment() {
        mNetworkChat = new NetworkChat();
        mMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              /*  String mess = intent.getStringExtra("MESSAGE");
                Toast.makeText(getActivity(),  mess, Toast.LENGTH_LONG).show();*/
                fetchDiscussions();
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageBroadcastReceiver,
                new IntentFilter(Parameters.PRIVATE_MESSAGE_RECEIVED));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);

        mDiscussionList = new LinkedList<>();
        mDiscussionListAdapter = new DiscussionListAdapter(getActivity(), mDiscussionList);
        setListAdapter(mDiscussionListAdapter);

        fetchDiscussions();

        return v;
    }

    private void fetchDiscussions()
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

            mNoError = mNetworkChat.getDiscussionList(getContext());

            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            if(mNoError) {
                mDiscussionList = mNetworkChat.getmDiscussionList();

                mDiscussionListAdapter = new DiscussionListAdapter(getActivity(), mDiscussionList);
                setListAdapter(mDiscussionListAdapter);

                mDiscussionListAdapter.notifyDataSetChanged();
                //getListView().smoothScrollToPosition(mMessagesListAdapter.getCount());
            }

        }
    }
    static class MessageItem {

        public User sender;
        public String text;
        public MessageItem(User sender, String text) {
            this.sender = sender;
            this.text = text;
        }
    }
    static class MessageListAdapter  extends BaseAdapter {


        public MessageListAdapter(Context context, List<MessageItem> items) {
            super(context, R.layout.inbox_list_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null) {
                // inflate the GridView item layout
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.inbox_list_item, parent, false);

                // initialize the view holder
                viewHolder = new ViewHolder();
                viewHolder.liName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.liText = (TextView) convertView.findViewById(R.id.text);
                viewHolder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);

                convertView.setTag(viewHolder);
            } else {
                // recycle the already inflated view
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // update the item view
            PrivateDiscussionItem item = getItem(position);
            viewHolder.liPicture.setImageDrawable(item.contact.getmProfilePicture());
            viewHolder.liName.setText(item.contact.getFirstName());
            viewHolder.liText.setText(item.lastMessage);

            return convertView;
        }


        private static class ViewHolder {
            ImageView liPicture;
            TextView liName;
            TextView liText;
        }
    }
}