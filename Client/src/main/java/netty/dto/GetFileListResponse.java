package netty.dto;

import java.io.File;
import java.util.List;

public class GetFileListResponse extends BasicResponse {
    private final List<File> itemsList;

    public GetFileListResponse(String response, List<File> itemsList) {
        super(response);
        this.itemsList = itemsList;
    }

    public List<File> getItemsList() {
        return itemsList;
    }
}
