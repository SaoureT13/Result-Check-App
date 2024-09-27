module org.example.studentrevealexam {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;

    opens org.example.studentrevealexam to javafx.fxml;
    exports org.example.studentrevealexam;
    exports org.example.studentrevealexam.models;
}