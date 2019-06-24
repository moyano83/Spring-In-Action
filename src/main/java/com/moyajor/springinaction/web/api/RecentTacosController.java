package com.moyajor.springinaction.web.api;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import java.util.List;

import com.moyajor.springinaction.data.jpa.TacoRepository;
import com.moyajor.springinaction.model.Taco;
import com.moyajor.springinaction.web.api.resource.TacoResource;
import com.moyajor.springinaction.web.api.resource.TacoResourceAssembler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RepositoryRestController
public class RecentTacosController {
    private TacoRepository tacoRepo;
    public RecentTacosController(TacoRepository tacoRepo) {
        this.tacoRepo = tacoRepo;
    }
    @GetMapping(path="/tacos/recent", produces="application/hal+json")
    public ResponseEntity<Resources<TacoResource>> recentTacos() {

        PageRequest page = PageRequest.of(
                0, 12, Sort.by("createdAt").descending());
        List<Taco> tacos = tacoRepo.findAll(page).getContent();
        List<TacoResource> tacoResources =
                new TacoResourceAssembler().toResources(tacos);
        Resources<TacoResource> recentResources =
                new Resources<TacoResource>(tacoResources);
        recentResources.add(
                linkTo(methodOn(RecentTacosController.class).recentTacos())
                        .withRel("recents"));
        return new ResponseEntity<>(recentResources, HttpStatus.OK);
    }
}

