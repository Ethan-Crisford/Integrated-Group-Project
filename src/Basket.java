import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;

public class Basket {
    public static ArrayList<String> basketItems = new ArrayList<>();
    private VBox itemsBox;

    public void start(Stage stage) {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #f4f4f4;");

        // Back Button
        Button back = new Button("Back");
        back.setStyle("-fx-background-color: #6b21a8; -fx-text-fill: white; -fx-cursor: hand;");
        back.setOnAction(this::back);

        HBox backBox = new HBox(back);
        backBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Your Basket");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Container for items
        itemsBox = new VBox(15);
        itemsBox.setAlignment(Pos.TOP_CENTER);
        itemsBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(itemsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(450);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Checkout Button
        Button checkoutBtn = new Button("Proceed to Checkout");
        checkoutBtn.setStyle("-fx-background-color: #6b21a8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 30; -fx-font-size: 16px; -fx-background-radius: 5; -fx-cursor: hand;");
        checkoutBtn.setOnAction(event -> {
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Checkout checkout = new Checkout();
            checkout.start(currentStage);
        });

        mainLayout.getChildren().addAll(backBox, titleLabel, scrollPane, checkoutBtn);

        BasketItems();

        Scene scene = new Scene(mainLayout, 750, 750);
        stage.setScene(scene);
        stage.setTitle("Basket");
        stage.show();
    }

    private void BasketItems() {
        itemsBox.getChildren().clear();

        if (basketItems.isEmpty()) {
            Label emptyLabel = new Label("Your basket is currently empty.");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #888; -fx-padding: 50;");
            itemsBox.getChildren().add(emptyLabel);
            return;
        }

        for (String itemData : new ArrayList<>(basketItems)) {
            String title, price, imgPath;


            if (itemData.contains("|")) {
                String[] parts = itemData.split("\\|");
                title = parts[0];
                price = (parts.length > 1) ? parts[1] : "0.00";
                imgPath = (parts.length > 2) ? parts[2] : "";
            } else {

                String[] parts = itemData.split(" - ");
                title = parts[0];
                price = (parts.length > 1) ? parts[1].replace("£", "") : "0.00";
                imgPath = "";
            }

            HBox card = createBasketCard(title, price, imgPath, itemData);
            itemsBox.getChildren().add(card);
        }
    }

    private HBox createBasketCard(String title, String price, String imgPath, String originalData) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 15; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );


        ImageView imageView = new ImageView();
        try {
            if (imgPath != null && !imgPath.isEmpty()) {
                File file = new File(imgPath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString(), 120, 80, true, true));
                } else {
                    imageView.setImage(new Image("https://via.placeholder.com/120x80.png?text=No+Photo"));
                }
            } else {
                imageView.setImage(new Image("https://via.placeholder.com/120x80.png?text=No+Image"));
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
        info.setAlignment(Pos.CENTER_LEFT);
        info.setPrefWidth(250); // Give it enough space for the title

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        lblTitle.setWrapText(true);

        Label lblPrice = new Label("£" + price);
        lblPrice.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #6b21a8;");

        info.getChildren().addAll(lblTitle, lblPrice);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Remove Button
        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> {
            basketItems.remove(originalData);
            BasketItems();
        });

        card.getChildren().addAll(imageView, info, spacer, removeBtn);
        return card;
    }

    private void back(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        HomePageUI home = new HomePageUI();
        home.start(new Stage());
    }
}