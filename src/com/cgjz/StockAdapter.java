package com.cgjz;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StockAdapter extends ArrayAdapter<MyStock> {
	private int resourceId;

	public StockAdapter(Context context, int itemResourceId,
			List<MyStock> objects) {
		super(context, itemResourceId, objects);
		resourceId = itemResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		MyStock myStock = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.stockName = (TextView) view
					.findViewById(R.id.stockNameTV);
			viewHolder.stockCode = (TextView) view
					.findViewById(R.id.stockCodeTV);
			viewHolder.stockNumber = (TextView) view
					.findViewById(R.id.stockNumberTV);
			viewHolder.stockCost = (TextView) view
				.findViewById(R.id.stockCostTV);
			viewHolder.stockPrice = (TextView) view
					.findViewById(R.id.stockPriceTV);
			viewHolder.stockMoney = (TextView) view
					.findViewById(R.id.stockMoneyTV);
			viewHolder.stockProfit = (TextView) view
				.findViewById(R.id.stockProfitTV);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.stockName.setText(myStock.getStockName());
		viewHolder.stockCode.setText(myStock.getStockCode());
		viewHolder.stockNumber
				.setText(String.valueOf(myStock.getStockNumber()));
		viewHolder.stockCost.setText(String.valueOf(myStock.getStockCost()));
		viewHolder.stockPrice.setText(String.valueOf(myStock.getStockPrice()));
		viewHolder.stockMoney.setText(String.valueOf(myStock.getStockMoney()));
		viewHolder.stockProfit.setText(String.valueOf(myStock.getStockProfit()));
		return view;
	}

	class ViewHolder {
		TextView stockName, stockCode, stockNumber, stockCost,stockPrice, stockMoney,stockProfit;
	}
}
