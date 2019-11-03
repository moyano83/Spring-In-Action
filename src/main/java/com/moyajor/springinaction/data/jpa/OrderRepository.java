package com.moyajor.springinaction.data.jpa;

import com.moyajor.springinaction.model.db.Order;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface OrderRepository
        extends CrudRepository<Order, Long> {
}
