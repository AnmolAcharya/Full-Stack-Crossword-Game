const readyButton = document.querySelector(".readyButton")
const exitReadyButton = document.getElementById("readyScreen").querySelector(".exitButton");

readyButton.addEventListener('click', () => {
    // update ready button state for user
    let readyStatus;
    if (!readyButton.classList.contains("ready")) {
        readyStatus = true;
        readyButton.classList.add("ready");
        readyButton.innerHTML = "Unready"
    } else {
        readyStatus = false;
        readyButton.classList.remove("ready");
        readyButton.innerHTML = "Ready"
    }

    // send status to server
    let message = {
        screen: "ready",
        type: "updateStatus",
        uid: userSession.uid,
        ready: readyStatus,
        gameId: userSession.gameId
    };
    connection.send(JSON.stringify(message));
});

exitReadyButton.addEventListener('click', () => {
    // send status to server
    let message = {
        screen: "lobby",
        type: "leaveGame",
        uid: userSession.uid,
        gameId: userSession.gameId,
    };
    connection.send(JSON.stringify(message));

    // update user state
    userSession.gameId = null;
    window.enterLobby();
})

function enterGame(gameData) {
    readyPage.classList.add("hidden");
    gamePage.classList.remove("hidden");
    userSession.screen = "game";
    window.setUpGame(gameData);
}

function resetPlayerDiv(playerDiv) {
    playerBubble = playerDiv.querySelector(".playerBubble");
    playeName = playerBubble.querySelector("h2");
    readyStatus = playerDiv.querySelector(".readyStatus");

    playerDiv.dataset.uid = "";
    playerBubble.id = "";
    readyStatus.classList.remove("ready");
}

function resetPlayerDivs(joinedPlayers, numPlayers) {
    for(let i=numPlayers; i<4; i++) {
        resetPlayerDiv(joinedPlayers[i]);
    }
}

function updateReadyScreen(players, canStart, gameData) {
    // if no players in the game, dont update ready screen
    if(!players) {
        return;
    }

    const joinedPlayers = document.querySelectorAll(".joinedPlayer");
    const numJoinedPlayers = document.querySelector(".readyMiddle").querySelector("h1");
    numJoinedPlayers.innerHTML = players.length + "/4 Players";

    resetPlayerDivs(joinedPlayers, players.length);

    for (let i = 0; i < players.length; i++) {
        playerDiv = joinedPlayers[i];
        playerBubble = playerDiv.querySelector(".playerBubble");
        playeName = playerBubble.querySelector("h2");
        readyStatus = playerDiv.querySelector(".readyStatus");

        for (let j = 0; j < players.length; j++) {
            playerObject = JSON.parse(players[i]);
            // set player color for game
            if(userSession.uid == playerObject.uid) {
                userSession.color = playerObject.color;
            }
            playerDiv.dataset.uid = playerObject.uid;
            playerBubble.id = playerObject.color + "Player";
            playeName.innerHTML = playerObject.userName;
            if (playerObject.ready) {
                readyStatus.classList.add("ready");
            } else {
                readyStatus.classList.remove("ready");
            }
        }
    }

    if(canStart) {
        readyButton.classList.remove("ready");
        readyButton.innerHTML = "Ready"
        resetPlayerDivs(joinedPlayers, 4);
        enterGame(gameData);
    }
}

window.updateReadyScreen = updateReadyScreen;