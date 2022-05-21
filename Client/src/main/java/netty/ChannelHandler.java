package netty;

import netty.dto.BasicResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


import java.io.File;
import java.util.List;


public class ChannelHandler extends ChannelInboundHandlerAdapter {

    private final ClientService clientService = new ClientService();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("Server is on");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicResponse response = (BasicResponse) msg;
        System.out.println(response.getResponse());

        if("login ok".equals(response)){
            clientService.loginSuccessful();
            ctx.writeAndFlush(new GetFileListRequest());
            return;
        }

        if(response instanceof GetFileListResponse){
            List<File> serverItemsList =  ((GetFileListResponse)response).getItemsList();
            clientService.putServerFileList(serverItemsList);
            return;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
