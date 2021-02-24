function WebSocketClient() {
}

WebSocketClient.prototype._generateEndpoint = function () {
    let protocol = 'ws://';
    if (window.location.protocol === 'https:') {
        protocol = 'wss://';
    }
    return protocol + window.location.host + '/shell';
}

WebSocketClient.prototype.connect = function (options) {
    var endpoint = this._generateEndpoint();
    if (window.WebSocket) {
        //如果支持websocket
        this._connection = new WebSocket(endpoint);
    }else {
        //否则报错
        options.onError('WebSocket Not Supported');
        return;
    }

    this._connection.onopen = function () {
        options.onConnect();
    };

    this._connection.onmessage = function (evt) {
        var data = evt.data.toString();
        options.onData(data);
    };

    this._connection.onclose = function (evt) {
        options.onClose();
    };
}

WebSocketClient.prototype.send = function (data) {
    this._connection.send(JSON.stringify(data));
}

WebSocketClient.prototype.sendInitData = function (options) {
    //连接参数
    this._connection.send(JSON.stringify(options));
}

WebSocketClient.prototype.sendClientData = function (data) {
    //发送指令
    this._connection.send(JSON.stringify({"operate": "command", "command": data}))
}
