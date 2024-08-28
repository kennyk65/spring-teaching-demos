package com.example;

import org.springframework.stereotype.Service;

@Service
public class StockService {

    public Double getStockPrice(String symbol) {
		//	For now, just return a hard-coded response:
		return Math.random()*800;
	}  

	public Integer getStockVolume(String symbol) {
		//	For now, just return a hard-coded response:
        return (int)(Math.random()*10000);
	}      

	public String getStockNews(String symbol) {
		//	For now, just return a hard-coded response:
        return "Activist investors are attempting a short squeeze.";
	}      

}
