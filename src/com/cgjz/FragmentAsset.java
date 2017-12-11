package com.cgjz;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentAsset extends Fragment{
	private TextView titleTV;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_asset, container, false);	
	}
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		titleTV=(TextView)getView().findViewById(R.id.titleTV);
		titleTV.setText("�ҵ��ʲ�");
		
	}
}
