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

function showTips(msg) {
    $("#message_console").html(new Date().toLocaleTimeString() + "：" + msg);
}
function uploadFile() {
    let val = $('#current_path').val();
    // 设置上传路径
    $("#path").val(val);
    // 拼接所有上传文件名
    let files = $("#file").get(0).files;
    let fileNames = [];
    for (let i = 0; i < files.length; i++) {
        fileNames.push(files[i].name)
    }
    //获取form数据
    let formData = new FormData(document.querySelector("#upload_form"));
    $.ajax({
        url: "/sftp/upload",
        type: "POST",
        data: formData,
        processData: false,  // 不处理数据
        contentType: false,   // 不设置内容类型
        success: function (res) {
            let tips = fileNames.join(",") + res;
            showTips(tips);
            let $tree = $('#file_tree').jstree(true);
            $tree.select_node(val);
            $tree.open_node($tree.get_node(val));
            alert(tips);
        }
    });
}
function checkErr(data) {
    if (data.code === 200) {
        showTips("查询成功！");
    } else {
        showTips("查询失败！");
    }
}