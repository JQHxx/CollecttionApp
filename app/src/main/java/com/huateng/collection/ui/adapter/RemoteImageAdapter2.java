package com.huateng.collection.ui.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.ImageSectionBean;
import com.luck.picture.lib.entity.LocalMedia;
import com.tools.utils.FileUtils;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-20 18:30
 * description:
 */
public class RemoteImageAdapter2 extends BaseSectionQuickAdapter<ImageSectionBean, BaseViewHolder> {

    public RemoteImageAdapter2(int layoutResId, int sectionHeadResId, List<ImageSectionBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ImageSectionBean item) {
        if( item.isAdd()) {
            helper.setGone(R.id.ll_header,false);
            helper.setGone(R.id.ll_add,true);
        }else {
            helper.setGone(R.id.ll_header,true);
            helper.setGone(R.id.ll_add,false);
            helper.setText(R.id.tv_title,item.header);
        }
    }

    public RemoteImageAdapter2() {

        super(R.layout.item_remote_image, R.layout.item_audio_header, null);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, ImageSectionBean item) {
        LocalMedia bean = item.t;
        ImageView imageView = helper.getView(R.id.iv_image);
        if(bean == null && item.isAdd()) {

            Glide.with(mContext)
                    .load(R.drawable.ic_add2)
                    .into(imageView);
        }else {

            Log.e("nb",bean.getPath());
            if(FileUtils.isFileExists(bean.getPath()) ) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .error(R.color.rect)
                        .placeholder(R.color.grey_69)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mContext)
                        .load(bean.getPath())
                        .apply(options)
                        .into(imageView);
            }else {
                Glide.with(mContext)
                        .load(R.drawable.default_image)
                        .into(imageView);
            }
        }

    }
}
