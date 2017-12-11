package com.cgjz;

import java.math.BigDecimal;

public class MyStock {
	private String stockName,stockCode;
	private int stockNumber;
	private double stockCost,stockPrice,stockMoney,stockProfit;
	public MyStock(String name,String code,int number,double cost,double price,Double money,double profit){
		stockName=name;
		stockCode=code;
		stockNumber=number;
		stockCost=cost;
		stockPrice=price;
		stockMoney=money;//cost应为分时价，联网
		stockProfit=profit;
	}
	public String getStockName() {
		return stockName;
	}
	public String getStockCode(){
		return stockCode;
	}
	public int getStockNumber(){
		return stockNumber;
	}
	public double getStockCost(){
		
		double tempCost=new BigDecimal(stockCost)
		.setScale(2, BigDecimal.ROUND_HALF_UP)
		.doubleValue();
		return tempCost;
	}
	public double getStockPrice(){
		double tempPrice=new BigDecimal(stockPrice)
		.setScale(2, BigDecimal.ROUND_HALF_UP)
		.doubleValue();
		return tempPrice;
	}
	public double getStockMoney(){
		double tempMoney=new BigDecimal(stockMoney)
		.setScale(2, BigDecimal.ROUND_HALF_UP)
		.doubleValue();
		return tempMoney;
	}
	public double getStockProfit(){
		double tempProfit=new BigDecimal(stockProfit)
		.setScale(2, BigDecimal.ROUND_HALF_UP)
		.doubleValue();
		return tempProfit;
	}
	public void setStockName(String name){
		stockName=name;
	}
	public void setStockCode(String code){
		stockCode=code;
	}
	public void setStockNumber(int number){
		stockNumber=number;
	}
	public void setStockCost(double cost){
		stockCost=cost;
	}
	public void setStockPrice(double price){
		stockPrice=price;
	}
	public void setStockMoney(double money){
		stockMoney=money;
	}
	public void setStockProfit(double profit){
		stockProfit=profit;
	}
}
