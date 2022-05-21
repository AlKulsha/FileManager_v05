package netty;

import dto.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class BasicHandler extends ChannelInboundHandlerAdapter {
    private static final BasicResponse LOGIN_OK_RESPONCE = new BasicResponse ("login ok");
    private static final BasicResponse LOGIN_BAD_RESPONCE = new BasicResponse ("login bad");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("Client connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicRequest request = (BasicRequest) msg;
        System.out.println(request.getType());

        if(request instanceof AuthRequest){
            AuthRequest authRequest = (AuthRequest) request;

            if(AuthRequest.getLogin().equals("dp")){
                ctx.writeAndFlush(LOGIN_OK_RESPONCE);
            }else{
                ctx.writeAndFlush(LOGIN_BAD_RESPONCE);
            }
        } else if(request instanceof GetFileListRequest){
            Path serverPath = Paths.get("D:/Alena/Java4_cloudStorage/FileManager_v04/server-dir");
            List<File> pathList = Files.list(serverPath)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            BasicResponse basicResponse = new GetFileListResponse("OK", pathList);
            ctx.writeAndFlush(basicResponse);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
