### 1. 基本介绍

Zilean是一款MyBatis代码生成IntelliJ *IDEA*   plugin。旨在提高后台开发效率，减少重复编写模版代码的困扰。



### 2. 功能特性

1. 生成entity、dto、vo、form、query等领域驱动模型对象。
2. 生成mapper、dao、service、serviceImpl、Controller层模版代码。
3. 根据配置 生成满足业务需求的模版代码。
4. 支持生成通用mybatis(**暂未实现**)以及myabtis plus模版代码。
5. 支持自定义代码模版。
6. 保存上次代码生成配置。
7. 支持生成kim框架模版代码。



### 3. 注意

1. 暂不支持生成kotlin模版代码

2. **IntelliJ *IDEA*版本需要IntelliJ IDEA 2017.3版本以上。**

3. 生成的service、Controller层模版代码中使用的BeanUtils工具类，需要引入相应的依赖坐标。

   请使用如下依赖坐标:

   ```
   <dependency>
       <groupId>com.github.fengyuchenglun</groupId>
       <artifactId>kim-boot-util</artifactId>
       <version>1.0.13</version>
   </dependency>
   ```



### 4. 开始

#### 1. 安装

1. IntelliJ *IDEA*官方仓库安装(**暂未实现**)。
2. IntelliJ IDEA个人仓库安装(**暂未实现**)。
3. 本地jar安装
   - IntelliJ *IDEA*界面操作File->Settings->Plugins，进入插件管理页面。
   - 左键点击Installed右侧的设置按钮，点击下拉列表的Install Plugin from Dist。
   - 选择Zilean插件安装jar包。

#### 2. 设置数据源

#### 3. 生成代码

#### 4. 保存设置

#### 5. 自定义模版

### 5. 感谢

本项目主要参考:

1. [batiso]: https://gitee.com/cnscoo/batiso	"batiso"

2. [EasyCode]: https://gitee.com/makejava/EasyCode	"EasyCode"

3. [kvn-code-plugin]: https://gitee.com/kkk001/kvn-code-plugin	"kvn-code-plugin"

   

### 6. 后续计划

1. 支持生成mybatis模版代码。
2. 支持Project项目代码结构，右键直接生成代码。
3. 支持更多的代码生成配置项。
4. 支持IntelliJ *IDEA*官方仓库、自定义IntelliJ *IDEA*个人仓库安装方式。
5. 支持生成kotlin模版代码。
6. 支持Oracle等其他数据模版代码生成。