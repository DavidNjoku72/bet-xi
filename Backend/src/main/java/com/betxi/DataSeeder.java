package com.betxi.backend;

import com.betxi.backend.Player.Player;
import com.betxi.backend.Player.PlayerRepository;
import com.betxi.backend.Team.Team;
import com.betxi.backend.Team.TeamRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public DataSeeder(
            TeamRepository teamRepository,
            PlayerRepository playerRepository) {

        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(String... args) {

        // Don't add the test data again every time Spring Boot starts
        if (playerRepository.count() > 1) {
            return;
        }

        // Create test teams
        Team arsenal = new Team("Arsenal");
        Team chelsea = new Team("Chelsea");

        arsenal = teamRepository.save(arsenal);
        chelsea = teamRepository.save(chelsea);

        // Create enough players to test a 4-3-3
        List<Player> players = List.of(

                // Goalkeeper
                new Player("David Raya", "GK", arsenal),

                // Defenders
                new Player("William Saliba", "DEF", arsenal),
                new Player("Gabriel Magalhaes", "DEF", arsenal),
                new Player("Reece James", "DEF", chelsea),
                new Player("Marc Cucurella", "DEF", chelsea),

                // Midfielders
                new Player("Declan Rice", "MID", arsenal),
                new Player("Martin Odegaard", "MID", arsenal),

                // Forwards
                new Player("Bukayo Saka", "FWD", arsenal),
                new Player("Joao Pedro", "FWD", chelsea),
                new Player("Pedro Neto", "FWD", chelsea)
        );

        playerRepository.saveAll(players);

        System.out.println("Bet XI test players added.");
    }
}