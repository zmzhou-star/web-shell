/*
 * Created by zmzhou on 2021/2/24 16:50
 */

/**
 * 加密
 * @param content 加密内容
 * @returns {*}
 */
let encrypt = function (content) {
    let aseKey = "ws9ybUMn4F81t5oPKqJrqLKxERaYAS12"
    return CryptoJS.AES.encrypt(content, CryptoJS.enc.Utf8.parse(aseKey), {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    }).toString();
}
