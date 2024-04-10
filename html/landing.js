const usernameInput = document.querySelector(".usernameInput");
const usernameSubmit = document.querySelector(".usernameSubmit");

usernameInput.addEventListener('input', () => {
    if(usernameInput.value.trim() !== '') {
        usernameSubmit.disabled = false;
    } else {
        usernameSubmit.disabled = true;
    }
});


// Validate username on 'Enter'
chatInput.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault();
        sendMessage();
    }
});

// Validate username on 'Start' click
chatForm.addEventListener('submit', function (e) {
    e.preventDefault();
    sendMessage();
});


function validateUsername() {
    const username = usernameInput.value.trim();
    let message = {
        type: "validateUsername",
        // uid: userSession.uid,
        username: username
    };
    socket.send(JSON.stringify(message));
}