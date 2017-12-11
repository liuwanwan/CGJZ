package com.cgjz;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.net.*;
import java.io.*;

import android.widget.*;

public class FragmentStock extends Fragment implements OnClickListener {
	public static double TOTAL_STOCK_MONEY;
	public double DAILY_STOCK_PROFIT;
	private double TOTLE_STOCK_PROFIT;
	float stockExpensesRate;
	float stockStamptaxRate, stockTransferfeeRate;
	private int EXCHANGE_DIRECTION = 0, count = 0;;
	private TextView titleTV;//totleStockValueTextView;
	private ArrayList<MyStock> myStockList = new ArrayList<MyStock>();
	private ArrayList<StockProfit> myStockProifList = new ArrayList<StockProfit>();
	private StockAdapter stockAdapter;
	private ListView myStockListView;
	private Button refreshButton, setButton;
	private RadioGroup myRadioGroup;
	private RadioButton sellRadioButton, buyRadioButton;
	private EditText stockNameCodeEditText, stockNumberEditText,
			stockPriceEditText;
	private TextView stockExpensesTextView, stockTaxTextView,
			stockTransferfeeTextView;
	private ArrayList<String> stockRudeInfoList;
	private ArrayList<String> stockCodeList = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> stockDataArrayList;
	private DecimalFormat myformat = new DecimalFormat("###,###.00");//
	private ProgressDialog progressDialog;
	private DailyStockInfoSqlite StockDB;
	SharedPreferences mySharedPreferences;
	double costN, stockExpenses, stockTransferfee, stockTax;
	double SELL_ALL_PROFIT,SELL_PROFIT;
	boolean SELL_ALL;
	String codeS;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stock, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		refreshButton = (Button) getView().findViewById(R.id.stock_refreshBT);
		setButton = (Button) getView().findViewById(R.id.stock_setBT);
		//totleStockValueTextView = (TextView) getView().findViewById(
		//		R.id.totle_stockTV);
		titleTV = (TextView) getView().findViewById(R.id.stock_titleTV);
		titleTV.setText("我的股票");

		initStockList();
		Log.v("ttt", "myStockList=" + myStockList.size());
		stockAdapter = new StockAdapter(getActivity(), R.layout.stock_item,
				myStockList);

		myStockListView = (ListView) getView().findViewById(R.id.stock_list);

