package com.chester.android.choosemultiimage;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends Activity {

	// collection of items (data)
	ArrayList<ImageEntity> mDataList;
	
	// UI component
    GridView mGridView;  
    GridAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();  
        initView(); 
	}
	
	/**
	 *  suppose we have interpret the data from network or local file
	 */
	private void initData(){
		mDataList = new ArrayList<ImageEntity>();  
        for(int i=-0;i<6;i++){  
            ImageEntity entity = new ImageEntity(R.drawable.picture, false);  
            mDataList.add(entity);  
        }
    } 

	/**
	 * initialize the view
	 */
	private void initView(){  
        mGridView = (GridView) findViewById(R.id.gridView1);  
        mAdapter = new GridAdapter();  
        mGridView.setAdapter(mAdapter);  
          
        mGridView.setOnItemClickListener(new OnItemClickListener() {  
  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view,  
                    int position, long id) {
                if(mDataList.get(position).isSelected()){  
                    mDataList.get(position).setSelected(false);  
                } else {  
                    mDataList.get(position).setSelected(true);  
                }   
                // notify adapter if data changed
                mAdapter.notifyDataSetChanged();  
            }  
              
        });  
    }  
	
	/**
	 * adapter used to bind data and GridView
	 * @author CHazyhabiT
	 *
	 */
	class GridAdapter extends BaseAdapter{  
		  
        @Override  
        public int getCount() {  
            int count = 0;  
            if(mDataList!=null){  
                count = mDataList.size();  
            }  
            return count;  
        }  
  
        @Override  
        public Object getItem(int position) {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return 0;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {
        	/**
        	 * used to interpret the subView layout of GridView,
        	 * and set the image and the select status based on the instance of each ImageEntity
        	 */
        	
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, null);  
            ImageView image = (ImageView) convertView.findViewById(R.id.image);  
            ImageView selectImage = (ImageView) convertView.findViewById(R.id.isselected);  
            image.setImageResource(mDataList.get(position).getImageUri());  
              
            if(mDataList.get(position).isSelected()){  
                selectImage.setVisibility(View.VISIBLE);  
            }else{  
                selectImage.setVisibility(View.GONE);  
            }  
              
            return convertView;  
        }  
          
    }

}
