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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends ListFragment {


    private NetworkChat mNetworkChat;
    private BroadcastReceiver mMessageBroadcastReceiver;
    private LinkedList<PrivateDiscussionItem> mDiscussionList;
    private DiscussionListAdapter mDiscussionListAdapter;
    public InboxClickCallback inboxClickCallback;


    public interface InboxClickCallback
    {
        void inboxClick(String convid);
    }



    public InboxFragment() {
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
        mDiscussionList = new LinkedList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);


        mDiscussionListAdapter = new DiscussionListAdapter(getActivity(), mDiscussionList);
        setListAdapter(mDiscussionListAdapter);

        fetchDiscussions();

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(inboxClickCallback != null)
            inboxClickCallback.inboxClick(mDiscussionListAdapter.getItem(position).id);
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
    static class PrivateDiscussionItem {

        public User contact;
        public String lastMessage;
        public User lastAuthor;
        public String id;

        public PrivateDiscussionItem(User contact, String lastMessage, User lastAuthor, String id) {
            this.contact = contact;
            this.lastMessage = lastMessage;
            this.lastAuthor = lastAuthor;
            this.id = id;
        }
    }
    static class DiscussionListAdapter  extends ArrayAdapter<PrivateDiscussionItem> {

        public int selectedView=0;
        public boolean animate=false;
        public DiscussionListAdapter(Context context, List<PrivateDiscussionItem> items) {
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
            PrivateDiscussionItem item = getItem(position);
            viewHolder.liPicture.setImageDrawable(item.contact.getmProfilePicture());
            viewHolder.liName.setText(item.contact.getFirstName());
            viewHolder.liText.setText(item.lastMessage);

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
