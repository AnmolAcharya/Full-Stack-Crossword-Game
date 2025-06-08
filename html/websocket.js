// let serverUrl = "ws://" + window.location.hostname + `:${parseInt(window.location.port) + 100}`;
let serverUrl = "ws://localhost:9102";
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
              window.updateLobby(msg.lobby, msg.allTimeLeaderboard);
              document.title = "TWSG " + msg.version;
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
              let allTimeLeaderboard = JSON.parse(msg.leaderboard)
              window.updateAllTimeLeaderboard(allTimeLeaderboard);
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
            window.manageConcurrentLeaderboard(msg.gameId, gameData.leaderboard, "add");
          }
          if(userSession.gameId == msg.gameId) {
            let playersArray = JSON.parse(msg.players, msg.start);
            window.updateReadyScreen(playersArray, msg.start, gameData);
          }
          break;
        case "game":
          if(msg.leaderboard) {
            let gameLeaderboard = JSON.parse(msg.leaderboard)
            if(msg.type == "endGame") {
              window.manageConcurrentLeaderboard(msg.gameId, gameLeaderboard, "remove");
            } else {
              window.manageConcurrentLeaderboard(msg.gameId, gameLeaderboard, "update");
            }
          }
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
              case "hint":
                hint = JSON.parse(msg.hint);
                window.giveHint(hint);
                break;
              case "chatRoom":
                window.updateChatBox(msg.userName, msg.color, msg.textToAdd);
                break;
              case "leaveGame":
                window.updateLeaderboard(JSON.parse(msg.leaderboard));
                if(userSession.uid == msg.uid) {
                  userSession.gameId == null;
                  window.resetGame();
                }
                break;
              case "endGame":
                userSession.screen = "endGame";
                window.endGame();
                break;
            }
          }
      }
  };
