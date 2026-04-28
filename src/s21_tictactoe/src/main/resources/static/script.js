const newGameButton = document.getElementById("newGame");
const gamesContainer = document.getElementById("games-container");

let games = {};

newGameButton.onclick = newGame;

async function newGame() {
  const res = await fetch("/game", { method: "POST" });
  const gameId = await res.text();

  const stateRes = await fetch(`/game/${gameId}`);
  const state = await stateRes.json();

  games[gameId] = {
    board: state.gameFieldDto.field,
    gameOver: false,
    winner: -1
  };

  const gameDiv = document.createElement("div");
  gameDiv.id = `game-${gameId}`;
  gameDiv.className = "game-container";
  gameDiv.innerHTML = `
    <h2>Игра ${gameId}</h2>
    <div id="board-${gameId}" class="board"></div>
    <div id="status-${gameId}" class="status">Твоя очередь (X)</div>
  `;
  gamesContainer.appendChild(gameDiv);

  renderGame(gameId);
}

function renderGame(gameId) {
  const game = games[gameId];
  const boardElement = document.getElementById(`board-${gameId}`);
  const statusElement = document.getElementById(`status-${gameId}`);

  boardElement.innerHTML = "";
  for (let i = 0; i < 3; i++) {
    for (let j = 0; j < 3; j++) {
      const cell = document.createElement("div");
      cell.className = "cell";
      cell.textContent = game.board[i][j] === 1 ? "X" : game.board[i][j] === 2 ? "O" : "";
      cell.onclick = () => makeMove(gameId, i, j);
      boardElement.appendChild(cell);
    }
  }

  if (game.gameOver) {
    if (game.winner === 1) statusElement.textContent = "Ты выиграл!";
    else if (game.winner === 2) statusElement.textContent = "Победил компьютер!";
    else if (game.winner === 0) statusElement.textContent = "Ничья!";
  } else {
    statusElement.textContent = "Твоя очередь (X)";
  }
}

async function makeMove(gameId, x, y) {
  const game = games[gameId];
  if (!game || game.gameOver) return;
  if (game.board[x][y] !== 0) return;

  const statusElement = document.getElementById(`status-${gameId}`);
  statusElement.textContent = "Компьютер думает...";

  const body = { lastTurnX: x, lastTurnY: y };

  const res = await fetch(`/game/${gameId}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });

  if (!res.ok) {
    const err = await res.text();
    alert("Ошибка: " + err);
    return;
  }

  const state = await res.json();
  game.board = state.gameFieldDto.field;
  game.winner = checkWinner(game.board);
  game.gameOver = game.winner !== -1;

  renderGame(gameId);
}

function checkWinner(board) {
  const lines = [
    [[0,0],[0,1],[0,2]],
    [[1,0],[1,1],[1,2]],
    [[2,0],[2,1],[2,2]],
    [[0,0],[1,0],[2,0]],
    [[0,1],[1,1],[2,1]],
    [[0,2],[1,2],[2,2]],
    [[0,0],[1,1],[2,2]],
    [[0,2],[1,1],[2,0]]
  ];

  for (const player of [1,2]) {
    for (const line of lines) {
      if (line.every(([i,j]) => board[i][j] === player)) return player;
    }
  }

  if (board.flat().every(cell => cell !== 0)) return 0; // ничья

  return -1;
}
