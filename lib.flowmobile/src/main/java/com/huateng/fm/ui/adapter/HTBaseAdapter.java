/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huateng.fm.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public abstract class HTBaseAdapter extends BaseAdapter {
    private int[] mTo;
    private String[] mFrom;


    private int mResource;
    private LayoutInflater mInflater;

    private ArrayList<?> mData;
    private int s;
    private Context mContext;
   
    public abstract int getCheckableViewId();
    
    
    

    public HTBaseAdapter(Context context, List<?> data) {
    	this.mContext=context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

 
    public int getCount() {
        return mData.size();
    }

 
    public Object getItem(int position) {
        return mData.get(position);
    }

 
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }
    

    private View createViewFromResource(int position, View convertView,
            ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }

        bindView(position, v);

        return v;
    }



    public abstract void bindView(int position, View view) ;



  
   
}
