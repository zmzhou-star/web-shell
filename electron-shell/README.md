# electron-shell

> An electron-vue project

#### Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:9080
npm run dev

# build electron application for production
npm run build

# run unit & end-to-end tests
npm test


# lint all JS/Vue component files in `src/`
npm run lint

```

---

This project was generated with [electron-vue](https://github.com/SimulatedGREG/electron-vue)@[45a3e22](https://github.com/SimulatedGREG/electron-vue/tree/45a3e224e7bb8fc71909021ccfdcfec0f461f634) using [vue-cli](https://github.com/vuejs/vue-cli). Documentation about the original structure can be found [here](https://simulatedgreg.gitbooks.io/electron-vue/content/index.html).


#### npm 安装 electron 依赖时下载失败（或下载缓慢）的解决方案 （electron-v8.2.1-win32-x64.zip 下载失败）
``` bash
#1. 修改npm配置
npm config edit

#2.在打开的文件中增加下面一行配置，然后保存关闭
electron_mirror=https://npm.taobao.org/mirrors/electron/

#3.重新下载包（建议先把node_modules中的electron文件夹删除再重新下载）
npm install
```