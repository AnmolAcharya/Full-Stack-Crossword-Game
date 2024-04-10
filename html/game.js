const wordGrid = document.querySelector(".wordGrid");
const chatForm = document.querySelector('.chatForm')
const chatInput = document.querySelector('.chatInput');

wordGrid.innerHTML = '';

// Test grid fill
for (let row = 0; row < 20; row++) {
    for (let col = 0; col < 20; col++) {
        const gridItem = document.createElement('div');
        gridItem.classList.add('gridItem');
        gridItem.dataset.x = col;
        gridItem.dataset.y = row;
        gridItem.innerHTML = 'A';

        wordGrid.appendChild(gridItem);
    }
}

// Send chat on 'Enter'
chatInput.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault();
        sendMessage();
    }
});

// Send chat on button submit
chatForm.addEventListener('submit', function (e) {
    e.preventDefault();
    sendMessage();
});

function sendMessage() {
    // Get message
    const message = chatInput.value;

    // Send message to server

    // Clear message field
    chatInput.value = '';
}