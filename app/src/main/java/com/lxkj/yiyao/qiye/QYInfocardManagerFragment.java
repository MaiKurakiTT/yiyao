package com.lxkj.yiyao.qiye;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lxkj.yiyao.R;
import com.lxkj.yiyao.base.BaseFragment;
import com.lxkj.yiyao.global.GlobalString;
import com.lxkj.yiyao.jianguan.adapter.MBaseAdapter;
import com.lxkj.yiyao.qiye.adapter.QiyeInfoCardManagerAdapter;
import com.lxkj.yiyao.view.DoubleDatePickerDialog;
import com.lxkj.yiyao.view.RefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/19.
 */

public class QYInfocardManagerFragment extends BaseFragment {
    private static final String TAG = "QYInfocardManagerF";

    // ======================== 模板代码=============================

    MBaseAdapter adapter;
    @BindView(R.id.list_view)
    RefreshListView listView;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.content)
    EditText content;
    private int page = 1;


    @Override
    protected void initView() {


        requestData("","","");


        listView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {


                adapter.clear();
                adapter.notifyDataSetChanged();
                page = 1;


                requestData("","","");


            }

            @Override
            public void onLoadingMore() {


                requestData("","","");


            }
        });


    }
    // ======================== 模板代码=============================


    // ======================== 模板代码=============================
    public void requestData(String sousuo,String star_time,String end_time) {
        RequestParams params = new RequestParams(GlobalString.BaseURL + GlobalString.qiye_xxk);
        params.addBodyParameter("page", page + "");

        SharedPreferences sp = getActivity().getSharedPreferences("shiyao", getActivity().MODE_PRIVATE);
        params.addBodyParameter("username",sp.getString("username","") + "");

        params.addBodyParameter("xx", sousuo);

        params.addBodyParameter("sj1", star_time);
        params.addBodyParameter("sj2", end_time);


        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, result);
                if (adapter == null) {
                    adapter = new QiyeInfoCardManagerAdapter(result);
                    listView.setAdapter(adapter);
                } else {
                    listView.setAdapter(adapter);
                    adapter.addData(result);
                    listView.deferNotifyDataSetChanged();
                }
                page++;

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                listView.onRefreshComplete();
                listView.loadMoreComplete();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


    // ======================== 模板代码=============================

    @Override
    public int getLayout() {
        return R.layout.qy_fragment_layout_infocard_manage_search;
    }


    @OnClick({R.id.start_time, R.id.end_time, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select:
                adapter.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                requestData(content.getText().toString(),startTime.getText().toString(),endTime.getText().toString());

                break;


            case R.id.start_time:
                startTime.setOnClickListener(new View.OnClickListener() {
                    Calendar c = Calendar.getInstance();

                    @Override
                    public void onClick(View view) {
                        // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                        new DoubleDatePickerDialog(getContext(), 0, new DoubleDatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                                  int startDayOfMonth) {
                                String textString = String.format("%d-%d-%d", startYear,
                                        startMonthOfYear + 1, startDayOfMonth);
                                startTime.setText(textString);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
                    }
                });
                break;
            case R.id.end_time:
                endTime.setOnClickListener(new View.OnClickListener() {
                    Calendar c = Calendar.getInstance();

                    @Override
                    public void onClick(View view) {
                        // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                        new DoubleDatePickerDialog(getContext(), 0, new DoubleDatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                                  int startDayOfMonth) {
                                String textString = String.format("%d-%d-%d", startYear,
                                        startMonthOfYear + 1, startDayOfMonth);
                                endTime.setText(textString);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
                    }
                });
                break;
        }
    }

}
