package com.cgjz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DailyStockInfoSqlite extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "myDailyStockInfoDB.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME_A = "stock_table";
	public static final String STOCK_CODE = "stock_code";
	public static final String STOCK_NAME = "stock_name";
	public static final String STOCK_NUMBER = "stock_number";
	public static final String STOCK_COST = "stock_cost";
	public static final String STOCK_PRICE = "stock_price";
	public static final String STOCK_MONEY = "stock_money";
	public static final String STOCK_PROFIT = "stock_profit";
	public static final String TABLE_NAME_B = "stock_table_profit";
	public static final String STOCK_DATE = "stock_profit_date";
	public static final String STOCK_COUNT = "stock_profit_count";
	public static final String STOCK_DAILY_PROFIT = "stock_daily_profit";
	public static final String STOCK_EXCHANGE_PROFIT = "stock_exchange_profit";
	public static final String STOCK_TOTLE_PROFIT = "stock_totle_profit";

	public DailyStockInfoSqlite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 创建表
	@Override
	public void onCreate(SQLiteDatabase db) {

		String sqlA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_A + " ("
				+ STOCK_CODE + " TEXT primary key," + STOCK_NAME + " TEXT,"
				+ STOCK_NUMBER + " INTEGER," + STOCK_COST + " NUMERIC,"
				+ STOCK_PRICE + " NUMERIC," + STOCK_MONEY + " NUMERIC,"
				+ STOCK_PROFIT + " NUMERIC" + ");";
		db.execSQL(sqlA);
		String sqlB = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_B + " ("
				+ STOCK_DATE + " TEXT primary key," + STOCK_COUNT + " INTEGER,"
				+ STOCK_DAILY_PROFIT + " NUMERIC,"+STOCK_EXCHANGE_PROFIT+ " NUMERIC,"+ STOCK_TOTLE_PROFIT
				+ " NUMERIC" + ");";
		db.execSQL(sqlB);
	}

	// 更新表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sqlA = "DROP TABLE IF EXISTS " + TABLE_NAME_A;
		db.execSQL(sqlA);
		String sqlB = "DROP TABLE IF EXISTS " + TABLE_NAME_B;
		db.execSQL(sqlB);
		onCreate(db);
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME_A, null, null, null, null, null,
				null, null);
		return cursor;
	}

	public double getTodayProfit(String date) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select stock_daily_profit from stock_table_profit where stock_profit_date=?",
						new String[] { date });
		if(cursor.getCount() == 1 && cursor.moveToFirst())  {
			double todayProfit = cursor.getDouble(cursor
					.getColumnIndex("stock_daily_profit"));
			return todayProfit;

		} else {
			return 0;
		}
	}

	public boolean checkTodayNetProfitExist(String date) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from stock_table_profit where stock_profit_date=?",
				new String[] { date });
		if(cursor.getCount() == 1 && cursor.moveToFirst())  {

			return true;

		} else {
			return false;
		}
	}

	// 增加操作
	public long insert(int count, Double dailyProfit,Double exchangeProfit,  double totleProfit) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		String date = sDateFormat.format(new java.util.Date());
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		// 第一张表，股票基本信息
		cv.put(STOCK_DATE, date);
		cv.put(STOCK_COUNT, count);
		cv.put(STOCK_DAILY_PROFIT, dailyProfit);
		cv.put(STOCK_DAILY_PROFIT, exchangeProfit);
		cv.put(STOCK_TOTLE_PROFIT, totleProfit);
		long row = db.insert(TABLE_NAME_B, null, cv);
		return row;
	}

	public long insert(String code, String name, int number, double cost,
			double price, double money, double profit) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		// 第一张表，股票基本信息
		cv.put(STOCK_CODE, code);
		cv.put(STOCK_NAME, name);
		cv.put(STOCK_NUMBER, number);
		cv.put(STOCK_COST, cost);
		cv.put(STOCK_PRICE, price);
		cv.put(STOCK_MONEY, money);
		cv.put(STOCK_PROFIT, profit);
		long row = db.insert(TABLE_NAME_A, null, cv);
		return row;
	}

	// 删除操作
	public void delete(String code) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = STOCK_CODE + " = ?";
		String[] whereValue = { code};
		db.delete(TABLE_NAME_A, where, whereValue);
	}

	public MyStock checkCode(String c) {
		Log.v("ttt", c);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from stock_table where stock_code=?",
				new String[] { c });
		if (cursor.getCount() == 1 && cursor.moveToFirst()) {
			String name = cursor.getString(cursor.getColumnIndex("stock_name"));
			String code = cursor.getString(cursor.getColumnIndex("stock_code"));
			int number = cursor.getInt(cursor.getColumnIndex("stock_number"));
			double cost = cursor.getDouble(cursor.getColumnIndex("stock_cost"));
			double price = cursor.getDouble(cursor
					.getColumnIndex("stock_price"));
			double money = cursor.getDouble(cursor
					.getColumnIndex("stock_money"));
			double profit = cursor.getDouble(cursor
					.getColumnIndex("stock_profit"));
			MyStock stock = new MyStock(name, code, number, cost, price, money,
					profit);
			return stock;

		} else {
			return null;
		}
	}

	// 修改操作
	public void update(String code, String name, int number, double cost,
			double price, double money, double profit) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = STOCK_CODE + " = ?";
		String[] whereValue = { code };

		ContentValues cv = new ContentValues();
		cv.put(STOCK_CODE, code);
		cv.put(STOCK_NAME, name);
		cv.put(STOCK_NUMBER, number);
		cv.put(STOCK_COST, cost);
		cv.put(STOCK_PRICE, price);
		cv.put(STOCK_MONEY, money);
		cv.put(STOCK_PROFIT, profit);
		db.update(TABLE_NAME_A, cv, where, whereValue);
	}

	public void update(String date, int count, double dailyProfit,double exchangeProfit,
			double totleProfit) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = STOCK_DATE + " = ?";
		String[] whereValue = { date };

		ContentValues cv = new ContentValues();
		cv.put(STOCK_DATE, date);
		cv.put(STOCK_COUNT, count);
		cv.put(STOCK_DAILY_PROFIT, dailyProfit);
		cv.put(STOCK_EXCHANGE_PROFIT, exchangeProfit);
		cv.put(STOCK_TOTLE_PROFIT, totleProfit);
		db.update(TABLE_NAME_B, cv, where, whereValue);
	}

	public ArrayList<StockProfit> getProfits() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from stock_table_profit", null);
		ArrayList<StockProfit> stockProfitList = new ArrayList<StockProfit>();
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		for (int i = 0; i < resultCounts; i++) {
			String date = cursor.getString(cursor
					.getColumnIndex("stock_profit_date"));
			int count = cursor.getInt(cursor
					.getColumnIndex("stock_profit_count"));
			double dailyProfit = cursor.getDouble(cursor
					.getColumnIndex("stock_daily_profit"));
			double exchangeProfit = cursor.getDouble(cursor
					.getColumnIndex("stock_exchange_profit"));
			double totleProfit = cursor.getDouble(cursor
					.getColumnIndex("stock_totle_profit"));
			Log.v("ttt", date + count + "" + dailyProfit + totleProfit);
			StockProfit stockProfit = new StockProfit(date, count, dailyProfit,exchangeProfit,
					totleProfit);
			stockProfitList.add(stockProfit);

			cursor.moveToNext();
		}

		cursor.close();
		return stockProfitList;
	}

	public ArrayList<MyStock> getAllStockData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from stock_table", null);
		ArrayList<MyStock> stockList = new ArrayList<MyStock>();
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}

		for (int i = 0; i < resultCounts; i++) {
			String name = cursor.getString(cursor.getColumnIndex("stock_name"));
			String code = cursor.getString(cursor.getColumnIndex("stock_code"));
			int number = cursor.getInt(cursor.getColumnIndex("stock_number"));
			double cost = cursor.getDouble(cursor.getColumnIndex("stock_cost"));
			double price = cursor.getDouble(cursor
					.getColumnIndex("stock_price"));
			double money = cursor.getDouble(cursor
					.getColumnIndex("stock_money"));
			double profit = cursor.getDouble(cursor
					.getColumnIndex("stock_profit"));
			MyStock stock = new MyStock(name, code, number, cost, price, money,
					profit);
			stockList.add(stock);

			cursor.moveToNext();
		}

		cursor.close();
		return stockList;
	}
}