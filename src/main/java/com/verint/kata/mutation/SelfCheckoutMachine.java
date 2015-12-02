package com.verint.kata.mutation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.verint.kata.mutation.store.GiftCard;
import com.verint.kata.mutation.store.Product;
import com.verint.kata.mutation.util.ICreditCardValidator;
import com.verint.kata.mutation.util.StoreBuilder;

public class SelfCheckoutMachine {

	private List<Product> shoppingCart = new ArrayList<Product>();

	private Double currentTotal = 0D;
	private Double cashTotal = 0D;
	private Double change = 0D;
	private boolean checkoutCompleted = false;

	private Map<String, GiftCard> availableGiftCardMap = new HashMap<String, GiftCard>();
	private ICreditCardValidator ccValidator;

	public Double scanProduct(String id) {
		Product product = StoreBuilder.getProductMap().get(id);
		if (product != null) {
			shoppingCart.add(product);
			currentTotal += product.getPrice();
			return product.getPrice();
		}

		return null;
	}

	public GiftCard.GIFT_CARD_STATE addGiftCard(String code) {
		GiftCard giftCard = StoreBuilder.getGiftCardMap().get(code);
		Boolean used = availableGiftCardMap.containsKey(code);
		Boolean expired = giftCard.isExpired();
		if (giftCard != null && !used && !expired) {
			availableGiftCardMap.put(code, giftCard);
			currentTotal -= giftCard.getValue();
			currentTotal = Math.max(Double.valueOf(0), currentTotal);
			return GiftCard.GIFT_CARD_STATE.ACCEPTABLE;
		} else if (expired) {
			return GiftCard.GIFT_CARD_STATE.EXPIRED;
		}
		return GiftCard.GIFT_CARD_STATE.INVALID;
	}

	public void payWithCash(double value) {
		if (getCurrentTotal() > this.cashTotal) {
			this.cashTotal += value;
		}

		if (getCurrentTotal() <= this.cashTotal) {
			change = this.cashTotal - getCurrentTotal();
			checkoutCompleted = true;
		}
	}

	public void payWithCreditCard(String cardNumber) {
		if (ccValidator.validate(cardNumber)) {
			this.checkoutCompleted = true;
		}
	}

	public List<Product> getShoppingCart() {
		return shoppingCart;
	}

	public Double getCurrentTotal() {
		return currentTotal;
	}

	public Double getChange() {
		return change;
	}

	public boolean isCheckoutCompleted() {
		return checkoutCompleted;
	}

	public Double getCashTotal() {
		return cashTotal;
	}

	public void setValidatorClass(ICreditCardValidator classValidator) {
		this.ccValidator = classValidator;
	}

}
