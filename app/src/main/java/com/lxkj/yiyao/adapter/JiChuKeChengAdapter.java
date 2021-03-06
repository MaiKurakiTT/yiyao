package com.lxkj.yiyao.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxkj.yiyao.R;
import com.lxkj.yiyao.activity.ExamActivity;
import com.lxkj.yiyao.activity.PlayerActivity;
import com.lxkj.yiyao.global.GlobalString;
import com.lxkj.yiyao.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liqinpeng on 2017/3/11 0011.
 */

public class JiChuKeChengAdapter extends BaseAdapter {


    JSONArray objects;

    public JiChuKeChengAdapter(String result) {
        objects = JSONArray.parseArray(result);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        ViewHolder holder = null;
        if(view == null){
            view = View.inflate(viewGroup.getContext(),R.layout.basic_kecheng_name,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final JSONObject object = objects.getJSONObject(i);

        holder.name.setText(""+object.get("jckcmc"));
        // TODO: 2017/3/11 0011 写入事件监听



        return view;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
