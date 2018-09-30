package demo;

import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.BasicHttpCacheStorage;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;
import feign.httpclient.ApacheHttpClient;

/**
 *	Specialized configuration to allow Feign to work correctly with eTags. 
 */
@Configuration
public class FeignEtagConfiguration {

	/**
	 *	This re-defines the FeignClient 
	 *  that will be used to make external calls: 
	 */
    @Bean
	public Client feignClient(CloseableHttpClient httpClient) {
		return new ApacheHttpClient(httpClient);
	}
	
    
    /**
     *	HttpClient used by Feign to make external calls, re-configured with caching capability.
     */
    @Bean
    public CloseableHttpClient httpClient(
    		CacheConfig cacheConfig, 
    		HttpCacheStorage storage,
    		PoolingHttpClientConnectionManager connectionMgr) {
    	return 
    		CachingHttpClientBuilder.create()
    		.setCacheConfig(cacheConfig)
    		.setHttpCacheStorage(storage)
    		.setConnectionManager(connectionMgr)
    		.disableRedirectHandling()
    		.build();
    }

    @Bean
    public CacheConfig cacheConfig() {
    	CacheConfig result = CacheConfig
           .custom()
           .setMaxCacheEntries(1000)
           .build();
         return result;
    }
    
    @Bean
    public HttpCacheStorage httpCacheStorage(CacheConfig cacheConfig) {
    	BasicHttpCacheStorage storage = new BasicHttpCacheStorage(cacheConfig);
    	return storage;
    }    
    
    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
         PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
         result.setMaxTotal(20);
         return result;
    }    
//    @Bean
//    public RestTemplate httpClientFactory() {
//        CacheConfig cacheConfig = new CacheConfig();  
//        cacheConfig.setMaxCacheEntries(1000);
//        cacheConfig.setMaxObjectSize(8192);
//
//        HttpClient cachingClient = new CachingHttpClient( new DefaultHttpClient(), cacheConfig );
//
//        ClientHttpRequestFactory requestFactory = new       
//            HttpComponentsClientHttpRequestFactory(cachingClient);
//
//        return new RestTemplate(requestFactory);
//    }
    
}
