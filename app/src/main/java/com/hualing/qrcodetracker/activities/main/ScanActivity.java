package com.hualing.qrcodetracker.activities.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.BCPInDataInputActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.BCPInRKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_out.BCPCKDInputActivity;
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
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.CheckExistParam;
import com.hualing.qrcodetracker.bean.DataBean;
import com.hualing.qrcodetracker.bean.DataResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.CPType;
import com.hualing.qrcodetracker.model.FunctionType;
import com.hualing.qrcodetracker.model.TrackType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.hualing.qrcodetracker.model.TrackType.END_INDEX;
import static com.hualing.qrcodetracker.model.TrackType.START_INDEX;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.zxingview)
    ZXingView mZxingview;
    private MainDao mainDao;

    //授权请求码
    private static final int MY_PERMISSIONS_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);

        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(ScanActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        //6.0以上先授权
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission4Version6Up();
        }

        mZxingview.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //已打开权限
        startScan();
    }

    private void startScan() {
        mZxingview.startCamera();
        //        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mZxingview.showScanRect();
        mZxingview.startSpot();
    }

    @Override
    protected void onStop() {
        mZxingview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZxingview.onDestroy();
        super.onDestroy();
    }

    /**
     * 6.0以上版本权限授权
     */
    private void checkPermission4Version6Up() {
        //检测权限授权（针对6.0以上先安装后检查权限的情况）
        List<String> permissionsList = new ArrayList<>();
        String[] permissions = null;
        //        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        //            permissionsList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.CAMERA);
        }
        if (permissionsList.size() != 0) {
            permissions = new String[permissionsList.size()];
            for (int i = 0; i < permissionsList.size(); i++) {
                permissions[i] = permissionsList.get(i);
            }
            //此句调起权限授权框
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * 授权结束后回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Permission Denied
                    new AlertDialog.Builder(this)
                            .setMessage("您已拒绝了授权申请，无法使用扫码功能，是否去系统设置中打开权限?")
                            .setPositiveButton("去打开", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent localIntent = new Intent();
                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                    } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
                                        localIntent.setAction(Intent.ACTION_VIEW);
                                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                        localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                                    }
                                    startActivity(localIntent);
                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭此页，无法使用扫描功能
                            AllActivitiesHolder.removeAct(ScanActivity.this);
                        }
                    }).create().show();
                    //                    Toast.makeText(TheApplication.getContext(), "您已拒绝了授权申请，无法使用扫码功能，请通过授权或者手动在系统设置中打开权限", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //授权通过开始扫描(异步问题导致onStart里面的启动扫描不管用,所以需要再次开启)
            startScan();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan;
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        //Log.e("Scan===============", "result:" + result);
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        vibrate();
        //        mZxingview.startSpot();
        //获取到二维码id
        boolean isFirst = getIntent().getBooleanExtra("isFirst",false);
        Intent intent = null;
        switch (GlobalData.currentFunctionType) {
            case FunctionType.MATERIAL_IN:
            case FunctionType.HALF_PRODUCT_IN:
            case FunctionType.PRODUCT_IN:
            case FunctionType.PRODUCT_OUT:
                checkExistByQrCodeId(result);//这几个条件下是对原料进行入库、出库、退库等操作，必须验证下是否已入出退库，已经操作过的直接显示信息就行
              break;
            case FunctionType.MATERIAL_OUT:
                if(isFirst)
                    intent = new Intent(this, WLCKDInputActivity.class);
                else
                    intent = new Intent(this, MaterialOutDataInputActivity.class);
                break;
            case FunctionType.MATERIAL_RETURN:
                if(isFirst)
                    intent = new Intent(this, WLTKDInputActivity.class);
                else
                    intent = new Intent(this, MaterialTKDataInputActivity.class);
                break;
            case FunctionType.MATERIAL_THROW:
                intent = new Intent(this, MaterialThrowActivity.class);
                break;
            case FunctionType.HALF_PRODUCT_RETURN:
                if(isFirst)
                    intent = new Intent(this, BcpTKDInputActivity.class);
                else
                    intent = new Intent(this, BcpTKDataInputActivity.class);
                break;
            case FunctionType.HALF_PRODUCT_OUT:
                if(isFirst)
                    intent = new Intent(this, BCPCKDInputActivity.class);
                else
                    intent = new Intent(this, BcpTKDataInputActivity.class);
                    break;
            case FunctionType.HALF_PRODUCT_THROW:
                intent = new Intent(this, BcpThrowActivity.class);
                break;
            case FunctionType.DATA_TRACK:
                dataTrackByQrCodeId(result);
                break;
        }

        switch (GlobalData.currentFunctionType) {
            case FunctionType.MATERIAL_OUT:
            case FunctionType.MATERIAL_RETURN:
            case FunctionType.MATERIAL_THROW:
            case FunctionType.HALF_PRODUCT_RETURN:
            case FunctionType.HALF_PRODUCT_OUT:
            case FunctionType.HALF_PRODUCT_THROW:
                SharedPreferenceUtil.setQrCodeId(result);
                startActivity(intent);
                AllActivitiesHolder.removeAct(this);
                break;
        }

        /**
        //获取到二维码id
        boolean isFirst = getIntent().getBooleanExtra("isFirst",false);
        Intent intent = null;
        switch (GlobalData.currentFunctionType) {
            case FunctionType.MATERIAL_IN:
                //intent = new Intent(this, MaterialInDataInputActivity.class);
                if (isFirst)
                    intent = new Intent(this, WLInRKDInputActivity.class);
                else
                    intent = new Intent(this, MaterialInDataInputActivity.class);
                break;
            case FunctionType.MATERIAL_OUT:
                if(isFirst)
                    intent = new Intent(this, WLCKDInputActivity.class);
                else
                    intent = new Intent(this, MaterialOutDataInputActivity.class);
                break;
            case FunctionType.MATERIAL_THROW:
                intent = new Intent(this, MaterialThrowActivity.class);
                break;
            case FunctionType.MATERIAL_RETURN:
                if(isFirst)
                    intent = new Intent(this, WLTKDInputActivity.class);
                else
                    intent = new Intent(this, MaterialTKDataInputActivity.class);
                break;
            case FunctionType.HALF_PRODUCT_IN:
                if(isFirst)
                    intent = new Intent(this, BCPInRKDInputActivity.class);
                else
                    intent = new Intent(this, BCPInDataInputActivity.class);
                break;
            case FunctionType.HALF_PRODUCT_THROW:
                intent = new Intent(this, BcpThrowActivity.class);
                break;
            case FunctionType.HALF_PRODUCT_RETURN:
                if(isFirst)
                    intent = new Intent(this, BcpTKDInputActivity.class);
                else
                    intent = new Intent(this, BcpTKDataInputActivity.class);
                break;
            case FunctionType.PRODUCT_IN:
                    switch (GlobalData.currentCPInType) {
                        case CPType.BIG_CP_IN:
                            if(isFirst)
                                intent = new Intent(this, CPRKDInputActivity.class);
                            else{
                                intent = new Intent(this, BigCPInDataInputActivity.class);
                            }
                            break;
                        case CPType.SMALL_CP_IN:
                            intent = new Intent(this, SmallCPInDataInputActivity.class);
                            break;
                    }
                break;
            case FunctionType.PRODUCT_OUT:
                if(isFirst)
                    intent = new Intent(this, CPCKDInputActivity.class);
                else {
                    switch (GlobalData.currentCPInType) {
                        case CPType.BIG_CP_OUT:
                            intent = new Intent(this, BigCPOutDataInputActivity.class);
                            break;
                        case CPType.SMALL_CP_OUT:
                            intent = new Intent(this, SmallCPOutDataInputActivity.class);
                            break;
                    }
                }
                break;
            case FunctionType.DATA_TRACK:
                //二维码信息包含着扫描的属于物料、半成品、小包装还是大包装
                String sort = result.substring(START_INDEX, END_INDEX);

                //测试
                //                String sort = "4";

                switch (sort) {
                    case TrackType.WL:
                        intent = new Intent(this, WlDataTrackActivity.class);
                        break;
                    case TrackType.BCP:
                        intent = new Intent(this, BcpDataTrackActivity.class);
                        break;
                    case TrackType.SMALL_CP:
                        intent = new Intent(this, SmallCpDataTrackActivity.class);
                        break;
                    case TrackType.BIG_CP:
                        intent = new Intent(this, BigCpDataTrackActivity.class);
                        break;
                }

                break;

                /*
            case FunctionType.QUALITY_CHECKING:
                intent = new Intent(this, QualityCheckActivity.class);
                break;
                */
        /**
        }
        //intent.putExtra("qrCodeId", result);
        SharedPreferenceUtil.setQrCodeId(result);
        startActivity(intent);
        AllActivitiesHolder.removeAct(this);
        **/

    }

    /**
     * 根据二维码验证原料是否已存在
     * @param qrCodeId
     */
    private void checkExistByQrCodeId(final String qrCodeId){
        final boolean[] exist = new boolean[1];
        final CheckExistParam param = new CheckExistParam();
        param.setQrCodeId(qrCodeId);
        param.setCurrentFunctionType(GlobalData.currentFunctionType);
        //Log.e("currentFunctionType===",""+GlobalData.currentFunctionType);
        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.checkExistByQrCodeId(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<ActionResult>>() {
                    @Override
                    public void accept(ActionResult<ActionResult> result) throws Exception {
                        Log.e("Code======",""+result.getCode());
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            exist[0] = true;
                            //Log.e("11111111","11111111");
                        } else {
                            exist[0] = false;
                        }
                        Message message = new Message();
                        message.what = GlobalData.currentFunctionType;
                        JSONObject jo=new JSONObject();
                        jo.put("exist",exist[0]);
                        jo.put("qrCodeId",qrCodeId);
                        message.obj=jo;
                        myHandler.sendMessage(message);
                    }
                });
    }

    //这里是验证原料是否存在后异步执行的方法，要返回是否结果，在调用异步请求的接口获取数据后才回调返回结果的
    Handler myHandler = new Handler(){
        public void handleMessage(Message msg) {
            //获取到二维码id
            boolean isFirst = getIntent().getBooleanExtra("isFirst",false);
            Intent intent = null;
            Log.e("obj===",""+msg.obj);
            JSONObject jo=(JSONObject)msg.obj;
            boolean exist = false;
            String qrCodeId = null;
            try {
                exist = jo.getBoolean("exist");
                qrCodeId = jo.getString("qrCodeId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(exist){
                dataTrackByQrCodeId(qrCodeId);//原料存在的话，就直接追溯显示信息
            } else {//原料不存在的话，就执行入出退库等操作
                switch (msg.what) {
                    case FunctionType.MATERIAL_IN:
                        //intent = new Intent(this, MaterialInDataInputActivity.class);
                        if (isFirst)
                            intent = new Intent(ScanActivity.this, WLInRKDInputActivity.class);
                        else
                            intent = new Intent(ScanActivity.this, MaterialInDataInputActivity.class);
                        break;
                    case FunctionType.HALF_PRODUCT_IN:
                        if(isFirst)
                            intent = new Intent(ScanActivity.this, BCPInRKDInputActivity.class);
                        else
                            intent = new Intent(ScanActivity.this, BCPInDataInputActivity.class);
                        break;
                    case FunctionType.PRODUCT_IN:
                        switch (GlobalData.currentCPInType) {
                            case CPType.BIG_CP_IN:
                                if(isFirst)
                                    intent = new Intent(ScanActivity.this, CPRKDInputActivity.class);
                                else{
                                    intent = new Intent(ScanActivity.this, BigCPInDataInputActivity.class);
                                }
                                break;
                            case CPType.SMALL_CP_IN:
                                intent = new Intent(ScanActivity.this, SmallCPInDataInputActivity.class);
                                break;
                        }
                        break;
                    case FunctionType.PRODUCT_OUT:
                        if(isFirst)
                            intent = new Intent(ScanActivity.this, CPCKDInputActivity.class);
                        else {
                            switch (GlobalData.currentCPInType) {
                                case CPType.BIG_CP_OUT:
                                    intent = new Intent(ScanActivity.this, BigCPOutDataInputActivity.class);
                                    break;
                                case CPType.SMALL_CP_OUT:
                                    intent = new Intent(ScanActivity.this, SmallCPOutDataInputActivity.class);
                                    break;
                            }
                        }
                        break;
                }
                SharedPreferenceUtil.setQrCodeId(qrCodeId);
                startActivity(intent);
                AllActivitiesHolder.removeAct(ScanActivity.this);
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 根据二维码追溯对应的原料
     * @param qrCodeId
     */
    private void dataTrackByQrCodeId(String qrCodeId){
        //二维码信息包含着扫描的属于物料、半成品、小包装还是大包装
        String sort = qrCodeId.substring(START_INDEX, END_INDEX);

        //测试
        //                String sort = "4";

        Intent intent = null;
        switch (sort) {
            case TrackType.WL:
                intent = new Intent(this, WlDataTrackActivity.class);
                break;
            case TrackType.BCP:
                intent = new Intent(this, BcpDataTrackActivity.class);
                break;
            case TrackType.SMALL_CP:
                intent = new Intent(this, SmallCpDataTrackActivity.class);
                break;
            case TrackType.BIG_CP:
                intent = new Intent(this, BigCpDataTrackActivity.class);
                break;
        }
        SharedPreferenceUtil.setQrCodeId(qrCodeId);
        startActivity(intent);
        AllActivitiesHolder.removeAct(this);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("Scan", "打开相机出错");
    }
}
