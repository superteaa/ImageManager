import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.stream.Stream;

public class OverlayLayout_Demo extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Node Overlay Demo");
        primaryStage.show();

        HBox hBox = new HBox(new Button("One"), new Button("Two"));
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        StackPane hPane = new StackPane(hBox);
        hPane.setMaxHeight(100);
        hPane.setVisible(false);
        hPane.setStyle("-fx-background-color:#55555550");

        VBox vBox = new VBox(new Button("One"), new Button("Two"));
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        StackPane vPane = new StackPane(vBox);
        vPane.setMaxWidth(100);
        vPane.setVisible(false);
        vPane.setStyle("-fx-background-color:#55555550");

        Button left = new Button("Left");
        Button top = new Button("Top");
        Button right = new Button("Right");
        Button bottom = new Button("Bottom");
        VBox buttons = new VBox(left, top, right, bottom);
        buttons.setStyle("-fx-border-width:2px;-fx-border-color:black;");
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        StackPane.setMargin(buttons, new Insets(15));

        StackPane content = new StackPane(buttons);
        content.setOnMouseClicked(e -> {
            Node node = vPane.isVisible() ? vPane : hPane;
            FadeTransition ft = new FadeTransition(Duration.millis(300), node);
            ft.setOnFinished(e1 -> node.setVisible(false));
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();
        });

        root.getChildren().addAll(content, hPane, vPane);

        Stream.of(left, top, right, bottom).forEach(button -> {
            button.setOnAction(e -> {
                vPane.setVisible(false);
                hPane.setVisible(false);
                Node node;
                switch (button.getText()) {
                    case "Left":
                    case "Right":
                        node = vPane;
                        StackPane.setAlignment(vPane, button.getText().equals("Left") ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
                        break;
                    default:
                        node = hPane;
                        StackPane.setAlignment(hPane, button.getText().equals("Top") ? Pos.TOP_CENTER : Pos.BOTTOM_CENTER);
                }
                node.setVisible(true);
                FadeTransition ft = new FadeTransition(Duration.millis(300), node);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.play();
            });
        });
    }

    public static void main(String... args) {
        Application.launch(args);
    }
}