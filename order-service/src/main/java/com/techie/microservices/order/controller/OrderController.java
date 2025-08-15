package com.techie.microservices.order.controller;

import com.techie.microservices.order.dto.OrderRequest;
import com.techie.microservices.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderService orderService;
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String placeOrder(@RequestBody OrderRequest orderRequest) {
    orderService.placeOrder(orderRequest);
    log.info("Incoming payload: {}", orderRequest);
    return "Order Placed Successfully";
  }
}
