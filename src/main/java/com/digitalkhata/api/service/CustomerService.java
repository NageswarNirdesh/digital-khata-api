package com.digitalkhata.api.service;

import com.digitalkhata.api.dto.CustomerRequest;
import com.digitalkhata.api.dto.CustomerResponse;
import com.digitalkhata.api.entity.Customer;
import com.digitalkhata.api.entity.User;
import com.digitalkhata.api.repository.CustomerRepository;
import com.digitalkhata.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    // Get current logged-in shop owner
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<CustomerResponse> getAllCustomers() {
        User currentUser = getCurrentUser();
        List<Customer> customers = customerRepository.findByShopOwnerId(currentUser.getId());

        return customers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        User currentUser = getCurrentUser();
        Customer customer = customerRepository.findByIdAndShopOwnerId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return toResponse(customer);
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        User currentUser = getCurrentUser();

        // Check if email already exists for this shop owner
        if (request.getEmail() != null &&
                customerRepository.existsByEmailAndShopOwnerId(request.getEmail(), currentUser.getId())) {
            throw new RuntimeException("Customer with this email already exists");
        }

        Customer customer = new Customer();
        customer.setShopOwnerId(currentUser.getId());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        User currentUser = getCurrentUser();
        Customer customer = customerRepository.findByIdAndShopOwnerId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        Customer updated = customerRepository.save(customer);
        return toResponse(updated);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        User currentUser = getCurrentUser();
        Customer customer = customerRepository.findByIdAndShopOwnerId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerRepository.delete(customer);
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        response.setLastTransactionDate(customer.getLastTransactionDate());
        response.setCreatedAt(customer.getCreatedAt());
        response.setPendingAmount(0.0);
        return response;
    }
}