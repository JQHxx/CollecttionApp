package com.huateng.fm.ui.widget;

import java.util.Observable;
import java.util.Observer;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 通用Dialog
 * @author Devin
 * 2015年1月9日  上午10:12:27
 */
public class FmAlertDialog extends Dialog implements Observer{

	
	public FmAlertDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public FmAlertDialog(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		
	}

	public void setLayout(double width, double height) {
		getWindow().setLayout((int) width, (int) height);
	}

	public void setAnimStyle(int styleId) {
		if (getWindow() != null) {
			getWindow().setWindowAnimations(styleId);
		}
	}

	public interface OnLeftBtnClickListener{
		public void leftBtnClicked(View v,Dialog dialog );
	}
	
	public interface OnRightBtnClickListener{
		public void rightBtnClicked(View v,Dialog dialog );
	}
	
	public interface OnCenterBtnClickListener{
		public void centerBtnClicked(View v,Dialog dialog);
	}
	
	
	
	
	public static class Builder {

		private static final int DEFAULT_STYLE = R.style.HTDialog;

		private LinearLayout mTitlePanel;
		private ImageView mIconImageView;
		private TextView mTitleTextView;
		private CharSequence mTitleText;
		private Drawable mIcon;

		private LinearLayout mContentPanel;
		private ScrollView mMessagePanel;
		private TextView mMessageTextView;
		private CharSequence mMessageText;

		private View mCustomView;

		private LinearLayout mButtonPanel;
		private Button mLeftButton;
		private Button mRightButton;
		private Button mCenterButton;
//		private View mDividerLeftView;
//		private View mDividerRightView;
		private CharSequence mLeftText;
		private CharSequence mRightText;
		private CharSequence mCenterText;
		private OnLeftBtnClickListener mLeftButtonListener;
		private OnRightBtnClickListener mRightButtonListener;
		private OnCenterBtnClickListener mCenterButtonListener;

		private ListView mListView;
		private ListAdapter mAdapter;
		private ListAdapter mSingleChoiceAdapter;
		private CharSequence[] mItems;
		private DialogInterface.OnClickListener mItemListener;
		private int mItemResourceId = -1;
		private int mItemTextViewId = -1;
		private int mCheckedItemIndex;
		private boolean mIsSingleChoice;

		private Context mContext;
		private View mLineView;
		private FmAlertDialog mDialog;
		private FmAttributeValues mAttrValues;

	    
		public Builder(Context context) {
			this.mContext = context;
		}

	    
		/**
		 * 标题栏图标
		 * @param iconId
		 * @return
		 */
		public Builder setIcon(int iconId) {
			if (iconId!=0) {
				this.mIcon = getDrawable(mContext, iconId);
			}
			return this;
		}

