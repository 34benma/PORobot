#PORobot
###A tool for javaer to generate po file（only for mysql now）

##
为Javaer Web开发者提供的自动化PO文件生成工具，同时支持生成docx格式的数据字典

## 工具用途

在Java Web开发过程中，我们总要手动去生成一些PO类(Persistent Object),这些类一般映射到数据库中的一张表。这些代码简单，模式化程度高。因此做了这个工具来简化开发者的工作。

本工具为一个maven 工程，使用非常简单，效率极高。

## 目前支持

 
- 批量生成所配置的数据库的全部表；
- 自定义类后缀
- 自定义生成类路径
- 根据数据库表字段注释生成java文件字段注释
- 自定义排除特定表的生成类

## 后续可能支持特性

- 更加智能化和易用化
- 自定义生成表

## 约束

要生成开发者友好的文件，建议数据库表名，字段名采用驼峰命名法。命名合符Java规范。

##使用方法

Step 1 down下本工程，导入Maven工程：
![](http://o9z6i1a1s.bkt.clouddn.com/2016082401.png)

导入成功后，工程目录如下：
![](http://o9z6i1a1s.bkt.clouddn.com/2016082402.png)

*注意： 如果要生成docx格式的数据字典，需要依赖POI，直接使用maven导入poi依赖会报错，因此采用本地导入，建了一个lib目录，已经将依赖包放到lib目录，在pom配置文件中配置好了本地依赖*

Step2 根据自己的需求，修改配置文件，配置文件在工程根目录下，文件名字是 **datasource.properties**
![](http://o9z6i1a1s.bkt.clouddn.com/2014082403.png)
**配置说明**

- mysqlDriver 目前仅支持mysql，因此这里可以不用变(后续可能会支持多种数据库)
- globalUserName 如果你有很多数据库要生成，并且这些数据库的用户名一样，可以配置这个变量
- globalPassword 同上，这里是配置统一的密码
- url_jdbc_your_db_name 单独配置某个数据库的链接url，注意，url_这个前缀必须统一
- username_your_db_name 单独配置某个数据库的用户名，这里前缀必须有，如果全部数据库都用一个用户名，这里可以缺省不配
- password_your_db_name 同上，配置密码，如果全局密码一致，可以缺省
- exclude_your_db_name 跳过生成PO的表,使用英文状态下的逗号分隔 前缀固定 databaseName必须是所连接的数据库真实名称
- po_package 你要生成的PO文件包名，使用标准的包，用 . 分隔，生成的PO文件会直接放到这下面，方便直接拖入你的工程
- po_suffix 生成的PO文件后缀，默认是xxxPO.java，可以自定义
- serialize 是否需要序列化，默认是false，如果该选项为true，则自动实现序列化接口

*可以一次配置多个数据库，只要按照格式注意前缀和数据库名，如果连接失败会报错*

配置完之后，找到入口Main.java, 按下F11 运行工程即可。如果需要生成数据字段，则将入参配置为true
![](http://o9z6i1a1s.bkt.clouddn.com/2016082404.png)

如果没有任何输出就结束了工程就是好结果。

如果有输出，根据输出排错

## 正确生成的结果

生成的部分文件
![](http://o9z6i1a1s.bkt.clouddn.com/2016082405.png)
文件内容
![](http://o9z6i1a1s.bkt.clouddn.com/2014082406.png)
生成的数据字典样式
![](http://o9z6i1a1s.bkt.clouddn.com/2016082407.png)

##关键源码说明：

Main.java 启动类

DataBaseEntity.java 业务类，具体的生产逻辑都在这里

utils包中

ConfigParser.java 配置文件解析类，校验配置有效性也在这里完成

ConnectionHelper.java Mysql 链接获取以及操作类

## 致谢

生成PO文件的逻辑是在同事的基础上修改完成，修改引用了同事(仇老板)的parse(我修改为了parseTable)，自动生成字段，get，set方法，文件输出等逻辑，在此表示感谢~~~

##联系
Author:JackWang

Contact Me: <wantedonline@outlook.com>

