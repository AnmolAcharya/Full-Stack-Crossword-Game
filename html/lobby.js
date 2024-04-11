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
joinableGames.forEach(function(game) {
    game.addEventListener('click', function (e) {
        // Remove previous game selections
        if(previousSelection) {
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