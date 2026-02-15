package com.digitalkhata.api.repository;

import com.digitalkhata.api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Multi-tenant: Get all customers for specific shop owner
    List<Customer> findByShopOwnerId(Long shopOwnerId);

    // Multi-tenant: Get single customer only if belongs to shop owner
    Optional<Customer> findByIdAndShopOwnerId(Long id, Long shopOwnerId);

    // Check if email exists for this shop owner
    boolean existsByEmailAndShopOwnerId(String email, Long shopOwnerId);

    // Delete only if belongs to shop owner
    void deleteByIdAndShopOwnerId(Long id, Long shopOwnerId);
}