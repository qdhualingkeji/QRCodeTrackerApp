package com.hualing.qrcodetracker.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.BCPInDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.BCPInRKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_return.BcpTKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_return.BcpTKDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_tl.BcpThrowActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_in.BigCPInDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_in.CPRKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_in.SmallCPInDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_out.BigCPOutDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_out.CPCKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_out.SmallCPOutDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_track.BcpDataTrackActivity;
import com.hualing.qrcodetracker.activities.operation_track.BigCpDataTrackActivity;
import com.hualing.qrcodetracker.activities.operation_track.SmallCpDataTrackActivity;
import com.hualing.qrcodetracker.activities.operation_track.WlDataTrackActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.MaterialInDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.WLInRKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_out.MaterialOutDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_out.WLCKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_return.MaterialTKDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_return.WLTKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_tl.MaterialThrowActivity;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.CPType;
import com.hualing.qrcodetracker.model.FunctionType;
import com.hualing.qrcodetracker.model.TrackType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hualing.qrcodetracker.model.TrackType.END_INDEX;
import static com.hualing.qrcodetracker.model.TrackType.START_INDEX;

public class ScanHWActivity extends BaseActivity {

    @BindView(R.id.scanHW)
    Button scanHW;
    private ScanBroadcastReceiver scanBroadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册接扫描结果收消息广播
        if(scanBroadcastReceiver==null) {
            scanBroadcastReceiver = new ScanBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.zkc.scancode");
            this.registerReceiver(scanBroadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void initLogic() {

    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan_hw;
    }

    @OnClick({R.id.scanHW})
    public void onViewClicked(View view){

        switch (view.getId()) {
            case R.id.scanHW:
                //app发送按键广播消息方式
                Intent intentBroadcast = new Intent();
                intentBroadcast.setAction("com.zkc.keycode");
                intentBroadcast.putExtra("keyvalue", 136);
                sendBroadcast(intentBroadcast);
                break;
        }
    }

    /**
     * 扫描结果广播
     */
    class ScanBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String decodeResult = intent.getExtras().getString("code");
            String keyStr = "";
            if(decodeResult.contains("{")&&decodeResult.contains("}")) {
                int strStart = decodeResult.lastIndexOf("{");
                int strEnd = decodeResult.lastIndexOf("}");
                //check keycode
                if (strStart > -1 && strEnd > -1 && strEnd - strStart < 5) {
                    keyStr = decodeResult.substring(strStart + 1, strEnd);
                    decodeResult = decodeResult.substring(0, strStart);
                }
            }
            //Log.i("ScanBroadcastReceiver", "ScanBroadcastReceiver code:" + decodeResult);
            //Toast.makeText(TheApplication.getContext(), decodeResult, Toast.LENGTH_LONG).show();

            //获取到二维码id
            boolean isFirst = getIntent().getBooleanExtra("isFirst",false);
            Intent intent1 = null;
            switch (GlobalData.currentFunctionType) {
                case FunctionType.MATERIAL_IN:
                    if(isFirst)
                        intent1 = new Intent(ScanHWActivity.this, WLInRKDInputActivity.class);
                    else
                        intent1 = new Intent(ScanHWActivity.this, MaterialInDataInputActivity.class);
                    break;
                case FunctionType.MATERIAL_OUT:
                    if(isFirst)
                        intent1 = new Intent(ScanHWActivity.this, WLCKDInputActivity.class);
                    else
                        intent1 = new Intent(ScanHWActivity.this, MaterialOutDataInputActivity.class);
                    break;
                case FunctionType.MATERIAL_THROW:
                    intent1 = new Intent(ScanHWActivity.this, MaterialThrowActivity.class);
                    break;
                case FunctionType.MATERIAL_RETURN:
                    if(isFirst)
                        intent1 = new Intent(ScanHWActivity.this, WLTKDInputActivity.class);
                    else
                        intent1 = new Intent(ScanHWActivity.this, MaterialTKDataInputActivity.class);
                    break;
                case FunctionType.HALF_PRODUCT_IN:
                    if(isFirst)
                        intent1 = new Intent(ScanHWActivity.this, BCPInRKDInputActivity.class);
                    else
                        intent1 = new Intent(ScanHWActivity.this, BCPInDataInputActivity.class);
                    break;
                case FunctionType.HALF_PRODUCT_THROW:
                    intent1 = new Intent(ScanHWActivity.this, BcpThrowActivity.class);
                    break;
                case FunctionType.HALF_PRODUCT_RETURN:
                    if(isFirst)
                        intent1 = new Intent(ScanHWActivity.this, BcpTKDInputActivity.class);
                    else
                        intent1 = new Intent(ScanHWActivity.this, BcpTKDataInputActivity.class);
                    break;
                case FunctionType.PRODUCT_IN:
                    if(isFirst)
                        intent1 = new Intent(ScanHWActivity.this, CPRKDInputActivity.class);
                    else{
                        switch (GlobalData.currentCPInType) {
                            case CPType.BIG_CP_IN:
                                intent1 = new Intent(ScanHWActivity.this, BigCPInDataInputActivity.class);
                                break;
                            case CPType.SMALL_CP_IN:
                                intent1 = new Intent(ScanHWActivity.this, SmallCPInDataInputActivity.class);
                                break;
                        }
                    }
                    break;
                case FunctionType.PRODUCT_OUT:
                    if(isFirst)
                        intent1 = new Intent(ScanHWActivity.this, CPCKDInputActivity.class);
                    else {
                        switch (GlobalData.currentCPInType) {
                            case CPType.BIG_CP_OUT:
                                intent1 = new Intent(ScanHWActivity.this, BigCPOutDataInputActivity.class);
                                break;
                            case CPType.SMALL_CP_OUT:
                                intent1 = new Intent(ScanHWActivity.this, SmallCPOutDataInputActivity.class);
                                break;
                        }
                    }
                    break;
                case FunctionType.DATA_TRACK:
                    //二维码信息包含着扫描的属于物料、半成品、小包装还是大包装
                    String sort = decodeResult.substring(START_INDEX, END_INDEX);

                    //测试
                    //                String sort = "4";

                    switch (sort) {
                        case TrackType.WL:
                            intent1 = new Intent(ScanHWActivity.this, WlDataTrackActivity.class);
                            break;
                        case TrackType.BCP:
                            intent1 = new Intent(ScanHWActivity.this, BcpDataTrackActivity.class);
                            break;
                        case TrackType.SMALL_CP:
                            intent1 = new Intent(ScanHWActivity.this, SmallCpDataTrackActivity.class);
                            break;
                        case TrackType.BIG_CP:
                            intent1 = new Intent(ScanHWActivity.this, BigCpDataTrackActivity.class);
                            break;
                    }

                    break;
                /*
            case FunctionType.QUALITY_CHECKING:
                intent1 = new Intent(this, QualityCheckActivity.class);
                break;
                */


            }
            SharedPreferenceUtil.setQrCodeId(decodeResult);
            startActivity(intent1);
            AllActivitiesHolder.removeAct(ScanHWActivity.this);
        }
    }

}
