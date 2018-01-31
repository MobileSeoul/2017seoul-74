package com.tou4u.dulejule.contentviewer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tou4u.dulejule.R;
import com.tou4u.dulejule.contentviewer.main.MainActivity;
import com.tou4u.dulejule.data.Content;

import java.util.ArrayList;

public class BaseContentAdapter extends RecyclerView.Adapter<BaseContentHolder> implements ContentAdapter{

    private MainActivity mActivity;

    private ArrayList<Content> mContents;
    private BindCallBack mBindCallBack;

    public BaseContentAdapter(MainActivity activity, BindCallBack bindCallBack) {
        super();
        mActivity = activity;
        mBindCallBack = bindCallBack;
        mContents = new ArrayList<>();
    }

    interface BindCallBack {
        void onBind(BaseContentHolder holder, String contentIndex);
    }

    @Override
    public BaseContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = layoutInflater
                .inflate(R.layout.list_item_base_content, parent, false);
        return new BaseContentHolder(mActivity, view);
    }

    @Override
    public void onBindViewHolder(BaseContentHolder holder, int position) {
        Content content = mContents.get(position);
        holder.bindContent(content);
        mBindCallBack.onBind(holder, content.getIndex());
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }

    public void changeContents(ArrayList<Content> contents) {
        mContents = contents;
        notifyDataSetChanged();
    }

}