package azzaoui.sociadee;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.ListIterator;

public class PictureActivity extends AppCompatActivity {

    private PictureList pictureList;
    private PicturePagerAdapter mAdapter;
    private ViewPager mViewPager;
    private Drawable[] drawablePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent myIntent = getIntent();
        pictureList = MainActivity.pictureList;
        setContentView(R.layout.activity_picture);

        mAdapter = new PicturePagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        ListIterator<FacebookImage> listIterator = pictureList.imageList.listIterator();
        drawablePic = new Drawable[pictureList.imageList.size()];
        int i =0;

        while (listIterator.hasNext()) {
            FacebookImage curIm = listIterator.next();
            drawablePic[i] = curIm.getLowResImage();
            i++;
        }
        mAdapter.setResources(drawablePic);
        int startpos = myIntent.getIntExtra("ITEM_NUM",0);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(startpos);
        getHDPicture(startpos);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getHDPicture(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getHDPicture(int position)
    {
        UpdatePicture async = new UpdatePicture();
        async.execute(position);
    }
    private class UpdatePicture extends AsyncTask<Integer, Void, Void> {


        private NetworkUserInfo mNetworkUserInfo;
        private boolean noError = true;
        private int pos;
        @Override
        protected void onPreExecute()
        {
            mNetworkUserInfo = new NetworkUserInfo();

        }

        @Override
        protected Void doInBackground(Integer... params) {

            try {
                pos = params[0];
                long picId = pictureList.imageList.get(pos).getId();
                noError = mNetworkUserInfo.fetchPicture(picId);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            if(noError)
            {
                Drawable newD = new BitmapDrawable(getResources(),mNetworkUserInfo.getLastPicture());
                drawablePic[pos] =  newD;
                mAdapter.mImageViews[pos].setImageDrawable(newD);
                mAdapter.updateResource(newD,pos);
                mAdapter.notifyDataSetChanged();

            }
            // fadeStuffIn();
        }


    }
    public void stopActivity(View v)
    {
        this.finish();
    }
}
