package dev.nelon.ecommerce.kafka;

import dev.nelon.ecommerce.customer.CustomerResponse;
import dev.nelon.ecommerce.order.PaymentMethod;
import dev.nelon.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
	String orderReference,
	BigDecimal totalAmount,
	PaymentMethod paymentMethod,
	CustomerResponse customer,
	List<PurchaseResponse> products
) {
}
