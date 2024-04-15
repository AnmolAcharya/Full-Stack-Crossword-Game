const wordGrid = document.querySelector(".wordGrid");
const wordBank = document.querySelector(".wordBank");
const chatForm = document.querySelector('.chatForm')
const chatInput = document.querySelector('.chatInput');

let firstSelection = null;
let secondSelection = null;

// set up color map for selections
const colorMap = new Map();
colorMap.set("red", "#E51B1B");
colorMap.set("blue", "#106BFF");
colorMap.set("pink", "#E100A5");
colorMap.set("green", "#06A600");

wordGrid.innerHTML = '';

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

// Set up game (fill grid, fill word bank, reset leaderboard, reset chat, start timer)
function setUpGame(gameData) {
    grid = gameData.grid.grid;
    wordBankList = gameData.grid.wordBank;
    fillGrid(grid);
    fillWordBank(wordBankList);
}

function fillGrid(grid) {
    console.log(grid);
    for (let row = 0; row < 20; row++) {
        for (let col = 0; col < 20; col++) {
            const gridItem = document.createElement('div');
            gridItem.classList.add('gridItem');
            gridItem.dataset.x = col;
            gridItem.dataset.y = row;
            gridItem.id = `gridItem-${col}-${row}`;
            gridItem.innerHTML = grid[row][col].letter.toUpperCase();

            wordGrid.appendChild(gridItem);
        }
    }
}

function clearWordBank() {
    while (wordBank.firstChild) {
        wordBank.removeChild(wordBank.firstChild);
    }
}

function fillWordBank(wordBankList) {
    clearWordBank()
    for (const word in wordBankList) {
        const wordElement = document.createElement('p');
        wordElement.id = word;
        wordElement.textContent = word;
        wordBank.appendChild(wordElement);
    }
}

wordGrid.addEventListener('click', function (event) {
    if (event.target.classList.contains('gridItem')) {
        console.log(event.target);
        if (!firstSelection) {
            firstSelection = event.target;
            sendLetterSelection(firstSelection.dataset.x, firstSelection.dataset.y);
            //event.target.classList.add('selected'); // Visually mark the item
        } else if (!secondSelection && event.target !== firstSelection) {
            secondSelection = event.target;
            //event.target.classList.add('selected'); // Visually mark the item
            //sendSelectionToServer(firstSelection, secondSelection);
            //resetSelections();
        }
    }
});

// function resetSelections() {
//     document.querySelectorAll('.grid-item.selected').forEach(item => {
//         item.classList.remove('selected');
//     });
//     firstSelection = null;
//     secondSelection = null;
// }

function sendLetterSelection(xCoordinate, yCoordinate) {
    let message = {
        screen: "game",
        type: "letterSelection",
        uid: userSession.uid,
        gameId: userSession.gameId,
        letterCoordinate: [xCoordinate, yCoordinate]
    };
    connection.send(JSON.stringify(message));
}

function sendWordSelection(firstLetter, secondLetter) {
    let message = {
        screen: "game",
        type: "validateWord",
        uid: userSession.uid,
        gameId: userSession.gameId,
        letterCoordinate: [xCoordinate, yCoordinate]
    };
    connection.send(JSON.stringify(message));
}

function updateLetterSelection(xCoordinate, yCoordinate, selections) {
    const gridItem = document.getElementById(`gridItem-${xCoordinate}-${yCoordinate}`);

    // Initialize box-shadow value to accumulate multiple shadows
    let boxShadowValue = '';
    const borderThickness = 2; // Set consistent border thickness

    // Iterate through selections and create a colored border for each
    selections.forEach((selection, index) => {
        const insetAmount = (index + 1) * borderThickness; // Calculate inset to grow inward
        boxShadowValue += `inset 0 0 0 ${insetAmount}px ${colorMap.get(selection.color)},`;
    });

    // Remove the trailing comma and apply the box-shadow to simulate inner borders
    boxShadowValue = boxShadowValue.slice(0, -1);
    gridItem.style.boxShadow = boxShadowValue;
}

function highlightWordOnGrid(firstCoord, secondCoord) {
    // Assuming row-major order and 1D transformation of coordinates
    const startX = Math.min(firstCoord.x, secondCoord.x);
    const endX = Math.max(firstCoord.x, secondCoord.x);
    const startY = Math.min(firstCoord.y, secondCoord.y);
    const endY = Math.max(firstCoord.y, secondCoord.y);

    for (let x = startX; x <= endX; x++) {
        for (let y = startY; y <= endY; y++) {
            const elementId = `grid-item-${x}-${y}`;
            document.getElementById(elementId).classList.add('highlighted');
        }
    }
}

window.setUpGame = setUpGame;
window.updateLetterSelection = updateLetterSelection;