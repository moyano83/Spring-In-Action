package com.moyajor.springinaction.data.jpa;

import com.moyajor.springinaction.model.Taco;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface TacoRepository extends CrudRepository<Taco, Long> {
}
