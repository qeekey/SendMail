package cn.com.yourcompany.mailapi;
import cn.com.yourcompany.mailapi.comm.BlackEmailThread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * User: qeekey
 * Date: 2018/5/14
 * Time: 下午5:02
 * To change this template use File | Settings | File Templates.
 */
public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + HttpServer.class.getSimpleName() +
                            " <port>");
            return;
        }

        //为了初始化SMTP参数
        PropUtils propUtils = new PropUtils();

        int port = Integer.parseInt(args[0]);
        new HttpServer(port).start();

        //把一些离职的Email忽略，不用发送邮件了
        Timer timer = new Timer();
        timer.schedule(new BlackEmailThread(), 0 , 86400000);  //一天重新更新一下外部需要忽略的邮件列表

//        timer.schedule(new BlackEmailThread(), 0 , 10*1000);  //一天重新更新一下外部需要忽略的邮件列表
    }

    public void start() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            NioEventLoopGroup group = new NioEventLoopGroup();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline()
                                    .addLast("decoder", new HttpRequestDecoder())   // 1
                                    .addLast("encoder", new HttpResponseEncoder())  // 2
                                    .addLast("aggregator", new HttpObjectAggregator(5120 * 1024))    // 3
                                    .addLast("handler", new HttpHandler());        // 4
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // determining the number of connections queued
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

            b.bind(port).sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
