package com.example.pr7;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Flux<Order> findAll(Sort sort);
    Flux<Order> findByCustomerEmailIgnoreCase(String customerEmail);
    Flux<Order> findByStatus(String status);
}
