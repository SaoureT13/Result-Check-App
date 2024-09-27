package org.example.studentrevealexam;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.event.EventHandler;
import org.example.studentrevealexam.models.Student;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HomeController {
    private static final Logger logger = Logger.getLogger(HomeController.class.getName());
    private final StudentDao studentDao = new StudentDao();
    Student student;

    @FXML
    public Label infoText1;

    //Composant du texte à afficher, devra decider de la couleur quand le résultat sera calculé et du texte à écrire.
    @FXML
    public Label infoText;

    //Bouton pour afficher les details de l'examen
    @FXML
    public Button detailsButton;

    @FXML
    public VBox submitBox;

    @FXML
    //Contenir du résultat
    public VBox resultBox;

    @FXML
    private Label welcomeText;

    @FXML
    private TextField matEtudTextField;

    @FXML
    public HBox errorBox;

    public void animation(VBox element, int X, int duration, EventHandler<ActionEvent> onFinish) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(element);
        transition.setToX(X);
        transition.setDuration(Duration.millis(duration));
        transition.setAutoReverse(true);
        transition.setCycleCount(1);
        if (onFinish != null) {
            transition.setOnFinished(onFinish);
        }
        transition.play();
    }

    @FXML
    protected void onHelloButtonClick() throws SQLException {
        String mat = matEtudTextField.getText().trim();

        //Rendre invisible

        //option 1
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                student = studentDao.retrieveResult(mat);

                Platform.runLater(() -> {
                    if (student != null) {
                        animation(submitBox, -1000, 2000, actionEvent -> {
                            submitBox.setVisible(false);
                            submitBox.setManaged(false);
                        });

                        resultBox.setVisible(true);
                        resultBox.setManaged(true);
                        animation(resultBox, 0, 2000, null);
                        infoText1.setText("Félicitations" + student.getStr_nom_etud() + " " + student.getStr_prenoms_etud());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Erreur");
                        alert.setHeaderText(null);
                        alert.setContentText("Le matricule entré ne correspond à aucun étudiant " + mat);
                        alert.showAndWait();
                    }
                });


                return null;
            }
        };
//
        new Thread(task).start();

    }
}