package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.cli.service.SendOrderService;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.OrderEnum;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Component
public class GameCli {

    private static String tokenPath;
    private static   SendOrderService sendOrderService = new SendOrderService();
    private static Scanner scanner = new Scanner(System.in);
    private static   Bootstrap b;
    private static Channel channel;
    private static final EventLoopGroup workerGroup;

    private static String token;

    public static void setChannel(Channel channel) {
        GameCli.channel = channel;
    }

    public static Channel getChannel() {
        return channel;
    }

    static {
        b = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        b.group(workerGroup).remoteAddress("127.0.0.1", 8085);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new CliChannelInitializer());
    }
    /**
     * 初始化客户端
     */
    public static void init(String tokenFileName) {
        tokenPath = tokenFileName;
        ChannelFuture future = null;
        try {

            future = b.connect();
            future.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    boolean succeed = channelFuture.isSuccess();
                    //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
                    if (!succeed) {
                        final EventLoop loop = channelFuture.channel().eventLoop();
                        loop.schedule(new Runnable() {
                            @Override
                            public void run() {
                                System.err.println("服务端链接不上，开始重连操作...");
                                init(tokenPath);
                            }
                        }, 1L, TimeUnit.SECONDS);
                    }else{
                        System.out.println("连接成功");
                        channel.writeAndFlush(sendOrderService.checkToken(tokenPath));
                    }
                }
            });
            channel = future.channel();

            //readToken();
        } catch (Exception e) {
            //---做反应
            e.printStackTrace();
            //------future.channel().pipeline().fireChannelInactive();
        }
    }

    public static void input() {
        String line = "";
        while (scanner.hasNext()) {
            try {
                line = scanner.nextLine();
                sendMsg(line);
                // line = "";
            } catch (Exception e) {
                e.printStackTrace();
                //关闭连接
                //future.channel().close().sync();
            }
        }
    }

    public static String getTokenPath() {
        return tokenPath;
    }


    /**
     * 发送请求
     * @param send
     * @return
     */
    public static void sendMsg(String send) throws Exception {
        // 传数据给服务端
        MsgEntity sendMsgEntity = sendMsgEntity(send);
        if(sendMsgEntity == null) {
            return;
        }
        channel.writeAndFlush(sendMsgEntity);
    }

    private  static MsgEntity sendMsgEntity(String request) throws Exception{
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


    public static void setToken(String token) {
        GameCli.token = token;
    }
}
