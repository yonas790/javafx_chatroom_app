module com.yonas.chatfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.yonas.chatfx to javafx.fxml;
    exports com.yonas.chatfx;
}