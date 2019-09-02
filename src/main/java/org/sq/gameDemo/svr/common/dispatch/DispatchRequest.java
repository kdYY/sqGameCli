package org.sq.gameDemo.svr.common.dispatch;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.svr.common.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

//做请求转发
@Component
public class DispatchRequest{


    // 指令，OrderBean
    //site, {userController,method}
    private static final Map<String, OrderBean> request2Handler = new HashMap();


    /**
     * 加载指令到内存
     * @throws Exception
     */
    //@PostConstruct
    public void init() throws Exception {
        //扫描所有包下GameOrderMapping注解的方法
        //偷懒... 获取所有bean
        String[] allDefinationBeanName = SpringUtil.getAllDefinationBeanName();
        for (String beanName : allDefinationBeanName) {
            Object bean = SpringUtil.getBean(beanName);
            Method[] methods = bean.getClass().getDeclaredMethods();

            for (Method method : methods) {
                OrderMapping order = method.getAnnotation(OrderMapping.class);
                if(order != null) {
                    OrderEnum value = order.value();
                    String key = value.getOrder();
                    if(request2Handler.get(key) != null) {
                        throw new Exception("指令映射出现重复");
                    } else {
                        request2Handler.put(key, new OrderBean(beanName, method));
                    }

                }
            }
        }
        System.out.println("指令加载完毕");
    }

    public static void dispatchRequest(ChannelHandlerContext ctx, MsgEntity msgEntity) throws InvocationTargetException, IllegalAccessException {
        SpringUtil.getBean(DispatchRequest.class).dispatch(ctx, msgEntity);
    }


    // TODO 改造成Queue进行消费处理也不错
    /**
     * 根据requestOrder，分发执行方法
     * @param ctx
     * @param msgEntity 请求实体
     */
    public void dispatch(ChannelHandlerContext ctx, MsgEntity msgEntity) throws InvocationTargetException, IllegalAccessException {
            getResponse(msgEntity);

    }

    private void getResponse(MsgEntity msgEntity) throws IllegalAccessException, InvocationTargetException {

        OrderBean orderBean;
        Object response;
        short cmdCode = msgEntity.getCmdCode();
        orderBean = request2Handler.get(OrderEnum.getOrderByCode(cmdCode));
        Object bean = SpringUtil.getBean(orderBean.getBeanName());
        byte[] data = msgEntity.getData();

        Method method = orderBean.getMethod();
        if(data != null && data.length != 0) {
            method.invoke(bean, msgEntity);
        } else {
            method.invoke(bean);
        }
        System.out.println("-------------------------------------------------");
    }

}

