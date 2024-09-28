module org.example.studentrevealexam {

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires mysql.connector.j;
    requires MaterialFX;

    opens org.example.studentrevealexam to javafx.fxml;
    exports org.example.studentrevealexam;
    exports org.example.studentrevealexam.models;
}