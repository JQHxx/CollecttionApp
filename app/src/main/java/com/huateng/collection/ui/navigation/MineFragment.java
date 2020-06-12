package com.huateng.collection.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.orm.UserLoginInfo;
import com.huateng.collection.ui.activity.AboutActivity;
import com.huateng.collection.ui.activity.ChangePasswordActivity;
import com.huateng.collection.ui.activity.LoginActivity;
import com.huateng.collection.ui.dialog.AlertDialogFragment;
import com.huateng.collection.ui.dialog.AlertFragmentUtil;
import com.huateng.collection.utils.DataCleanManager;
import com.huateng.collection.utils.OrmHelper;
import com.huateng.network.RxSchedulers;
import com.orm.SugarRecord;
import com.tools.ActivityUtils;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * author: yichuan
 * Created on: 2020-04-02 16:14
 * description:
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_header)
    CircleImageView mIvHeader;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.ll_clear_cache)
    LinearLayout mLlClearCache;
    @BindView(R.id.ll_change_account)
    LinearLayout mLlChangeAccount;
    @BindView(R.id.ll_update_version)
    LinearLayout mLlUpdateVersion;
    @BindView(R.id.ll_chage_password)
    LinearLayout mLlChagePassword;
    @BindView(R.id.ll_about)
    LinearLayout mLlAbout;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        UserLoginInfo loginInfo = OrmHelper.getLastLoginUserInfo();
        mTvName.setText(String.format("%s(%s)",loginInfo.getNickName(),loginInfo.getUserId()));


    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    @OnClick({R.id.iv_header, R.id.ll_clear_cache, R.id.ll_change_account, R.id.ll_update_version, R.id.ll_chage_password, R.id.ll_about})
    public void onClick(View view) {
        switch (view.getId()) {
            //
            case R.id.iv_header:
                //  startActivity(new Intent(mContext, LoginActivity.class));
                break;
            case R.id.ll_clear_cache:
                //缓存清理
                String length = "";
                try {
                    length = DataCleanManager.getTotalCacheSize(mContext);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                AlertFragmentUtil.showAlertDialog(getActivity(), "您确认要清除" + length + "数据吗(清除后应用正常退出)？", new AlertDialogFragment.OnDialogButtonClickListener() {
                    @Override
                    public void onClickLeft() {

                    }

                    @Override
                    public void onClickRight() {
                        clearData();

                    }
                });

                break;
            case R.id.ll_change_account:
                //账号切换
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra(Constants.IS_FIRST, true);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.ll_update_version:
                //检查版本更新
                RxToast.showToast("已经是最新版本了");
                break;
            case R.id.ll_chage_password:
                //修改密码
                if (Perference.getBoolean(Perference.IS_LOGIN)) {
                    Intent intent1 = new Intent(mContext,ChangePasswordActivity.class);
                    intent1.putExtra("tlrNo",Perference.getUserId());
                    startActivity(intent1);
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.ll_about:
                //关于我们
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
        }
    }

    /**
     * 情况缓存
     */
    private void clearData() {
        showLoading();
        Observable.create(new ObservableOnSubscribe<Integer>() { // 第一步：初始化Observable
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                SugarRecord.deleteAll(UserLoginInfo.class);
                DataCleanManager.clearAllCache(getActivity());
                Perference.clear();
                e.onNext(1);
                e.onComplete();
            }
        })
                .compose(getRxlifecycle())
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        hideLoading();
                        ActivityUtils.getAppManager().finishAllActivity();
                        System.exit(0);// 退出整个程序的方法
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoading();
                    }
                });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
       // Log.e("nb", bean.code + ":" + bean.getObject());
        if (bean == null) {
            return;
        }
        if (bean.getCode() == BusEvent.LOGIN_SUCESS) {
            UserLoginInfo info = (UserLoginInfo) bean.getObject();
            if (info != null) {
                mTvName.setText(info.getNickName());
            }
        }


    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }
}
