package qs.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

@Slf4j
public class NettyClient {

    private Bootstrap bootstrap = null;
    private Channel ch = null;
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyClient(InetSocketAddress address) {
        try {

            log.info("client init...");
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            // 解码编码
                            socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                            socketChannel.pipeline().addLast( new ByteArrayEncoder());
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });


            ch = bootstrap.connect(address).sync().channel();
            log.info("netty client start at {}:{}", address.getHostName(), address.getPort());

            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Object msg) {
        // 传数据给服务端
        ch.writeAndFlush(msg);
        log.info("netty client is sending message: {}", msg);
    }

    @PreDestroy
    public void close() {
        ch.close();
        workerGroup.shutdownGracefully();
        log.info("netty client closed");
    }
    
    private void sendFromStdIn() throws Exception{
        // Read commands from the stdin.
        ChannelFuture lastWriteFuture = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            String line = in.readLine();
            if (line == null) {
                break;
            }

            // Sends the received line to the server.
            lastWriteFuture = ch.writeAndFlush(line + "\r\n");

            // If user typed the 'bye' command, wait until the server closes
            // the connection.
            if ("bye".equals(line.toLowerCase())) {
                ch.closeFuture().sync();
                break;
            }
        }

        // Wait until all messages are flushed before closing the channel.
        if (lastWriteFuture != null) {
            lastWriteFuture.sync();
        }
    }


}