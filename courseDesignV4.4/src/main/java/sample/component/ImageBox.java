package sample.component;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * @author 12242
 * @date 2021/05/18
 */
public class ImageBox extends VBox {
    ImageView imageView;

    public ImageBox(ImageView imageView) {
        this.imageView = imageView;
        getChildren().add(imageView);
        setStyle("-fx-border-color: #1e2f40;");
        setAlignment(Pos.CENTER);
        setPrefWidth(200);
        setPrefHeight(200);
    }
}
