package com.huateng.collection.ui.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.luck.picture.lib.entity.LocalMedia;
import com.tools.utils.FileUtils;

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
        Log.e("nb",item.getPath());
        if(FileUtils.isFileExists(item.getPath()) ) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.color.rect)
                    .placeholder(R.color.grey_69)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext)
                    .load(item.getPath())
                    .apply(options)
                    .into(imageView);
        }else {
            Glide.with(mContext)
                    .load(R.drawable.default_image)
                    .into(imageView);
        }


    }
}
