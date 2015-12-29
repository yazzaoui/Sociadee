package azzaoui.sociadee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;

public class PicturesVoteActivity extends AppCompatActivity {

    private CardContainer mCardContainer;
    private int[] imagesArray = {R.drawable.ins_1,
                                R.drawable.ins_2,
                                R.drawable.ins_3,
                                R.drawable.ins_4,
                                R.drawable.ins_5,
                                R.drawable.ins_6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures_vote);
        mCardContainer = (CardContainer) findViewById(R.id.layoutview);
        mCardContainer.setOrientation(Orientations.Orientation.Disordered);
        addCards();

    }

    private void addCards()
    {

        for(int i =0; i < imagesArray.length;i++) {
            CardModel card = new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1));
            card.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
                @Override
                public void onLike() {
                    Log.d("Swipeable Card", "I liked it");
                }

                @Override
                public void onDislike() {
                    Log.d("Swipeable Card", "I did not liked it");
                }
            });
        }
    }

}
