module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires io.netty.codec;
    requires java.sql;

    exports netty;
    opens netty to javafx.fxml;
    exports dto;
    opens dto to javafx.fxml;
}
