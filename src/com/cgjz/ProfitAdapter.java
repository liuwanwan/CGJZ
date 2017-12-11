package com.cgjz;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProfitAdapter extends ArrayAdapter<StockProfit> {
	private int resourceId;

	public ProfitAdapter(Context context, int itemResourceId,
			List<StockProfit> objects) {
		super(context, itemResourceId, objects);
		resourceId = itemResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		StockProfit myStockProfit = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			Log.v("ttt", "33345");
			viewHolder.stockDateTV = (TextView) view
					.findViewById(R.id.stockDateTV);
			viewHolder.stockCountTV = (TextView) view
					.findViewById(R.id.stockCountTV);
			viewHolder.stockDailyProiftTV = (TextView) view
					.findViewById(R.id.stockDailyProfitTV);
			viewHolder.stockExchangeProiftTV = (TextView) view
					.findViewById(R.id.stockExchangeProfitTV);
			viewHolder.stockTotleProfitTV = (TextView) view
				.findViewById(R.id.stockTotleProfitTV);
			view.setTag(viewHolder);
			Log.v("ttt", "1111");
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		Log.v("ttt", "234");
		viewHolder.stockDateTV.setText(myStockProfit.getStockDate());
		viewHolder.stockCountTV.setText(myStockProfit.getStockCount()+"");
		viewHolder.stockDailyProiftTV.setText(String.valueOf(myStockProfit.getStockDailyProfit()));
		viewHolder.stockExchangeProiftTV.setText(String.valueOf(myStockProfit.getStockExchangeProfit()));
		viewHolder.stockTotleProfitTV.setText(String.valueOf(myStockProfit.getStockTotleProfit()));
		Log.v("ttt", "432");
		return view;
	}

	class ViewHolder {
		TextView stockDateTV, stockCountTV, stockDailyProiftTV,stockExchangeProiftTV,stockTotleProfitTV;
	}
}
