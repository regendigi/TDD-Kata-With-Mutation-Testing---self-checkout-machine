package com.verint.kata.mutation.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {
	
	private Product product;
	
	@Before
	public void setup(){
		product = new Product("KitKat", "KitKat Chocolate Bar", 28000D);
	}

    @Test
    public void testProductId() {
        assertEquals("KitKat", product.getId());
    }

    @Test
    public void testProductName() {
        assertEquals("KitKat Chocolate Bar", product.getName());
    }

    @Test
    public void testProductPrice() {
        assertEquals(28000, product.getPrice(), 0.1);
    }

}
