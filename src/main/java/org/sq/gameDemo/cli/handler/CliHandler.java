package org.sq.gameDemo.cli.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.dispatch.DispatchRequest;

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
             } catch (Exception e1) {
                throw e1;
            }
        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);
    }
}
