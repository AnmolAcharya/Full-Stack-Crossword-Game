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

    switch(message.type) {
        case "validateUsername":
          // handle validation response
          break;
        case "updateLeaderboard":
          // update leaderboard UI
          break;
        case "chatMessage":
          // display chat message
          break;
      }
  };