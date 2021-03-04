function WebSocketClient() {
}

/**
 * 根据location路径生成WebSocket地址
 * @returns {string} WebSocket地址
 */
WebSocketClient.prototype.getWebSocketUrl = function () {
    let protocol = 'ws://';
    if (window.location.protocol === 'https:') {
        protocol = 'wss://';
    }
    return protocol + window.location.host + '/shell';
}
/**
 * 连接WebSocket
 * @param params
 */
WebSocketClient.prototype.connect = function (params) {
    if (window.WebSocket) {
        //如果支持websocket
        this._connection = new WebSocket(this.getWebSocketUrl());
    }else {
        //否则报错
        params.onError('WebSocket Not Supported');
        return;
    }

    this._connection.onopen = function () {
        params.onConnect();
    };

    this._connection.onmessage = function (evt) {
        let data = evt.data.toString();
        params.onData(data);
    };

    this._connection.onclose = function (evt) {
        params.onClose();
    };
}
/**
 * 发送指令
 * @param {Object} params 指令参数（必须含有operate参数）
 */
WebSocketClient.prototype.send = function (params) {
    this._connection.send(JSON.stringify(params));
}
/**
 * 发送普通操作指令
 * @param {String} data 操作指令
 */
WebSocketClient.prototype.sendClientData = function (data) {
    //发送指令
    this._connection.send(JSON.stringify({"operate": "command", "command": data}))
}
