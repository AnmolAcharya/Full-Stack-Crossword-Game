let serverUrl = "ws://" + window.location.hostname + ":9880";
let connection = new WebSocket(serverUrl);

let userSession = {
    uid: null,
    username: null,
    gameId: null,
    color: null,
    screen: "landing"
};

connection.onopen = function (evt) {
    console.log("open");
}

connection.onclose = function (evt) {
    console.log("close");
}

connection.onmessage = function(event) {
    let msg = JSON.parse(event.data);
    console.log("Message Received: ", msg);

    switch(msg.screen) {
        case "landing":
          switch(msg.type) {
            case "newSession":
              window.setUserId(msg.uid);
              window.updateLobby(msg.lobby);
              break;
            case "validateUsername":
              window.checkValidationMessage(msg.valid, msg.uid, msg.username);
              break
          }
          break;
        case "lobby":
          switch(msg.type) {
            case "updateGameList":
              window.updateGames(msg);
              break;
            case "updateAllTimeLeaderboard":

              break;
            case "updateConcurrentLeaderboard":

              break;
          }
          break;
        case "ready":
          let gameData;
          if(msg.start) {
            gameData = JSON.parse(msg.gameData);
            window.removeGameFromList(gameData);
          }
          if(userSession.gameId == msg.gameId) {
            let playersArray = JSON.parse(msg.players, msg.start);
            window.updateReadyScreen(playersArray, msg.start, gameData);
          }
          break;
        case "game":
          if(userSession.gameId == msg.gameId) {
            let firstLetter;
            let secondLetter;
            switch(msg.type) {
              case "letterSelection":
                firstLetter = JSON.parse(msg.letter)
                let letterSelections = firstLetter.selections;
                window.updateLetterSelection(firstLetter.coordinate[0], firstLetter.coordinate[1], letterSelections);
                break;
              case "validWord":
                firstLetter = JSON.parse(msg.firstLetter);
                secondLetter = JSON.parse(msg.secondLetter);
                
                window.updateLetterSelection(firstLetter.coordinate[0], firstLetter.coordinate[1], firstLetter.selections);
                window.updateLetterSelection(secondLetter.coordinate[0], secondLetter.coordinate[1], secondLetter.selections);
                window.highlightWordOnGrid(firstLetter, secondLetter, msg.playerColor);
                window.updateWordBank(msg.wordBank);
                window.updateLeaderboard(JSON.parse(msg.leaderboard));
                break;
              case "invalidWord":
                firstLetter = JSON.parse(msg.firstLetter);
                window.updateLetterSelection(firstLetter.coordinate[0], firstLetter.coordinate[1], firstLetter.selections);
                break;
              case "chatRoom":
                window.updateChatBox(msg.userName, msg.color, msg.textToAdd);
                break;
            }
          }
      }
  };
