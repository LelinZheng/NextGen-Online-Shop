package com.techie.microservices.order.service;

import com.techie.microservices.order.dto.OrderRequest;
import com.techie.microservices.order.model.Order;
import com.techie.microservices.order.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  public void placeOrder(OrderRequest orderedRequest){
    // map OrderRequest to Order object
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());
    order.setPrice(orderedRequest.price());
    order.setSkuCode(orderedRequest.skuCode());
    order.setQuantity(orderedRequest.quantity());

    // save order to OrderRepository
    orderRepository.save(order);
  }

}
