package com.example.pr7;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class OrderService {

    private final OrderRepository orderRepository;


    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Flux<Order> getAllOrders(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest.getSort())
                .skip((long) pageRequest.getPageNumber() * pageRequest.getPageSize())
                .take(pageRequest.getPageSize());
    }

    public Mono<Order> getOrderById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "Order not found")));
    }

    public Mono<Order> createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setLastUpdated(LocalDateTime.now());
        order.setStatus("NEW");
        return orderRepository.save(order);
    }

    public Mono<Order> updateOrder(Long id, Order order) {
        return orderRepository.findById(id)
                .flatMap(existingOrder -> {
                    existingOrder.setCustomerName(order.getCustomerName());
                    existingOrder.setCustomerEmail(order.getCustomerEmail());
                    existingOrder.setDeliveryAddress(order.getDeliveryAddress());
                    existingOrder.setTotalAmount(order.getTotalAmount());
                    existingOrder.setStatus(order.getStatus());
                    existingOrder.setLastUpdated(LocalDateTime.now());
                    return orderRepository.save(existingOrder);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")));
    }

    public Mono<Order> updateOrderStatus(Long id, String status) {
        return orderRepository.findById(id)
                .flatMap(existingOrder -> {
                    existingOrder.setStatus(status);
                    existingOrder.setLastUpdated(LocalDateTime.now());
                    return orderRepository.save(existingOrder);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")));
    }

    public Mono<Void> deleteOrder(Long id) {
        return orderRepository.deleteById(id);
    }

    public Flux<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailIgnoreCase(email);
    }

    public Flux<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
