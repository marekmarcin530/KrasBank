module com.example.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
   requires org.json;



    opens com.example.projekt to javafx.fxml;
    exports com.example.projekt;
}