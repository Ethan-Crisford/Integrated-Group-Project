import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.Objects;

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
    private TextField NameField;
    private TextField CategoryField;
    private TextField PriceField;
    private TextField PartConditionField;
    private TextField BrandField;
    private VBox Owned;

    private final ComboBox<String> type = new ComboBox<>();

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

        Label title = new Label(Session.username + "'s Garage");

        title.setAlignment(Pos.TOP_CENTER);
        main2.getChildren().addAll(Back, title);

        Button addItemBtn = new Button("Add Item");
        addItemBtn.setOnAction(this::openAddItemForm);

        type.getItems().addAll("Cars", "Parts");
        type.setValue("Cars");

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(addItemBtn,type);
        buttons.setAlignment(Pos.TOP_CENTER);

        Owned = new VBox(20);
        ScrollPane scroll = new ScrollPane(Owned);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        main.getChildren().addAll(main2, buttons, scroll);
        stage.setScene(new Scene(main, 600, 600));
        stage.setTitle("Garage");
        stage.show();

        if (formStage == null) {
            ShowItems();
        }
    }

    private void openAddItemForm(ActionEvent event) {
        if (type.getValue().equals("Cars")){
            formStage = new Stage();
            CarShowForm(formStage);
        }
        else{
            formStage = new Stage();
            PartShowForm(formStage);
        }
    }

    private void CarShowForm(Stage stage) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);

        Label Title = new Label("Car Form");
        Title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

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
        uploadBtn.setOnAction(this::Upload);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(this::SubmitCar);

        form.getChildren().addAll(Title,makeField, modelField, yearField, milesField, priceField, uploadBtn, imageLabel, submitBtn);
        stage.setScene(new Scene(form, 400, 500));
        stage.setTitle("Add Item");
        stage.show();
    }

    private void PartShowForm(Stage stage) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);

        Label Title = new Label("Part Form");
        Title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        NameField = new TextField();
        NameField.setPromptText("Name");

        CategoryField = new TextField();
        CategoryField.setPromptText("Category");

        PriceField = new TextField();
        PriceField.setPromptText("Price");

        PartConditionField = new TextField();
        PartConditionField.setPromptText("Part Condition");

        BrandField = new TextField();
        BrandField.setPromptText("Brand");

        imageLabel = new Label("No image selected");

        Button uploadBtn = new Button("Upload Image");
        uploadBtn.setOnAction(this::Upload);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(this::SubmitPart);

        form.getChildren().addAll(Title,NameField, CategoryField, PriceField, PartConditionField, BrandField, uploadBtn, imageLabel, submitBtn);
        stage.setScene(new Scene(form, 400, 500));
        stage.setTitle("Add Item");
        stage.show();
    }

    private void Upload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"));
        File file = fileChooser.showOpenDialog(formStage);
        if (file != null) {
            selectedFile = file;
            imageLabel.setText(file.getName());
        }
    }

    private void SubmitCar(ActionEvent event) {
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
            Owned.getChildren().clear();
            ShowItems();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void SubmitPart(ActionEvent event) {
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            String sql = "INSERT INTO parts (user_id, name, category, price, part_condition, brand) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Session.userId);
            ps.setString(2, NameField.getText());
            ps.setString(3, CategoryField.getText());
            ps.setInt(4, Integer.parseInt(PriceField.getText()));
            ps.setString(5,PartConditionField.getText());
            ps.setString(6, BrandField.getText());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int PartId = -1;

            if (rs.next()) {
                PartId = rs.getInt(1);
            }

            if (selectedFile != null && PartId != -1) {
                File dir = new File("images/parts/" + PartId);
                dir.mkdirs();
                File dest = new File(dir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                String path = "images/parts/" + PartId + "/" + selectedFile.getName();
                PreparedStatement ps2 = con.prepareStatement("UPDATE parts SET image_path = ? WHERE id = ?");
                ps2.setString(1, path);
                ps2.setInt(2, PartId);
                ps2.executeUpdate();
            }
            formStage.close();
            Owned.getChildren().clear();
            ShowItems();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void ShowItems(){
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            String OwnedCars = "SELECT * FROM cars WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(OwnedCars);
            ps.setInt(1, Session.userId);

            String OwnedParts =  "SELECT * FROM parts WHERE user_id = ?";
            PreparedStatement ps2 = con.prepareStatement(OwnedParts);
            ps2.setInt(1, Session.userId);

            ResultSet rs = ps.executeQuery();
            ResultSet rs2 = ps2.executeQuery();

            while (rs.next()) {
                String model = rs.getString("model");
                String make = rs.getString("make");
                int year = rs.getInt("year");
                int miles = rs.getInt("miles");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("image_path");
                HBox Rrow = new HBox(10);
                Label result = new Label(make + " | " + model + " | Year: " + year + " | Miles: " + miles + " | Price: £" + price + " |");
                result.setMinWidth(357);
                Rrow.getChildren().add(result);
                FileInputStream fileInputStream = new FileInputStream(imagePath);
                Image image = new Image(fileInputStream);
                ImageView imageViewer = new ImageView(image);
                imageViewer.setFitHeight(187);
                imageViewer.setFitWidth(187);
                imageViewer.setPreserveRatio(true);
                Rrow.getChildren().add(imageViewer);
                Owned.getChildren().add(Rrow);
            }

            while (rs2.next()) {
                String name = rs2.getString("name");
                String category = rs2.getString("category");
                double price = rs2.getDouble("price");
                String partCondition = rs2.getString("part_condition");
                String brand = rs2.getString("brand");
                String imagePath = rs2.getString("image_path");
                HBox Rrow = new HBox(10);
                Label result = new Label(name + " | " + category + " | Price: " + price + " | " + "Part Condition: " + partCondition + " | Brand: " + brand);
                result.setMinWidth(357);
                Rrow.getChildren().add(result);
                FileInputStream fileInputStream = new FileInputStream(imagePath);
                Image image = new Image(fileInputStream);
                ImageView imageViewer = new ImageView(image);
                imageViewer.setFitHeight(187);
                imageViewer.setFitWidth(187);
                imageViewer.setPreserveRatio(true);
                Rrow.getChildren().add(imageViewer);
                Owned.getChildren().add(Rrow);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void Back(ActionEvent event)
    {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        HomePageUI home = new HomePageUI();
        home.start(new Stage());
    }
}