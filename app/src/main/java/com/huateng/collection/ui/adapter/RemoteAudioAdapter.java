package com.huateng.collection.ui.adapter;

/**
 * author: yichuan
 * Created on: 2020/5/27 20:54
 * description:
 */
/*public class RemoteAudioAdapter extends BaseQuickAdapter<RemoteAudioBean.RecordsBean, BaseViewHolder> {
    int[] bgs = new int[]{R.drawable.icon_audio_bg1, R.drawable.icon_audio_bg2, R.drawable.icon_audio_bg3, R.drawable.icon_audio_bg4};


    public RemoteAudioAdapter() {
        super(R.layout.list_item_record);
    }


    @Override
    protected void convert(BaseViewHolder helper, RemoteAudioBean.RecordsBean item) {
        LinearLayout linearLayout = helper.getView(R.id.card_view);

        linearLayout.setBackgroundResource(bgs[helper.getAdapterPosition() % 4]);
        helper.setText(R.id.tv_name, item.getFileName())
                .setText(R.id.tv_addDate,DateUtil.getDate2(item.getFileTime()))
                .setText(R.id.tv_duration, DateUtil.formatSeconds(Integer.valueOf(item.getFileSize())));



    }
}*/
