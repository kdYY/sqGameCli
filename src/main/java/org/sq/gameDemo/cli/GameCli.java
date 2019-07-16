package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.cli.handler.CliHandler;
import org.sq.gameDemo.cli.handler.ConnectionWatchdog;
import org.sq.gameDemo.cli.handler.ConnectorIdleStateTrigger;
import org.sq.gameDemo.cli.service.SendOrderService;
import org.sq.gameDemo.common.MsgDecoder;
import org.sq.gameDemo.common.MsgEncoder;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.svr.common.PoiUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Component
public class GameCli {

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
    public static void init() {
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
                                init();
                            }
                        }, 1L, TimeUnit.SECONDS);
                    }else{
                        System.out.println("重连成功");
                    }
                }
            });
            channel = future.channel();
            //readToken();
        } catch (Exception e) {
            //---做反应
            e.printStackTrace();
            future.channel().pipeline().fireChannelInactive();
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
