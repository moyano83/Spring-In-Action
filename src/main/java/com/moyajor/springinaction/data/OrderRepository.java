package com.moyajor.springinaction.data;

import com.moyajor.springinaction.model.Order;

public interface OrderRepository {
    Order save(Order order);

}
