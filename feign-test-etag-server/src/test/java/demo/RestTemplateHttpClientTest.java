package demo;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class RestTemplateHttpClientTest {

	@Test
	public void testRestTemplate() {
		
		
//		CacheConfig cacheConfig = new CacheConfig();  
//		cacheConfig.setMaxCacheEntries(1000);
//		cacheConfig.setMaxObjectSize(8192);
//
//		HttpClient cachingClient = new CachingHttpClient( new DefaultHttpClient(), cacheConfig );
//
//		ClientHttpRequestFactory requestFactory = new       
//		    HttpComponentsClientHttpRequestFactory(cachingClient);
//
//		RestTemplate rest = new RestTemplate(requestFactory);		
//		

		CacheConfig cacheConfig = CacheConfig.custom()
		        .setMaxCacheEntries(1000)
		        .setMaxObjectSize(8192)
		        .build();
		CloseableHttpClient cachingClient = CachingHttpClients.custom()
		        .setCacheConfig(cacheConfig)
		        .build();
		
		RestTemplate template = 
			new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(cachingClient));		
		
		HttpEntity<String> entity = null;
		entity = template.getForEntity("http://localhost:8081", String.class);
		entity = template.getForEntity("http://localhost:8081", String.class);
		entity = template.getForEntity("http://localhost:8081", String.class);
		entity = template.getForEntity("http://localhost:8081", String.class);
		template.getForObject("http://localhost:8081", String.class);
		template.getForObject("http://localhost:8081", String.class);
		
	}
	
}
