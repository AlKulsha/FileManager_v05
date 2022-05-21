package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.dto.BasicRequest;


public class Network {

    private static final Network  INSTANCE = new Network();
    private SocketChannel channel;
    private static final String HOST = "localhost";
    private static final int PORT = 45001;
    public static final int MB_20 = 20 * 1_000_000;

    public Network(){
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try{
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            channel = socketChannel;
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(MB_20, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new ChannelHandler()
                            );
                        }
                    });
                ChannelFuture channelFuture = bootstrap.connect(HOST, PORT).sync();
                /*чтобы канал после соединения сразу не закрылся,
                делаем ожидание внешнего закрытия*/
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.setDaemon(true);
        t.start();
    }
    public void close(){
        channel.close();
    }

    public void sendRequest(BasicRequest basicRequest) throws InterruptedException {
        channel.writeAndFlush(basicRequest).sync();
    }

    public static Network getInstance(){
        return INSTANCE;
    }
}
