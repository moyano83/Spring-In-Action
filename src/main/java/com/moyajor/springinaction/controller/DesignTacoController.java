package com.moyajor.springinaction.controller;

import com.moyajor.springinaction.data.IngredientRepository;
import com.moyajor.springinaction.data.TacoRepository;
import com.moyajor.springinaction.model.Ingredient;
import com.moyajor.springinaction.model.Ingredient.Type;
import com.moyajor.springinaction.model.Order;
import com.moyajor.springinaction.model.Taco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    @Autowired
    private IngredientRepository repository;

    @Autowired
    private TacoRepository tacoRepository;

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }
    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        for(Ingredient ingredient : repository.findAll()){
            ingredients.add(ingredient);
        }

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
        model.addAttribute("design", new Taco());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        Taco saved = tacoRepository.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }

    public List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
        List<Ingredient> returnList = new ArrayList<>();
        for (Ingredient ingredient: ingredients){
            if(ingredient.getType().equals(type)){
                returnList.add(ingredient);
            }
        }
        return returnList;
    }

}
