


package com.huateng.collection.ui.adapter;

import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.AudioSectionBean;
import com.huateng.collection.bean.RecorderBean;
import com.huateng.collection.utils.DateUtil;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020/5/27 20:54
 * description:
 */
public class RemoteAudioAdapter2 extends BaseSectionQuickAdapter<AudioSectionBean, BaseViewHolder> {
    int[] bgs = new int[]{R.drawable.icon_audio_bg1, R.drawable.icon_audio_bg2, R.drawable.icon_audio_bg3, R.drawable.icon_audio_bg4};

    public RemoteAudioAdapter2(int layoutResId, int sectionHeadResId, List<AudioSectionBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, AudioSectionBean item) {

        if( item.isAdd()) {
            helper.setGone(R.id.ll_header,false);
            helper.setGone(R.id.ll_add,true);
        }else {
            helper.setGone(R.id.ll_header,true);
            helper.setGone(R.id.ll_add,false);
            helper.setText(R.id.tv_title,item.header);
        }
        helper.addOnClickListener(R.id.ll_add);
    }

    public RemoteAudioAdapter2() {
        super(R.layout.list_item_record, R.layout.item_audio_header, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, AudioSectionBean item) {
        RecorderBean recorderBean = item.t;
        LinearLayout linearLayout = helper.getView(R.id.card_view);

        linearLayout.setBackgroundResource(bgs[helper.getAdapterPosition() % 4]);
        helper.setText(R.id.tv_name, recorderBean.getFileName())
                .setText(R.id.tv_addDate, DateUtil.getDate2(recorderBean.getFileTime()))
                .setText(R.id.tv_duration, DateUtil.formatSeconds(Integer.valueOf(recorderBean.getFileSize())));

    }

}

