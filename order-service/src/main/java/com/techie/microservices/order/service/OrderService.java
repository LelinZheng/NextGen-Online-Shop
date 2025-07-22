package com.techie.microservices.order.service;

import com.techie.microservices.order.client.InventoryClient;
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
  private final InventoryClient inventoryClient;

  public void placeOrder(OrderRequest orderedRequest){
    var isProductInStock = inventoryClient.isInStock(orderedRequest.skuCode(), orderedRequest.quantity());
    // map OrderRequest to Order object
    if(isProductInStock) {
      Order order = new Order();
      order.setOrderNumber(UUID.randomUUID().toString());
      order.setPrice(orderedRequest.price());
      order.setSkuCode(orderedRequest.skuCode());
      order.setQuantity(orderedRequest.quantity());
      // save order to OrderRepository
      orderRepository.save(order);
    }else{
      throw new RuntimeException("Product with SkuCode " + orderedRequest.skuCode() + " is not in stock");
    }
  }

}
