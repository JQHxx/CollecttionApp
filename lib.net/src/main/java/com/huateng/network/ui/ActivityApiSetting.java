package com.huateng.network.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.network.ApiConstants;
import com.huateng.network.NetworkConfig;
import com.huateng.network.R;
import com.huateng.network.RetrofitManager;
import com.orhanobut.logger.Logger;
import com.tools.CommonUtils;
import com.tools.utils.RegexUtils;
import com.tools.utils.StringUtils;
import com.tools.view.FmAutoCompleteTextView;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.tools.view.spinner.NiceSpinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by shanyong on 2017/2/7.
 * 测试使用 切换ip
 */
public class ActivityApiSetting extends AppCompatActivity {

    RxTitle rxTitle;
    NiceSpinner spAppMode;
    FmAutoCompleteTextView actvHost;
    LinearLayout layoutIpPortRoot;
    FmAutoCompleteTextView actvPort;
    FmAutoCompleteTextView actvRoot;
    FmAutoCompleteTextView tvHostname;
    private SharedPreferences sp;
    private String app_mode;

    List<String> hostList;
    List<String> portList;
    List<String> rootList;

    static final String HOST = "host";
    static final String PORT = "port";
    static final String ROOT = "root";
    static final String LAST_HOST = "last_host";
    static final String LAST_PORT = "last_port";
    static final String LAST_ROOT = "last_root";
    static final String HOST_PORT_ROOT = "host_port_root";
    public static final String SP_KEY_HOST_PORT = "host_port";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_setting);
        initView();
        init();
    }

    private void initView() {
        rxTitle = (RxTitle) this.findViewById(R.id.rx_title);

        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, rxTitle);

        spAppMode = (NiceSpinner) this.findViewById(R.id.sp_appMode);
        actvHost = (FmAutoCompleteTextView) this.findViewById(R.id.actv_host);
        actvPort = (FmAutoCompleteTextView) this.findViewById(R.id.actv_port);
        actvRoot = (FmAutoCompleteTextView) this.findViewById(R.id.actv_root);
        tvHostname = (FmAutoCompleteTextView) this.findViewById(R.id.tv_hostname);
        layoutIpPortRoot = (LinearLayout) this.findViewById(R.id.layout_IpPortRoot);

    }

    private void init() {
        sp = getSharedPreferences(SP_KEY_HOST_PORT, Context.MODE_PRIVATE);

        rxTitle.setLeftFinish(this);
        rxTitle.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host_port_root = null;

                if (app_mode.equals(ApiConstants.API_MODE_DEVELOP)) {
                    String host = actvHost.getText().toString();
                    String port = actvPort.getText().toString();
                    String root = actvRoot.getText().toString();
                    if (StringUtils.isEmpty(host, port, root)) {
                        RxToast.showToast("当前模式下 : ip,port,root为必填项");
                        return;
                    }

                    SharedPreferences.Editor editor = sp.edit();
                    hostList.add(host);
                    portList.add(port);
                    rootList.add(root);
                    editor.putStringSet(HOST, new HashSet<String>(hostList));
                    editor.putStringSet(PORT, new HashSet<String>(portList));
                    editor.putStringSet(ROOT, new HashSet<String>(rootList));
                    editor.putString(LAST_HOST, host);
                    editor.putString(LAST_PORT, port);
                    editor.putString(LAST_ROOT, root);

                    host_port_root = String.format("http://%s:%s/%s/", host, port, root);
                    editor.putString(HOST_PORT_ROOT, host_port_root);
                    editor.apply();

                    ApiConstants.DEVELOP_BASE_URL = String.format("http://%s:%s/%s/", host, port, root);

                    NetworkConfig.C.setCustomHost(host);
                    NetworkConfig.C.setCustomPort(port);
                    NetworkConfig.C.setCustomRoot(root);
                    tvHostname.setText(host_port_root);
                } else if (app_mode.equals(ApiConstants.API_MODE_RELEASE)) {
                    tvHostname.setText(ApiConstants.RELEASE_BASE_URL);
                    NetworkConfig.C.setDomain(ApiConstants.DOMAIN);
                } else if (app_mode.equals(ApiConstants.API_MODE_CUSTOM)) {
                    String url = tvHostname.getText().toString();
                    boolean isUrl = RegexUtils.isURL(url);
                    if (isUrl) {
                        NetworkConfig.C.setCustomURL(url);
                        String host = CommonUtils.getUrlHost(url);
                        NetworkConfig.C.setDomain(host);
                        Logger.i(host);
                    } else {
                        RxToast.showToast("请输入正确url");
                    }
                }
                NetworkConfig.C.setApiMode(app_mode);
                RetrofitManager.clearInstance();
              //  RetrofitUtil.clearInstance();
                RxToast.showToast("保存成功");
            }
        });


        // 历史ip与端口
        Set<String> hostSet = sp.getStringSet(HOST, new HashSet<String>());
        final Set<String> portSet = sp.getStringSet(PORT, new HashSet<String>());
        Set<String> rootSet = sp.getStringSet(ROOT, new HashSet<String>());

        // 上一次的ip与端口
        String lastHost = sp.getString(LAST_HOST, null);
        String lastPort = sp.getString(LAST_PORT, null);
        String lastRoot = sp.getString(LAST_ROOT, null);

        hostList = new ArrayList<String>(hostSet);
        portList = new ArrayList<String>(portSet);
        rootList = new ArrayList<String>(rootSet);

        actvHost.setText(lastHost);
        actvPort.setText(lastPort);
        actvRoot.setText(lastRoot);
        actvHost.setDataSource(hostList);
        actvPort.setDataSource(portList);
        actvRoot.setDataSource(rootList);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.app_mode));
        spAppMode.setAdapter(adapter);
        spAppMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    tvHostname.setEnabled(false);
                    app_mode = ApiConstants.API_MODE_RELEASE;
                    layoutIpPortRoot.setVisibility(View.GONE);
                    tvHostname.setText(ApiConstants.RELEASE_BASE_URL);
                } else if (position == 1) {
                    app_mode = ApiConstants.API_MODE_DEVELOP;
                    layoutIpPortRoot.setVisibility(View.VISIBLE);
                    actvRoot.setText(NetworkConfig.C.getCustomRoot());
                    setHostPort();
                } else if (position == 2) {
                    app_mode = ApiConstants.API_MODE_CUSTOM;
                    tvHostname.setEnabled(true);
                    layoutIpPortRoot.setVisibility(View.GONE);
                    tvHostname.setText(NetworkConfig.C.getCustomURL());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String mode = NetworkConfig.C.getApiMode();
        app_mode = mode;
//        Log.i("mode", "--------->" + mode);
        if (mode.equals(ApiConstants.API_MODE_RELEASE)) {
            tvHostname.setEnabled(false);
            spAppMode.setSelectedIndex(0);
            layoutIpPortRoot.setVisibility(View.GONE);
            tvHostname.setText(ApiConstants.RELEASE_BASE_URL);
        }  else if (mode.equals(ApiConstants.API_MODE_DEVELOP)) {
            spAppMode.setSelectedIndex(1);
            layoutIpPortRoot.setVisibility(View.VISIBLE);
            setHostPort();
        } else if (mode.equals(ApiConstants.API_MODE_CUSTOM)) {
            tvHostname.setEnabled(true);
            spAppMode.setSelectedIndex(2);
            layoutIpPortRoot.setVisibility(View.GONE);
            tvHostname.setText(NetworkConfig.C.getCustomURL());
        }

    }

    public void setHostPort() {
        tvHostname.setEnabled(false);
        tvHostname.setText(sp.getString(HOST_PORT_ROOT, null));
        actvHost.setText(sp.getString(LAST_HOST, null));
        actvPort.setText(sp.getString(LAST_PORT, null));
    }


}
