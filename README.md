# web-shell
> An awesome project.

## 介绍
纯Java实现一个web shell登录Linux远程主机，技术选型 SpringBoot + WebSocket + jsch + xterm.js

## 软件架构说明
* [Spring Boot](https://start.spring.io/)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-spring-mvc-template-engines)
* [WebSocket](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-websockets)
* [jsch](https://github.com/is/jsch)
* [xterm.js](https://github.com/xtermjs/xterm.js/)
* 缓存使用 [spring cache](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-caching-provider-ehcache2) + [ehcache](https://www.ehcache.org/)

## 安装教程
1. `打包`
```
mvn clean install -X -DskipTests
```
2. `运行（Windows或Linux）`
```
java -jar -server web-shell-1.0.jar
```
当前ssh窗口被锁定，可按CTRL + C打断程序运行，或直接关闭窗口，程序退出。

3. `运行（Linux后台运行）`
```
nohup java -jar -server web-shell-1.0.jar > logs/web-shell.out 2>&1 &
```
nohup 意思是不挂断运行命令,当账户退出或终端关闭时,程序仍然运行。
&代表在后台运行

4. `访问地址：`http://127.0.0.1:9598/


5. `使用说明`
* sftp页面文件详情列表窗口双击文件可下载
* sftp页面点击`选择文件上传`按钮选择文件上传（可以多选）

## 运行效果图
![登录页面](https://gitee.com/zmzhou-star/web-shell/raw/main/docs/login.png)
![shell页面](https://gitee.com/zmzhou-star/web-shell/raw/main/docs/shell.png)
![sftp页面](https://gitee.com/zmzhou-star/web-shell/raw/main/docs/sftp.png)

#### License
[The Apache-2.0 License](./LICENSE)

请自由地享受和参与开源

#### 捐赠
开源不易，请多鼓励！（注：如果该项目对您有帮助，请捐赠以表示支持，谢谢！捐赠请备注web-shell捐赠和称呼哦，谢谢！）

| 支付宝 | 微信 |
| :------------: | :------------: |
| ![Alipay](https://gitee.com/zmzhou-star/web-shell/raw/main/docs/alipay.png) | ![Wechat](https://gitee.com/zmzhou-star/web-shell/raw/main/docs/wechatpay.png) |

#### 联系作者
email：<a href="mailto:zmzhou-star@foxmail.com">Contact zmzhou-star</a>

微信公众号：![微信公众号](https://gitee.com/zmzhou-star/web-shell/raw/main/docs/wechat-zmzhou-star.png)
