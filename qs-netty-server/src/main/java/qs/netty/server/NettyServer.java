package qs.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class NettyServer {
    @Value("${netty.server.port}")
    private int port;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private volatile AtomicBoolean started = new AtomicBoolean(false);

    /**
     * 启动服务
     */
    public void run() {
        if (!started.compareAndSet(false, true))
            return;

        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Start the server.
            ChannelFuture f = serverBootstrap.bind(port).sync();
            log.info("netty server started at port {}", port);
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            //f.channel().closeFuture().sync();
            //log.info("Netty closed");
        } catch (Exception e) {
            log.error("netty server start error:", e);
            close();
        }


    }

    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("netty server closed");
    }


}