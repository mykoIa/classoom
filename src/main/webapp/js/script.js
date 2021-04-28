var Classroom = {};

Classroom.socket = null;

Classroom.connect = (function(host) {
    if ('WebSocket' in window) {
        Classroom.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        Classroom.socket = new MozWebSocket(host);
    } else {
        console.error('Error: WebSocket is not supported by this browser.');
        return;
    }

    Classroom.socket.onmessage = function(message) {
        var msg = JSON.parse(message.data);
        console.log(msg.topic);
        console.log(msg.username);
    };
});

Classroom.initialize = function() {
    if (window.location.protocol == 'http:') {
        Classroom.connect('ws://' + window.location.host + '/classroom/websocket');
    } else {
        Classroom.connect('wss://' + window.location.host + '/classroom/websocket');
    }
};

Classroom.initialize();