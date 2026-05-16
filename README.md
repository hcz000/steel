# steel-cloud-prd

#### 介绍
ERP综合管理平台

### 核心依赖

| 依赖                          | 版本         |
|-----------------------------|------------|
| Spring Boot                 | 2.7.12     |
| Spring Cloud                | 2021.0.7   |
| Spring Cloud Alibaba        | 2021.0.5.0 |
| Spring Authorization Server | 0.4.2      |
| Mybatis Plus                | 3.5.3.1    |
| hutool                      | 5.8.19     |

### 模块说明

```lua
steel-cloud-prd
├── nacos-server    -- Alibaba Nacos 服务注册中心
├── seata-server    -- Alibaba Seate 分布式事务
├── sentinel-dashboard    -- Alibaba Sentinel 限流
├── steel-auth  -- 授权模块，端口：3000
└── steel-common    -- 公共模块
    ├── steel-common-bom    -- 全局依赖管理控制
    ├── steel-common-core   -- 公共工具类核心包
    ├── steel-common-datasource -- 数据源管理模块
    ├── steel-common-feign  -- 远程调用feign模块
    ├── steel-common-job    -- xxl-job 任务调度模块
    ├── steel-common-log    -- 公共日志管理模块
    ├── steel-common-mvc    -- spring mvc模块
    ├── steel-common-mybatis    -- mybatis-plus 模块封装
    ├── steel-common-oss    -- oss文件管理模块
    ├── steel-common-redis  -- redis缓存模块
    ├── steel-common-security   -- 安全工具模块
    ├── steel-common-swagger    -- swagger doc模块
    └── steel-common-xss    -- xss安全封装
├── steel-gateway -- 服务网关，端口：9999
└── steel-system -- 业务系统
    ├── base-system    -- 项目基础模块，端口：4200
    ├── im-system    -- im系统，端口：4100
    └── user-system    -- 用户权限系统，端口：4000
└── xxl-job-admin  -- xxl-job 分布式任务调度服务，端口：5000
```

### 开放端口

```
## 服务端口
firewall-cmd --zone=public --add-port=3000/tcp --permanent
firewall-cmd --zone=public --add-port=4000/tcp --permanent
firewall-cmd --zone=public --add-port=4100/tcp --permanent
firewall-cmd --zone=public --add-port=4200/tcp --permanent
firewall-cmd --zone=public --add-port=5000/tcp --permanent
firewall-cmd --zone=public --add-port=9999/tcp --permanent
## 组件端口
## redis
firewall-cmd --zone=public --add-port=7001/tcp --permanent
firewall-cmd --zone=public --add-port=17001/tcp --permanent 
firewall-cmd --zone=public --add-port=7002/tcp --permanent
firewall-cmd --zone=public --add-port=17002/tcp --permanent
## nacos
firewall-cmd --zone=public --add-port=7848/tcp --permanent
firewall-cmd --zone=public --add-port=8848/tcp --permanent
firewall-cmd --zone=public --add-port=9848/tcp --permanent
firewall-cmd --zone=public --add-port=9849/tcp --permanent
## postgresql
firewall-cmd --zone=public --add-port=5432/tcp --permanent
## minio
firewall-cmd --zone=public --add-port=9009/tcp --permanent
firewall-cmd --zone=public --add-port=9001/tcp --permanent
## seata
firewall-cmd --zone=public --add-port=7091/tcp --permanent
firewall-cmd --zone=public --add-port=8091/tcp --permanent
## rocketmq
firewall-cmd --zone=public --add-port=9876/tcp --permanent
firewall-cmd --zone=public --add-port=10911/tcp --permanent
firewall-cmd --zone=public --add-port=11911/tcp --permanent
firewall-cmd --zone=public --add-port=11912/tcp --permanent
firewall-cmd --reload
```

### 开发环境

hosts配置
```
192.168.1.187	steel.nacos.server1
192.168.1.188	steel.nacos.server2
192.168.1.187	steel.redis.server1
192.168.1.187	steel.redis.server2
192.168.1.188	steel.postgresql.server1
192.168.1.187	steel.postgresql.server2
192.168.1.187	steel.minio.server1
192.168.1.187	steel.gateway.server1
192.168.1.188	steel.gateway.server2
```

### Swagger注解对应表

| swagger2                                    | swagger3                                                        | 注解位置          |
|---------------------------------------------|-----------------------------------------------------------------|---------------|
| @Api                                        | @Tag                                                            | Controller类   |
| @ApiOperation(value = "foo", notes = "bar") | @Operation(summary = "foo", description = "bar")                | api端口方法       |
| @ApiImplicitParams                          | @Parameters                                                     | api端口方法       |
| @ApiImplicitParam                           | @Parameter                                                      | api方法的参数      |
| @ApiParam                                   | @Parameter                                                      | api方法的参数      |
| @ApiIgnore                                  | @Parameter(hidden = true) 或 @Operation(hidden = true) 或 @Hidden | 各处皆可          |
| @ApiModel                                   | @Schema                                                         | DTO类          |
| @ApiModelProperty                           | @Schema                                                         | DTO类          |
| @ApiModelProperty(hidden = true)            | @ApiModelProperty(hidden = true)                                | DTO类          |
| @ApiResponse(code = 404, message = "foo")   | @ApiResponse(responseCode = "404", description = "foo")         | api端口方法       |

