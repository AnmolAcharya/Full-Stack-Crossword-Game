const usernameInput = document.querySelector(".usernameInput");
const usernameSubmit = document.querySelector(".usernameSubmit");
const usernameForm = document.querySelector(".usernameForm");
const landingPage = document.getElementById("landingScreen");
const lobbyPage = document.getElementById("lobbyScreen");

usernameInput.addEventListener('input', () => {
    if(usernameInput.value.trim() !== '') {
        usernameSubmit.disabled = false;
    } else {
        usernameSubmit.disabled = true;
    }
});


// Validate username on 'Enter'
usernameInput.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault();
        validateUsername();
    }
});

// Validate username on 'Start' click
usernameForm.addEventListener('submit', function (e) {
    e.preventDefault();
    validateUsername();
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

function enterLobby() {
    landingPage.classList.add("hidden");
    lobbyPage.classList.remove("hidden");
}