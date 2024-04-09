const usernameInput = document.querySelector(".usernameInput");
const usernameSubmit = document.querySelector(".usernameSubmit");

usernameInput.addEventListener('input', () => {
    if(usernameInput.value.trim() !== '') {
        usernameSubmit.disabled = false;
    } else {
        usernameSubmit.disabled = true;
    }
});