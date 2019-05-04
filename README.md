# lyshop
乐优商城学习ing....，项目未完成，笔者写到跨域之后无法获取cookie，未实现支付功能，未实现登录购物车的添加功能。

# 博客地址：
- [简书](https://www.jianshu.com/u/071e3ec425fd)
- [GitHub博客](https://smallmartial.github.io/blog/)
## 目录
- [乐优商城学习笔记一-项目环境搭建](https://smallmartial.github.io/blog/2019/04/12/lyshop-1/)
- [乐优商城学习笔记二-通用异常处理](https://smallmartial.github.io/blog/2019/04/12/lyshop-2/)
- [乐优商城学习笔记三-利用cors解决跨域问题](https://smallmartial.github.io/blog/2019/04/13/lyshop-3/)
- [乐优商城学习笔记四-FastDFS学习](https://smallmartial.github.io/blog/2019/04/14/lyshop-4/)
- [乐优商城学习笔记五-商品规格管理](https://smallmartial.github.io/blog/2019/04/15/lyshop-5/)
- [乐优商城学习笔记六-商品管理](https://smallmartial.github.io/blog/2019/04/16/lyshop-6/)
- [乐优商城学习笔记七-商品管理（添加商品）](https://smallmartial.github.io/blog/2019/04/17/lyshop-7/)
- [乐优商城学习笔记八-商品管理(修改商品)](https://smallmartial.github.io/blog/2019/04/17/lyshop-8/)
- [乐优商城学习笔记九-Elasticsearch学习（一）](https://smallmartial.github.io/blog/2019/04/18/lyshop-9/)
- [乐优商城学习笔记十-Elasticsearch学习（二）](https://smallmartial.github.io/blog/2019/04/18/lyshop-10/)
- [乐优商城学习笔记十一-Elasticsearch学习（三）](https://smallmartial.github.io/blog/2019/04/18/lyshop-11/)
- [乐优商城学习笔记十二-Elasticsearch学习（四）](https://smallmartial.github.io/blog/2019/04/18/lyshop-12/)
- [乐优商城学习笔记十三-搜索微服务](https://smallmartial.github.io/blog/2019/04/19/lyshop-13/)
- [乐优商城学习笔记十四-搜索微服务（二）](https://smallmartial.github.io/blog/2019/04/19/lyshop-14/)
- [乐优商城学习笔记十五-搜索微服务（三）](https://smallmartial.github.io/blog/2019/04/19/lyshop-15/)
- [乐优商城学习笔记十六-搜索过滤（一）](https://smallmartial.github.io/blog/2019/04/20/lyshop-16/)
- [乐优商城学习笔记十七-搜索过滤（二）](https://smallmartial.github.io/blog/2019/04/20/lyshop-17/)
- [乐优商城学习笔记十八-商品详情](https://smallmartial.github.io/blog/2019/04/21/lyshop-18/)
- [乐优商城学习笔记十九-商品详情（二）](https://smallmartial.github.io/blog/2019/04/21/lyshop-19/)
- [Ubuntu安装RabbitMQ](https://smallmartial.github.io/blog/2019/04/21/lyshop-20/)
- [乐优商城学习笔记二十-RabbitMQ简介及使用](https://smallmartial.github.io/blog/2019/04/22/lyshop-21/)
- [乐优商城学习笔记二十一-RabbitMQ项目改造](https://smallmartial.github.io/blog/2019/04/22/lyshop-22/)
- [乐优商城学习笔记二十二-用户注册（一）](https://smallmartial.github.io/blog/2019/04/23/lyshop-23/)
- [乐优商城学习笔记二十三-用户注册（二）](https://smallmartial.github.io/blog/2019/04/24/lyshop-24/)
- [乐优商城学习笔记二十四-授权中心（一）](https://smallmartial.github.io/blog/2019/04/24/lyshop-25/)
- [乐优商城学习笔记二十五-购物车（一）](https://smallmartial.github.io/blog/2019/04/25/lyshop-26/)
- [乐优商城学习笔记二十六-购物车（二）](https://smallmartial.github.io/blog/2019/04/27/lyshop-27/)
- 未完待续...
## 项目架构
![](http://img.smallmartial.cn/%E4%B9%90%E4%BC%98%E5%95%86%E5%9F%8E%20.png)

整个乐优商城可以分为两部分：后台管理系统、前台门户系统。

- 后台管理：

  - 后台系统主要包含以下功能：
    - 商品管理，包括商品分类、品牌、商品规格等信息的管理
    - 销售管理，包括订单统计、订单退款处理、促销活动生成等
    - 用户管理，包括用户控制、冻结、解锁等
    - 权限管理，整个网站的权限控制，采用JWT鉴权方案，对用户及API进行权限控制
    - 统计，各种数据的统计分析展示
  - 后台系统会采用前后端分离开发，而且整个后台管理系统会使用Vue.js框架搭建出单页应用（SPA）。

- 前台门户

  - 前台门户面向的是客户，包含与客户交互的一切功能。例如：
    - 搜索商品
    - 加入购物车
    - 下单
    - 评价商品等等
  - 前台系统我们会使用Thymeleaf模板引擎技术来完成页面开发。出于SEO优化的考虑，我们将不采用单页应用。

无论是前台还是后台系统，都共享相同的微服务集群，包括：

- 商品微服务：商品及商品分类、品牌、库存等的服务
- 搜索微服务：实现搜索功能
- 订单微服务：实现订单相关
- 购物车微服务：实现购物车相关功能
- 用户中心：用户的登录注册等功能
- Eureka注册中心
- Zuul网关服务
- Spring Cloud Config配置中心
- ...

# 3.1.技术点

前端技术：

- 基础的HTML、CSS、JavaScript（基于ES6标准）
- JQuery
- Vue.js 2.0以及基于Vue的框架：Vuetify
- 前端构建工具：WebPack
- 前端安装包工具：NPM
- Vue脚手架：Vue-cli
- Vue路由：vue-router
- ajax框架：axios
- 基于Vue的富文本框架：quill-editor

后端技术：

- 基础的SpringMVC、Spring 5.0和MyBatis3
- Spring Boot 2.0.1版本
- Spring Cloud 最新版 Finchley.RC1
- Redis-4.0
- RabbitMQ-3.4
- Elasticsearch-5.6.8
- nginx-1.10.2：
- FastDFS - 5.0.8
- MyCat
- Thymeleaf

> 如需要前端源码 请联系作者邮箱：smallmartail@qq.com,欢迎各位star,遇到问题可以一起探讨解决。
