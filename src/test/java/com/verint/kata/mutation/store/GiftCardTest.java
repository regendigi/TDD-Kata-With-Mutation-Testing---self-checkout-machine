package com.verint.kata.mutation.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;

public class GiftCardTest {
	
	private GiftCard giftCard;

	@Before
	public void setUp() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7); //Add 7 days from current date
		giftCard = new GiftCard("XYZ123", Double.valueOf(10000), cal.getTime());
	}

	@Test
	public void testGiftCardId() {
		assertEquals("XYZ123", giftCard.getCode());
	}

	@Test
	public void testGiftCardValue() {
		assertEquals(10000, giftCard.getValue(), 0.1);
	}

	@Test
	public void testGiftCardExpirationDate() {
		Calendar expectedCal = Calendar.getInstance();
		expectedCal.add(Calendar.DATE, 7); //Add 7 days from current date
		assertEquals(expectedCal.getTime(), giftCard.getExpirationDate());
	}
	
	@Test
	public void testGiftCardIsNotExpired() {
		assertFalse(giftCard.isExpired());
	}

	@Test
	public void testGiftCardIsExpired() {
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		GiftCard card = new GiftCard("ABC123", Double.valueOf(10000), yesterday.getTime());
		assertTrue(card.isExpired());
	}

}
