const usernameInput = document.querySelector(".usernameInput");
const usernameSubmit = document.querySelector(".usernameSubmit");
const usernameForm = document.querySelector(".usernameForm");
const landingPage = document.getElementById("landingScreen");
const lobbyPage = document.getElementById("lobbyScreen");
const errorMessage = document.querySelector(".errorMessage");

usernameInput.addEventListener('input', () => {
    if(usernameInput.value.trim() !== '') {
        usernameSubmit.disabled = false;
        defaultForm();
    } else {
        usernameSubmit.disabled = true;
    }
});


// Validate username on 'Enter'
usernameInput.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault();
        validateUsername();
        usernameSubmit.disabled = true;
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

function enterLobby() {
    landingPage.classList.add("hidden");
    lobbyPage.classList.remove("hidden");
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

window.enterLobby = enterLobby;
window.invalidUsername = invalidUsername;