		public Builder setIcon(Drawable icon) {
			this.mIcon = icon;
			return this;
		}

		
		public Builder setTitle(int titleId) {
			this.mTitleText = getText(mContext, titleId);
			return this;
		}

		
		public Builder setTitle(String title) {
			this.mTitleText = title;
			return this;
		}

		
		public Builder setMessage(int messageId) {
			this.mMessageText = getText(mContext, messageId);
			return this;
		}

		
		public Builder setMessage(String message) {
			this.mMessageText = message;
			return this;
		}

		
		public Builder setContentView(View v) {
			this.mCustomView = v;
			return this;
		}

		
		public Builder setLeftButton(int textId,
				OnLeftBtnClickListener listener) {
			this.mLeftText = getText(mContext, textId);
			this.mLeftButtonListener = listener;
			return this;
		}

		
		public Builder setLeftButton(String text,
				OnLeftBtnClickListener listener) {
			this.mLeftText = text;
			this.mLeftButtonListener = listener;
			return this;
		}

		
		public Builder setRightButton(int textId,
				OnRightBtnClickListener listener) {
			this.mRightText = getText(mContext, textId);
			this.mRightButtonListener = listener;
			return this;
		}

		
		public Builder setRightButton(String text,
				OnRightBtnClickListener listener) {
			this.mRightText = text;
			this.mRightButtonListener = listener;
			return this;
		}

		
		public Builder setCenterButton(int textId,
				OnCenterBtnClickListener listener) {
			this.mCenterText = getText(mContext, textId);
			this.mCenterButtonListener = listener;
			return this;
		}

		
		public Builder setCenterButton(String text,
				OnCenterBtnClickListener listener) {
			this.mCenterText = text;
			this.mCenterButtonListener = listener;
			return this;
		}

		
		public Builder setItems(int itemsId, OnClickListener listener) {
			this.mItems = getTextArray(mContext, itemsId);
			this.mItemListener = listener;
			return this;
		}

		
		public Builder setItems(String[] items, OnClickListener listener) {
			this.mItems = items;
			this.mItemListener = listener;
			return this;
		}

		
		public Builder setItems(int itemsId, int resourceId, int textviewId,
				final OnClickListener listener) {
			this.mItems = getTextArray(mContext, itemsId);
			this.mItemListener = listener;
			this.mItemResourceId = resourceId;
			this.mItemTextViewId = textviewId;
			return this;
		}

		
		public Builder setItems(String[] items, int resourceId,
				int textviewId, final OnClickListener listener) {
			this.mItems = items;
			this.mItemListener = listener;
			this.mItemResourceId = resourceId;
			this.mItemTextViewId = textviewId;
			return this;
		}

	
		public Builder setSingleChoiceItems(int itemsId, int checkedItem,
				final OnClickListener listener) {
			this.mItems = mContext.getResources().getTextArray(itemsId);
			this.mItemListener = listener;
			this.mCheckedItemIndex = checkedItem;
			this.mIsSingleChoice = true;
			return this;
		}

		
		public Builder setSingleChoiceItems(String[] items,
				int checkedItem, final OnClickListener listener) {
			this.mItems = items;
			this.mItemListener = listener;
			this.mCheckedItemIndex = checkedItem;
			this.mIsSingleChoice = true;
			return this;
		}

		
		public Builder setItems(ListAdapter adapter) {
			this.mSingleChoiceAdapter = adapter;
			return this;
		}
		View contentView;
		/**
		 * 创建Dialog
		 * @return
		 */
		public FmAlertDialog create() {

			mDialog = new FmAlertDialog(mContext, DEFAULT_STYLE);
			LayoutInflater inflater = LayoutInflater.from(mContext);
			 contentView = inflater.inflate(R.layout.ht_alert_dialog, null);
			int width = (int) (mDialog.getWindow().getWindowManager().getDefaultDisplay().getWidth() * 0.5);
			LayoutParams lParams=new LayoutParams(width,LayoutParams.WRAP_CONTENT);
//			contentView.setLayoutParams(lParams);
			setupTitlePanel(contentView); // set title panel
			setupContentPanel(contentView); // set content panel
			setupListView(contentView); // set list view

			setupButtonPanel(contentView); // set button panel

			setupCustomView(contentView); // set custom view
			setupWindowParams();
			mDialog.setContentView(contentView,lParams);
			return mDialog;
		}

		private void setupWindowParams() {
			mDialog.setAnimStyle(R.style.HTDialog);
			Window window=mDialog.getWindow();
			WindowManager wm = window.getWindowManager();
			double width = wm.getDefaultDisplay().getWidth();
			mDialog.setLayout(width, WindowManager.LayoutParams.MATCH_PARENT);
//	        WindowManager.LayoutParams lp = window.getAttributes();
//	        lp.gravity=Gravity.CENTER;
//	        window.setAttributes(lp);
	        window.setGravity(Gravity.CENTER);

		}
		
		private void setupTitlePanel(View contentView) {
			mAttrValues= new FmAttributeValues();
//			mAttrValues.addObserver(HTAlertDialog.this);
			mTitlePanel = (LinearLayout) contentView
					.findViewById(R.id.title_panel);
			mIconImageView = (ImageView) contentView.findViewById(R.id.icon);
			mTitleTextView = (TextView) contentView.findViewById(R.id.title);
			mLineView =  contentView.findViewById(R.id.line);

			if (mIcon != null) {
				mIconImageView.setImageDrawable(mIcon);
			} else {
				mIconImageView.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(mTitleText)) {
				mTitleTextView.setText(mTitleText);
			} else {
				mTitlePanel.setVisibility(View.GONE);
			}
			mLineView.setBackgroundColor(mAttrValues.getColor(2));
		}

