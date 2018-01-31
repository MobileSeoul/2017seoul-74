package com.tou4u.dulejule.contentviewer.main;

import com.tou4u.dulejule.util.DebugUtil;

import java.util.Vector;

public class MainDataSubject {

    private static final String TAG = "MainDataSubject";
    private Vector<MainDataObserver> observers;

    private int mCurrentPage;

    private boolean changed;

    public MainDataSubject() {
        observers = new Vector<>();
        mCurrentPage = -1;
    }

    public void setCurrentPage(int currentPage) {
        if(currentPage != mCurrentPage){
            this.mCurrentPage = currentPage;
            DebugUtil.logD(TAG, String.format("DataSetChanged %d", mCurrentPage));
            setChanged();
        }
    }

    public void addObserver(MainDataObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(MainDataObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void notifyObservers() {
        if (changed) {
            synchronized (observers) {
                for (MainDataObserver observer : observers)
                    observer.update(mCurrentPage);
            }
        }
    }

    public void setChanged() {
        if (isVaild()) {
            changed = true;
            notifyObservers();
        }
    }

    public boolean isVaild(){
        return mCurrentPage != -1;
    }
}

