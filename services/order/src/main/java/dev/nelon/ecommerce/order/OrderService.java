package dev.nelon.ecommerce.order;

import dev.nelon.ecommerce.customer.CustomerClient;
import dev.nelon.ecommerce.exception.BusinessException;
import dev.nelon.ecommerce.product.ProductClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final CustomerClient customerClient;
	private final ProductClient productClient;
	
	public Integer createOrder(@Valid OrderRequest request) {
		var customer = this.customerClient.findCustomerById(request.customerId())
			.orElseThrow(() -> new BusinessException("Cannot create order:: No Customer exist with the provided ID"));
		
		
		return null;
	}
}
