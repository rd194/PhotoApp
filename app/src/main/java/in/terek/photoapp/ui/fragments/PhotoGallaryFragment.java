package in.terek.photoapp.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.terek.photoapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGallaryFragment extends Fragment {


    public PhotoGallaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_gallary, container, false);
    }

}
