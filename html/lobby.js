const allTimeList = document.querySelector(".allTimeList");

const createGame = document.querySelector(".createGame");
const joinGame = document.querySelector(".joinGame");
const gameList = document.querySelector(".gameList");
const joinableGames = document.querySelectorAll(".gameListItem");

let selectedGameID = null;

// Create Game
createGame.addEventListener('click', function (e) {
    let message = {
        screen: "lobby",
        type: "createGame",
        uid: userSession.uid
    };

    connection.send(JSON.stringify(message));
});



// Select Game
let previousSelection;
joinableGames.forEach(function (game) {
    game.addEventListener('click', function (e) {
        // Remove previous game selections
        if (previousSelection) {
            previousSelection.classList.remove("gameSelected");
        }
        // New Selection
        previousSelection = game;
        game.classList.add("gameSelected")
        selectedGameID = game.dataset.gameId;
        joinGame.disabled = false;
    });
})

// Join Game
joinGame.addEventListener('click', function (e) {
    const gameId = selectedGameID;
    let message = {
        screen: "lobby",
        type: "joinGame",
        uid: userSession.uid,
        gameId: gameId
    };

    connection.send(JSON.stringify(message));
});

function addGameToList(msg) {
    playersArray = JSON.parse(msg.players)
    firstPlayer = JSON.parse(playersArray[0]);

    const newGame = document.createElement('li');
    newGame.classList.add('gameListItem');
    newGame.setAttribute('tabindex', '0');
    newGame.setAttribute('data-game-id', msg.gameId);
    newGame.textContent = firstPlayer.userName + "'s Match";

    const playerSpan = document.createElement('span');
    playerSpan.className = 'players';
    playerSpan.textContent = msg.numPlayer + "/4";
    newGame.appendChild(playerSpan);

    gameList.appendChild(newGame);
}