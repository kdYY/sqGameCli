package org.sq.gameDemo.svr.common;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.sq.gameDemo.svr.game.entity.model.MessageProto;
import org.sq.gameDemo.svr.game.entity.model.MessageProto2;

import java.util.List;

public class ProtobufEncoder extends MessageToByteEncoder<MessageLite> {


    @Override
    protected void encode(
            ChannelHandlerContext ctx, MessageLite msg, ByteBuf out) throws Exception {


        byte[] body = msg.toByteArray();
        byte[] header = encodeHeader(msg, (short)body.length);

        out.writeBytes(header);
        out.writeBytes(body);

        return;
    }

    private byte[] encodeHeader(MessageLite msg, short bodyLength) {
        byte messageType = 0x0f;

        if (msg instanceof MessageProto.Msg) {
            messageType = 0x00;
        }
        if(msg instanceof MessageProto2.Msg) {
            messageType = 0x01;
        }

        byte[] header = new byte[4];
        header[0] = (byte) (bodyLength & 0xff);
        header[1] = (byte) ((bodyLength >> 8) & 0xff);
        header[2] = 0; // 保留字段
        header[3] = messageType;

        return header;

    }
}