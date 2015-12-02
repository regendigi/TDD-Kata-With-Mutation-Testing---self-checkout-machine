package com.verint.kata.mutation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.verint.kata.mutation.store.GiftCard;
import com.verint.kata.mutation.util.ICreditCardValidator;

public class SelfCheckoutMachineTest {

	private SelfCheckoutMachine machine;
	private ICreditCardValidator ccValidator;

	@Before
	public void setUp() {
		machine = new SelfCheckoutMachine();
		
		//Mock for Credit Card Validator class
		ccValidator = Mockito.mock(ICreditCardValidator.class);
		machine.setValidatorClass(ccValidator);
		Mockito.when(ccValidator.validate("1234567890")).thenReturn(true);
		Mockito.when(ccValidator.validate("0987654321")).thenReturn(false);
	}

	@Test
	public void testScanProductWithValidProductIDReturnPrice() {
		assertEquals(Double.valueOf(5000), machine.scanProduct("ABC0001"));
	}

	@Test
	public void testScanProductWithInvalidProductIDReturnNull() {
		assertEquals(null, machine.scanProduct("fakeProduct"));
	}

	/*
	 * Test Update Cart and Total Purchase
	 */
	@Test
	public void testScanValidProductWillUpdateShoppingCartAndTotalPurchase() {
		machine.scanProduct("ABC0001");
		assertEquals(1, machine.getShoppingCart().size());
		assertEquals(Double.valueOf(5000), machine.getCurrentTotal());
	}

	/*
	 * Test Add and validate gift card
	 */
	@Test
	public void testAddValidGIftCard() {
		assertEquals(GiftCard.GIFT_CARD_STATE.ACCEPTABLE,
				machine.addGiftCard("987ZYX"));
	}

	@Test
	public void testAddExpiredGIftCard() {
		assertEquals(GiftCard.GIFT_CARD_STATE.EXPIRED,
				machine.addGiftCard("987BKD"));
	}

	@Test
	public void testAddInvalidGiftCard() {
		machine.addGiftCard("987ZYX");
		assertEquals(GiftCard.GIFT_CARD_STATE.INVALID,
				machine.addGiftCard("987ZYX"));
	}

	@Test
	public void testGiftCardValue() {
		machine.addGiftCard("987ZYX");
		assertEquals(Double.valueOf(10000), machine.getGiftCardValue());
	}

	/*
	 * Test the effects of using gift cards to total purchase
	 */

	@Test
	public void testValidGiftCardReduceTotalPayment() {
		machine.scanProduct("ABC0001");
		machine.scanProduct("ABC0002");
		assertEquals(Double.valueOf(11000), machine.getCurrentTotal());
		machine.addGiftCard("987ZYX");
		assertEquals(Double.valueOf(1000), machine.getCurrentTotal());
	}

	@Test
	public void testIfTotalValueOfTheGiftCardIsMoreThanTotalPurchaseNoChangeGiven() {
		machine.scanProduct("ABC0001");
		assertEquals(Double.valueOf(5000), machine.getCurrentTotal());
		machine.addGiftCard("987ZYX");
		assertEquals(Double.valueOf(10000), machine.getGiftCardValue());
		assertEquals(Double.valueOf(0), machine.getCurrentTotal());
		assertEquals(Double.valueOf(0), machine.getChange());
	}

	@Test
	public void testIfTotalValueOfTheGiftCardIsEqualsTotalPurchase() {
		machine.scanProduct("DEF0003");
		machine.addGiftCard("987ZYX");
		assertEquals(Double.valueOf(0), machine.getCurrentTotal());
	}

	/*
	 * Test paying with cash, and receive change if necessary
	 */
	@Test
	public void testPayWithExactSingleCash() {
		machine.scanProduct("DEF0001");
		machine.scanProduct("DEF0002");
		machine.scanProduct("DEF0003");
		machine.payWithCash(Double.valueOf(22500));
		assertTrue(machine.isCheckoutCompleted());
		assertEquals(Double.valueOf(0), machine.getChange());
	}

	@Test
	public void testIfCashTotalIsAlreadyEnoughForPurchaseItShouldNotAcceptAnymoreCash() {
		machine.scanProduct("DEF0003");
		machine.payWithCash(Double.valueOf(5000));
		machine.payWithCash(Double.valueOf(5000));
		machine.payWithCash(Double.valueOf(5000));
		assertEquals(Double.valueOf(10000), machine.getCashTotal());
	}
	
	@Test
	public void testPayWithCashGiveChanges(){
		machine.scanProduct("ABC0001");
		assertEquals(Double.valueOf(5000), machine.getCurrentTotal());
		machine.payWithCash(Double.valueOf(10000));
		assertEquals(Double.valueOf(5000), machine.getChange());
		assertTrue(machine.isCheckoutCompleted());
	}
	
	@Test
	public void testPayWithCashMultipleTimes(){
		machine.scanProduct("ABC0001");
		machine.scanProduct("ABC0002");
		machine.scanProduct("DEF0001");
		assertEquals(Double.valueOf(15500), machine.getCurrentTotal());
		machine.payWithCash(Double.valueOf(5000));
		machine.payWithCash(Double.valueOf(5000));
		machine.payWithCash(Double.valueOf(5000));
		assertFalse(machine.isCheckoutCompleted());
		machine.payWithCash(Double.valueOf(5000));
		assertTrue(machine.isCheckoutCompleted());
		assertEquals(Double.valueOf(4500), machine.getChange());
	}

	/*
	 * Test paying with credit card
	 */
	@Test
	public void testPayWithValidCreditCard() {
		machine.scanProduct("GHI0001");
		machine.scanProduct("GHI0002");
		machine.payWithCreditCard("1234567890");
		assertEquals(Double.valueOf(0), machine.getChange());
		assertTrue(machine.isCheckoutCompleted());
	}
	
	@Test
	public void testPayWithInvalidCreditCard(){
		machine.scanProduct("GHI0001");
		machine.scanProduct("GHI0002");
		machine.payWithCreditCard("0987654321");
		assertFalse(machine.isCheckoutCompleted());
	}

}
