package com.moyajor.springinaction.data;

import com.moyajor.springinaction.model.db.Order;
import com.moyajor.springinaction.model.db.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);

    List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
