package com.huateng.fm.app;
import com.huateng.flowMobile.R;
import com.huateng.fm.ui.toolbar.ToolbarHelper;

/**  
 * @author Devin
 * @date 2016年1月11日 下午6:23:31 
 * @Description
 * @version 
*/
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by moon.zhong on 2015/6/12.
 * time : 10:26
 */
public abstract class FmToolBarActivity extends AppCompatActivity {
    private ToolbarHelper mToolBarHelper ;
    public Toolbar toolbar ;
    private LinearLayout rootLayout;
    private boolean mFitSysTint=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v= LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        if (blockFitSysWindowTint()) {
			v.setFitsSystemWindows(false);
		}else {
			v.setFitsSystemWindows(true);

		}
        super.setContentView(v);

    }
    
    public abstract boolean blockFitSysWindowTint();
    
    

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar=toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initToolbar();
    }
    
//    @Override
//    public void setContentView(int layoutResID) {
//
//        mToolBarHelper = new ToolbarHelper(this,layoutResID) ;
//        toolbar = mToolBarHelper.getToolBar() ;
//        setContentView(mToolBarHelper.getContentView());
//        /*把 toolbar 设置到Activity 中*/
//        setSupportActionBar(toolbar);
//        /*自定义的一些操作*/
////        onCreateCustomToolBar(toolbar) ;
//    }

    public void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.setContentInsetsRelative(0,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}
