## helper-demo

演示使用 helper plugins 插件 生成项目增删改查及分页查询的模板代码

- 下载项目
- 使用mysql数据库root用户，创建自己的数据库
- 切换到创建的数据库，执行 helper-demo下的 ***demo.sql*** 语句创建表
- 创建用户并授权刚才创建的数据库
- 修改demo-app下application-dev.yml中的数据源，如下：
```aidl
  datasource:
    url: jdbc:mysql://XXX:XXX/XXX?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false
    username: XXX
    password: XXX
```
- 修改demo-service下mybatis-generator.xml中的数据源，如下：
```aidl
        <!-- 数据库链接地址账号密码 -->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://XXX:XXX/XXX?useSSL=false"
                        userId="XXX" password="XXX">
        </jdbcConnection>
```
- 运行demo-app下AppApplication启动程序