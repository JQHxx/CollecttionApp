package com.huateng.collection.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.huateng.collection.R;
import com.huateng.fm.app.FmAttributeValues;
import com.huateng.fm.util.UiValuesUtil;
import com.tools.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @author dengzh
 * @description
 * @time 2016-12-02.
 * paddingLeft为标签距离输入区域的距离
 * leftMargin为标签到左边的距离
 */
public class UniversalInput extends AppCompatEditText {

    private final String TAG = getClass().getSimpleName();
    private Context context;
    private FmAttributeValues mAttrValues;
    private String mLabelText;
    float baseLine2 = 0;
    float baseLine = 0;
    private float mLeftLabelSize, mRightLabelSize;
    private Drawable background = null;
    private int mCornerStyle;
    private int mLabelPadding;
    private boolean mRequired;
    private int mRequireRightPadding, mOptionIconPadding;
    private int mInitLeftPadding, mInitRightPadding, mInitTopPadding, mInitBottomPadding;
    private int optionIconResId;
    private Drawable mOptionIconDrawable;
    private int mOptionIconSize = 0;
    private float mOptionIconLeft, mOptionIconTop;
    private OnHtOptionTouchListener onHtOptionTouchListener;
    private int mBgColor;
    private int mNormalColor;
    private int mInputType;
    //    public static final int TEXT = 0, EDIT_TEXT = 1, SPINNER = 2, DATE = 3, TIME = 4;
    private final int WRAP_CONTENT = -1;
    private boolean showBg = true;
    private int mBgStyle = 0;
    private final int LINE = 0, RETANGLE = 1;
    private SpinnerItemSelectedListener spinerListener;
    private BaseAdapter mAdapter;
    private List<String> itemList = new ArrayList<String>();
    private PopMenu popMenu;
    private View mPopView;
    private PopupWindow mPopUp;
    private ListView mListView;
    private int mLabelTextSize;
    private int mLineHeight;

