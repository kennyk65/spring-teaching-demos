package com.example;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired StockService stockService;

    @Bean
	@Description("Get stock price for a given symbol")
    public Function<StringRec, DoubleRec> getStockPriceFunction() {
        return (req) -> new DoubleRec(stockService.getStockPrice(req.value));
    }

    @Bean
	@Description("Get stock volume for a given symbol")
	public Function<StringRec, IntegerRec> getStockVolumeFunction() {
        return (req) -> new IntegerRec(stockService.getStockVolume(req.value));
    }

    @Bean
    @Description("Get stock news for a given symbol")
	public Function<StringRec, StringRec> getStockNewsFunction() {
        return (req) -> new StringRec(stockService.getStockNews(req.value));
    }

	public record StringRec(String value) {}
    public record IntegerRec(Integer value) {}
    public record DoubleRec(Double value) {}


    // @Bean
    // @Description("Get stock news for a given symbol")
	// public Function<String, String> getStockNewsFunction() {
    //     return (symbol) -> stockService.getStockNews(symbol);
    // }

}
