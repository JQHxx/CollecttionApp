package com.huateng.collection.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.utils.CommonUtils;
import com.huateng.collection.widget.AudioPlayer;
import com.huateng.collection.widget.AudioView;
import com.tools.utils.TimeUtils;

import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RecordedVoicesAdapter extends RecyclerView.Adapter<RecordedVoicesAdapter.ViewHolder> {


    private Context context;
    private List<FileData> dataList;
    private int[] dots = {R.drawable.dot1, R.drawable.dot2, R.drawable.dot3, R.drawable.dot4};
    private AudioPlayer audioPlayer;
    private AudioView lastAudioView;

    public RecordedVoicesAdapter(Context context, List dataList) {
        this.context = context;
        this.dataList = dataList;
        audioPlayer = AudioPlayer.get();
        audioPlayer.initPlayer();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_recorded_audio, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FileData bean = dataList.get(position);
        holder.ivDot.setImageResource(dots[new Random().nextInt(3)]);
        holder.audioView.useAudioPlayer(audioPlayer, bean.getRealPath());
        holder.audioView.setOnPlayListener(new AudioView.OnPlayListener() {
            @Override
            public void onPlayStarted() {
                if (lastAudioView != null && lastAudioView != holder.audioView) {
                    lastAudioView.toInitState();
                }
                lastAudioView = holder.audioView;
            }
        });
        holder.tvCreateTime.setText(TimeUtils.getFriendlyTimeSpanByNow(bean.getCreateTime()));
        holder.tvDuration.setText(CommonUtils.formatTimeMillis(bean.getDuration()));
        if (holder.getAdapterPosition() == 0) {
            holder.vLine.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.audioView)
        AudioView audioView;
        @BindView(R.id.tv_duration)
        TextView tvDuration;
        @BindView(R.id.iv_dot)
        ImageView ivDot;
        @BindView(R.id.tv_createTime)
        TextView tvCreateTime;
        @BindView(R.id.v_line)
        View vLine;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }
}
