package qs.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import qs.util.DateHelper;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {
        channelHandlerContext.channel().attr(AttributeKey.valueOf("attribute_key")).set(msg);
        log.info("received server-side's msg [{}]:{}" ,DateHelper.getNowString(), msg);
        channelHandlerContext.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("client channel acitived: " + ctx.channel());
    }
}