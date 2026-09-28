package com.betxi.backend.Formation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formations")
public class FormationController {

    private final FormationRepository formationRepository;

    public FormationController(FormationRepository formationRepository) {
        this.formationRepository = formationRepository;
    }

    @GetMapping
    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    @PostMapping
    public Formation createFormation(
            @RequestParam String name,
            @RequestParam int goalkeepers,
            @RequestParam int defenders,
            @RequestParam int midfielders,
            @RequestParam int forwards) {

        if (goalkeepers + defenders + midfielders + forwards != 11) {
            throw new RuntimeException("A formation must contain exactly 11 players");
        }

        Formation formation = new Formation(
                name,
                goalkeepers,
                defenders,
                midfielders,
                forwards
        );

        return formationRepository.save(formation);
    }
}