    public UniversalInput(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public UniversalInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public UniversalInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);
    }

    private int mLabelLeftMargin;
    private int mLabelColor, mLabelWidth;

    private void init(AttributeSet attrs) {
        mAttrValues = new FmAttributeValues();

//        mLabelLeftMargin= UiValuesUtil.getDimen(com.huateng.flowMobile.R.dimen.ht_widget_edittext_label_left_margin);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UniversalInput);
            mLabelText = a.getString(R.styleable.UniversalInput_labelText);
            //正文文字距标签距离
            mLabelPadding = (int) a.getDimension(R.styleable.UniversalInput_labelPadding, FmAttributeValues.ht2dp);
            mLineHeight = (int) a.getDimension(R.styleable.UniversalInput_lineHeight, FmAttributeValues.ht1dp);

            mRequired = a.getBoolean(R.styleable.UniversalInput_required, false);
            mOptionIconDrawable = a.getDrawable(R.styleable.UniversalInput_optionIcon);
            mBgColor = a.getColor(R.styleable.UniversalInput_bgColor, Color.WHITE);
            mLabelColor = a.getColor(R.styleable.UniversalInput_labelColor, getResources().getColor(R.color.gray_textcolor));
            mInputType = a.getInt(R.styleable.UniversalInput_inputType, UniversalInputType.TEXT);
            mLabelWidth = (int) a.getDimension(R.styleable.UniversalInput_labelWidth, WRAP_CONTENT);
            mBgStyle = a.getInt(R.styleable.UniversalInput_bgStyle, 0);
            mNormalColor = a.getColor(R.styleable.UniversalInput_normalColor, getResources().getColor(R.color.gray_bg));
            mLabelTextSize = (int) a.getDimension(R.styleable.UniversalInput_labelTextSize, getTextSize());
            a.recycle();
            int[] attrsArray = new int[]{android.R.attr.background};
            TypedArray b = getContext().obtainStyledAttributes(attrs,
                    attrsArray);
            background = b.getDrawable(0);
            b.recycle();
        }
        initInputType();
        setSingleLine(false);
        //若是WRAP_CONTENT 则需测量标签宽度
        if (mLabelWidth == WRAP_CONTENT) {
            measuredLabelWidth();
        }
        if (mRequired) {
            mRequireRightPadding = (int) getResources().getDimension(com.huateng.flowMobile.R.dimen.ht_widget_edittext_star_rightPadding);
        }
        if (mOptionIconDrawable != null) {
            mOptionIconPadding = (int) getResources().getDimension(com.huateng.flowMobile.R.dimen.ht_widget_edittext_star_rightPadding);
        }
        mInitTopPadding = getPaddingTop();
        mInitRightPadding = getPaddingRight();
        mInitLeftPadding = getPaddingLeft();
        mInitBottomPadding = getPaddingBottom();
        setPadding(mLabelWidth + mLabelPadding + mInitLeftPadding + 2 * FmAttributeValues.ht2dp, mInitTopPadding, mInitRightPadding + mRequireRightPadding + mOptionIconPadding + mOptionIconSize,
                mInitBottomPadding);

    }

    public void setInputType(int inputType) {
        mInputType = inputType;
        initInputType();
        invalidate();
        requestLayout();
    }

    private void initInputType() {
        switch (mInputType) {
            case UniversalInputType.TEXT:
                showBg = false;
                setEnabled(false);
                mOptionIconDrawable = null;
                break;
            case UniversalInputType.SPINNER:
                showBg = true;
                setEnabled(false);
                mOptionIconSize = UiValuesUtil.getDimen(R.dimen.universal_input_icon_size_default);
                mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.spinner_arrow));
                initSpinner();
                break;
            case UniversalInputType.IMAGE:
                showBg = false;
                setEnabled(false);
                mOptionIconSize = (int) getResources().getDimension(R.dimen.universal_input_icon_size_larger);
                if (optionIconResId != 0) {
                    mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), optionIconResId));
                }
                break;
            case UniversalInputType.DATE.DATE_SINGEL:
                showBg = true;
                setEnabled(false);
                mOptionIconSize = UiValuesUtil.getDimen(R.dimen.universal_input_icon_size_larger);
                mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.picker_calendar));
                break;
            case UniversalInputType.DATE.TIME:
                showBg = true;
                mOptionIconSize = UiValuesUtil.getDimen(R.dimen.universal_input_icon_size_larger);
                mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.time));
                setEnabled(false);
                break;
            case UniversalInputType.EDIT.TEXT:
                setInputType(EditorInfo.TYPE_CLASS_TEXT);
                initEditInputType();
                break;
            case UniversalInputType.EDIT.DECIMAL:
                setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                initEditInputType();
                break;
            case UniversalInputType.EDIT.TELEPHONE:
                setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                initEditInputType();
                break;
            case UniversalInputType.EDIT.NUMBER:
                setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                initEditInputType();
                break;
            case UniversalInputType.EDIT.PASSWARD:
                setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                initEditInputType();
                break;
        }
    }

    public void initEditInputType() {
        showBg = true;
        setEnabled(true);
        mOptionIconDrawable = null;

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onValueChangeListener != null) {
                    onValueChangeListener.onValueChanged(s.toString());
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        doAfterMeasured();
    }

    private void doAfterMeasured() {
        if (getMeasuredHeight() <= 0 || getMeasuredWidth() <= 0) {
            //布局可能有问题 需检查
            return;
        }

        if (showBg) {
            if (mBgStyle == LINE) {
                initStatesLine();
            } else if (mBgStyle == RETANGLE) {
                initStatesRetangle();
            }
        } else {
            setBackground(null);
        }
        if (mLabelWidth == WRAP_CONTENT) {
            mPopUp = new PopupWindow(mPopView, getMeasuredWidth() - measuredLabelWidth() - mInitLeftPadding - mLabelPadding,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            mPopUp = new PopupWindow(mPopView, getMeasuredWidth() - mLabelWidth - mInitLeftPadding - mLabelPadding,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 获取标签宽度  默认WRAP_CONTENT时调用
     */
    private int measuredLabelWidth() {
        if (mLabelText != null && !TextUtils.isEmpty(mLabelText)) {
            Paint textPaint = new Paint();
            textPaint.setTextSize(mLabelTextSize);
            mLabelWidth = (int) (textPaint.measureText(mLabelText));
            return mLabelWidth;
        }
        return 0;
    }

    public String getNoneNullText() {
        return getText().toString() == null ? "" : getText().toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLabelText != null) {
            drawLabel(canvas);
        }
        if (mRequired) {
            drawStar(canvas);
        }
        if (mOptionIconDrawable != null) {
            drawOptionIcon(canvas);
        }
        super.onDraw(canvas);

    }


    //label
    private void drawLabel(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(mLabelColor);
        textPaint.setTextSize(mLabelTextSize);
        if (baseLine == 0) {
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            baseLine = getMeasuredHeight() / 2 + (metrics.bottom - metrics.top)
                    / 2 - metrics.bottom;
        }
        int measuredWith = (int) (textPaint.measureText(mLabelText));
//        canvas.drawText(mLabelText,getScrollX(), baseLine, textPaint);
        if (mLabelWidth == WRAP_CONTENT) {
            canvas.drawText(mLabelText, getScrollX(), baseLine, textPaint);
        } else {
            canvas.drawText(mLabelText, mLabelWidth - measuredWith, baseLine, textPaint);
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

    //icon
    private Drawable getLineDrawable(int lineColor) {
        int leftLength;
        leftLength = mLabelWidth;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineHeight);
        paint.setColor(lineColor);

        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(mBgColor);
        canvas.drawLine(leftLength, getMeasuredHeight() - mLineHeight, getMeasuredWidth(), getMeasuredHeight() - mLineHeight, paint);
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    private Drawable getLineDrawableNormal() {
        return getLineDrawable(mNormalColor);
    }

    private Drawable getLineDrawableStated() {
        return getLineDrawable(getResources().getColor(R.color.theme_color));
    }

    public interface OnHtOptionTouchListener {
        public void onHtOptionTouched();
    }

    public void setOnHtOptionTouchListener(OnHtOptionTouchListener onHtOptionTouchListener) {
        this.onHtOptionTouchListener = onHtOptionTouchListener;
    }

    private float mTouchHelpOffset = 20;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (mOptionIconDrawable != null && x > mOptionIconLeft - mTouchHelpOffset && x < mOptionIconLeft + mOptionIconSize + mTouchHelpOffset
                        && y > mOptionIconTop - mTouchHelpOffset && y < mOptionIconTop + mOptionIconSize + mTouchHelpOffset) {
                    if (onHtOptionTouchListener != null) {
                        onHtOptionTouchListener.onHtOptionTouched();
                    }
                    requestFocus();
//                    hasFocus=true;
                    requestFocusFromTouch();
                    if (mInputType == UniversalInputType.SPINNER) {
                        showPopWindow();
                    } else if (mInputType == UniversalInputType.DATE.DATE_SINGEL || mInputType == UniversalInputType.DATE.TIME) {
                        showDateTimePicker();
                    }
                    //Toast.makeText(context, "点击了OptionIcon",Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //star
    private void drawStar(Canvas canvas) {
        Paint textPaint1 = new Paint();
        textPaint1.setTextSize(30);
        textPaint1.setColor(getResources().getColor(com.huateng.flowMobile.R.color.red));
        textPaint1.setTextAlign(Paint.Align.CENTER);
        if (baseLine2 == 0) {
            Paint.FontMetrics metrics = textPaint1.getFontMetrics();
            baseLine2 = getMeasuredHeight() / 2 + (metrics.bottom - metrics.top)
                    / 2 - metrics.bottom + 2 * FmAttributeValues.ht2dp;
        }
        canvas.drawText("*", getMeasuredWidth() - textPaint1.measureText("*") - 10
                + getScrollX(), baseLine2, textPaint1);
    }


    private void initStates() {
//        GradientDrawable drawable_pressed = new GradientDrawable();
//        drawable_pressed.setCornerRadius(mAttrValues.getRadius());
//        drawable_pressed.setSize(mAttrValues.getSize(), mAttrValues.getSize());
//        drawable_pressed.setColor(mBgColor);
//        drawable_pressed.setStroke(mAttrValues.getBorderWidth(),
//                FmAttributeValues.PRIMARY_COLOR);
        Drawable drawable = getLineDrawableNormal();
//        GradientDrawable drawable_normal = new GradientDrawable();
//        drawable_normal.setCornerRadius(mAttrValues.getRadius());
//        drawable_normal.setSize(mAttrValues.getSize(), mAttrValues.getSize());
//        drawable_normal.setColor(mBgColor);
//        drawable_normal.setStroke(mAttrValues.getBorderWidth(),
//                FmAttributeValues.GRAY_BORDER_COLOR);
        Drawable drawableStated = getLineDrawableStated();

        StateListDrawable states = new StateListDrawable();

//         states.addState(new int[] { android.R.attr.state_pressed,
//         android.R.attr.state_enabled,-android.R.attr.state_enabled }, drawableStated);
        states.addState(new int[]{android.R.attr.state_focused}, drawableStated);
        states.addState(new int[]{-android.R.attr.state_focused}, drawable);
        if (background == null) {
            setBackgroundDrawable(states);
        }

    }


    private void initStatesRetangle() {
        GradientDrawable drawable_pressed = new GradientDrawable();
        drawable_pressed.setCornerRadius(mAttrValues.getRadius());
        drawable_pressed.setSize(mAttrValues.getSize(), mAttrValues.getSize());
        drawable_pressed.setColor(mBgColor);
        drawable_pressed.setStroke(mAttrValues.getBorderWidth(),
                FmAttributeValues.PRIMARY_COLOR);

        GradientDrawable drawable_normal = new GradientDrawable();
        drawable_normal.setCornerRadius(mAttrValues.getRadius());
        drawable_normal.setSize(mAttrValues.getSize(), mAttrValues.getSize());
        drawable_normal.setColor(mBgColor);
        drawable_normal.setStroke(mAttrValues.getBorderWidth(), mNormalColor);

        StateListDrawable states = new StateListDrawable();

//         states.addState(new int[] { android.R.attr.state_pressed,
//         android.R.attr.state_enabled,-android.R.attr.state_enabled }, drawableStated);
        states.addState(new int[]{android.R.attr.state_focused}, drawable_pressed);
        states.addState(new int[]{-android.R.attr.state_focused}, drawable_normal);
        if (background == null) {
            setBackgroundDrawable(states);
        }
    }

    private void initStatesLine() {
        Drawable drawable = getLineDrawableNormal();
        Drawable drawableStated = getLineDrawableStated();

        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_focused}, drawableStated);
        states.addState(new int[]{-android.R.attr.state_focused}, drawable);
        if (background == null) {
            setBackgroundDrawable(states);
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        if (scaleWidth >= scaleHeight) {
            scale = scaleHeight;
        } else {
            scale = scaleWidth;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public void setLabel(String labelText) {
        this.mLabelText = labelText;
        invalidate();
        measuredLabelWidth();
        setPadding(mLabelWidth + mLabelPadding + mInitLeftPadding + 2 * FmAttributeValues.ht2dp, getPaddingTop(), getPaddingRight(),
                getPaddingBottom());
    }

    public void setOptionIcon(int resId) {
        this.optionIconResId = resId;
        initInputType();
        invalidate();
    }


    /*********************
     * Spinner related
     ********************/

    private void initSpinner() {
        mPopView = LayoutInflater.from(context).inflate(
                com.huateng.flowMobile.R.layout.ht_spinner_pop, null);
        mListView = (ListView) mPopView.findViewById(com.huateng.flowMobile.R.id.listView);
        mListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mAdapter = new SpinnerListAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * ListView数据源
     *
     * @param optionStrs
     * @return
     */
    public UniversalInput setDataSource(String[] optionStrs) {
        this.itemList.clear();
        for (String s : optionStrs)
            this.itemList.add(s);
        mAdapter.notifyDataSetChanged();
        return this;
    }

    public UniversalInput setDataSource(List<String> itemList) {
        this.itemList.clear();
        if (null != itemList) {
            this.itemList.addAll(itemList);
            mAdapter.notifyDataSetChanged();
        }
        return this;
    }


    private void showPopWindow() {
        popMenu = new PopMenu();
        // 菜单项点击监听器
        popMenu.showAsDropDown(UniversalInput.this);
        // 弹出菜单监听器
        AdapterView.OnItemClickListener popmenuItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                setText(itemList.get(position));
                if (onValueChangeListener != null) {
                    onValueChangeListener.onValueChanged(itemList.get(position));
                }
                if (spinerListener != null) {
                    spinerListener.onItemSelected(position);
                }
                popMenu.dismiss();
            }
        };
        popMenu.setOnItemClickListener(popmenuItemClickListener);

    }

    public void setSpinnerItemSelectedListener(
            SpinnerItemSelectedListener listener) {
        this.spinerListener = listener;
    }


    /**
     * item选中监听器
     */
    public interface SpinnerItemSelectedListener {
        public void onItemSelected(int position);
    }

    class PopMenu {

        public PopMenu() {
            mPopUp.setBackgroundDrawable(new BitmapDrawable());
        }

        public void setOnItemClickListener(
                AdapterView.OnItemClickListener listener) {
            mListView.setOnItemClickListener(listener);
        }

        public void showAsDropDown(View anchor) {
            int xOffset;
            if (mLabelWidth == WRAP_CONTENT) {
                xOffset = measuredLabelWidth() + mInitLeftPadding + mLabelPadding;
            } else {
                xOffset = mLabelWidth + mInitLeftPadding + mLabelPadding;
            }

            mPopUp.showAsDropDown(anchor, xOffset, 3);
            mPopUp.setFocusable(true);
            mPopUp.setOutsideTouchable(true);
            mPopUp.update();
        }

        public void dismiss() {
            mPopUp.dismiss();
        }
    }

    final class SpinnerListAdapter extends BaseAdapter {

        public SpinnerListAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        com.huateng.flowMobile.R.layout.ht_spinner_list_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.tv_item = (TextView) convertView
                        .findViewById(com.huateng.flowMobile.R.id.textView);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Log.i(TAG, "itemList.get(position):" + itemList.get(position));
            holder.tv_item.setText(itemList.get(position));
            return convertView;
        }

        private final class ViewHolder {
            TextView tv_item;
        }
    }

    /*********************
     * Date Time related
     ********************/

    private void showDateTimePicker() {
        SublimePickerFragment pickerFrag = new SublimePickerFragment();
        pickerFrag.setCallback(mFragmentCallback);
        Pair<Boolean, SublimeOptions> optionsPair = getOptions();
        if (!optionsPair.first) { // If options are not valid
            Toast.makeText(getContext(), "No pickers activated",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Valid options
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
        pickerFrag.setArguments(bundle);

        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(((FragmentActivity) context).getSupportFragmentManager(), "SUBLIME_PICKER");
    }

    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            String text;
            Calendar calendar = selectedDate.getStartDate();
            String date = String.format("%s-%s-%s", calendar.get(Calendar.YEAR), StringUtils.repairDouble((calendar.get(Calendar.MONTH) + 1)),
                    StringUtils.repairDouble(calendar.get(Calendar.DAY_OF_MONTH)));

            if (mInputType == UniversalInputType.DATE.DATE_SINGEL) {
                text = date;
            } else {
                text = String.format("%s %s:%s:00", date, StringUtils.repairDouble(hourOfDay), StringUtils.repairDouble(minute));
            }
            setText(text);
            if (onValueChangeListener != null) {
                onValueChangeListener.onValueChanged(text);
            }
        }
    };

    private Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        if (mInputType == UniversalInputType.DATE.DATE_SINGEL) {
            displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
            options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        } else if (mInputType == UniversalInputType.DATE.TIME) {
            displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
            displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
            options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
        }
        options.setDisplayOptions(displayOptions);
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    private OnValueChangeListener onValueChangeListener;

    public interface OnValueChangeListener {
        public void onValueChanged(String value);
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

}
