const allTimeList = document.querySelector(".allTimeList");

const createGame = document.querySelector(".createGame");
const joinGame = document.querySelector(".joinGame");
const gameList = document.querySelector(".gameList");
const readyPage = document.getElementById("readyScreen");
const gamePage = document.getElementById("gameScreen");

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
function resetGameSelection(changedGame) {
    const joinableGames = document.querySelectorAll(".gameListItem");

    if (previousSelection != null && previousSelection == changedGame) {
        joinGame.disabled = true;
        previousSelection.classList.remove("gameSelected");
    }
    joinableGames.forEach(function (game) {
        // game.classList.remove("gameSelected");
        game.addEventListener('click', function (e) {
            // Remove previous game selections
            if (previousSelection) {
                previousSelection.classList.remove("gameSelected");
            }
            // // New Selection
            previousSelection = game;
            game.classList.add("gameSelected")
            selectedGameID = game.dataset.gameId;
            joinGame.disabled = false;
        });
    })
}

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

function enterLobby() {
    // enters lobby based on what screen the user is currently in
    switch (userSession.screen) {
        case "landing":
            landingPage.classList.add("hidden");
            lobbyPage.classList.remove("hidden");
            break;
        case "ready":
            readyPage.classList.add("hidden");
            lobbyPage.classList.remove("hidden");
            break;
        case "game":
            gamePage.classList.add("hidden");
            lobbyPage.classList.remove("hidden");
            break
    }
    userSession.screen = "lobby";
}

function enterReadyScreen() {
    // show user ready screen
    lobbyPage.classList.add("hidden");
    readyPage.classList.remove("hidden");
}

function addPlayerToGame(uid, gameId, players) {
    // checks if user id is matching, then adds them to the ready screen for that game
    if (userSession.uid == uid) {
        userSession.screen = "ready";
        userSession.gameId = gameId;
        enterReadyScreen()
    }

    // check if user is in game, then updates the game's ready screen
    if (userSession.gameId == gameId) {
        // update ready screen contents
        window.updateReadyScreen(players);
    }
}

function removePlayerFromGame(uid, gameId, players) {
    // checks if user id is matching, then removes from game and enters lobby
    if (userSession.uid == uid) {
        userSession.gameId = null;
        enterLobby();
    }

    // check if user is in game, then updates the game's ready screen
    if (userSession.gameId == gameId) {
        // update ready screen contents
        window.updateReadyScreen(players);
    }
}

function handleUserState(msg) {
    let gameId = msg.gameId;
    let playersArray;
    // check if players exist in the game
    msg.players ? playersArray = JSON.parse(msg.players) : playersArray = null;

    switch (msg.userState) {
        case "join":
            addPlayerToGame(msg.uid, gameId, playersArray)
            break;
        case "leave":
            removePlayerFromGame(msg.uid, gameId, playersArray)
            break
    }
}

//!!!!! FIX MATCH NAMING SYSTEM
function addGameToList(msg) {
    // find the player who created the game
    let playersArray = typeof msg.players === 'string' ? JSON.parse(msg.players) : msg.players;
    let firstPlayer = typeof playersArray[0] === 'string' ? JSON.parse(playersArray[0]) : playersArray[0];

    // create the game HTML element
    const newGame = document.createElement('li');
    newGame.classList.add('gameListItem');
    newGame.setAttribute('tabindex', '0');
    newGame.setAttribute('data-game-id', msg.gameId);
    newGame.textContent = "Match " + msg.gameTitle;

    const playerSpan = document.createElement('span');
    playerSpan.className = 'players';
    let numOfPlayers = msg.numPlayer ? msg.numPlayer : playersArray.length;
    playerSpan.textContent = numOfPlayers + "/4";

    // if game is full, hide game from list by default
    if (numOfPlayers == 4) {
        newGame.style.display = "none";
    }

    newGame.appendChild(playerSpan);

    gameList.appendChild(newGame);

    // add player who created the game into that game
    addPlayerToGame(firstPlayer.uid, msg.gameId, playersArray);
    resetGameSelection();
}

function removeGameFromList(msg) {
    // get all games from list
    const games = document.querySelectorAll(".gameListItem");
    let gameId = msg.gameId;

    // if game hasnt started, add or remove player from game depending on state
    if(msg.joinable == null) {
        handleUserState(msg);
    }

    // if game id matches, remove game from lists
    games.forEach(game => {
        if (game.dataset.gameId == gameId) {
            // if no players in game or game has started, delete game element. Otherwise, just hide element
            if (msg.numPlayer == 0 || (msg.joinable == false)) {
                game.remove();
            } else {
                game.style.display = "none";
            }
            resetGameSelection(game);
        }
    })
}

function updateGameListItem(msg) {
    // get all games from game list
    const games = document.querySelectorAll(".gameListItem");
    let gameId = msg.gameId;

    // add or remove player from game depending on state
    handleUserState(msg);

    // find the game to update and update the number of players
    games.forEach(game => {
        if (game.dataset.gameId == gameId) {
            const numPlayers = game.querySelector(".players");
            numPlayers.innerHTML = msg.numPlayer + "/4";

            // if game is not full, and is hidden, show game in list again
            if (msg.numPlayer < 4 && game.style.display == "none") {
                game.style.display = "flex";
            }
        }
    })
}

function updateGames(msg) {
    switch (msg.function) {
        case "add":
            addGameToList(msg);
            break;
        case "remove":
            // game is full and must be hidden from list (for now)
            removeGameFromList(msg);
            break;
        case "update":
            updateGameListItem(msg)
            break
    }
}

window.updateGames = updateGames;
window.addGameToList = addGameToList;
window.removeGameFromList = removeGameFromList;
window.enterLobby = enterLobby;