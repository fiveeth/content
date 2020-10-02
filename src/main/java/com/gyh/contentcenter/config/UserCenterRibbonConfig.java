package com.gyh.contentcenter.config;

//配置细粒度的Ribbon规则（Java方式）
//@RibbonClient(name = "user-center")表明这个配置是为用户中心服务的
//新建一个ribbonConfig,且需要在usercenter这个包外，原因是：SpringBootApplication这个注解中的ComponentScan注解默认扫描当前启动类所在的包及这个包下所有的类,
//将RibbonConfig放在ComponentScan能扫描的位置下会出现的问题：父子上下文扫描重叠的问题。
//@Configuration
//@RibbonClient(name = "user-center", configuration = {RibbonConfig.class}) //指定客户端配置（只是针对user-center这个微服务适用的规则）
//@RibbonClients(defaultConfiguration = {RibbonConfig.class}) //设置全局配置（对所有需要调用的微服务都指定这个规则），目前只能通过这种方式实现全局配置，属性配置的方式还不行

/**
 * @author cncoder
 */
public class UserCenterRibbonConfig {
}