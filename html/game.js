const wordGrid = document.querySelector(".wordGrid");

wordGrid.innerHTML = '';

for(let row = 0; row < 20; row++) {
    for(let col = 0; col < 20; col++) {
        const gridItem = document.createElement('div');
        gridItem.classList.add('gridItem');
        gridItem.dataset.x = col;
        gridItem.dataset.y = row;
        gridItem.innerHTML = 'A';

        wordGrid.appendChild(gridItem);
    }
}