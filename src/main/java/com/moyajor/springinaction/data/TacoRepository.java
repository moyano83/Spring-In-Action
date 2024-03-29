package com.moyajor.springinaction.data;

import com.moyajor.springinaction.model.db.Taco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacoRepository extends JpaRepository<Taco, Long> {

    Taco save(Taco design);
}
