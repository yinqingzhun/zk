package qs.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyClient {

    //private Bootstrap bootstrap;
    private ChannelFuture channelFuture;
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyClient(InetSocketAddress address) {
        try {
            log.info("client init...");
            Bootstrap  bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            // 解码编码
                            socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                            socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });


            channelFuture = bootstrap.connect(address).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object send(Object send) throws InterruptedException {
        // 传数据给服务端
        channelFuture.channel().writeAndFlush(send);
        channelFuture.channel().closeFuture().sync();
        return channelFuture.channel().attr(AttributeKey.valueOf("attribute_key")).get();
    }

    public void close() {
        channelFuture.channel().close();
        workerGroup.shutdownGracefully();
        log.info("Closed client!");
    }


}