		private void setupContentPanel(View contentView) {
			mContentPanel = (LinearLayout) contentView
					.findViewById(R.id.content_panel);
			mMessageTextView = (TextView) contentView
					.findViewById(R.id.message);
			mMessagePanel = (ScrollView) contentView
					.findViewById(R.id.message_panel);

			if (!TextUtils.isEmpty(mMessageText)) {
				mMessageTextView.setText(mMessageText);
			} else {
				mMessagePanel.setVisibility(View.GONE);
			}
		}

		private void setupListView(View contentView) {
			mListView = (ListView) contentView.findViewById(R.id.lv);

			IOnClickItemListener itemListener = null;
			if (mItemListener != null) {
				itemListener = new IOnClickItemListener() {

					@Override
					public void onClickItem(View view, int position) {
						mItemListener.onClick(mDialog, position);
						if (!mIsSingleChoice) {
							mDialog.dismiss();
						}
					}
				};
			}

			if (mItems != null && mItems.length > 0) {
				if (!mIsSingleChoice) {
					if (mItemResourceId < 0 || mItemTextViewId < 0) {
						mAdapter = new ItemAdapter<CharSequence>(mContext,
								mItems, itemListener);
					} else {
						mAdapter = new ItemAdapter<CharSequence>(mContext,
								mItems, mItemResourceId, mItemTextViewId,
								itemListener);
					}
				} else {
					if (mSingleChoiceAdapter == null) {
						mAdapter = new ItemAdapter<CharSequence>(mContext,
								mItems, mCheckedItemIndex, true, itemListener);
					}
				}
			}
			if (mSingleChoiceAdapter != null) {
				mAdapter = mSingleChoiceAdapter;
			}
			mListView.setAdapter(mAdapter);
			if (mAdapter == null) {
				mListView.setVisibility(View.GONE);
			}
		}

