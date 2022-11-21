package com.consulteer.shoppingstore.controllers;

import com.consulteer.shoppingstore.dtos.OrderDto;
import com.consulteer.shoppingstore.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Page<OrderDto>> getAllOrders(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable page) {
        return ResponseEntity.ok(orderService.getAllOrders(page));
    }

    @GetMapping("/user/{user-id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    public ResponseEntity<Page<OrderDto>> getAllOrdersByUserId(@PathVariable("user-id") Long userId, Pageable page) {
        return ResponseEntity.ok(orderService.getAllOrdersByUserId(userId, page));
    }

    @GetMapping("/user/{user-id}/date")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    public ResponseEntity<Page<OrderDto>> getAllOrdersByUserIdAndTimeInterval(@PathVariable("user-id") Long userId,
                                                                              Pageable page,
                                                                              @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                              @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {

        return ResponseEntity.ok(orderService.getAllOrdersByUserIdAndTimeInterval(userId, page, from, to));
    }

    @GetMapping("/{order-id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("order-id") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
