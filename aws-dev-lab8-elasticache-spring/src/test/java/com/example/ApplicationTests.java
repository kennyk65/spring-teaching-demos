package com.example;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired PharmService service;
	@Autowired CacheManager cacheManager;
	
//	WARNING - This code can only run within AWS, AWS ElastiCache cannot be connected to externally.

	@Test
	public void contextLoads() {
	
		String info = service.getPharmaInfo("Octinoxate");
		assertTrue(info.indexOf("sunscreen") > 5);
		
		info = service.getPharmaInfo("Warfarin");
		assertTrue(info.indexOf("blood thinner") > 5);
		
		info = service.getPharmaInfo("Ibuprofen");
		assertTrue(info.indexOf("inflammatory") > 5);

		info = service.getPharmaInfo("Acetaminophen");
		assertTrue(info.indexOf("fever reducer") > 5);
		
		//	The real test: is this stuff in the cache?
		Cache cache = cacheManager.getCache("springexample");
		
		info = cache.get("Octinoxate", String.class);
		assertTrue(info.indexOf("sunscreen") > 5);
		
		info = cache.get("Warfarin", String.class);
		assertTrue(info.indexOf("blood thinner") > 5);
		
		info = cache.get("Ibuprofen", String.class);
		assertTrue(info.indexOf("inflammatory") > 5);
		
		info = cache.get("Acetaminophen", String.class);
		assertTrue(info.indexOf("fever reducer") > 5);
		
	}

}
