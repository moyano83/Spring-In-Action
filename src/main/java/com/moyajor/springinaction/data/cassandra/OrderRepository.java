package com.moyajor.springinaction.data.cassandra;

import com.moyajor.springinaction.model.cassandra.Order;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import java.util.UUID;

public interface OrderRepository extends ReactiveCassandraRepository<Order, UUID> {}