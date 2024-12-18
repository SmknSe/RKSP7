package com.example.pr7;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Pr7ApplicationTests {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order createSampleOrder(Long id) {
        return new Order(
                id,
                "John Doe",
                "john@example.com",
                "123 Main St, City",
                new BigDecimal("99.99"),
                "NEW",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void getAllOrders() {
        Order order1 = createSampleOrder(1L);
        Order order2 = createSampleOrder(2L);
        when(orderRepository.findAll()).thenReturn(Flux.just(order1, order2));

        Flux<Order> result = orderService.getAllOrders();

        StepVerifier.create(result)
                .expectNext(order1)
                .expectNext(order2)
                .verifyComplete();
    }

    @Test
    void getOrderById() {
        Long id = 1L;
        Order order = createSampleOrder(id);
        when(orderRepository.findById(id)).thenReturn(Mono.just(order));

        Mono<Order> result = orderService.getOrderById(id);

        StepVerifier.create(result)
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    void createOrder() {
        Order newOrder = createSampleOrder(null);
        Order savedOrder = createSampleOrder(1L);
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(savedOrder));

        Mono<Order> result = orderService.createOrder(newOrder);

        StepVerifier.create(result)
                .expectNext(savedOrder)
                .verifyComplete();

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void updateOrder() {
        Long id = 1L;
        Order existingOrder = createSampleOrder(id);
        Order updatedOrder = new Order(
                id,
                "Jane Doe",
                "jane@example.com",
                "456 Oak St, City",
                new BigDecimal("149.99"),
                "PROCESSING",
                existingOrder.getOrderDate(),
                LocalDateTime.now()
        );

        when(orderRepository.findById(id)).thenReturn(Mono.just(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(updatedOrder));

        Mono<Order> result = orderService.updateOrder(id, updatedOrder);

        StepVerifier.create(result)
                .expectNext(updatedOrder)
                .verifyComplete();
    }

    @Test
    void updateOrderStatus() {
        Long id = 1L;
        Order existingOrder = createSampleOrder(id);
        Order updatedOrder = new Order(
                id,
                existingOrder.getCustomerName(),
                existingOrder.getCustomerEmail(),
                existingOrder.getDeliveryAddress(),
                existingOrder.getTotalAmount(),
                "SHIPPED",
                existingOrder.getOrderDate(),
                LocalDateTime.now()
        );

        when(orderRepository.findById(id)).thenReturn(Mono.just(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(updatedOrder));

        Mono<Order> result = orderService.updateOrderStatus(id, "SHIPPED");

        StepVerifier.create(result)
                .expectNext(updatedOrder)
                .verifyComplete();
    }

    @Test
    void getOrdersByCustomerEmail() {
        String email = "john@example.com";
        Order order1 = createSampleOrder(1L);
        Order order2 = createSampleOrder(2L);
        when(orderRepository.findByCustomerEmailIgnoreCase(email))
                .thenReturn(Flux.just(order1, order2));

        Flux<Order> result = orderService.getOrdersByCustomerEmail(email);

        StepVerifier.create(result)
                .expectNext(order1)
                .expectNext(order2)
                .verifyComplete();
    }

    @Test
    void getOrdersByStatus() {
        String status = "PROCESSING";
        Order order1 = new Order(
                1L,
                "John Doe",
                "john@example.com",
                "123 Main St, City",
                new BigDecimal("99.99"),
                status,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Order order2 = new Order(
                2L,
                "Jane Doe",
                "jane@example.com",
                "456 Oak St, City",
                new BigDecimal("149.99"),
                status,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepository.findByStatus(status)).thenReturn(Flux.just(order1, order2));

        Flux<Order> result = orderService.getOrdersByStatus(status);

        StepVerifier.create(result)
                .expectNext(order1)
                .expectNext(order2)
                .verifyComplete();
    }

    @Test
    void deleteOrder() {
        Long id = 1L;
        when(orderRepository.deleteById(id)).thenReturn(Mono.empty());

        Mono<Void> result = orderService.deleteOrder(id);

        StepVerifier.create(result)
                .verifyComplete();

        verify(orderRepository).deleteById(id);
    }
}