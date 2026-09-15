package com.betxi.backend.Match;

import com.betxi.backend.Gameweek.Gameweek;
import com.betxi.backend.Gameweek.GameweekRepository;
import com.betxi.backend.Team.Team;
import com.betxi.backend.Team.TeamRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final GameweekRepository gameweekRepository;

    public MatchController(
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            GameweekRepository gameweekRepository) {

        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.gameweekRepository = gameweekRepository;
    }

    @PostMapping
    public Match createMatch(
            @RequestParam Long homeTeamId,
            @RequestParam Long awayTeamId,
            @RequestParam Long gameweekId,
            @RequestParam LocalDateTime kickoffTime) {

        Team homeTeam = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new RuntimeException("Home team not found"));

        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new RuntimeException("Away team not found"));

        Gameweek gameweek = gameweekRepository.findById(gameweekId)
                .orElseThrow(() -> new RuntimeException("Gameweek not found"));

        Match match = new Match(homeTeam, awayTeam, kickoffTime, gameweek);

        return matchRepository.save(match);
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }
}