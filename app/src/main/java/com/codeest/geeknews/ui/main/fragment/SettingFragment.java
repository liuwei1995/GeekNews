package com.codeest.geeknews.ui.main.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeest.geeknews.R;
import com.codeest.geeknews.app.Constants;
import com.codeest.geeknews.base.SimpleFragment;
import com.codeest.geeknews.component.ACache;
import com.codeest.geeknews.component.RxBus;
import com.codeest.geeknews.model.bean.NightModeEvent;
import com.codeest.geeknews.util.LogUtil;
import com.codeest.geeknews.util.ShareUtil;
import com.codeest.geeknews.util.SharedPreferenceUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by codeest on 16/8/23.
 */

public class SettingFragment extends SimpleFragment implements CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.cb_setting_cache)
    AppCompatCheckBox cbSettingCache;
    @BindView(R.id.cb_setting_image)
    AppCompatCheckBox cbSettingImage;
    @BindView(R.id.cb_setting_night)
    AppCompatCheckBox cbSettingNight;
    @BindView(R.id.ll_setting_feedback)
    LinearLayout llSettingFeedback;
    @BindView(R.id.tv_setting_clear)
    TextView tvSettingClear;
    @BindView(R.id.ll_setting_clear)
    LinearLayout llSettingClear;
    @BindView(R.id.tv_setting_update)
    TextView tvSettingUpdate;
    @BindView(R.id.ll_setting_update)
    LinearLayout llSettingUpdate;

    File cacheFile;
    boolean isNull = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initEventAndData() {
        cacheFile = new File(Constants.PATH_CACHE);
        tvSettingClear.setText(ACache.getCacheSize(cacheFile));
        cbSettingCache.setChecked(SharedPreferenceUtil.getAutoCacheState(mContext));
        cbSettingImage.setChecked(SharedPreferenceUtil.getNoImageState(mContext));
        cbSettingNight.setChecked(SharedPreferenceUtil.getNightModeState(mContext));
        cbSettingCache.setOnCheckedChangeListener(this);
        cbSettingImage.setOnCheckedChangeListener(this);
        cbSettingNight.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isNull = savedInstanceState == null;
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.ll_setting_feedback)
    void doFeedBack() {
        ShareUtil.sendEmail(mContext, "选择邮件客户端:");
    }

    @OnClick(R.id.ll_setting_clear)
    void doClear() {
        ACache.deleteDir(cacheFile);
        tvSettingClear.setText(ACache.getCacheSize(cacheFile));
    }

    @OnClick(R.id.ll_setting_update)
    void doUpdate() {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_setting_night:
                if (isNull) {   //防止夜间模式MainActivity执行reCreate后重复调用
                    NightModeEvent event = new NightModeEvent();
                    event.setNightMode(b);
                    RxBus.getDefault().post(event);
                    SharedPreferenceUtil.setNightModeState(mContext,b);
                }
                break;
            case R.id.cb_setting_image:
                SharedPreferenceUtil.setNoImageState(mContext,b);
                break;
            case R.id.cb_setting_cache:
                SharedPreferenceUtil.setAutoCacheState(mContext,b);
                break;
        }
    }
}
