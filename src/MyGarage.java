import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class MyGarage {
    private final String url = "jdbc:mysql://165.227.235.122/ceb96_CI536Database";
    private final String user = "ceb96_CI536Login";
    private final String pass = "4V9o&G$?!ro)chO%H[";
    private TextField makeField;
    private TextField modelField;
    private TextField yearField;
    private TextField milesField;
    private TextField priceField;
    private Label imageLabel;
    private File selectedFile;
    private Stage formStage;

    public void start(Stage stage) {
        VBox main = new VBox(20);
        main.setPadding(new Insets(20));
        main.setAlignment(Pos.TOP_CENTER);

        VBox main2 = new VBox(10);;
        main2.setAlignment(Pos.TOP_CENTER);

        Button BackButton = new Button ("Back");
        BackButton.setOnAction(this::Back);
        HBox Back = new HBox(BackButton);
        Back.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("My Garage - " + Session.username);

        title.setAlignment(Pos.TOP_CENTER);
        main2.getChildren().addAll(Back, title);

        Button addItemBtn = new Button("Add Item");
        addItemBtn.setOnAction(this::openAddItemForm);

        main.getChildren().addAll(main2, addItemBtn);
        stage.setScene(new Scene(main, 400, 300));
        stage.setTitle("Garage");
        stage.show();
    }

    private void openAddItemForm(ActionEvent event) {
        formStage = new Stage();
        showForm(formStage);
    }

    private void showForm(Stage stage) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);

        makeField = new TextField();
        makeField.setPromptText("Make");

        modelField = new TextField();
        modelField.setPromptText("Model");

        yearField = new TextField();
        yearField.setPromptText("Year");

        milesField = new TextField();
        milesField.setPromptText("Miles");

        priceField = new TextField();
        priceField.setPromptText("Price");

        imageLabel = new Label("No image selected");

        Button uploadBtn = new Button("Upload Image");
        uploadBtn.setOnAction(this::handleUpload);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(this::handleSubmit);

        form.getChildren().addAll(makeField, modelField, yearField, milesField, priceField, uploadBtn, imageLabel, submitBtn);
        stage.setScene(new Scene(form, 400, 500));
        stage.setTitle("Add Item");
        stage.show();
    }

    private void handleUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"));
        File file = fileChooser.showOpenDialog(formStage);
        if (file != null) {
            selectedFile = file;
            imageLabel.setText(file.getName());
        }
    }

    private void handleSubmit(ActionEvent event) {
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            String sql = "INSERT INTO cars (user_id, make, model, year, miles, price) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Session.userId);
            ps.setString(2, makeField.getText());
            ps.setString(3, modelField.getText());
            ps.setInt(4, Integer.parseInt(yearField.getText()));
            ps.setInt(5, Integer.parseInt(milesField.getText()));
            ps.setDouble(6, Double.parseDouble(priceField.getText()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int carId = -1;

            if (rs.next()) {
                carId = rs.getInt(1);
            }

            if (selectedFile != null && carId != -1) {
                File dir = new File("images/cars/" + carId);
                dir.mkdirs();
                File dest = new File(dir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                String path = "images/cars/" + carId + "/" + selectedFile.getName();
                PreparedStatement ps2 = con.prepareStatement("UPDATE cars SET image_path = ? WHERE id = ?");
                ps2.setString(1, path);
                ps2.setInt(2, carId);
                ps2.executeUpdate();
            }

            formStage.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void Back(ActionEvent event)
    {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        HomePageUI home = new HomePageUI();
        home.show(new Stage());
    }
}