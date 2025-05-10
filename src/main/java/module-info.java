module com.yonas.chat.chatroom_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires java.xml.bind; // For JAXB (Java 9–10)
    requires java.activation; // For JAXB's dependency
    requires java.xml; // For JAXB's XML processing

    opens com.yonas.chat.chatroom_javafx to javafx.fxml;
    exports com.yonas.chat.chatroom_javafx;
}
