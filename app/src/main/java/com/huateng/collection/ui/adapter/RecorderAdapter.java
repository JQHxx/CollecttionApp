package com.huateng.collection.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.collection.R;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.utils.FileUtil;
import com.huateng.collection.utils.Utils;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tools.view.RxToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.pictureselector.adapter
 * email：893855882@qq.com
 * data：16/7/27
 */
public class RecorderAdapter extends
        RecyclerView.Adapter<RecorderAdapter.ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    int[] bgs = new int[]{R.drawable.icon_audio_bg1, R.drawable.icon_audio_bg2, R.drawable.icon_audio_bg3, R.drawable.icon_audio_bg4};
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private int selectMax = 9;
    private Context context;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    private String[] stringItems = {"删除"};


    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    public RecorderAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        TextView tv_name;
        TextView tv_duration;
        TextView tv_addDate;
        TextView tv_fileSize;
        View iv_add;
        View ll_audioInfo;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            tv_addDate = (TextView) view.findViewById(R.id.tv_addDate);
            tv_fileSize = (TextView) view.findViewById(R.id.tv_fileSize);
            ll_audioInfo = view.findViewById(R.id.ll_audioInfo);
            iv_add = view.findViewById(R.id.iv_add);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            if (mOnAddPicClickListener == null) {
                return list.size();
            }
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_record,
                viewGroup, false);
        return new ViewHolder(view);
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        //少于8张，显示继续添加的图标
        viewHolder.cardView.setBackgroundResource(bgs[position % 4]);
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.iv_add.setVisibility(View.VISIBLE);
            viewHolder.ll_audioInfo.setVisibility(View.GONE);
            viewHolder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAddPicClickListener != null) {
                        mOnAddPicClickListener.onAddPicClick();
                    }
                }
            });

        } else {

            LocalMedia media = list.get(position);
            Log.e("nb", "media:--->" + media.toString());
            //  int mimeType = media.getMimeType();
            long itemDuration = media.getDuration();
            Log.e("nb", "itemDuration->" + itemDuration);
            // int pictureType = PictureMimeType.isPictureType(media.getPictureType());
            final File file = new File(media.getPath());

            viewHolder.tv_duration.setText(DateUtils.formatDurationTime(itemDuration));
            viewHolder.tv_name.setText(media.getFileName());
            viewHolder.tv_fileSize.setText(String.format("(%s)", FileUtil.formatFileSize(new File(media.getPath()).length(), "0")));
            viewHolder.tv_addDate.setText(android.text.format.DateUtils.formatDateTime(
                    context,
                    file.lastModified(),
                    android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_NUMERIC_DATE | android.text.format.DateUtils.FORMAT_SHOW_TIME | android.text.format.DateUtils.FORMAT_SHOW_YEAR
            ));

            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = viewHolder.getAdapterPosition();
                        mItemClickListener.onItemClick(adapterPosition, v);
                    }
                });
            }

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String tiltle = String.format(file.getName());
                    final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null);
                    dialog.title(tiltle)
                            .titleTextSize_SP(14.5f)
                            .show();

                    dialog.setOnOperItemClickL(new OnOperItemClickL() {
                        @Override
                        public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    //删除文件
                                    int index = viewHolder.getAdapterPosition();
                                    if (index != RecyclerView.NO_POSITION) {
                                        //删除文件
                                        showDeleteDialog(context, index);
                                        dialog.dismiss();
                                    }
                                    break;
                            }
                        }
                    });
                    return false;
                }
            });

        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    /**
     * 是否删除此张图片
     * 将数据库中FileData 数据 Exist 项改为false 让count能递增
     */
    private void showDeleteDialog(final Context context, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("要删除此文件吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalMedia localMedia = list.get(index);
                File file = new File(localMedia.getPath());

                if (file.isFile()) {
                    //删除文件
                    boolean isDel = Utils.deleteAudioFile(context, file);
                    if (isDel) {
                        list.remove(index);
                        notifyDataSetChanged();
                      /*  notifyItemRemoved(index);
                        notifyItemRangeChanged(index, list.size());*/

                        //删除sqlite中保存的数据
                        List<FileData> fileDatas = SugarRecord.find(FileData.class, "FILE_NAME=?", file.getName());
                        if (null != fileDatas && fileDatas.size() > 0) {
                            FileData fileData = fileDatas.get(0);
                            fileData.setRealPath(null);
                            fileData.setExist(false);
                            SugarRecord.save(fileData);
                        }

                        Utils.scanDirMedia(context, file.getParentFile());
                        Logger.i("delete position: %s", index + "--->remove after:" + list.size());
                    } else {
                        RxToast.showToast("文件删除失败,请稍后重试");
                    }
                }
            }
        });

        builder.show();
    }

}
