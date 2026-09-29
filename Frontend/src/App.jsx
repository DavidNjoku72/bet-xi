import { useEffect, useState } from "react";

function App() {
  const [formations, setFormations] = useState([]);
  const [selectedFormation, setSelectedFormation] = useState("");
  const [lineup, setLineup] = useState(null);

  // Load formations from the Spring Boot backend
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

  // Create a lineup using the selected formation
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
      });
  }

  return (
    <div>
      <h1>Bet XI</h1>

      <p>Build your Premier League prediction XI.</p>

      <h2>Gameweek 5</h2>

      <label>Formation: </label>

      <select
        value={selectedFormation}
        onChange={(event) => setSelectedFormation(event.target.value)}
      >
        {formations.map((formation) => (
          <option key={formation.id} value={formation.id}>
            {formation.name}
          </option>
        ))}
      </select>

      <h2>Your XI</h2>

      <p>
        {lineup ? lineup.picks.length : 0} / 11 players selected
      </p>

      {!lineup && (
        <button onClick={createLineup}>
          Build XI
        </button>
      )}

      {lineup && (
        <div>
          <p>Lineup created!</p>
          <p>Lineup ID: {lineup.id}</p>
          <p>Formation: {lineup.formation.name}</p>
        </div>
      )}
    </div>
  );
}

export default App;