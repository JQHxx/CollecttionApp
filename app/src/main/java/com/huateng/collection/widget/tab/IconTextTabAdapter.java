package com.huateng.collection.widget.tab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huateng.collection.R;

import java.util.List;


/**
 * Created by shanyong
 */
public class IconTextTabAdapter
        extends RecyclerTabLayout.Adapter<IconTextTabAdapter.ViewHolder> {

    private List<IconTextTabEntity> mTabEntities;
    private RecyclerTabLayout mTabLayout;

    public IconTextTabAdapter(RecyclerTabLayout tabLayout, List<IconTextTabEntity> entities) {
        this.mTabEntities = entities;
        this.mTabLayout = tabLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_icon_text_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IconTextTabEntity entity = mTabEntities.get(position);

        Drawable drawable = loadIconWithTint(holder.imageView.getContext(),
                entity.getIconRes());

        holder.imageView.setImageDrawable(drawable);

        boolean isSelect = position == getCurrentIndicatorPosition();

        holder.imageView.setSelected(isSelect);

        holder.textView.setText(entity.getText());
        holder.textView.setSelected(isSelect);

    }

    private Drawable loadIconWithTint(Context context, @DrawableRes int resourceId) {
        Drawable icon = ContextCompat.getDrawable(context, resourceId);
        ColorStateList colorStateList = ContextCompat
                .getColorStateList(context, R.color.color_icon_text_tab);
        icon = DrawableCompat.wrap(icon);
        DrawableCompat.setTintList(icon, colorStateList);
        return icon;
    }

    @Override
    public int getItemCount() {
        return mTabEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTabLayout.setCurrentItem(getAdapterPosition(), false);
                }
            });
        }
    }
}
