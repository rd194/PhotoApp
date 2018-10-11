package in.terek.photoapp;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import in.terek.photoapp.ui.fragments.PhotoGallaryFragment;
import in.terek.photoapp.zgallery.Constants;
import in.terek.photoapp.zgallery.entities.ZColor;

/**
 * @author Rajdeep Yadav
 * Date : 11-Oct-18
 * Time : 11:11 AM
 */
public class ZGallaryFragment {
    private Activity mActivity;
    private ArrayList<String> imagesURLs;
    private String title;
    private int spanCount = 2;
    private int toolbarColor = -1;
    private int imgPlaceHolderResId = -1;
    private ZColor color;
    private int selectedImgPosition;
    private ZColor backgroundColor;

    private ZGallaryFragment() {
    }

    /**
     * @param activity   Refrence from current activity
     * @param imagesURLs Image URLs to be displayed
     */
    public static ZGallaryFragment with(Activity activity, ArrayList<String> imagesURLs) {
        return new ZGallaryFragment(activity, imagesURLs);
    }


    private ZGallaryFragment(Activity activity, ArrayList<String> imagesURLs) {
        this.imagesURLs = imagesURLs;
        this.mActivity = activity;
    }

    /**
     * Set z_toolbar title
     *
     * @param title
     * @return
     */
    public ZGallaryFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Setting z_toolbar Color ResourceId
     *
     * @param colorResId
     * @return
     */
    public ZGallaryFragment setToolbarColorResId(int colorResId) {
        this.toolbarColor = colorResId;
        return this;
    }

    /**
     * Setting z_toolbar color
     *
     * @param color enum color may be black or white
     * @return
     */
    public ZGallaryFragment setToolbarTitleColor(ZColor color) {
        this.color = color;
        return this;
    }

    /**
     * Setting starting position
     *
     * @param selectedImgPosition
     * @return
     */
    public ZGallaryFragment setSelectedImgPosition(int selectedImgPosition) {
        this.selectedImgPosition = selectedImgPosition;
        return this;
    }

    public ZGallaryFragment setGalleryBackgroundColor(ZColor backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Start the gallery activity with builder settings
     */
    public PhotoGallaryFragment getFragment() {
       // Intent bundle = new Intent(mActivity, ZGalleryActivity.class);
        PhotoGallaryFragment photoGallaryFragment=new PhotoGallaryFragment();
        Bundle bundle = new Bundle();
       // bundle.putInt("statusType", 0);

        bundle.putStringArrayList(Constants.IntentPassingParams.IMAGES, imagesURLs);
        bundle.putString(Constants.IntentPassingParams.TITLE, title);
        bundle.putInt(Constants.IntentPassingParams.TOOLBAR_COLOR_ID, toolbarColor);
        bundle.putSerializable(Constants.IntentPassingParams.TOOLBAR_TITLE_COLOR, color);
        bundle.putInt(Constants.IntentPassingParams.SELECTED_IMG_POS, selectedImgPosition);
        bundle.putSerializable(Constants.IntentPassingParams.BG_COLOR, backgroundColor);

        photoGallaryFragment.setArguments(bundle);
       return photoGallaryFragment;
    }
}
