package in.terek.photoapp.zgallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import in.terek.photoapp.R;

/**
 * Created by mohamedzakaria on 8/7/16.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;

    public ImageViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
