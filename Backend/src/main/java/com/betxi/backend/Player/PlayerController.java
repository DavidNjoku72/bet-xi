package com.betxi.backend.Player;

import com.betxi.backend.Team.Team;
import com.betxi.backend.Team.TeamRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/players")
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @PostMapping
    public Player createPlayer(
            @RequestParam String name,
            @RequestParam String position,
            @RequestParam Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Player player = new Player(name, position, team);

        return playerRepository.save(player);
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
}