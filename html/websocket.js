let serverUrl = "ws://" + window.location.hostname + ":9880";
let connection = new WebSocket(serverUrl);

let userSession = {
    uid: null,
    username: null,
    gameId: null,
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
            switch(msg.type) {
              case "letterSelection":
                let letter = JSON.parse(msg.letter)
                //let letterSelections = JSON.parse(letter.selections)
                let letterSelections = letter.selections;
                updateLetterSelection(letter.coordinate[0], letter.coordinate[1], letterSelections);
                break;
              case "validWord":
                break;
              case "invalidWord":
                break;
              case "chatRoom":
                break;
            }
          }
      }
  };
