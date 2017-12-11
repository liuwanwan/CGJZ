package com.cgjz;
import android.widget.*;
import android.view.*;
import android.app.*;
import android.os.*;

public class FragmentInset extends Fragment{
	private TextView titleTV;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_inset, container, false);	
	}
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		titleTV=(TextView)getView().findViewById(R.id.titleTV);
		titleTV.setText("�ҵ��ʲ�");

	}
}
