let serverUrl = "ws://" + window.location.hostname + ":9880";
let connection = new WebSocket(serverUrl);

connection.onopen = function (evt) {
    console.log("open");
}

connection.onclose = function (evt) {
    console.log("close");
}

socket.onmessage = function(event) {
    let msg = event.data;
    console.log("Message Received: ", msg);
  };