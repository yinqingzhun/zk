package qs.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import qs.util.DateHelper;

import javax.activation.DataHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, byte[] bytes) {

        log.info("channelRead0: {} bytes", bytes.length);
        try (FileOutputStream fileInputStream = new FileOutputStream("d:/d.jpg")) {
            fileInputStream.write(bytes);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        
       /* log.info("---> received message: {}, type:{}", msg, msg.getClass().getName());
        String request = msg.toString();
        String response = null;
        boolean close = false;
        if (request.isEmpty()) {
            response = "Please type something.\r\n";
        } else if ("bye".equals(request.toLowerCase())) {
            response = "Have a good day!\r\n";
            close = true;
        } else {
            response = "Did you say '" + request + "'?\r\n";
        }

        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the conversion.
        ChannelFuture future = ctx.write(response);

        // Close the connection after sending 'Have a good day!'
        // if the client has sent 'bye'.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }*/
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channel actived:" + ctx.channel());

        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + DateHelper.getNowString() + " now.\r\n");
        ctx.flush();

        //time demo
        //response current time on server side to client 
        //final ByteBuf time = ctx.alloc().buffer(4); // (2)
        //time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        //
        //final ChannelFuture f = ctx.writeAndFlush(time); // (3)
        //f.addListener(new ChannelFutureListener() {
        //    @Override
        //    public void operationComplete(ChannelFuture future) {
        //        assert f == future;
        //        ctx.close();
        //    }
        //}); // (4)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}