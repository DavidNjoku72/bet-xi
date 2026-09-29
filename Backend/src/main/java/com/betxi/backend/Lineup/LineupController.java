package com.betxi.backend.Lineup;

import com.betxi.backend.Formation.Formation;
import com.betxi.backend.Formation.FormationRepository;
import com.betxi.backend.Gameweek.Gameweek;
import com.betxi.backend.Gameweek.GameweekRepository;
import com.betxi.backend.Pick.Pick;
import com.betxi.backend.Pick.PickRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/lineups")
public class LineupController {

    private final LineupRepository lineupRepository;
    private final GameweekRepository gameweekRepository;
    private final PickRepository pickRepository;
    private final FormationRepository formationRepository;

    public LineupController(
            LineupRepository lineupRepository,
            GameweekRepository gameweekRepository,
            PickRepository pickRepository,
            FormationRepository formationRepository) {

        this.lineupRepository = lineupRepository;
        this.gameweekRepository = gameweekRepository;
        this.pickRepository = pickRepository;
        this.formationRepository = formationRepository;
    }

    // Get all lineups
    @GetMapping
    public List<Lineup> getAllLineups() {
        return lineupRepository.findAll();
    }

    // Create a new lineup for a gameweek using a chosen formation
    @PostMapping
    public Lineup createLineup(
            @RequestParam Long gameweekId,
            @RequestParam Long formationId) {

        Gameweek gameweek = gameweekRepository.findById(gameweekId)
                .orElseThrow(() -> new RuntimeException("Gameweek not found"));

        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        Lineup lineup = new Lineup(gameweek, formation);

        return lineupRepository.save(lineup);
    }

    // Add a pick to a lineup
    @PostMapping("/{lineupId}/picks/{pickId}")
    public Lineup addPick(
            @PathVariable Long lineupId,
            @PathVariable Long pickId) {

        Lineup lineup = lineupRepository.findById(lineupId)
                .orElseThrow(() -> new RuntimeException("Lineup not found"));

        Pick pick = pickRepository.findById(pickId)
                .orElseThrow(() -> new RuntimeException("Pick not found"));

        lineup.addPick(pick);

        return lineupRepository.save(lineup);
    }

    // Submit and lock a completed lineup
    @PutMapping("/{lineupId}/submit")
    public Lineup submitLineup(@PathVariable Long lineupId) {

        Lineup lineup = lineupRepository.findById(lineupId)
                .orElseThrow(() -> new RuntimeException("Lineup not found"));

        lineup.submit();

        return lineupRepository.save(lineup);
    }

    // Recalculate the score using settled picks
    @PutMapping("/{lineupId}/score")
    public Lineup calculateScore(@PathVariable Long lineupId) {

        Lineup lineup = lineupRepository.findById(lineupId)
                .orElseThrow(() -> new RuntimeException("Lineup not found"));

        lineup.calculateScore();

        return lineupRepository.save(lineup);
    }
}