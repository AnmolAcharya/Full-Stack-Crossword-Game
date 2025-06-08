const usernameInput = document.querySelector(".usernameInput");
const usernameSubmit = document.querySelector(".usernameSubmit");
const usernameForm = document.querySelector(".usernameForm");
const landingPage = document.getElementById("landingScreen");
const lobbyPage = document.getElementById("lobbyScreen");
const errorMessage = document.querySelector(".errorMessage");

// Create and append the game title and instructions
const gameTitle = document.createElement("h1");
gameTitle.className = "game-title";
gameTitle.textContent = "WORDRUSH";

const gameSubtitle = document.createElement("p");
gameSubtitle.className = "game-subtitle";
gameSubtitle.textContent = "An online multiplayer word search game";

const instructionsTitle = document.createElement("h2");
instructionsTitle.className = "instructions-title";
instructionsTitle.textContent = "Instructions";

const instructionsList = document.createElement("ul");
instructionsList.className = "instructions-list";
const instructions = [
    "Find words hidden in the letter grid",
    "Compete against other players to score points"
];

instructions.forEach(text => {
    const li = document.createElement("li");
    li.textContent = text;
    instructionsList.appendChild(li);
});

// Create the player name prompt
const playerNamePrompt = document.createElement("p");
playerNamePrompt.className = "player-name-prompt";
playerNamePrompt.textContent = "Enter your player name to start";

// Insert elements at the start of landing page
landingPage.insertBefore(gameTitle, landingPage.firstChild);
landingPage.insertBefore(gameSubtitle, usernameForm);
landingPage.insertBefore(instructionsTitle, usernameForm);
landingPage.insertBefore(instructionsList, usernameForm);

// Insert the player name prompt before the input in the form
usernameForm.insertBefore(playerNamePrompt, usernameInput);

usernameInput.addEventListener('input', () => {
    if (usernameInput.value.trim() !== '') {
        usernameSubmit.disabled = false;
        defaultForm();
    } else {
        usernameSubmit.disabled = true;
    }
});

// Validate username on 'Enter'
usernameInput.addEventListener('keypress', function (e) {
    if (usernameInput.value.trim() !== '') {
        if (e.key === 'Enter') {
            e.preventDefault();
            validateUsername();
            usernameSubmit.disabled = true;
        }
    }
});

// Validate username on 'Start' click
usernameForm.addEventListener('submit', function (e) {
    e.preventDefault();
    validateUsername();
    usernameSubmit.disabled = true;
});


function validateUsername() {
    // Get username value
    const username = usernameInput.value;
    let message = {
        screen: "landing",
        type: "validateUsername",
        uid: userSession.uid,
        username: username
    };
    connection.send(JSON.stringify(message));

    // Clear username field
    usernameInput.value = '';
}

function invalidUsername() {
    errorMessage.style.display = "block";
    usernameInput.style.backgroundColor = "#FFB7B7";
    usernameInput.style.marginTop = "0";
}

function defaultForm() {
    usernameInput.style.marginTop = "40px";
    usernameInput.style.backgroundColor = "#D9D9D9";
    errorMessage.style.display = "none";
}

function setUserId(uid) {
    // only set user id if it is null
    if (userSession.uid == null) {
        userSession.uid = uid;
    }
}

function setUserName(uid, username) {
    // continue if uid matches
    if (userSession.uid == uid) {
        // only set user name if it is null
        if (userSession.username == null) {
            userSession.username = username;
        }
    }
}

function checkValidationMessage(valid, uid, username) {
    if (userSession.uid == uid) {
        if (valid == true) {
            setUserName(uid, username);
            window.enterLobby();
        } else {
            invalidUsername();
        }
    }
}

function updateLobby(lobby, allTimeLeaderboard) {
    lobbyArray = JSON.parse(lobby);
    allTimeLeaderboard = JSON.parse(allTimeLeaderboard);
    lobbyArray.joinableGames.forEach(game => {
        addGameToList(game);
    });
    
    for (const [gameId, leaderboard] of Object.entries(lobbyArray.concurrentLeaderboard)) {
        window.manageConcurrentLeaderboard(gameId, leaderboard, "add");
    }

    window.updateAllTimeLeaderboard(allTimeLeaderboard);
}


window.checkValidationMessage = checkValidationMessage;
window.setUserId = setUserId;
window.invalidUsername = invalidUsername;