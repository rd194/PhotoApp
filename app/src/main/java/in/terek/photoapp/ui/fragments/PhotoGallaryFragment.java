package in.terek.photoapp.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import in.terek.photoapp.R;
import in.terek.photoapp.zgallery.Constants;
import in.terek.photoapp.zgallery.CustomViewPager;
import in.terek.photoapp.zgallery.OnImgClick;
import in.terek.photoapp.zgallery.adapters.HorizontalListAdapters;
import in.terek.photoapp.zgallery.adapters.ViewPagerAdapter;
import in.terek.photoapp.zgallery.entities.ZColor;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGallaryFragment extends Fragment {


    protected Toolbar mToolbar;
    protected ArrayList<String> imageURLs;
    protected ZColor toolbarTitleColor;
    protected int toolbarColorResId;
    private String title;
    private RelativeLayout mainLayout;
    CustomViewPager mViewPager;
    ViewPagerAdapter adapter;
    RecyclerView imagesHorizontalList;
    LinearLayoutManager mLayoutManager;
    HorizontalListAdapters hAdapter;
    private int currentPos;
    private ZColor bgColor;


    public PhotoGallaryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.z_activity_gallery, container, false);


        if (getArguments() != null) {
            // get values
            imageURLs =getArguments().getStringArrayList(Constants.IntentPassingParams.IMAGES);
            toolbarColorResId = getArguments().getInt(Constants.IntentPassingParams.TOOLBAR_COLOR_ID, -1);
            title = getArguments().getString(Constants.IntentPassingParams.TITLE);
            toolbarTitleColor = (ZColor) getArguments().getSerializable(Constants.IntentPassingParams.TOOLBAR_TITLE_COLOR);
            currentPos = getArguments().getInt(Constants.IntentPassingParams.SELECTED_IMG_POS, 0);
            bgColor = (ZColor)  getArguments().getSerializable(Constants.IntentPassingParams.BG_COLOR);
        }


        afterInflation(view);
        return view;
    }   
    protected void afterInflation(View view) {
        // init layouts
        mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        mViewPager = (CustomViewPager) view.findViewById(R.id.pager);
        imagesHorizontalList = (RecyclerView) view.findViewById(R.id.imagesHorizontalList);



        if (bgColor == ZColor.WHITE) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        }

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        adapter = new ViewPagerAdapter(getActivity(), imageURLs, mToolbar, imagesHorizontalList);
        mViewPager.setAdapter(adapter);
        hAdapter = new HorizontalListAdapters(getActivity(), imageURLs, new OnImgClick() {
            @Override
            public void onClick(int pos) {
                mViewPager.setCurrentItem(pos, true);
            }
        });
        imagesHorizontalList.setLayoutManager(mLayoutManager);
        imagesHorizontalList.setAdapter(hAdapter);
        hAdapter.notifyDataSetChanged();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                imagesHorizontalList.smoothScrollToPosition(position);
                hAdapter.setSelectedItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        hAdapter.setSelectedItem(currentPos);
        mViewPager.setCurrentItem(currentPos);
    }

    public void setImageList(ArrayList<String> imageUrlList) {
        hAdapter.setImageList(imageUrlList);
        adapter.setImageList(imageUrlList);
    }
}
