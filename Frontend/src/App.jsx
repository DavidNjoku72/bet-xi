import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [formations, setFormations] = useState([]);
  const [selectedFormation, setSelectedFormation] = useState("");
  const [lineup, setLineup] = useState(null);

  const [players, setPlayers] = useState([]);
  const [selectedPlayer, setSelectedPlayer] = useState(null);

  const [statistic, setStatistic] = useState("SHOTS");
  const [line, setLine] = useState(2.5);
  const [prediction, setPrediction] = useState("OVER");

  // Stores the actual result entered for each pick
  const [actualValues, setActualValues] = useState({});

  // Load formations
  useEffect(() => {
    fetch("http://localhost:8080/formations")
      .then((response) => response.json())
      .then((data) => {
        setFormations(data);

        if (data.length > 0) {
          setSelectedFormation(data[0].id);
        }
      })
      .catch((error) => {
        console.error("Error loading formations:", error);
      });
  }, []);

  // Load players
  useEffect(() => {
    fetch("http://localhost:8080/players")
      .then((response) => response.json())
      .then((data) => {
        setPlayers(data);
      })
      .catch((error) => {
        console.error("Error loading players:", error);
      });
  }, []);

  // Create lineup
  function createLineup() {
    if (!selectedFormation) {
      alert("Please select a formation");
      return;
    }

    fetch(
      `http://localhost:8080/lineups?gameweekId=1&formationId=${selectedFormation}`,
      {
        method: "POST",
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Could not create lineup");
        }

        return response.json();
      })
      .then((data) => {
        setLineup(data);
      })
      .catch((error) => {
        console.error("Error creating lineup:", error);
        alert("Could not create lineup");
      });
  }

  // Available statistics depend on position
  function getStatisticOptions() {
    if (!selectedPlayer) {
      return [];
    }

    if (selectedPlayer.position === "GK") {
      return [
        { value: "SAVES", label: "Saves" },
        { value: "GOALS_CONCEDED", label: "Goals Conceded" },
        { value: "PASSES", label: "Passes" },
      ];
    }

    if (selectedPlayer.position === "DEF") {
      return [
        { value: "TACKLES", label: "Tackles" },
        { value: "INTERCEPTIONS", label: "Interceptions" },
        { value: "PASSES", label: "Passes" },
        { value: "FOULS", label: "Fouls Committed" },
        { value: "FOULS_WON", label: "Fouls Won" },
        { value: "SHOTS", label: "Shots" },
        { value: "SHOTS_ON_TARGET", label: "Shots on Target" },
        { value: "GOALS", label: "Goals" },
        { value: "ASSISTS", label: "Assists" },
      ];
    }

    if (selectedPlayer.position === "MID") {
      return [
        { value: "GOALS", label: "Goals" },
        { value: "ASSISTS", label: "Assists" },
        { value: "SHOTS", label: "Shots" },
        { value: "SHOTS_ON_TARGET", label: "Shots on Target" },
        { value: "TACKLES", label: "Tackles" },
        { value: "INTERCEPTIONS", label: "Interceptions" },
        { value: "PASSES", label: "Passes" },
        { value: "CHANCES_CREATED", label: "Chances Created" },
        { value: "FOULS", label: "Fouls Committed" },
        { value: "FOULS_WON", label: "Fouls Won" },
      ];
    }

    if (selectedPlayer.position === "FWD") {
      return [
        { value: "GOALS", label: "Goals" },
        { value: "ASSISTS", label: "Assists" },
        { value: "SHOTS", label: "Shots" },
        { value: "SHOTS_ON_TARGET", label: "Shots on Target" },
        { value: "CHANCES_CREATED", label: "Chances Created" },
        { value: "FOULS_WON", label: "Fouls Won" },
      ];
    }

    return [];
  }

  // Choose player
  function choosePlayer(player) {
    setSelectedPlayer(player);

    if (player.position === "GK") {
      setStatistic("SAVES");
    } else if (player.position === "DEF") {
      setStatistic("TACKLES");
    } else {
      setStatistic("GOALS");
    }

    setLine(2.5);
    setPrediction("OVER");
  }

  // Create prediction and add it to lineup
  function addToXI() {
    if (!selectedPlayer || !lineup) {
      return;
    }

    fetch(
      `http://localhost:8080/picks?playerId=${selectedPlayer.id}&statistic=${statistic}&line=${line}&prediction=${prediction}`,
      {
        method: "POST",
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Could not create prediction");
        }

        return response.json();
      })
      .then((createdPick) => {
        return fetch(
          `http://localhost:8080/lineups/${lineup.id}/picks/${createdPick.id}`,
          {
            method: "POST",
          }
        );
      })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Could not add player to lineup");
        }

        return response.json();
      })
      .then((updatedLineup) => {
        setLineup(updatedLineup);

        setSelectedPlayer(null);
        setStatistic("SHOTS");
        setLine(2.5);
        setPrediction("OVER");
      })
      .catch((error) => {
        console.error("Error adding player to XI:", error);
        alert(error.message);
      });
  }

  // Submit completed XI
  function submitLineup() {
    if (!lineup) {
      return;
    }

    if (lineup.picks.length !== 11) {
      alert("You need 11 players before submitting.");
      return;
    }

    fetch(
      `http://localhost:8080/lineups/${lineup.id}/submit`,
      {
        method: "PUT",
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Could not submit lineup");
        }

        return response.json();
      })
      .then((submittedLineup) => {
        setLineup(submittedLineup);
        setSelectedPlayer(null);

        alert("XI submitted!");
      })
      .catch((error) => {
        console.error("Error submitting lineup:", error);
        alert(error.message);
      });
  }

  // Settle one prediction
  function settlePick(pickId) {
    const actualValue = actualValues[pickId];

    if (
      actualValue === undefined ||
      actualValue === "" ||
      Number(actualValue) < 0
    ) {
      alert("Enter a valid actual value.");
      return;
    }

    fetch(
      `http://localhost:8080/picks/${pickId}/settle?actualValue=${actualValue}`,
      {
        method: "PUT",
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Could not settle prediction");
        }

        return response.json();
      })
      .then((settledPick) => {
        // Update the settled pick in the current lineup
        setLineup((currentLineup) => ({
          ...currentLineup,

          picks: currentLineup.picks.map((pick) =>
            pick.id === settledPick.id
              ? settledPick
              : pick
          ),
        }));
      })
      .catch((error) => {
        console.error("Error settling prediction:", error);
        alert(error.message);
      });
  }

  // Ask backend to calculate the final lineup score
  function calculateScore() {
    if (!lineup) {
      return;
    }

    fetch(
      `http://localhost:8080/lineups/${lineup.id}/score`,
      {
        method: "PUT",
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Could not calculate score");
        }

        return response.json();
      })
      .then((updatedLineup) => {
        setLineup(updatedLineup);
      })
      .catch((error) => {
        console.error("Error calculating score:", error);
        alert(error.message);
      });
  }

  // Check whether every pick has been settled
  function allPicksSettled() {
    if (!lineup || lineup.picks.length === 0) {
      return false;
    }

    return lineup.picks.every(
      (pick) => pick.result !== "PENDING"
    );
  }

  // Get selected picks by position
  function getPicksByPosition(position) {
    if (!lineup) {
      return [];
    }

    return lineup.picks.filter(
      (pick) => pick.player.position === position
    );
  }

  // Render formation row
  function renderPositionRow(position, amount) {
    const positionPicks = getPicksByPosition(position);
    const slots = [];

    for (let i = 0; i < amount; i++) {
      const pick = positionPicks[i];

      slots.push(
        <div
          className="player-slot"
          key={`${position}-${i}`}
        >
          {pick ? (
            <>
              <strong>{pick.player.name}</strong>

              <span className="player-team">
                {pick.player.team.name}
              </span>

              <span className="player-prediction">
                {pick.prediction} {pick.line}{" "}
                {pick.statistic}
              </span>

              {pick.result !== "PENDING" && (
                <span className="player-prediction">
                  {pick.result === "CORRECT" && "✅ CORRECT"}
                  {pick.result === "INCORRECT" && "❌ INCORRECT"}
                  {pick.result === "PUSH" && "➖ PUSH"}
                </span>
              )}
            </>
          ) : (
            <>
              <strong>{position}</strong>

              <span className="empty-slot">
                Empty
              </span>
            </>
          )}
        </div>
      );
    }

    return (
      <div className="position-row">
        {slots}
      </div>
    );
  }

  return (
    <div className="app">

      <header className="header">
        <h1>Bet XI</h1>

        <p>
          Build your Premier League prediction XI.
        </p>
      </header>

      <main className="main-content">

        {/* GAMEWEEK / FORMATION */}

        <section className="setup-panel">
          <h2>Gameweek 5</h2>

          <div className="formation-selector">
            <label>Formation</label>

            <select
              value={selectedFormation}
              onChange={(event) =>
                setSelectedFormation(event.target.value)
              }
              disabled={lineup !== null}
            >
              {formations.map((formation) => (
                <option
                  key={formation.id}
                  value={formation.id}
                >
                  {formation.name}
                </option>
              ))}
            </select>
          </div>

          {!lineup && (
            <button
              className="main-button"
              onClick={createLineup}
            >
              Build XI
            </button>
          )}

          {lineup && (
            <p className="lineup-count">
              {lineup.picks.length} / 11 players selected
            </p>
          )}
        </section>

        {lineup && (
          <>

            {/* FOOTBALL PITCH */}

            <section className="pitch-section">

              <div className="pitch-header">

                <div>
                  <h2>Your XI</h2>

                  <p>
                    Formation: {lineup.formation.name}
                  </p>
                </div>

                <div className="lineup-number">
                  {lineup.picks.length}/11
                </div>

              </div>

              <div className="pitch">

                <div className="penalty-area top-box"></div>
                <div className="centre-circle"></div>
                <div className="halfway-line"></div>
                <div className="penalty-area bottom-box"></div>

                <div className="formation">

                  {renderPositionRow(
                    "FWD",
                    lineup.formation.forwards
                  )}

                  {renderPositionRow(
                    "MID",
                    lineup.formation.midfielders
                  )}

                  {renderPositionRow(
                    "DEF",
                    lineup.formation.defenders
                  )}

                  {renderPositionRow(
                    "GK",
                    lineup.formation.goalkeepers
                  )}

                </div>

              </div>

              {/* SUBMIT BUTTON */}

              {lineup.picks.length === 11 &&
                !lineup.submitted && (
                  <div style={{ textAlign: "center" }}>
                    <button
                      className="main-button"
                      onClick={submitLineup}
                    >
                      Submit XI
                    </button>
                  </div>
                )}

              {/* SUBMITTED MESSAGE */}

              {lineup.submitted && (
                <div style={{ textAlign: "center" }}>
                  <h2>XI Submitted ✓</h2>

                  <p>
                    Your predictions are locked for this
                    gameweek.
                  </p>
                </div>
              )}

            </section>

            {/* PLAYER SELECTION */}

            {!lineup.submitted && (
              <section className="player-section">

                <h2>Choose a Player</h2>

                <div className="player-list">

                  {players
                    .filter(
                      (player) =>
                        !lineup.picks.some(
                          (pick) =>
                            pick.player.id === player.id
                        )
                    )
                    .map((player) => (
                      <button
                        className="player-button"
                        key={player.id}
                        onClick={() =>
                          choosePlayer(player)
                        }
                      >
                        <strong>
                          {player.name}
                        </strong>

                        <span>
                          {player.position} ·{" "}
                          {player.team.name}
                        </span>
                      </button>
                    ))}

                </div>

              </section>
            )}

            {/* PREDICTION BUILDER */}

            {selectedPlayer &&
              !lineup.submitted && (
                <section className="prediction-builder">

                  <h2>
                    Prediction for {selectedPlayer.name}
                  </h2>

                  <p>
                    {selectedPlayer.position} ·{" "}
                    {selectedPlayer.team.name}
                  </p>

                  <div className="prediction-fields">

                    <div>
                      <label>Statistic</label>

                      <select
                        value={statistic}
                        onChange={(event) =>
                          setStatistic(event.target.value)
                        }
                      >
                        {getStatisticOptions().map(
                          (option) => (
                            <option
                              key={option.value}
                              value={option.value}
                            >
                              {option.label}
                            </option>
                          )
                        )}
                      </select>
                    </div>

                    <div>
                      <label>Line</label>

                      <input
                        type="number"
                        step="0.5"
                        min="0"
                        value={line}
                        onChange={(event) =>
                          setLine(event.target.value)
                        }
                      />
                    </div>

                    <div>
                      <label>Prediction</label>

                      <select
                        value={prediction}
                        onChange={(event) =>
                          setPrediction(event.target.value)
                        }
                      >
                        <option value="OVER">
                          Over
                        </option>

                        <option value="UNDER">
                          Under
                        </option>
                      </select>
                    </div>

                  </div>

                  <button
                    className="main-button"
                    onClick={addToXI}
                  >
                    Add {selectedPlayer.name} to XI
                  </button>

                </section>
              )}

            {/* SELECTED PREDICTIONS */}

            <section className="predictions-section">

              <h2>Selected Predictions</h2>

              {lineup.picks.length === 0 && (
                <p>
                  Select a player to start building your XI.
                </p>
              )}

              {lineup.picks.map((pick) => (
                <div
                  className="prediction-card"
                  key={pick.id}
                >
                  <div>
                    <strong>
                      {pick.player.name}
                    </strong>

                    <span>
                      {pick.player.position} ·{" "}
                      {pick.player.team.name}
                    </span>
                  </div>

                  <strong>
                    {pick.prediction}{" "}
                    {pick.line}{" "}
                    {pick.statistic}
                  </strong>
                </div>
              ))}

            </section>

            {/* RESULTS / SETTLEMENT */}

            {lineup.submitted && (
              <section className="predictions-section">

                <h2>Gameweek Results</h2>

                <p style={{ textAlign: "center" }}>
                  Enter the actual match statistic for each
                  prediction.
                </p>

                {lineup.picks.map((pick) => (
                  <div
                    className="prediction-card"
                    key={`result-${pick.id}`}
                  >
                    <div>
                      <strong>
                        {pick.player.name}
                      </strong>

                      <span>
                        {pick.prediction} {pick.line}{" "}
                        {pick.statistic}
                      </span>
                    </div>

                    {pick.result === "PENDING" ? (
                      <div>
                        <input
                          type="number"
                          min="0"
                          placeholder="Actual"
                          value={
                            actualValues[pick.id] ?? ""
                          }
                          onChange={(event) =>
                            setActualValues({
                              ...actualValues,

                              [pick.id]:
                                event.target.value,
                            })
                          }
                        />

                        <button
                          className="main-button"
                          onClick={() =>
                            settlePick(pick.id)
                          }
                        >
                          Settle
                        </button>
                      </div>
                    ) : (
                      <div>
                        <strong>
                          Actual: {pick.actualValue}
                        </strong>

                        <span>
                          {pick.result === "CORRECT" &&
                            " ✅ CORRECT"}

                          {pick.result === "INCORRECT" &&
                            " ❌ INCORRECT"}

                          {pick.result === "PUSH" &&
                            " ➖ PUSH"}
                        </span>
                      </div>
                    )}
                  </div>
                ))}

                {allPicksSettled() && (
                  <div style={{ textAlign: "center" }}>

                    <button
                      className="main-button"
                      onClick={calculateScore}
                    >
                      Calculate Final Score
                    </button>

                    <h2>
                      Gameweek Score: {lineup.score} / 11
                    </h2>

                  </div>
                )}

              </section>
            )}

          </>
        )}

      </main>

    </div>
  );
}

export default App;