package azzaoui.sociadee;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPicFragment extends Fragment {

    private gridAddPicAdapter mGridAddPicAdapter;

    public AddPicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View v = inflater.inflate(R.layout.fragment_add_pic, container, false);
        GridView gridView = (GridView)v.findViewById(R.id.GridLayoutAddPic);
        mGridAddPicAdapter = new gridAddPicAdapter(getContext());
        gridView.setAdapter(mGridAddPicAdapter);
        return v;
    }

    public void retrievePictures()
    {
        mGridAddPicAdapter.retrievePictures();
    }


}
