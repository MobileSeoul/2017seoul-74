package com.tou4u.dulejule.contentviewer.main;

import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tou4u.dulejule.R;
import com.tou4u.dulejule.contentviewer.fragment.ContentListFragment;
import com.tou4u.dulejule.contentviewer.main.network.NetworkUtil;
import com.tou4u.dulejule.polling.PollService;
import com.tou4u.dulejule.polling.QueryPreferences;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class MainActivity extends AppCompatActivity implements ContentsUpdateExcuter.OnUpdateCallBack {


    private static final String TAG = "MainActivity";

    public static final String CONTENT_TYPE_FORMAT_NOTICE_BASE = " 공지사항 ";
    public static final String CONTENT_TYPE_FORMAT_NOTICE_IMAGE = " 영상목록 ";
    public static final String CONTENT_TYPE_FORMAT_NOTICE_EVENT = " 행사목록 ";

    public static final int CONTENT_TYPE_CODE_NOTICE_BASE = 1;
    public static final int CONTENT_TYPE_CODE_NOTICE_IMAGE = 2;
    public static final int CONTENT_TYPE_CODE_NOTICE_EVENT = 3;

    private static final int[] CONTENT_TYPE_CODE =
            new int[]{
                    CONTENT_TYPE_CODE_NOTICE_BASE,
                    CONTENT_TYPE_CODE_NOTICE_IMAGE,
                    CONTENT_TYPE_CODE_NOTICE_EVENT};

    private static final String[] CONTENT_TYPE_FORMAT =
            new String[]{
                    CONTENT_TYPE_FORMAT_NOTICE_BASE,
                    CONTENT_TYPE_FORMAT_NOTICE_IMAGE,
                    CONTENT_TYPE_FORMAT_NOTICE_EVENT};

    private ViewPager mViewPager;

    private ConcurrentMap<Integer, ContentListFragment> mFragmentMap = new ConcurrentHashMap<>();

    private MainDataSubject mMainDataSubject;

    private ContentTypeAdapter mContentTypeAdapter;
    private FloatingActionButton mRefreshButton;
    private FloatingActionButton mPushButton;

    private ContentsUpdateExcuter mContentsUpdateExcuter;

    private InfoOpener mInfoOpener;
    private CopyrightOpener mCopyrightOpener;

    public MainDataSubject getMainData() {
        return mMainDataSubject;
    }

    @Override
    protected void onDestroy() {
        mInfoOpener.dismissDialog();
        mCopyrightOpener.dismissDialog();
        super.onDestroy();
    }

    @Override
    public void onContentsUpdate(int currentPage) {
        ContentListFragment fragment = mFragmentMap.get(currentPage);
        if (fragment != null)
            fragment.updateUI();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Info) {
            mInfoOpener.showDialog();
            return true;
        } else if (id == R.id.action_copyright) {
            mCopyrightOpener.showDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainDataSubject.setCurrentPage(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mInfoOpener = new InfoOpener(this);
        mCopyrightOpener = new CopyrightOpener(this);
        mMainDataSubject = new MainDataSubject();
        mContentsUpdateExcuter = new ContentsUpdateExcuter(this, this, mMainDataSubject);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mContentTypeAdapter = new ContentTypeAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mContentTypeAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ContentListFragment fragment = mFragmentMap.get(position);
                if (fragment != null) {
                    mMainDataSubject.setCurrentPage(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRefreshButton = (FloatingActionButton) findViewById(R.id.button_refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.getInstance().isConnected(MainActivity.this) == NetworkUtil.CODE_CONNECT) {
                    Toast.makeText(MainActivity.this, TOAST_REFRESH, Toast.LENGTH_SHORT).show();
                    mMainDataSubject.setChanged();
                } else {
                    Toast.makeText(MainActivity.this, "네트워크에 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPushButton = (FloatingActionButton) findViewById(R.id.button_set_push);

        boolean push = QueryPreferences.getPushState(MainActivity.this);
        PollService.setServiceAlarm(this, push);
        if (push) {
            mPushButton.setImageDrawable(getResources().getDrawable(R.mipmap.button_state_sync_enable));
            Toast.makeText(this, TOAST_PUSH_STATE_SYSN_ENDABLE, Toast.LENGTH_SHORT).show();
        } else {
            mPushButton.setImageDrawable(getResources().getDrawable(R.mipmap.button_state_sync_disable));
            Toast.makeText(this, TOAST_PUSH_STATE_SYSN_DISABLE, Toast.LENGTH_SHORT).show();
        }

        mPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean push = !QueryPreferences.getPushState(MainActivity.this);
                if (push) {
                    mPushButton.setImageDrawable(getResources().getDrawable(R.mipmap.button_state_sync_enable));
                    Toast.makeText(MainActivity.this, TOAST_PUSH_STATE_SYSN_ENDABLE, Toast.LENGTH_SHORT).show();
                } else {
                    mPushButton.setImageDrawable(getResources().getDrawable(R.mipmap.button_state_sync_disable));
                    Toast.makeText(MainActivity.this, TOAST_PUSH_STATE_SYSN_DISABLE, Toast.LENGTH_SHORT).show();
                }
                QueryPreferences.setPushState(MainActivity.this, push);
                PollService.setServiceAlarm(MainActivity.this, push);
            }
        });

    }

    private static final String TOAST_REFRESH = "REFESH";
    private static final String TOAST_PUSH_STATE_SYSN_ENDABLE = "현재 새 공지 알림 기능 : ON";
    private static final String TOAST_PUSH_STATE_SYSN_DISABLE = "현재 새 공지 알림 기능 : OFF";

    class ContentTypeAdapter extends FragmentPagerAdapter {
        public ContentTypeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ContentListFragment fragment = ContentListFragment.newInstance(CONTENT_TYPE_CODE[position]);
            mFragmentMap.put(position, fragment);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT_TYPE_FORMAT[position % CONTENT_TYPE_FORMAT.length];
        }

        @Override
        public int getCount() {
            return CONTENT_TYPE_FORMAT.length;
        }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


}
