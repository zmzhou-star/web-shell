function WebSocketClient() {
}

WebSocketClient.prototype._generateEndpoint = function () {
    let protocol = 'ws://';
    if (window.location.protocol === 'https:') {
        protocol = 'wss://';
    }
    return protocol + window.location.host + '/shell';
}

WebSocketClient.prototype.connect = function (params) {
    var endpoint = this._generateEndpoint();
    if (window.WebSocket) {
        //如果支持websocket
        this._connection = new WebSocket(endpoint);
    }else {
        //否则报错
        params.onError('WebSocket Not Supported');
        return;
    }

    this._connection.onopen = function () {
        params.onConnect();
    };

    this._connection.onmessage = function (evt) {
        var data = evt.data.toString();
        params.onData(data);
    };

    this._connection.onclose = function (evt) {
        params.onClose();
    };
}

WebSocketClient.prototype.send = function (data) {
    this._connection.send(JSON.stringify(data));
}

WebSocketClient.prototype.sendInitData = function (params) {
    //连接参数
    this._connection.send(JSON.stringify(params));
}

WebSocketClient.prototype.sendClientData = function (data) {
    //发送指令
    this._connection.send(JSON.stringify({"operate": "command", "command": data}))
}
