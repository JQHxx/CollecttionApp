package com.huateng.collection.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huateng.collection.R;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.utils.Utils;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
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
public class GridImageAdapter extends
        RecyclerView.Adapter<GridImageAdapter.ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private int selectMax = 12;
    private Context context;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    public GridImageAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
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

        ImageView mImg;
        LinearLayout ll_del;
        TextView tv_duration;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.fiv);
            ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
        }
    }

    @Override
    public int getItemCount() {
       /* if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }*/

        return list.size() + 1;
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
        View view = mInflater.inflate(R.layout.list_item_photo,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
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
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.drawable.ic_add2);
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddPicClickListener.onAddPicClick();
                }
            });
            viewHolder.ll_del.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.ll_del.setVisibility(View.VISIBLE);
            viewHolder.ll_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = viewHolder.getAdapterPosition();
                    // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                    // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                    if (index != RecyclerView.NO_POSITION) {
                        //删除文件
                        showDeleteDialog(context, index);
                    }
                }
            });
            LocalMedia media = list.get(position);
            String mimeType = media.getMimeType();
            String path = "";
            if (media.isCompressed()) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            // 图片
            if (media.isCompressed()) {
                Log.i("compress image result:", new File(media.getCompressPath()).length() / 1024 + "k");
                Log.i("压缩地址::", media.getCompressPath());
            }
            int pictureType = 0;
            long duration = media.getDuration();
            viewHolder.tv_duration.setVisibility(pictureType == PictureConfig.TYPE_VIDEO
                    ? View.VISIBLE : View.GONE);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(viewHolder.itemView.getContext())
                    .load(path)
                    .apply(options)
                    .into(viewHolder.mImg);
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
                if(list.size()>index) {
                    LocalMedia localMedia = list.get(index);
                    File file = new File(localMedia.getPath());

                    if (file.isFile()) {
                        //删除文件
                        boolean isDel = Utils.deleteMediaFile(context, file);

                        if (isDel) {
                            list.remove(index);
                            notifyDataSetChanged();
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

            }
        });

        builder.show();
    }

}
