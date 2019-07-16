package org.sq.gameDemo.cli.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;

@Sharable
public class ConnectorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //抓捕到有4秒没接受到write的事件后
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                MsgEntity msgEntity = new MsgEntity();
                msgEntity.setCmdCode(OrderEnum.PING.getOrderCode());
                MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
                builder.setContent("heartbeat");
                msgEntity.setData(builder.build().toByteArray());
                ctx.channel().writeAndFlush(msgEntity);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
