package dev.nelon.ecommerce.customer;

import dev.nelon.ecommerce.exception.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
	
	private final CustomerRepository repository;
	private final CustomerMapper mapper;
	
	public String createCustomer(CustomerRequest request) {
		var customer = repository.save(mapper.toCustomer(request));
		return customer.getId();
	}
	
	public void updateCustomer(@Valid CustomerRequest request) {
		var customer = repository.findById(request.id())
			.orElseThrow(() -> new CustomerNotFoundException
				("Cannot update customer. No customer found with the ID=" + request.id()));
		mergeCustomer(customer, request);
	}
	
	private void mergeCustomer(Customer customer, CustomerRequest request) {
		if (StringUtils.isNotBlank(request.firstname())) {
			customer.setFirstname(request.firstname());
		}
		if (StringUtils.isNotBlank(request.lastname())) {
			customer.setLastname(request.lastname());
		}
		if (StringUtils.isNotBlank(request.email())) {
			customer.setEmail(request.email());
		}
		if (request.address() != null) {
			customer.setAddress(request.address());
		}
		
	}
	
	public List<CustomerResponse> findAllCustomers() {
		return repository.findAll()
			.stream()
			.map(mapper::fromCustomer)
			.collect(Collectors.toList());
	}
	
	public Boolean existsById(String customerId) {
		return repository.findById(customerId).isPresent();
	}
	
	public CustomerResponse findById(String customerId) {
		return repository.findById(customerId)
			.map(mapper::fromCustomer)
			.orElseThrow(() -> new CustomerNotFoundException
				("No customer found with the ID=" + customerId));
	}
	
	
	public void deleteCustomer(String customerId) {
		repository.deleteById(customerId);
	}
}
