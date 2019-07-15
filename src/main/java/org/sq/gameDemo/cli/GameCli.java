package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.cli.service.SendOrderService;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.svr.common.PoiUtil;

import java.io.IOException;
import java.io.InputStream;

@Component
public class GameCli {

    private  SendOrderService sendOrderService = new SendOrderService();
    private  final Logger log = Logger.getLogger(GameCli.class);
    private  Bootstrap b;
    private  ChannelFuture f;
    private  final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static String token;

    /**
     * 初始化客户端
     */
    public  void init() {
        try {
            b = new Bootstrap();
            b.group(workerGroup).remoteAddress("127.0.0.1", 8085);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new CliChannelInitializer());
            f = b.connect().sync();
            //readToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readToken() throws InterruptedException {
        System.out.println("登陆校验中");
        InputStream in = PoiUtil.class.getClassLoader().getResourceAsStream("token");
        byte b[] = new byte[1024];
        int len = 0;
        int temp=0;          //全部读取的内容都使用temp接收
        try {
            while((temp = in.read()) != -1) {    //当没有读取完时，继续读取
                b[len] = (byte)temp;
                len++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if(len <= 0) {
//            System.out.println("请先登录");
//        } else {
//            token = new String(b, 0, len);
//            sendMsg("checkToken token=" + token);
//        }

    }

    /**
     * 发送请求
     * @param send
     * @return
     */
    public  void sendMsg(String send) throws Exception {
        // 传数据给服务端

        MsgEntity sendMsgEntity = sendMsgEntity(send);
        if(sendMsgEntity == null) {
            return;
        }
        f.channel().writeAndFlush(sendMsgEntity);
    }

    private MsgEntity sendMsgEntity(String request) throws Exception{
        //eg: register name=kevins&password=123456
        if(request == null || request.equals("")) {
            return null;
        }
        String[] input = request.split(" ");
        MsgEntity msgEntity = new MsgEntity();
        if(input == null || input.length == 0) {
            return null;
        }
        OrderEnum orderEnum = OrderEnum.getOrderEnumByOrder(input[0]);
        msgEntity.setCmdCode(orderEnum.getOrderCode());
        switch (orderEnum) {
            case Register:
                sendOrderService.register(msgEntity, input);
                return msgEntity;
            case Login:
                sendOrderService.login(msgEntity, input);
                return msgEntity;
            case Help:
                sendOrderService.help();
                return null;
            case ErrOrder:
                System.out.println("输入指令有误");
                return null;
        }
        //拦截
        if(token == null || token.length() == 0) {
            System.out.println("请先登录");
            return null;
        }

        switch (orderEnum) {
            case BindRole:
                sendOrderService.bindRole(msgEntity, input);
                break;
            case Aoi:
                sendOrderService.aoi(msgEntity);
                break;
            case Move:
                sendOrderService.move(msgEntity,input);
                break;
            case TalkWithNpc:
                sendOrderService.talkToNpc(msgEntity, input);
            default:break;
        }
        return msgEntity;
    }

    public ChannelFuture getFuture() {
        return f;
    }

    public static String getToken() {
        return token;
    }

    public static void  setToken(String token1) {
        token = token1;
    }
}
