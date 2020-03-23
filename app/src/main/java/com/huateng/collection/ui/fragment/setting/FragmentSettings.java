//package com.huateng.collection.ui.fragment.setting;
//
//import android.animation.IntEvaluator;
//import android.animation.ValueAnimator;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.huateng.collection.R;
//import com.huateng.collection.app.GlobalConstants;
//import com.huateng.collection.app.Perference;
//import com.huateng.collection.bean.api.RespBase;
//import com.huateng.collection.network.CommonInteractor;
//import com.huateng.collection.network.RequestCallbackImpl;
//import com.huateng.collection.event.BusTag;
//import com.huateng.collection.ui.activity.ActivityLogin;
//import com.huateng.collection.ui.dialog.DialogCenter;
//import com.huateng.collection.ui.dialog.dm.BaseDM;
//import com.huateng.collection.ui.dialog.dm.CommonContentDM;
//import com.huateng.collection.ui.base.BaseFragment;
//import com.huateng.collection.utils.DataCleanManager;
//import com.huateng.collection.utils.rxbus.annotation.Subscribe;
//import com.huateng.collection.utils.rxbus.annotation.Tag;
//import com.huateng.fm.ui.widget.FmButton;
//import com.huateng.network.ApiConstants;
//import com.huateng.network.NetworkConfig;
//import com.tools.utils.RxActivityUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
//public class FragmentSettings extends BaseFragment implements View.OnClickListener {
//
//    @BindView(R.id.layout_modifyPassword)
//    LinearLayout layoutModifyPassword;
//    @BindView(R.id.layout_clearCache)
//    LinearLayout layoutClearCache;
//    @BindView(R.id.layout_checkVersion)
//    LinearLayout layoutCheckVersion;
//    @BindView(R.id.fl_container)
//    FrameLayout flContainer;
//    @BindView(R.id.tv_cacheSize)
//    TextView tvCacheSize;
//    @BindView(R.id.tv_name)
//    TextView tvName;
//    @BindView(R.id.btn_logout)
//    FmButton btnLogout;
//
//    private String beforeMemory;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_settings, container, false);
//        ButterKnife.bind(this, view);
//        return view;
//    }
//
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        layoutModifyPassword.setOnClickListener(this);
//        layoutClearCache.setOnClickListener(this);
//        layoutCheckVersion.setOnClickListener(this);
//        btnLogout.setOnClickListener(this);
//
//        if (null != GlobalConstants.getCurrentUserInfo()) {
//            tvName.setText(GlobalConstants.getCurrentUserInfo().getNickName());
//        }
//
//        updateUiStatus();
//    }
//
//
//    @Subscribe(tags = {@Tag(BusTag.REFRESH_MEMORY)})
//    public void refreshMemoryCache(final String lastMemory) {
//
//        int startValue;
//        if (beforeMemory.contains("MB")) {
//            startValue = Integer.parseInt(beforeMemory.split("MB")[0]);
//        } else if (beforeMemory.contains("G")) {
//            startValue = Integer.parseInt(beforeMemory.split("G")[0]) * 1024;
//        } else {
//            tvCacheSize.setText(lastMemory);
//            return;
//        }
//
//        final int endValue;
//        if (lastMemory.contains("KB")) {
//            endValue = 0;
//        } else if (lastMemory.contains("MB")) {
//            endValue = Integer.parseInt(lastMemory.split("MB")[0]);
//        } else {
//            tvCacheSize.setText(lastMemory);
//            return;
//        }
//
//        ValueAnimator animator = ValueAnimator.ofObject(new IntEvaluator(), startValue, endValue);
//        animator.setDuration(1000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int value = (int) animation.getAnimatedValue();
//                if (value != endValue) {
//                    tvCacheSize.setText(String.valueOf(value + "MB"));
//                } else {
//                    tvCacheSize.setText(lastMemory);
//                }
//            }
//        });
//        animator.start();
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        updateUiStatus();
//    }
//
//    public void updateUiStatus() {
//        beforeMemory = DataCleanManager.getTotalCacheSize(getContext());
//        tvCacheSize.setText(beforeMemory);
//    }
//
//    public void initBackgroudColor() {
//        layoutModifyPassword.setBackgroundColor(Color.WHITE);
//        layoutClearCache.setBackgroundColor(Color.WHITE);
//        layoutCheckVersion.setBackgroundColor(Color.WHITE);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        initBackgroudColor();
//        if (id == R.id.layout_modifyPassword) {
//            v.setBackgroundColor(Color.parseColor("#EFEFF4"));
//
//            replaceFragment(BaseFragment.newInstance(FragmentModifyPassword.class), false);
//        } else if (id == R.id.layout_clearCache) {
//            v.setBackgroundColor(Color.parseColor("#EFEFF4"));
//
//            replaceFragment(BaseFragment.newInstance(FragmentClearCache.class), false);
//        } else if (id == R.id.layout_checkVersion) {
//            v.setBackgroundColor(Color.parseColor("#EFEFF4"));
//
//            replaceFragment(BaseFragment.newInstance(FragmentCheckVersion.class), false);
//        } else if (id == R.id.btn_logout) {
//            final CommonContentDM dm = new DialogCenter(getContext()).showLogoutDialog();
//            dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
//                @Override
//                public void onLeftClicked(View v) {
//                    Map<String, String> map = new HashMap<>();
//                    map.put("token", NetworkConfig.C.getAuth());
//                    map.put("url", "http://170.252.199.174:18088/ccms-app/service/request/call2.htm");
//
//                    CommonInteractor.request(new RequestCallbackImpl<RespBase>() {
//
//                        @Override
//                        public void beforeRequest() {
//                            super.beforeRequest();
//                            showLoading();
//                        }
//
//                        @Override
//                        public void response(RespBase resp) {
//                            dm.getDialog().dismiss();
//                            NetworkConfig.C.setAuth(null);
//                            Perference.clear();
//                            RxActivityUtils.skipActivityAndFinishAll(getContext(), ActivityLogin.class);
//                        }
//                    }, ApiConstants.APP_ROOT, ApiConstants.METHOD_LOGOUT, map);
//
//                }
//
//                @Override
//                public void onRightClicked(View v) {
//                    dm.getDialog().dismiss();
//                }
//            });
//        }
//
//    }
//
//}
