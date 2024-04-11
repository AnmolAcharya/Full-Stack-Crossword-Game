const allTimeList = document.querySelector(".allTimeList");

const createGame = document.querySelector(".createGame");
const joinGame = document.querySelector(".joinGame");
const gameList = document.querySelector(".gameList");
const joinableGames = document.querySelectorAll(".gameListItem");

const readyPage = document.getElementById("readyScreen");

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
    let playersArray = typeof msg.players === 'string' ? JSON.parse(msg.players) : msg.players;
    // playersArray = JSON.parse(msg.players)
    let firstPlayer = typeof playersArray[0] === 'string' ? JSON.parse(playersArray[0]) : playersArray[0];
    // firstPlayer = JSON.parse(playersArray[0]);
    
    const newGame = document.createElement('li');
    newGame.classList.add('gameListItem');
    newGame.setAttribute('tabindex', '0');
    newGame.setAttribute('data-game-id', msg.gameId);
    newGame.textContent = firstPlayer.userName + "'s Match";

    const playerSpan = document.createElement('span');
    playerSpan.className = 'players';
    let numOfPlayers = msg.numPlayer ? msg.numPlayer : playersArray.length;
    playerSpan.textContent = numOfPlayers + "/4";
    newGame.appendChild(playerSpan);
    
    gameList.appendChild(newGame);

    addPlayerToGame(firstPlayer.uid, msg.gameId);
}

function enterReadyScreen() {
    lobbyPage.classList.add("hidden");
    readyPage.classList.remove("hidden");
}

function addPlayerToGame(uid, gameId) {
    if(userSession.uid == uid) {
        userSession.gameId = gameId;
        enterLobby();
    }
}

function removePlayerFromGame() {
    
}

function updateGames(msg) {
    switch(msg.function) {
        case "add":
            addGameToList(msg);
            break;
        case "remove":
            break;
        case "update":

            break
    }
}

window.updateGames = updateGames;
window.addGameToList = addGameToList;