		myStockListView.setAdapter(stockAdapter);
		myStockListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MyStock myStock = myStockList.get(position);
				listStockInfo();
			}
		});
		refreshButton.setOnClickListener(this);
		setButton.setOnClickListener(this);
	}

	private void listStockInfo() {

	}

	public ArrayList<HashMap<String, String>> analyseStockData(
			ArrayList<String> s) {
		stockDataArrayList = new ArrayList<HashMap<String, String>>();
		StockInfo myStockInfo = new StockInfo();
		String myStockInfoAtrr[] = myStockInfo.getStockInfo();
		int size = s.size();
		for (int j = 0; j < size; j++) {
			String ssp = s.get(j).toString();
			HashMap<String, String> map = new HashMap<String, String>();
			int endId = ssp.lastIndexOf("\"");
			if (endId <= 12) {
				Toast.makeText(getActivity(), "股票信息不存在！", Toast.LENGTH_SHORT)
						.show();
				for (int i = 0; i < myStockInfoAtrr.length; i++) {
					map.put(myStockInfoAtrr[i], "");
				}
			} else {
				String tempString = ssp.substring(12, endId);
				Pattern p;
				p = Pattern.compile("([^~]+)\\~");// 鍦ㄨ繖閲岋紝缂栬瘧 鎴愪竴涓鍒欍��
				Matcher m;
				m = p.matcher(tempString);// 鑾峰緱鍖归厤
				for (int i = 0; m.find(); i++) {
					map.put(myStockInfoAtrr[i], m.group(1));
				}
			}
			stockDataArrayList.add(map);
		}
		return stockDataArrayList;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stock_setBT:
			stockExchange();
			break;
		case R.id.stock_refreshBT:
			if (myStockList.size() > 0) {
				try {
					refreshPrice();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	private void stockExchange() {
		final View layout = LayoutInflater.from(getActivity()).inflate(
				R.layout.stock_exchange_layout,
				(ViewGroup) getView().findViewById(R.id.myDialogLayout));
		myRadioGroup = (RadioGroup) layout.findViewById(R.id.buyOfsellRG);
		sellRadioButton = (RadioButton) layout.findViewById(R.id.sellRButton);
		buyRadioButton = (RadioButton) layout.findViewById(R.id.buyRButton);
		stockNameCodeEditText = (EditText) layout
				.findViewById(R.id.stock_namecodeET);
		stockNumberEditText = (EditText) layout
				.findViewById(R.id.stock_numberET);
		stockPriceEditText = (EditText) layout.findViewById(R.id.stock_priceET);
		stockExpensesTextView = (TextView) layout
				.findViewById(R.id.stock_expensesTV);
		stockTaxTextView = (TextView) layout.findViewById(R.id.stock_taxTV);
		stockTransferfeeTextView = (TextView) layout
				.findViewById(R.id.stock_transferfeeTV);

		// stockPriceEditText.setOnKeyListener(editTextChangeListener);
		myRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == buyRadioButton.getId()) {
					EXCHANGE_DIRECTION = 1; // buy=1
				} else if (checkedId == sellRadioButton.getId()) {
					EXCHANGE_DIRECTION = -1; // sell=-1
				} else {
					EXCHANGE_DIRECTION = 0;
				}
			}
		});
		stockPriceEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				stockExpensesTextView.setText("AAA");

				/*
				 * if (EXCHANGE_DIRECTION == 1) {// 卖出时有印花税
				 * stockTaxTextView.setText(stockTax + ""); } if
				 * (EXCHANGE_DIRECTION == -1) { stockTaxTextView.setText("0"); }
				 * 
				 * if (codeS.substring(0, 1).equals("6")) {// 沪市A股的代码是以600或601打头
				 * stockTransferfeeTextView.setText(stockTransferfee + "");//
				 * 过户费上证收取 } else { stockTransferfeeTextView.setText("0");//
				 * 过户费上证收取 }
				 */
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("股票交易").setView(layout)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						codeS = stockNameCodeEditText.getText().toString();
						String numberS = stockNumberEditText.getText()
								.toString();

						String costS = stockPriceEditText.getText().toString();
						int numberN = Integer.parseInt(numberS);
						costN = Double.parseDouble(costS);
						stockExpensesRate = mySharedPreferences.getFloat(
								"expenseRate", 0.25F) / 1000;
						stockStamptaxRate = mySharedPreferences.getFloat(
								"stamptaxRate", 0.5F) / 1000;
						stockTransferfeeRate = mySharedPreferences.getFloat(
								"transferfeeRate", 0.02F) / 1000;
						stockExpenses = numberN * costN * stockExpensesRate;
						if (EXCHANGE_DIRECTION == 1 && codeS.length() > 0
								&& numberS.length() > 0 && costS.length() > 0) {
							if (codeS.substring(0, 1).equals("6")) {
								stockTransferfee = numberN * costN
										* stockTransferfeeRate;
							} else {
								stockTransferfee = 0;
							}
							MyStock existStock = StockDB.checkCode(codeS);
							if (existStock == null) {
								stockCodeList.add(codeS);
								double newCost = (costN * numberN
										+ stockExpenses + stockTransferfee)
										/ numberN;
								double priceN = costN;
								double moneyN = numberN * priceN;
								double profitN = numberN * (priceN - newCost);
								MyStock stock = new MyStock(codeS, codeS,
										numberN, newCost, priceN, moneyN,
										profitN);
								TOTAL_STOCK_MONEY += stock.getStockMoney();
								myStockList.add(stock);
								StockDB.insert(codeS, codeS, numberN, newCost,
										priceN, moneyN, profitN);
							} else {// 已有该股票
								int newNumber = numberN
										+ existStock.getStockNumber();
								double newCost = (existStock.getStockCost()
										* existStock.getStockNumber() + numberN
										* costN + stockExpenses + stockTransferfee)
										/ newNumber;
								double newPrice = newCost;
								double newMoney = newNumber * newCost;
								double newProfit = (newPrice - newCost)
										* newNumber;
								StockDB.update(codeS, codeS, newNumber,
										newCost, newPrice, newMoney, newProfit);
							}
						} else if (EXCHANGE_DIRECTION == -1
								&& codeS.length() > 0 && numberS.length() > 0
								&& costS.length() > 0) {
							SELL_ALL_PROFIT=0;
							MyStock existStock = StockDB.checkCode(codeS);
							if (existStock == null) {// 卖出前未持有该股票
								Toast.makeText(getActivity(), "未持有该股，无法卖出！",
										Toast.LENGTH_SHORT).show();
							} else {

								stockTax = numberN * costN * stockStamptaxRate;
								int newNumber = existStock.getStockNumber()
										- numberN;
								double newCost = existStock.getStockCost();
								
								double newPrice = costN;
								double newMoney = newNumber * newCost;
								double newProfit =existStock.getStockProfit()+numberN*(newPrice-existStock.getStockPrice())
										- stockExpenses - stockTax
										- stockTransferfee;

								if (numberN > existStock.getStockNumber()) {
									Toast.makeText(getActivity(),
											"持有该股数量小于卖出数量，无法卖出！",
											Toast.LENGTH_SHORT).show();
								}
								Log.v("ttt", "newNumber=" + newNumber
										+ "numberN=" + numberN + "+Number"
										+ existStock.getStockNumber());
								if (numberN < existStock.getStockNumber()) {
									StockDB.update(codeS, codeS, newNumber,
											newCost, newPrice, newMoney,
											newProfit);
									SELL_ALL_PROFIT = numberN*(newPrice-existStock.getStockPrice())
											- stockExpenses - stockTax
											- stockTransferfee;// 该股票出售收益
									
								}
								if (numberN == existStock.getStockNumber()) {
									StockDB.delete(existStock.getStockCode());
									SELL_ALL_PROFIT = numberN*(newPrice-existStock.getStockPrice())
											- stockExpenses - stockTax
											- stockTransferfee;// 该股票出售收益
									SELL_ALL = true;
									TOTAL_STOCK_MONEY -= existStock
											.getStockMoney();

									stockCodeList.remove(codeS);// 删去该股票代码？
									myStockList.remove(existStock);// 删去该股票？

								}
								
							}

						} else {// 不是6和0开头的股票
							Log.v("ttt", "aaaa2222222aaaaaaaaaaaa");
						}

						ArrayList<MyStock> tempL =StockDB.getAllStockData();
						myStockList.clear();
						if (tempL!=null) {
							myStockList.addAll(tempL);//清空时即tempL=null,此时出错
						}
						stockAdapter.notifyDataSetChanged();
						//totleStockValueTextView.setText(myformat
						//		.format(TOTAL_STOCK_MONEY));
						if (myStockList.size() > 0) {
							try {
								refreshPrice();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					}

				}).setNegativeButton("Cancel", null).show();

	}

	private void refreshPrice() throws InterruptedException {
		CountDownLatch myLatch = new CountDownLatch(stockCodeList.size());
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(getActivity(), "正在刷新",
					"正在获取最新股票数据......", true, false);
		} else if (progressDialog.isShowing()) {
			progressDialog.setTitle("AAA");
			progressDialog.setMessage("DDD");
		}
		progressDialog.show();
		for (String code : stockCodeList) {
			MyThread thread = new MyThread(code, myLatch);
			thread.start();
		}
		myLatch.await();
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		DAILY_STOCK_PROFIT = 0;
		TOTAL_STOCK_MONEY = 0;
		ArrayList<HashMap<String, String>> maplist = analyseStockData(stockRudeInfoList);
		int stockRudeInfoListLength = stockRudeInfoList.size();
		for (int i = 0; i < stockRudeInfoListLength; i++) {
			MyStock tempmap = myStockList.get(i);
			for (int j = 0; j < stockRudeInfoListLength; j++) {
				if (maplist.get(j).get("Code").equals(tempmap.getStockCode())) {
					String nowStockPrice = maplist.get(j).get("NowPrice");
					double newPrice = Double.parseDouble(nowStockPrice);
					double newMoney = Double.parseDouble(nowStockPrice)
							* tempmap.getStockNumber();
					double newProfit = (Double.parseDouble(nowStockPrice) - tempmap
							.getStockCost()) * tempmap.getStockNumber();
					TOTAL_STOCK_MONEY += newMoney;
					DAILY_STOCK_PROFIT += newProfit;
					StockDB.update(tempmap.getStockCode(),tempmap.getStockCode(),tempmap.getStockNumber(),tempmap.getStockCost(),newPrice,newMoney,newProfit);
				}

			}
		}
		ArrayList<MyStock> tempL =StockDB.getAllStockData();
		myStockList.clear();
		if (tempL!=null) {
			myStockList.addAll(tempL);//清空时即tempL=null,此时出错
		}
		stockAdapter.notifyDataSetChanged();
		/*if (SELL_ALL) {
			DAILY_STOCK_PROFIT += SELL_ALL_PROFIT;
			SELL_ALL_PROFIT = 0;
			SELL_ALL = false;
		}*/
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		String today = sDateFormat.format(new java.util.Date());
		if (StockDB.checkTodayNetProfitExist(today)) {
			for (int i = 0; i < myStockProifList.size(); i++) {
				String date = today;
				int count = myStockList.size();
				double dailyProfit = DAILY_STOCK_PROFIT;
				double totleProfit = DAILY_STOCK_PROFIT;
				Log.v("ttt","SELL_ALL_PROFIT="+SELL_ALL_PROFIT);
				StockDB.update(date, count, dailyProfit,SELL_ALL_PROFIT, totleProfit);
			}
		} else {
			count = myStockList.size();
			double dailyProfit = DAILY_STOCK_PROFIT;
			double totleProfit = DAILY_STOCK_PROFIT;

			StockDB.insert(count, dailyProfit,SELL_ALL_PROFIT, totleProfit);
		}

		//totleStockValueTextView
				//.setText(myformat.format(TOTAL_STOCK_MONEY) + "");
	}

	private void initStockList() {
		mySharedPreferences = ((MainActivity) getActivity())
				.getSharedPreferences();
		StockDB = ((MainActivity) getActivity()).getStockDB();

		if (StockDB.getAllStockData() != null) {
			TOTAL_STOCK_MONEY = 0.0;
			myStockList = StockDB.getAllStockData();
			int n = myStockList.size();
			for (int i = 0; i < n; i++) {
				stockCodeList.add(myStockList.get(i).getStockCode());
				TOTAL_STOCK_MONEY += myStockList.get(i).getStockMoney();
			}
		}
		if (StockDB.getProfits() != null) {
			DAILY_STOCK_PROFIT = 0.0;
			myStockProifList = StockDB.getProfits();
		}
		//totleStockValueTextView.setText(myformat.format(TOTAL_STOCK_MONEY));
	}

	public class MyThread extends Thread {

		String stockUri;
		CountDownLatch countDownLatch;

		public MyThread(String threadName, CountDownLatch latch) {
			stockRudeInfoList = new ArrayList<String>();
			countDownLatch = latch;
			String STOCK_EXCGANGE = threadName.substring(0, 1);
			if (STOCK_EXCGANGE.equals("0"))
				stockUri = "http://qt.gtimg.cn/q=sz" + threadName;
			if (STOCK_EXCGANGE.equals("6"))
				stockUri = "http://qt.gtimg.cn/q=sh" + threadName;
		}

		public void run() {
			getStockData(stockUri);
			countDownLatch.countDown();
		}

		private void getStockData(String httpUrl) {
			StringBuilder resultData = new StringBuilder("");
			URL url = null;

			try {
				url = new URL(httpUrl);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			try {
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				urlConn.setRequestMethod("GET");
				InputStreamReader isr = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(isr);
				String inputLine = null;

				while ((inputLine = buffer.readLine()) != null) {
					resultData.append(inputLine);
					resultData.append("\n");
				}
				buffer.close();
				isr.close();
				urlConn.disconnect();
				stockRudeInfoList.add(resultData.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
