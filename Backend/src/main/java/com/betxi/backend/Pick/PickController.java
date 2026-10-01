package com.betxi.backend.Pick;

import com.betxi.backend.Player.Player;
import com.betxi.backend.Player.PlayerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/picks")
public class PickController {

    private final PickRepository pickRepository;
    private final PlayerRepository playerRepository;

    public PickController(PickRepository pickRepository,
                          PlayerRepository playerRepository) {
        this.pickRepository = pickRepository;
        this.playerRepository = playerRepository;
    }

    @PostMapping
    public Pick createPick(
            @RequestParam Long playerId,
            @RequestParam String statistic,
            @RequestParam double line,
            @RequestParam String prediction) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        String direction = prediction.toUpperCase();

        if (!direction.equals("OVER") && !direction.equals("UNDER")) {
            throw new IllegalArgumentException(
                    "Prediction must be OVER or UNDER");
        }

        if (line < 0 || !Double.isFinite(line)) {
            throw new IllegalArgumentException(
                    "Line must be a valid non-negative number");
        }

        Pick pick = new Pick(player, statistic, line, direction);

        return pickRepository.save(pick);
    }

    @GetMapping
    public List<Pick> getAllPicks() {
        return pickRepository.findAll();
    }

    @PutMapping("/{id}/settle")
    public Pick settlePick(
            @PathVariable Long id,
            @RequestParam int actualValue) {

        Pick pick = pickRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pick not found"));

        if (actualValue < 0) {
            throw new IllegalArgumentException(
                    "Actual value cannot be negative");
        }

        pick.setActualValue(actualValue);

        boolean correct;

        if (pick.getPrediction().equals("OVER")) {
            correct = actualValue > pick.getLine();
        } else {
            correct = actualValue < pick.getLine();
        }

        if (correct) {
            pick.setResult("CORRECT");
        } else if (actualValue == pick.getLine()) {
            pick.setResult("PUSH");
        } else {
            pick.setResult("INCORRECT");
        }

        return pickRepository.save(pick);
    }
}