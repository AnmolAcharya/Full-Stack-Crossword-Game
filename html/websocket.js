let serverUrl = "ws://" + window.location.hostname + ":9880";
let connection = new WebSocket(serverUrl);

let userSession = {
    uid: null,
    username: null
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
              userSession.uid = msg.uid;
              break;
            case "validateUsername":
              if(msg.valid == true) {
                userSession.username = msg.username
                window.enterLobby();
              } else {
                window.invalidUsername();
              }
              break
          }
          break;
        case "lobby":
          break;
        case "game":
          break;
      }
  };