		private void setupButtonPanel(View contentView) {
			mLeftButton = (Button) contentView
					.findViewById(R.id.leftBtn);
			mRightButton = (Button) contentView
					.findViewById(R.id.rightBtn);
			mCenterButton = (Button) contentView
					.findViewById(R.id.centerBtn);
//			mDividerLeftView = contentView.findViewById(R.id.view_divider_left);
//			mDividerRightView = contentView
//					.findViewById(R.id.view_divider_right);
			mButtonPanel = (LinearLayout) contentView
					.findViewById(R.id.button_panel);

			boolean showLeft = false;
			boolean showCenter = false;
			boolean showRight = false;

			// set the confirm button visible
			if (!TextUtils.isEmpty(mLeftText)) {
				showLeft = true;
				mLeftButton.setText(mLeftText);
				mLeftButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mLeftButtonListener != null) {
							mLeftButtonListener.leftBtnClicked(v,mDialog);;
						}
					}
				});
			} else {
				mLeftButton.setVisibility(View.GONE);
			}

			// set the cancel button visible
			if (!TextUtils.isEmpty(mRightText)) {
				showRight = true;
				mRightButton.setText(mRightText);
				mRightButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mRightButtonListener != null) {
							mRightButtonListener.rightBtnClicked(mRightButton,mDialog);
						}
					}
				});
			} else {
				mRightButton.setVisibility(View.GONE);
			}

			if (!TextUtils.isEmpty(mCenterText)) {
				showCenter = true;
				mCenterButton.setText(mCenterText);
				mCenterButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mCenterButtonListener != null) {
							mCenterButtonListener.centerBtnClicked(mCenterButton,mDialog);
						}
					}
				});
			} else {
				mCenterButton.setVisibility(View.GONE);
			}

			if (!showLeft && !showCenter && !showRight) {
				mButtonPanel.setVisibility(View.GONE);
			}
		}

		/**
		 * 设置自定义视图
		 * @param contentView
		 */
		private void setupCustomView(View contentView) {
			if (mCustomView != null) {
				mContentPanel.setVisibility(View.VISIBLE);
				mContentPanel.removeAllViews();
				mContentPanel.addView(contentView, new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}

		public FmAlertDialog show() {
			mDialog = create();
			mDialog.show();
			return mDialog;
		}

		private CharSequence getText(Context context, int resId) {
			if (context == null) {
				return null;
			}
			try {
				return context.getText(resId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		private CharSequence[] getTextArray(Context context, int resId) {
			if (context == null) {
				return null;
			}
			try {
				return context.getResources().getTextArray(resId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public Drawable getDrawable(Context context, int iconId) {
			if (context == null) {
				return null;
			}
			try {
				return context.getResources().getDrawable(iconId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static interface IOnClickItemListener {

		void onClickItem(View view, int position);
	}

	private static class ItemAdapter<T> extends BaseAdapter {

		private static final int DEFAULT_RESOURCE_ID = R.layout.ht_alert_dialog_list_item;
		private static final int DEFAULT_TEXTVIEW_ID = R.id.text1;
		private static final int DEFAULT_RADIO_BUTTON_ID = R.id.radio_button;

		private int mCheckedItem = -1;
		private int mLayoutResourceId = DEFAULT_RESOURCE_ID;
		private int mTextViewResourceId = DEFAULT_TEXTVIEW_ID;
		private int mRadioButtonResourceId = DEFAULT_RADIO_BUTTON_ID;

		private boolean mIsSingleChoice;

		private T[] mItems;

		private IOnClickItemListener mListener;

		private Context mContext;

		public ItemAdapter(Context context, T[] objects,
				IOnClickItemListener listener) {
			this(context, objects, DEFAULT_RESOURCE_ID, DEFAULT_TEXTVIEW_ID,
					listener);
		}

		public ItemAdapter(Context context, T[] objects, int resource,
				int textViewResourceId, IOnClickItemListener listener) {
			this.mContext = context;
			this.mLayoutResourceId = resource;
			this.mTextViewResourceId = textViewResourceId;
			this.mItems = objects;
			this.mIsSingleChoice = false;
			this.mListener = listener;
		}

		/**
		 * Constructor for single choice item.
		 */
		public ItemAdapter(Context context, T[] objects, int checkedItem,
				boolean isSingleChoice, IOnClickItemListener listener) {
			this.mContext = context;
			this.mItems = objects;
			this.mCheckedItem = checkedItem;
			this.mIsSingleChoice = isSingleChoice;
			this.mListener = listener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						mLayoutResourceId, null);
			}
			TextView textView = (TextView) convertView
					.findViewById(mTextViewResourceId);
			textView.setText(mItems[position].toString());

			View radio = convertView.findViewById(mRadioButtonResourceId);
			if (radio != null) {
				if (!mIsSingleChoice) {
					radio.setVisibility(View.GONE);
				} else {
					radio.setVisibility(View.VISIBLE);
					if (radio instanceof RadioButton) {
						if (mCheckedItem == position) {
							((RadioButton) radio).setChecked(true);
						} else {
							((RadioButton) radio).setChecked(false);
						}
					}
				}
			}
			View rootView = convertView.findViewById(R.id.root);
			rootView.setTag(position);
			rootView.setOnClickListener(mItemListener);
			textView.setTag(position);
			textView.setOnClickListener(mItemListener);
			radio.setTag(position);
			radio.setOnClickListener(mItemListener);
			return convertView;
		}

		private View.OnClickListener mItemListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				mCheckedItem = position;
				notifyDataSetChanged();
				if (mListener != null) {
					mListener.onClickItem(v, position);
				}
			}
		};

		@Override
		public int getCount() {
			if (mItems == null) {
				return 0;
			}
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			if (mItems == null) {
				return null;
			}
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	@Override
	public void update(Observable observable, Object data) {
	}

}
