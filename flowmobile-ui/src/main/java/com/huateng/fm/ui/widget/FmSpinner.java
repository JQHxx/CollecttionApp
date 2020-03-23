package com.huateng.fm.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
/**
 * Spinner
 * @author Devin
 * 2015年1月6日  下午2:56:31
 */
public class FmSpinner extends LinearLayout implements Observer{

	private String TAG = getClass().getSimpleName();
	private Context context;
	private TextView tv_Text,tv_labelText,tv_required;
	private ImageView iv_indicator;
	private HTSpinnerItemSelectedListener listener;
	private BaseAdapter mAdapter;
	private List<String> itemList = new ArrayList<String>();
	private PopMenu popMenu;
	private String mLabelText,mText;
	private FmAttributeValues mAttrValues;
	private Drawable background = null;
	private int mCornerStyle;
	private boolean mRequired;
	private View mPopView;
	private PopupWindow mPopUp;
	private ListView mListView;
	private View view;
	private int mBgColor;
	
	public FmSpinner(Context context) {
		super(context);
		this.context = context;
		init();
		initViews();
	}

	public FmSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
		init(attrs);
		initViews();
	}

	private void init(){
		mAttrValues= new FmAttributeValues();
		mAttrValues.addObserver(this);

		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER_VERTICAL);
		setClickable(true);
		setFocusableInTouchMode(true);  
	}

	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.ht_Spinner);
		mCornerStyle = a.getInt(R.styleable.ht_Spinner_cornerStyle,
				FmAttributeValues.CornerStyle.ROUND);
		mLabelText = a.getString(R.styleable.ht_Spinner_labelText);
		mText = a.getString(R.styleable.ht_Spinner_text);
		mRequired = a.getBoolean(R.styleable.ht_Spinner_required,false);
		mBgColor=a.getColor(R.styleable.ht_Spinner_bgColor, Color.WHITE);
		a.recycle();

		int[] attrsArray = new int[] { android.R.attr.background };
		TypedArray b = getContext().obtainStyledAttributes(attrs,
				attrsArray);
		background = b.getDrawable(0);
		b.recycle();

	}
	
	public void setText(String text){
		tv_Text.setText(text);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setCornerStyleInner(mCornerStyle);
		mPopUp = new PopupWindow(mPopView,getMeasuredWidth(),
				LayoutParams.WRAP_CONTENT);
	}
	private void initStates() {
		final int paddingTop = getPaddingTop();
		final int paddingRight = getPaddingRight();
		final int paddingLeft = getPaddingLeft();
		final int paddingBottom = getPaddingBottom();
		

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
		drawable_normal.setStroke(mAttrValues.getBorderWidth(),
				FmAttributeValues.GRAY_BORDER_COLOR);

		StateListDrawable states = new StateListDrawable();

		// states.addState(new int[] { android.R.attr.state_pressed,
		// android.R.attr.state_enabled }, drawable_pressed);
		states.addState(new int[] { android.R.attr.state_focused
			}, drawable_pressed);
		states.addState(new int[] { -android.R.attr.state_focused },
				drawable_normal);
		states.addState(new int[] { android.R.attr.state_enabled },
				drawable_normal);
		if (background == null) {
			setBackgroundDrawable(states);
		}
		setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

	}
	
	public void setIndicatorResId(int id){
		iv_indicator.setImageResource(id);
	}
	
	private void createSpinner() {
		 view=LayoutInflater.from(context).inflate(R.layout.ht_spinner, null);
		tv_labelText=(TextView)view.findViewById(R.id.labelText);		
		tv_Text=(TextView)view.findViewById(R.id.text);	
		tv_required=(TextView)view.findViewById(R.id.require);
		iv_indicator=(ImageView)view.findViewById(R.id.indicator);
		tv_labelText.setText(mLabelText);
		if (mLabelText!=null) {
			tv_labelText.setText(mLabelText);
		}else{
			tv_labelText.setVisibility(View.GONE);
		}
		tv_Text.setText(mText);
		tv_required.setVisibility(mRequired?VISIBLE:GONE);
		addView(view);  
	}
	
	private void initViews() {
		createSpinner();
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestFocus();
				showPopWindow();
			}
		});
		 mPopView = LayoutInflater.from(context).inflate(
				R.layout.ht_spinner_pop, null);
		 mListView = (ListView) mPopView.findViewById(R.id.listView);
		 mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mAdapter = new SpinnerListAdapter();
		mListView.setAdapter(mAdapter);
	}
	
	 public void setAdapter(BaseAdapter adapter) {
		 mAdapter=adapter;
	 }
	 
	/**
	 * ListView数据源
	 * 
	 * @param optionStrs
	 * @return
	 */
	public FmSpinner setDataSource(String[] optionStrs) {
		this.itemList.clear();
		for (String s : optionStrs)
			this.itemList.add(s);
		mAdapter.notifyDataSetChanged();
		return this;
	}

	public FmSpinner setDataSource(List<String> itemList) {
		this.itemList.clear();
		this.itemList.addAll(itemList);
		
			mAdapter.notifyDataSetChanged();
		
		return this;
	}


	private void showPopWindow() {
		popMenu = new PopMenu();
		// 菜单项点击监听器
		popMenu.showAsDropDown(FmSpinner.this);
		// 弹出菜单监听器
		AdapterView.OnItemClickListener popmenuItemClickListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				tv_Text.setText(itemList.get(position));
				if (listener != null) {
					listener.onItemSelected(position);
				}
				popMenu.dismiss();
			}
		};
		popMenu.setOnItemClickListener(popmenuItemClickListener);

	}

	public void setHTSpinnerItemSelectedListener(
			HTSpinnerItemSelectedListener listener) {
		this.listener = listener;
	}
	

	/**
	 * 获取选中后的文字
	 * 
	 * @return
	 */
	public String getSelectedText() {
		return tv_Text.getText().toString();
	}

	/**
	 * item选中监听器
	 */
	public interface HTSpinnerItemSelectedListener {
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
			mPopUp.showAsDropDown(anchor, 0, 3);
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
						R.layout.ht_spinner_list_item, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
				holder.tv_item = (TextView) convertView
						.findViewById(R.id.textView);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Log.i(TAG, "itemList.get(position):"+itemList.get(position));
			holder.tv_item.setText(itemList.get(position));
			return convertView;
		}

		private final class ViewHolder {
			TextView tv_item;
		}
	}
	
	public void setCornerStyleInner(int cornerStyle) {
		switch (cornerStyle) {
		case FmAttributeValues.CornerStyle.NONE:
			mAttrValues.setRadius(0);
			break;
		case FmAttributeValues.CornerStyle.SMALL:
			mAttrValues.setRadius(FmAttributeValues.SMALL_CORNER_RADIUS_DP);
			break;
		case FmAttributeValues.CornerStyle.ROUND:
			int height = (getMeasuredHeight()) / 2;
			mAttrValues.setRadius(height);
			break;

		default:
			break;

		}
		initStates();

	}

	public void setCornerStyle(int cornerStyle) {
		this.mCornerStyle=cornerStyle;
		initStates();		
	}

	@Override
	public void update(Observable observable, Object data) {
		initStates();		
		
	}
}
