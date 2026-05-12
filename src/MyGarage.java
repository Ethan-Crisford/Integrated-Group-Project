import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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

    private TextField makeField, modelField, yearField, milesField, priceField;
    private TextField NameField, CategoryField, PriceField, PartConditionField, BrandField;
    private Label imageLabel;
    private File selectedFile;
    private Stage formStage;
    private VBox Owned;
    private final ComboBox<String> type = new ComboBox<>();

    public void start(Stage stage) {
        VBox main = new VBox(20);
        main.setPadding(new Insets(20));
        main.setAlignment(Pos.TOP_CENTER);
        main.setStyle("-fx-background-color: #f4f4f4;"); // Light grey background like search

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.TOP_CENTER);

        Button BackButton = new Button("Back");
        BackButton.setStyle("-fx-background-color: #6b21a8; -fx-text-fill: white; -fx-cursor: hand;");
        BackButton.setOnAction(this::Back);
        HBox backContainer = new HBox(BackButton);
        backContainer.setAlignment(Pos.TOP_LEFT);

        Label title = new Label(Session.username + "'s Garage");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        headerBox.getChildren().addAll(backContainer, title);

        Button addItemBtn = new Button("Add Item");
        addItemBtn.setStyle("-fx-background-color: #6b21a8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
        addItemBtn.setOnAction(this::openAddItemForm);

        type.getItems().addAll("Cars", "Parts");
        type.setValue("Cars");

        HBox controls = new HBox(15);
        controls.getChildren().addAll(addItemBtn, type);
        controls.setAlignment(Pos.CENTER);

        Owned = new VBox(15);
        Owned.setPadding(new Insets(10));

        ScrollPane scroll = new ScrollPane(Owned);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        main.getChildren().addAll(headerBox, controls, scroll);
        stage.setScene(new Scene(main, 750, 700)); // Widened slightly for the cards
        stage.setTitle("My Garage");
        stage.show();

        ShowItems();
    }

    private void ShowItems() {
        Owned.getChildren().clear();
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            // Load Cars
            String carSql = "SELECT * FROM cars WHERE user_id = ?";
            PreparedStatement psCar = con.prepareStatement(carSql);
            psCar.setInt(1, Session.userId);
            ResultSet rsCar = psCar.executeQuery();

            while (rsCar.next()) {
                String title = rsCar.getInt("year") + " " + rsCar.getString("make") + " " + rsCar.getString("model");
                String subtitle = rsCar.getInt("miles") + " miles";
                double price = rsCar.getDouble("price");
                String imgPath = rsCar.getString("image_path");
                int id = rsCar.getInt("id");

                HBox card = createGarageCard(title, subtitle, price, imgPath, "cars", id);
                Owned.getChildren().add(card);
            }

            // Load Parts
            String partSql = "SELECT * FROM parts WHERE user_id = ?";
            PreparedStatement psPart = con.prepareStatement(partSql);
            psPart.setInt(1, Session.userId);
            ResultSet rsPart = psPart.executeQuery();

            while (rsPart.next()) {
                String title = rsPart.getString("name");
                String subtitle = rsPart.getString("brand") + " | " + rsPart.getString("part_condition");
                double price = rsPart.getDouble("price");
                String imgPath = rsPart.getString("image_path");
                int id = rsPart.getInt("id");

                HBox card = createGarageCard(title, subtitle, price, imgPath, "parts", id);
                Owned.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox createGarageCard(String title, String subtitle, double price, String imgPath, String tableName, int id) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 15; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        // Image Handling
        ImageView imageView = new ImageView();
        try {
            if (imgPath != null && !imgPath.isEmpty()) {
                File file = new File(imgPath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString(), 120, 80, true, true));
                } else {
                    imageView.setImage(new Image("https://via.placeholder.com/120x80.png?text=No+Photo"));
                }
            }
        } catch (Exception e) {
            imageView.setImage(new Image("https://via.placeholder.com/120x80.png?text=Error"));
        }

        imageView.setFitWidth(120);
        imageView.setFitHeight(80);
        Rectangle clip = new Rectangle(120, 80);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        imageView.setClip(clip);


        VBox info = new VBox(5);
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label lblSub = new Label(subtitle);
        lblSub.setStyle("-fx-text-fill: #666;");
        Label lblPrice = new Label("£" + String.format("%.2f", price));
        lblPrice.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #6b21a8;");
        info.getChildren().addAll(lblTitle, lblSub, lblPrice);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Remove Button
        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        removeBtn.setOnAction(e -> deleteItem(tableName, id));

        card.getChildren().addAll(imageView, info, spacer, removeBtn);
        return card;
    }

    private void deleteItem(String table, int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this item?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try (Connection con = DriverManager.getConnection(url, user, pass)) {
                String sql = "DELETE FROM " + table + " WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                ShowItems(); // Refresh
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void openAddItemForm(ActionEvent event) {
        formStage = new Stage();
        if (type.getValue().equals("Cars")) CarShowForm(formStage);
        else PartShowForm(formStage);
    }

    private void CarShowForm(Stage stage) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
        Label Title = new Label("Add Car");
        Title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        makeField = new TextField(); makeField.setPromptText("Make");
        modelField = new TextField(); modelField.setPromptText("Model");
        yearField = new TextField(); yearField.setPromptText("Year");
        milesField = new TextField(); milesField.setPromptText("Miles");
        priceField = new TextField(); priceField.setPromptText("Price");
        imageLabel = new Label("No image selected");
        Button uploadBtn = new Button("Upload Image");
        uploadBtn.setOnAction(this::Upload);
        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(this::SubmitCar);
        form.getChildren().addAll(Title, makeField, modelField, yearField, milesField, priceField, uploadBtn, imageLabel, submitBtn);
        stage.setScene(new Scene(form, 400, 500));
        stage.show();
    }

    private void PartShowForm(Stage stage) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
        Label Title = new Label("Add Part");
        Title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        NameField = new TextField(); NameField.setPromptText("Name");
        CategoryField = new TextField(); CategoryField.setPromptText("Category");
        PriceField = new TextField(); PriceField.setPromptText("Price");
        PartConditionField = new TextField(); PartConditionField.setPromptText("Condition");
        BrandField = new TextField(); BrandField.setPromptText("Brand");
        imageLabel = new Label("No image selected");
        Button uploadBtn = new Button("Upload Image");
        uploadBtn.setOnAction(this::Upload);
        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(this::SubmitPart);
        form.getChildren().addAll(Title, NameField, CategoryField, PriceField, PartConditionField, BrandField, uploadBtn, imageLabel, submitBtn);
        stage.setScene(new Scene(form, 400, 500));
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
            if (rs.next()) {
                int carId = rs.getInt(1);
                handleImageSaving(con, carId, "cars");
            }
            formStage.close();
            ShowItems();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void SubmitPart(ActionEvent event) {
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            String sql = "INSERT INTO parts (user_id, name, category, price, part_condition, brand) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Session.userId);
            ps.setString(2, NameField.getText());
            ps.setString(3, CategoryField.getText());
            ps.setDouble(4, Double.parseDouble(PriceField.getText()));
            ps.setString(5, PartConditionField.getText());
            ps.setString(6, BrandField.getText());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int partId = rs.getInt(1);
                handleImageSaving(con, partId, "parts");
            }
            formStage.close();
            ShowItems();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleImageSaving(Connection con, int id, String type) throws Exception {
        if (selectedFile != null) {
            File dir = new File("images/" + type + "/" + id);
            dir.mkdirs();
            File dest = new File(dir, selectedFile.getName());
            Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String path = "images/" + type + "/" + id + "/" + selectedFile.getName();
            PreparedStatement ps = con.prepareStatement("UPDATE " + type + " SET image_path = ? WHERE id = ?");
            ps.setString(1, path);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private void Back(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
        new HomePageUI().start(new Stage());
    }
}