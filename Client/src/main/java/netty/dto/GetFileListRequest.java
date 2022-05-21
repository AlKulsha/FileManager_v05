package netty.dto;

public class GetFileListRequest implements BasicRequest{
    @Override
    public String getType() {
        return "fileListRequest";
    }
}
