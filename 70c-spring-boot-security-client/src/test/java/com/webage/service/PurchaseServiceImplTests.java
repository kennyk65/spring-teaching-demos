package com.webage.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.webage.dao.AuthorizationServerDao;
import com.webage.dao.ResourceServerDao;
import com.webage.domain.Customer;
import com.webage.domain.Purchase;

/**
 * This test exercises the PurchaseServiceImpl.
 * This is 100% Mockito, and a true unit test.
*/
@ExtendWith(MockitoExtension.class)
public class PurchaseServiceImplTests {

    @Mock           AuthorizationServerDao authorizationServerDao;
    @Mock           ResourceServerDao resourceServerDao;
    @InjectMocks    PurchaseServiceImpl purchaseService;
    
    @Test
    //@Disabled
    public void testFindAllPurchases() {
        // Mock the behavior of the authorization service client
        String jwt = "mockJwt";
        when(authorizationServerDao.getJwt()).thenReturn(jwt);
        
        // Mock the behavior of the resource server client
        when(resourceServerDao.findAllPurchases(jwt)).thenReturn(expectedPurchases());
        
        // Call the method under test
        Iterable<Purchase> result = purchaseService.findAllPurchases();
        
        // Verify the result
        assertThat(result).containsExactlyElementsOf(expectedPurchases());        
        //assertEquals(expectedPurchases(), result);
        
        // Verify interactions
        verify(authorizationServerDao).getJwt();
        verify(resourceServerDao).findAllPurchases(jwt);
    }
    
    @Test
    //@Disabled
    public void testFindPurchaseById() {
        // Mock the behavior of the authorization service client
        String jwt = "mockJwt";
        when(authorizationServerDao.getJwt()).thenReturn(jwt);
        
        // Mock the behavior of the resource server client
        long purchaseId = 1L; // Adjust as needed
        when(resourceServerDao.findPurchaseById(jwt, purchaseId)).thenReturn(expectedPurchase());
        
        // Call the method under test
        Optional<Purchase> result = purchaseService.findPurchaseById(purchaseId);
        
        // Verify the result
        assertEquals(expectedPurchase(), result);
        
        // Verify interactions
        verify(authorizationServerDao).getJwt();
        verify(resourceServerDao).findPurchaseById(jwt, purchaseId);
    }

    private Optional<Purchase> expectedPurchase() { 
        return Optional.of(expectedPurchases().get(0));
    }

    private List<Purchase> expectedPurchases() {
		Customer c = new Customer();
		c.setName("test");
		c.setId(1);
		c.setEmail("a@a.com");
		Purchase p = new Purchase();
		p.setId(1);
		p.setProduct("something");
		p.setCustomer(c);

        return Arrays.asList(p);
    }
}
