package com.lxkj.yiyao.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxkj.yiyao.bean.RenYuanBean;
import com.lxkj.yiyao.view.ExpandListView;
import com.lxkj.yiyao.R;
import com.lxkj.yiyao.activity.adapter.XuanGouKeChengJiChuKeChengAdapter;
import com.lxkj.yiyao.activity.adapter.XuanGouKeChengQiYeRenYuanAdapter;
import com.lxkj.yiyao.base.BaseActivity;
import com.lxkj.yiyao.global.GlobalString;
import com.lxkj.yiyao.utils.SPUtil;
import com.lxkj.yiyao.utils.ToastUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sun on 2017/3/25.
 * 选购参加培训的人员
 */

public class XuanGouKeChengRenYuanActivity extends BaseActivity {
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.peixun_image)
    ImageView peixunImage;
    @BindView(R.id.peixun_name)
    TextView peixunName;
    @BindView(R.id.shiyingdiqu)
    TextView shiyingdiqu;
    @BindView(R.id.suoshuhangye)
    TextView suoshuhangye;
    @BindView(R.id.peixunshijian)
    TextView peixunshijian;
    @BindView(R.id.peixunbanleibie)
    TextView peixunbanleibie;
    @BindView(R.id.zhengshumingcheng)
    TextView zhengshumingcheng;
    @BindView(R.id.jieyetiaojian)
    TextView jieyetiaojian;
    @BindView(R.id.jieguo)
    TextView jieguo;
    @BindView(R.id.jieshushijian)
    TextView jieshushijian;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.jichukecheng_listview)
    ExpandListView jichukechengListview;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.qiyerenyuanleibiao_listview)
    ExpandListView qiyerenyuanleibiaoListview;
    @BindView(R.id.tijiao_but)
    TextView tijiaoBut;
    private String userName;
    private String pxid;
    private XuanGouKeChengQiYeRenYuanAdapter xuanGouKeChengQiYeRenYuanAdapter;

    @Override
    protected void init() {
        userName = SPUtil.getUserName(this);
        //从上一个界面传过来的培训id
        Intent intent = getIntent();
        pxid = intent.getStringExtra("pxid") + "";
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        requestData();
        requestPeixunban();
        requestJichukecheng();
        requestRenyuan();
    }

    private void commitData() {
        RequestParams requestParams = new RequestParams(GlobalString.xuangou_tijiao);
        requestParams.addBodyParameter("username", userName);
        requestParams.addBodyParameter("id", pxid);
        HashMap<Integer, Boolean> checkMap = xuanGouKeChengQiYeRenYuanAdapter.getCheckMap();
        int count = xuanGouKeChengQiYeRenYuanAdapter.getCount();
        JSONArray objects = xuanGouKeChengQiYeRenYuanAdapter.getObjects();
        List<RenYuanBean> renyuanList = new ArrayList<RenYuanBean>();
        for (int i = 0; i < count; i++){
            Boolean aBoolean = checkMap.get(i);
            if (aBoolean){
                int id = JSONObject.parseObject(objects.get(i).toString()).getIntValue("id");
                RenYuanBean renYuanBean = new RenYuanBean();
                renYuanBean.setId(id);
                renyuanList.add(renYuanBean);
            }
        }
        String json  = JSONArray.toJSONString(renyuanList);
        //String json = JSONObject.toJSONString(renyuanList);
        //username=gmy&id=1&data=[{"id":115}]
        requestParams.addBodyParameter("data", json);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)){
                    int code = JSONObject.parseObject(result).getIntValue("code");
                    if (code == 111111){
                        ToastUtil.show("提交成功");
                        //下单成功，开始付款，跳转付款界面
                        String data = JSONObject.parseObject(result).getString("data");
                        Intent intent = new Intent(XuanGouKeChengRenYuanActivity.this, YongHuYuEFuKuanActivity.class);
                        intent.putExtra("data", data);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 请求企业人员列表
     */
    private void requestRenyuan() {
        RequestParams requestParams = new RequestParams(GlobalString.xuangou_qiyerenyuan);
        requestParams.addBodyParameter("username", userName);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)){
                    String data = JSONObject.parseObject(result).getString("data");
                    setRenYuanData(data);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 请求基础课程名称列表
     */
    private void requestJichukecheng() {
        RequestParams requestParams = new RequestParams(GlobalString.xuangou_jichukecheng);
        requestParams.addBodyParameter("id", pxid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)){
                    String data = JSONObject.parseObject(JSONObject.parseObject(result).getString("data")).getString("data");
                    setKeChengData(data);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 请求培训班的信息
     */
    private void requestPeixunban() {
        RequestParams requestParams = new RequestParams(GlobalString.xuangou_peixunbanxinxi);
        requestParams.addBodyParameter("id", pxid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    JSONObject data = JSONObject.parseObject(jsonObject.getString("data"));
                    setPeiXunBanData(data);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void requestData() {

        if (!TextUtils.isEmpty(pxid)){
            RequestParams requestParams = new RequestParams(GlobalString.BaseURL + GlobalString.xuangoukechengrenyuanUrl);
            //临时用1
            requestParams.addBodyParameter("id", pxid);
            x.http().post(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (!TextUtils.isEmpty(result)){
                        JSONObject resultJsonObject = JSONObject.parseObject(result);
                        int code = (int) resultJsonObject.get("code");
                        if (code == 111111){
                            String peiXunBanResultString = resultJsonObject.getString("data");
                            if (!TextUtils.isEmpty(peiXunBanResultString)){
                                JSONObject peiXunBanParse = JSONObject.parseObject(peiXunBanResultString);
                                setPeiXunBanData(peiXunBanParse);
                            }
                            String keChengResultString = resultJsonObject.getString("datb");
                            if (!TextUtils.isEmpty(keChengResultString)){
                                JSONArray keChengParse = JSONArray.parseArray(keChengResultString);
//                                String jckcmc = JSONObject.parseObject(keChengParse.getString(0)).getString("jckcmc");
                                setKeChengData(keChengResultString);
                            }
                            String renYuanResultString = resultJsonObject.getString("datc");
                            if (!TextUtils.isEmpty(renYuanResultString)){
                                JSONArray renYuanParse = JSONArray.parseArray(keChengResultString);
                                setRenYuanData(renYuanResultString);
                            }
                        }else {
                            //请求失败
                            ToastUtil.show("加载失败");
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    /**
     * 填充培训班数据
     * @param peiXunBanParse
     */
    private void setPeiXunBanData(JSONObject peiXunBanParse) {
        //图片地址
        String tpdz = GlobalString.BaseURL + "uploads/" + peiXunBanParse.getString("tpdz") + "";
        x.image().bind(peixunImage, tpdz);
        //标题
        String bt = peiXunBanParse.getString("bt") + "";
        peixunName.setText("" + bt);
        //适应地区
        String sydq = peiXunBanParse.getString("sydq") + "";
        shiyingdiqu.setText("" + sydq);
        //所属行业
        String sshy = peiXunBanParse.getString("sshy") + "";
        suoshuhangye.setText("" + sshy);
        //培训时间开始
        String pxsj = peiXunBanParse.getString("pxsj") + "";
        //培训时间结束
        String pxsj1 = peiXunBanParse.getString("pxsj1") + "";
        peixunshijian.setText("" + pxsj + " 到 " + pxsj1);
        //培训班类别
        String pxblb = peiXunBanParse.getString("pxblb") + "";
        peixunbanleibie.setText("" + pxblb);
        //证书名称
        String zsmc = peiXunBanParse.getString("zsmc") + "";
        zhengshumingcheng.setText("" + zsmc);
        //结业条件
        String jytj = peiXunBanParse.getString("jytj") + "";
        jieyetiaojian.setText("" + jytj);
        //提示
//        String ts = peiXunBanParse.getString("ts") + "";
        jieguo.setVisibility(View.GONE);
        //结业考试时间限制开始
        String jysj = peiXunBanParse.getString("jysj") + "";
        //结业考试时间限制结束
        String jysj1 = peiXunBanParse.get("jysj1") + "";
        jieshushijian.setText("" + jysj + " 到 " + jysj1);
        //关联字段
//        String pxid = peiXunBanParse.getString("pxid") + "";


    }

    /**
     * 填充课程列表数据
     * @param keChengParse
     */
    private void setKeChengData(String keChengParse) {
        XuanGouKeChengJiChuKeChengAdapter xuanGouKeChengJiChuKeChengAdapter = new XuanGouKeChengJiChuKeChengAdapter(keChengParse);
        jichukechengListview.setAdapter(xuanGouKeChengJiChuKeChengAdapter);
    }

    /**
     * 填充人员列表数据
     * @param renYuanParse
     */
    private void setRenYuanData(String renYuanParse) {
        xuanGouKeChengQiYeRenYuanAdapter = new XuanGouKeChengQiYeRenYuanAdapter(renYuanParse);
        qiyerenyuanleibiaoListview.setAdapter(xuanGouKeChengQiYeRenYuanAdapter);
        tijiaoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitData();
            }
        });
    }


    @Override
    public int getLayout() {
        return R.layout.xuangou_kecheng_yibaopeixun;
    }



}
