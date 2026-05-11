import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Basket {
    public static ArrayList<String> basketItems = new ArrayList<>();
    private VBox itemsBox;

    public void start(Stage stage) {
        Label title = new Label("Basket");

        Button back = new Button("Back");
        back.setOnAction(this::back);

        Button Checkout = new Button("Checkout");
        //Checkout.setOnAction(this::Checkout);

        Checkout.setOnAction(event -> {
            Stage currentStage = (Stage) ((Button) event.getSource())
                    .getScene()
                    .getWindow();

            Checkout checkout = new Checkout();
            checkout.start(currentStage);
        });

        HBox BackBox = new HBox(back);
        HBox TitleBox = new HBox(title);

        Checkout.setAlignment(Pos.BOTTOM_CENTER);
        BackBox.setAlignment(Pos.TOP_LEFT);
        TitleBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.TOP_CENTER);

        vbox.getChildren().addAll(BackBox, TitleBox);

        itemsBox = new VBox(10);
        itemsBox.setAlignment(Pos.CENTER);

        vbox.getChildren().add(itemsBox);
        BasketItems();

        vbox.getChildren().add(Checkout);

        Scene scene = new Scene(vbox, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Basket");
        stage.show();
    }

    private void BasketItems() {
        itemsBox.getChildren().clear();
        for (String item : basketItems) {
            HBox Rrow = new HBox(10);
            Label label = new Label(item);
            Button remove = new Button("Remove");
            remove.setOnAction(e -> {
                basketItems.remove(item);
                BasketItems();
            });
            Rrow.getChildren().addAll(label, remove);
            itemsBox.getChildren().add(Rrow);
        }
    }

    private void back(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        HomePageUI home = new HomePageUI();
        home.start(new Stage());
    }
}