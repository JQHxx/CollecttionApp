package com.tools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoCompleteTextView
 *
 * @author devin
 *         2014-12-10 下午5:03:04
 */
public class FmAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    private final String TAG = getClass().getSimpleName();
    private Context context;
    private String mLabelText;
    float baseLine2 = 0;
    float baseLine = 0;
    private float mLeftLabelSize, mRightLabelSize;
    private Drawable background = null;
    private int mCornerStyle;
    private int mLabelPadding;
    private int mLabelFixedPadding;
    private boolean mRequired;
    private int mRequireRightPadding, mOptionIconPadding;
    private int mInitLeftPadding, mInitRightPadding, mInitTopPadding, mInitBottomPadding;
    private int optionIconResId;
    private Drawable mOptionIconDrawable;
    private int mOptionIconSize = 0;
    private float mOptionIconLeft, mOptionIconTop;
    private List<String> itemList = new ArrayList<String>();
    private AutoCompleteAdapter mAdapter;
    private OnHtOptionClickListener onHtOptionClickListener;

    public FmAutoCompleteTextView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public FmAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public FmAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.ht_AutoCompleteTextView);
            mLabelText = a.getString(R.styleable.ht_AutoCompleteTextView_labelText);
            mLabelPadding = (int) a.getDimension(R.styleable.ht_AutoCompleteTextView_labelPadding, 2);
            mRequired = a.getBoolean(R.styleable.ht_AutoCompleteTextView_required, false);
            mOptionIconDrawable = a.getDrawable(R.styleable.ht_AutoCompleteTextView_optionIcon);
            a.recycle();

            int[] attrsArray = new int[]{android.R.attr.background};
            TypedArray b = getContext().obtainStyledAttributes(attrs,
                    attrsArray);
            background = b.getDrawable(0);
            b.recycle();

            if (mOptionIconDrawable != null) {
                mOptionIconPadding = (int) getResources().getDimension(R.dimen.d_15dp);
                mOptionIconSize = 35;
            }
            mInitTopPadding = getPaddingTop();
            mInitRightPadding = getPaddingRight();
            mInitLeftPadding = getPaddingLeft();
            mInitBottomPadding = getPaddingBottom();
            setPadding((int) (mInitLeftPadding + 2 * getResources().getDimension(R.dimen.d_2dp)), mInitTopPadding, mInitRightPadding + mOptionIconPadding + mOptionIconSize,
                    mInitBottomPadding);
        }
        setSingleLine(true);
    }


    private void initStates() {
        GradientDrawable drawable_pressed = new GradientDrawable();
        drawable_pressed.setCornerRadius(0);
        drawable_pressed.setSize(20,20);
        drawable_pressed.setColor(Color.WHITE);
        drawable_pressed.setStroke(2,
               getResources().getColor(R.color.gray));

        GradientDrawable drawable_normal = new GradientDrawable();
        drawable_normal.setCornerRadius(0);
        drawable_normal.setSize(20, 20);
        drawable_normal.setColor(Color.WHITE);
        drawable_normal.setStroke(2,
                getResources().getColor(R.color.white));

        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_focused,
                android.R.attr.state_enabled}, drawable_pressed);
        states.addState(new int[]{-android.R.attr.state_enabled},
                drawable_normal);
        states.addState(new int[]{android.R.attr.state_enabled},
                drawable_normal);
        if (background == null) {
            setBackgroundDrawable(states);
        }
    }

    //icon
    private void drawOptionIcon(Canvas canvas) {
        Paint textPaint1 = new Paint();
        textPaint1.setTextSize(30);
        BitmapDrawable bd = (BitmapDrawable) mOptionIconDrawable;
        Bitmap bitmap = bd.getBitmap();
        Bitmap newBitmap = getResizedBitmap(bitmap, mOptionIconSize, mOptionIconSize);
        if (mRequired) {
            mOptionIconLeft = (getMeasuredWidth() - textPaint1.measureText("*") - 10)
                    + getScrollX() - mOptionIconSize - 10;
        } else {
            mOptionIconLeft = getMeasuredWidth()
                    + getScrollX() - mOptionIconSize - 10;
        }

        mOptionIconTop = (getMeasuredHeight() - mOptionIconSize) / 2;
        canvas.drawBitmap(newBitmap, mOptionIconLeft, mOptionIconTop, null);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initStates();
    }

    public void setOptionIcon(int resId) {
        this.optionIconResId = resId;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mOptionIconDrawable != null) {
            drawOptionIcon(canvas);
        }
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (mOptionIconDrawable != null && x > mOptionIconLeft && x < mOptionIconLeft + mOptionIconSize && y > mOptionIconTop && y < mOptionIconTop + mOptionIconSize) {
                    if (onHtOptionClickListener != null) {
                        onHtOptionClickListener.onHtOptionClicked();
                    }
                    return false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    interface OnHtOptionClickListener {
        public void onHtOptionClicked();
    }

    public void setOnHtOptionTouchListener(OnHtOptionClickListener onHtOptionClickListener) {
        this.onHtOptionClickListener = onHtOptionClickListener;
    }


    private int mMaxMatch = 10;//最多显示多少个选项,负数表示全部


    public int getmMaxMatch() {
        return mMaxMatch;
    }

    public void setmMaxMatch(int mMaxMatch) {
        this.mMaxMatch = mMaxMatch;
    }

    public FmAutoCompleteTextView setDataSource(List<String> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        if (mAdapter == null) {
            mAdapter = new AutoCompleteAdapter();
            setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        return this;
    }

    class AutoCompleteAdapter extends BaseAdapter implements Filterable {
        private ArrayFilter mFilter;
        private List<String> mObjects = new ArrayList<String>();//过滤后的item
        private final Object mLock = new Object();

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new ArrayFilter();
            }
            return mFilter;
        }

        private class ArrayFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();

//		          if (mOriginalValues == null) {    
//		                synchronized (mLock) {    
//		                    mOriginalValues = new ArrayList<String>(mObjects);//    
//		                }    
//		            }  

                if (prefix == null || prefix.length() == 0) {
                    synchronized (mLock) {
                        Log.i("tag", "mOriginalValues.size=" + itemList.size());
                        ArrayList<String> list = new ArrayList<String>(itemList);
                        results.values = list;
                        results.count = list.size();
                        return results;
                    }
                } else {
                    String prefixString = prefix.toString().toLowerCase();

                    final int count = itemList.size();

                    final ArrayList<String> newValues = new ArrayList<String>(count);

                    for (int i = 0; i < count; i++) {
                        final String value = itemList.get(i);
                        final String valueText = value.toLowerCase();
                        if (valueText.startsWith(prefixString)) {  //源码 ,匹配开头
                            newValues.add(value);
                        }

                        if (mMaxMatch > 0) {//有数量限制
                            if (newValues.size() > mMaxMatch - 1) {//不要太多
                                break;
                            }
                        }
                    }

                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mObjects = (List<String>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        }

        @Override
        public int getCount() {
            Log.i(TAG, "mObjects.size():" + mObjects.size());
            return mObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return mObjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Log.i(TAG, "getView--");
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.ht_autocomplete_textview_list_item, null);
                holder.tv_text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_text.setText(mObjects.get(position));
//		        holder.iv.setOnClickListener(new OnClickListener() {  
//		              
//		            @Override  
//		            public void onClick(View v) {  
//		                String obj=mObjects.remove(position);  
//		                mOriginalValues.remove(obj);  
//		                notifyDataSetChanged();  
//		            }  
//		        });  
            return convertView;
        }

        class ViewHolder {
            TextView tv_text;
        }

        public List<String> getAllItems() {
            return itemList;
        }
    }

}


