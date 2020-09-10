# content
springcloud项目: 内容中心
## Feign
### ribbon方式 vs feign方式
![Alt text](src/main/resources/static/ribbon配置和feign配置的对比.jpg)
### feign代码方式 vs 属性方式
###### 配置优先级：全局代码<全局属性<细粒度代码<细粒度属性
###### java配置优势：全局代码<全局属性<细粒度代码<细粒度属性
###### 属性配置优势：全局代码<全局属性<细粒度代码<细粒度属性
总结：尽量使用属性配置，属性方式实现不了的情况下再考虑用代码配置
### restTemplate方式 vs feign方式
![Alt text](src/main/resources/static/restTemplate跟feign方式的对比.jpg)
总结：尽量使用feign，特殊情况在考虑用restTemplate
### feign性能优化方式
###### 1.配置连接池
###### 2.设置合适的日志级别（开发可以设置成full,生产还是建议设置成basic）
## Sentinel
### 服务容错方式
###### 1.超时
###### 2.限流
###### 3.仓壁模式
###### 4.断路器模式