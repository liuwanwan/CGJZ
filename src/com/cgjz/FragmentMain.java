package com.cgjz;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.os.*;

public class FragmentMain extends Fragment{
	private TextView titleTV;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_main, container, false);	
	}
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		titleTV=(TextView)getView().findViewById(R.id.titleTV);
		titleTV.setText("function");
		
	}
}
