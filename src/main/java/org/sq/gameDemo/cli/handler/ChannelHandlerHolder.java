package org.sq.gameDemo.cli.handler;

import io.netty.channel.ChannelHandler;

public interface ChannelHandlerHolder {
    ChannelHandler[] handlers();
}
