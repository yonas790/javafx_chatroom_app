module com.yonas.chat.javafx_chatroom_app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.yonas.chat.javafx_chatroom_app to javafx.fxml;
    exports com.yonas.chat.javafx_chatroom_app;
}