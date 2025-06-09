const usernameInput = document.querySelector(".usernameInput");
const usernameSubmit = document.querySelector(".usernameSubmit");
const usernameForm = document.querySelector(".usernameForm");
const landingPage = document.getElementById("landingScreen");
const lobbyPage = document.getElementById("lobbyScreen");
const errorMessage = document.querySelector(".errorMessage");

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