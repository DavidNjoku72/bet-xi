package com.betxi.backend.Gameweek;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gameweeks")
public class GameweekController {

    private final GameweekRepository gameweekRepository;

    public GameweekController(GameweekRepository gameweekRepository) {
        this.gameweekRepository = gameweekRepository;
    }

    @PostMapping
    public Gameweek createGameweek(@RequestBody Gameweek gameweek) {
        return gameweekRepository.save(gameweek);
    }

    @GetMapping
    public List<Gameweek> getAllGameweeks() {
        return gameweekRepository.findAll();
    }
}