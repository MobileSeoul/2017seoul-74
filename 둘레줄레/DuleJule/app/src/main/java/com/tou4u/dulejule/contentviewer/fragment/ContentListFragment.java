package com.tou4u.dulejule.contentviewer.fragment;

import android.app.Activity;

import android.content.Context;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tou4u.dulejule.contentviewer.BaseContentAdapter;
import com.tou4u.dulejule.contentviewer.ImageContentAdapter;
import com.tou4u.dulejule.util.DebugUtil;
import com.tou4u.dulejule.R;
import com.tou4u.dulejule.contentviewer.ContentAdapter;
import com.tou4u.dulejule.contentviewer.ContentLoadManager;
import com.tou4u.dulejule.contentviewer.ContentsLoadManager;
import com.tou4u.dulejule.contentviewer.main.MainActivity;


public class ContentListFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private static final String ARG_CONTENT_TYPE = "contentType";

    private int mContentType;

    private RecyclerView.Adapter mAdapter;
    private ContentLoadManager mContentLoadManager;
    private ContentsLoadManager mContentsLoadManager;

    private RecyclerView mContentRecyclerView;

    private MainActivity mActivity;

    private volatile boolean canUpdate = false;


    /**
     * 프레그먼트가 엑티비티의 프레그먼트 매니저에 등록될때 실행되는 메소드
     * 프레그먼트를 호스팅한 엑티비티를 CallBack 인터페이스로 업캐스팅 후 멥버변수로 등록
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            if (a instanceof MainActivity) {
                mActivity = (MainActivity) a;
            }
        }

    }

    /**
     * 프레그먼트가 소멸될때 등록 된 콜백 메소드를 해제
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public static ContentListFragment newInstance(int contentType) {
        Bundle args = new Bundle();
        args.putInt(ARG_CONTENT_TYPE, contentType);
        ContentListFragment fragment = new ContentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canUpdate = false;
        mContentLoadManager.clearQueue();
        DebugUtil.logD(TAG, "content load manager clear");
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugUtil.logD(TAG, "onResume");
        canUpdate = true;
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentsLoadManager.cancle();
        mContentLoadManager.quit();
        DebugUtil.logD(TAG, "Background thread destroyed");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentType = getArguments().getInt(ARG_CONTENT_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_list, container, false);

        mContentRecyclerView = (RecyclerView) view
                .findViewById(R.id.content_recycler_view);

        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mContentLoadManager = new ContentLoadManager(mActivity, mContentType);
        if (mContentType == MainActivity.CONTENT_TYPE_CODE_NOTICE_BASE) {
            mAdapter = new BaseContentAdapter(mActivity, mContentLoadManager);
        } else {
            mAdapter = new ImageContentAdapter(mActivity, mContentLoadManager);
        }

        mContentRecyclerView.setAdapter(mAdapter);

        mContentsLoadManager = new ContentsLoadManager(this.getContext(), (ContentAdapter) mAdapter);

        return view;
    }

    public void updateUI() {

        if (!canUpdate) {
            DebugUtil.logD(TAG, "update fail -> view not ready");
            return;
        }

        mContentsLoadManager.loadContents(mContentType);

    }

}
