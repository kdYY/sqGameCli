package org.sq.gameDemo.cli.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import org.sq.gameDemo.cli.GameCli;
import org.sq.gameDemo.cli.service.SendOrderService;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.dispatch.DispatchRequest;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@ChannelHandler.Sharable
public class CliHandler extends SimpleChannelInboundHandler<MsgEntity> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgEntity msgEntity) throws Exception {

        try {
            DispatchRequest.dispatchRequest(ctx, msgEntity);
        } catch (Exception e) {
            try {
                MessageProto.Msg msg = MessageProto.Msg.parseFrom(msgEntity.getData());
                //gameCli.setToken( msg.getToken());
                System.out.println(msg.getContent());
                if(msgEntity.getCmdCode() == OrderEnum.Exit.getOrderCode() && msg.getResult() == 200) {
                    GameCli.setToken("");
                }
             } catch (Exception e1) {
                throw e1;
            }
        }

    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("激活时间是："+new Date());
        //System.out.println("HeartBeatClientHandler channelActive");
        ctx.fireChannelActive();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("停止时间是："+new Date());
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                GameCli.init(GameCli.getTokenPath());
            }
        }, 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

}
