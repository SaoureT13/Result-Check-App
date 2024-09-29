package org.example.studentrevealexam;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.event.EventHandler;
import org.example.studentrevealexam.models.Exams;
import org.example.studentrevealexam.models.Student;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;


public class HomeController {
    private static final Logger logger = Logger.getLogger(HomeController.class.getName());
    StudentDao studentDao = new StudentDao();
    ExamDao examDao = new ExamDao();
    Student student;
    double noteTotal;
    int examCount;
    TableView<Map.Entry<String, Integer>> table;

    @FXML
    public MFXButton restartButton;

    @FXML
    public Label fullname;

    @FXML
    public Label school;

    @FXML
    public Label birthday;

    @FXML
    public Label totalPoint;

    @FXML
    public Label moyenne;

    @FXML
    public VBox detailsBox;

    @FXML
    public VBox tableBox;

    @FXML
    public ImageView successImage;

    @FXML
    public ImageView lostImage;

    @FXML
    public Button detailsButton1;

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
    TextField matEtudTextField;

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
    protected void onHelloButtonClick() {
        String mat = matEtudTextField.getText().trim();

        //Rendre invisible

        //option 1
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                student = studentDao.retrieveResult(mat);
                examCount = examDao.retrieveExamNum();

                Platform.runLater(() -> {
                    if (student != null) {
                        animation(submitBox, -1000, 2000, actionEvent -> {
                            submitBox.setVisible(false);
                            submitBox.setManaged(false);
                        });

                        resultBox.setVisible(true);
                        resultBox.setManaged(true);
                        animation(resultBox, 0, 2000, null);

                        //Student.getNotes retourne un HashMap
                        for (String i : student.getNotes().keySet()) {
                            noteTotal += student.getNotes().get(i);
                        }

                        if ((noteTotal / examCount) >= 10) {
                            infoText1.setText("Félicitation " + student.getStr_nom_etud() + " " + student.getStr_prenoms_etud());
                            infoText1.setTextFill(Color.web("#3bda15"));
                            successImage.setVisible(true);
                            successImage.setManaged(true);
                            lostImage.setVisible(false);
                            lostImage.setManaged(false);
                        } else {
                            infoText1.setText("Échec");
                            infoText1.setTextFill(Color.web("#FF4242"));
                            lostImage.setVisible(true);
                            lostImage.setManaged(true);
                            successImage.setVisible(false);
                            successImage.setManaged(false);
                        }
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

    @FXML
    protected void restart() {
        animation(detailsBox, 1000, 2000, event -> {
            detailsBox.setManaged(false);
            detailsBox.setVisible(false);
            tableBox.getChildren().remove(table);
            noteTotal = 0;
            examCount = 0;
            resultBox.setVisible(true);
            resultBox.setManaged(true);
            animation(resultBox, 0, 2000, event1 -> {
                animation(resultBox, 1000, 2000, event2 -> {
                    submitBox.setVisible(true);
                    submitBox.setManaged(true);
                    animation(submitBox, 0, 2000, null);
                });
            });
        });
    }

    @FXML
    protected void onRevealDetails(){
        animation(resultBox, -1000, 2000, event -> {
            resultBox.setManaged(false);
            resultBox.setVisible(false);

            detailsBox.setManaged(true);
            detailsBox.setVisible(true);
        });

        animation(detailsBox, 0, 2000, null);

        fullname.setText("Nom et prénoms: " + student.getStr_nom_etud() + " " + student.getStr_prenoms_etud());
        birthday.setText("Date de naissance: " + student.getDt_date_naissance_etud());
        school.setText("Ecole : " + student.getStr_ecole_etud());
        totalPoint.setText("Total point : " + noteTotal + "/" + (examCount * 20));
        moyenne.setText("Moyenne : " + (noteTotal / examCount));

        Task<Void> task = new Task<>() {
            @Override
            public Void call() throws Exception {

                //Initialiser un hashmap pour accueillir des objets {matière : note}
                HashMap<String, Integer> studentNotesDetails = new HashMap<>();
                Exams exams = examDao.retrieveAllExam();

                //Transformer le hashmap en un list pour pouvoir accepter au objet grâce à leur index
                List<Map.Entry<String, Integer>> entryList = new ArrayList<>(student.getNotes().entrySet());

                int i = 0;
                //Lier les notes de l'étudiant aux examens correspondant
                for (String exam : exams.getExamList()) {
                    Map.Entry<String, Integer> entry = null;

                    for (Map.Entry<String, Integer> objet : entryList) {
                        if (Objects.equals(objet.getKey(), exam)) {
                            entry = objet;
                        }
                    }
                    if (entry != null) {
                        studentNotesDetails.put(exam, entry.getValue());
                    } else {
                        studentNotesDetails.put(exam, 0);
                    }
                    i += 1;
                }

                List<Map.Entry<String, Integer>> entryList1 = new ArrayList<>(studentNotesDetails.entrySet());
                ObservableList<Map.Entry<String, Integer>> data = FXCollections.observableArrayList(entryList1);

                table = new TableView<>();
                table.setItems(data);

                TableColumn<Map.Entry<String, Integer>, String> examCol = new TableColumn<>("Matière");
                examCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));

                TableColumn<Map.Entry<String, Integer>, Integer> noteCol = new TableColumn<>("Note");
                noteCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue()));

                Platform.runLater(() -> {
                    examCol.setPrefWidth(256);
                    noteCol.setPrefWidth(117);
                    table.getColumns().add(examCol);
                    table.getColumns().add(noteCol);

                    table.setPrefHeight(159);
                    table.setPrefWidth(345);

                    tableBox.getChildren().add(table);
                });

                return null;
            }
        };

        new Thread(task).start();
    }
}