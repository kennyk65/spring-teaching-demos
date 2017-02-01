package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PharmServiceImpl implements PharmService {

	@Autowired DynamoDBManager dao;
	
	@Cacheable(value="springexample", key="#drugName")
	public String getPharmaInfo(String drugName) {
		return dao.getPharmaInfo(drugName);
	}

}
