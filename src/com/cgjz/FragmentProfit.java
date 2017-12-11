package com.cgjz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentProfit extends Fragment {
	private TextView profitTitleTV, profitDateTV, profitCount, profitDaily,
			profitTotle, totleStockProfitTextView;
	private Button refreshButton;
	private DailyStockInfoSqlite StockDB;
	private ProfitAdapter profitAdapter;
	private ListView profitListView;
	private ArrayList<StockProfit> myStockProifList = new ArrayList<StockProfit>();
	private double myTodayProfit;
	private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd",
			Locale.CHINA);
	private String today;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profit, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("ttt", "myStockProifList=" +myStockProifList.get(0).getStockExchangeProfit());
				myStockProifList.clear();//http://m.blog.csdn.net/blog/u011290399/21654153
				ArrayList<StockProfit> tempList=StockDB.getProfits();
				if (tempList!=null) {
					myStockProifList.addAll(tempList);
				profitAdapter.notifyDataSetChanged();
				
				today = sDateFormat.format(new java.util.Date());
				myTodayProfit = StockDB.getTodayProfit(today);
				
				totleStockProfitTextView.setText(myTodayProfit + "");
				}
				
			}
		});
	}

	public void init() {
		today = sDateFormat.format(new java.util.Date());
		StockDB = ((MainActivity) getActivity()).getStockDB();
		profitTitleTV = (TextView) getView().findViewById(R.id.profit_titleTV);
		totleStockProfitTextView = (TextView) getView().findViewById(
				R.id.totleStockProfitTV);
		profitListView = (ListView) getView().findViewById(
				R.id.stock_profit_list);
		refreshButton = (Button) getView().findViewById(R.id.profit_refreshBT);

		profitTitleTV.setText("我的收益");

		myTodayProfit = StockDB.getTodayProfit(today);
		totleStockProfitTextView.setText(myTodayProfit + "");

		if (StockDB.getProfits() != null) {
			myStockProifList = StockDB.getProfits();
		}
		profitAdapter = new ProfitAdapter(getActivity(), R.layout.profit_item,
				myStockProifList);
		profitListView.setAdapter(profitAdapter);
	}
}
