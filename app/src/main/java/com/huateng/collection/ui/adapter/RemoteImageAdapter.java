package com.huateng.collection.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.luck.picture.lib.entity.LocalMedia;

/**
 * author: yichuan
 * Created on: 2020-05-20 18:30
 * description:
 */
public class RemoteImageAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {


    public RemoteImageAdapter() {
        super(R.layout.item_remote_image);
    }


    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, LocalMedia item) {

        ImageView imageView = helper.getView(R.id.iv_image);

       /* RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.color_f6)
                .diskCacheStrategy(DiskCacheStrategy.ALL);*/
        Glide.with(mContext)
                .load(item.getPath())
               // .apply(options)
                .into(imageView);
    }
}
