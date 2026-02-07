package dev.nelon.ecommerce.product;

import java.math.BigDecimal;

public record PurchaseResponse(
	Integer product,
	String name,
	String description,
	BigDecimal price,
	double quantity
) {
}
