package com.gyh.contentcenter.sentinel;

/**
 * sentinel持久化-推模式
 *
 * @author cncoder
 * @date 2020/9/24 16:54
 */
//public class DataSourceInitFunc implements InitFunc {
//
//    /**
//     * Nacos服务端的地址
//     */
//    private final String remoteAddress = "localhost:8848";
//    /**
//     * groupId
//     */
//    private final String groupId = "SENTINEL_GROUP";
//    /**
//     * 流控
//     */
//    private final String flowDataId = "content-center-flow-rules";
//    /**
//     * 降级
//     */
//    private final String degradeDataId = "content-center-degrade-rules";
//    /**
//     * 热点
//     */
//    private final String paramDataId = "content-center-param-flow-rules";
//
//    @Override
//    public void init() throws Exception {
//        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, flowDataId,
//                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
//                }));
//        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
//
////        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, degradeDataId,
////                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
////                }));
////        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
////
////        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, paramDataId,
////                source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
////                }));
////        ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());
//    }
//}
