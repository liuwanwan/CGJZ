package com.cgjz;

import java.math.BigDecimal;

public class StockProfit {
	private String stockDate;
	private int stockCount;
	private double stockDailyProfit,stockExchangeProfit,stockTotleProfit;
	public StockProfit(String date,int count,double dailyProfit,double exchangeProfit,double totleProfit){
		stockDate=date;
		stockCount=count;
		stockDailyProfit=dailyProfit;
		stockExchangeProfit=exchangeProfit;
		stockTotleProfit=totleProfit;
	}
	public String getStockDate() {
		return stockDate;
	}
	public int getStockCount(){
		return stockCount;
	}
	public double getStockDailyProfit(){
		
		double tempDailyProfit=new BigDecimal(stockDailyProfit)
		.setScale(2, BigDecimal.ROUND_HALF_UP)
		.doubleValue();
		return tempDailyProfit;
	}
public double getStockExchangeProfit(){
		
		double tempExchangeProfit=new BigDecimal(stockExchangeProfit)
		.setScale(2, BigDecimal.ROUND_HALF_UP)
		.doubleValue();
		return tempExchangeProfit;
	}
	public double getStockTotleProfit(){
		double tempTotleProfit=new BigDecimal(stockTotleProfit)
		.setScale(2, BigDecimal.ROUND_HALF_UP)
		.doubleValue();
		return tempTotleProfit;
	}
	public void setStockDate(String date){
		stockDate=date;
	}
	public void setStockCount(int count){
		stockCount=count;
	}
	public void setStockDailyProfit(double dailyProfit){
		stockDailyProfit=dailyProfit;
	}
	public void setStockExchangeProfit(double exchangeProfit){
		stockExchangeProfit=exchangeProfit;
	}
	public void setStockTotleProfit(double totleProfit){
		stockTotleProfit=totleProfit;
	}
}
