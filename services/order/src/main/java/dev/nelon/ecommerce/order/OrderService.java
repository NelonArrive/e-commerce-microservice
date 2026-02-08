package dev.nelon.ecommerce.order;

import dev.nelon.ecommerce.customer.CustomerClient;
import dev.nelon.ecommerce.exception.BusinessException;
import dev.nelon.ecommerce.kafka.OrderConfirmation;
import dev.nelon.ecommerce.kafka.OrderProducer;
import dev.nelon.ecommerce.orderline.OrderLineRequest;
import dev.nelon.ecommerce.orderline.OrderLineService;
import dev.nelon.ecommerce.product.ProductClient;
import dev.nelon.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository repository;
	private final CustomerClient customerClient;
	private final ProductClient productClient;
	private final OrderMapper mapper;
	private final OrderLineService orderLineService;
	private final OrderProducer orderProducer;
	
	public Integer createOrder(OrderRequest request) {
		var customer = this.customerClient.findCustomerById(request.customerId())
			.orElseThrow(() -> new BusinessException("Cannot create order:: No Customer exist with the provided ID"));
		
		var purchasedProducts = this.productClient.purchaseProducts(request.products());
		
		var order = this.repository.save(mapper.toOrder(request));
		
		for (PurchaseRequest purchaseRequest : request.products()) {
			orderLineService.saveOrderLine(
				new OrderLineRequest(
					null,
					order.getId(),
					purchaseRequest.productId(),
					purchaseRequest.quantity()
				)
			);
		}
		
		orderProducer.sendOrderConfirmation(
			new OrderConfirmation(
				request.reference(),
				request.amount(),
				request.paymentMethod(),
				customer,
				purchasedProducts
			)
		);
		
		return order.getId();
	}
	
	public List<OrderResponse> findAll() {
		return repository.findAll()
			.stream()
			.map(mapper::fromOrder)
			.collect(Collectors.toList());
	}
	
	public OrderResponse findById(Integer orderId) {
		return repository.findById(orderId)
			.map(mapper::fromOrder)
			.orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided Id: %d", orderId)));
	}
}
