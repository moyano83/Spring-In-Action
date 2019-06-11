package com.moyajor.springinaction.data;

import com.moyajor.springinaction.model.Taco;

public interface TacoRepository {

    Taco save(Taco design);
}
