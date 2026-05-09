package Screens;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Wraps an FXML screen in a proportional scaler.
 *
 * The FXML keeps its designed layout size, while the whole screen is
 * uniformly scaled up/down with the window. This makes text, buttons,
 * tables, spacing, and controls all scale together instead of only stretching.
 */
public final class ResponsiveScaler {
    private static final double FALLBACK_WIDTH = 1000.0;
    private static final double FALLBACK_HEIGHT = 650.0;

    private ResponsiveScaler() {}

    public static StackPane wrap(Parent content) {
        double designWidth = FALLBACK_WIDTH;
        double designHeight = FALLBACK_HEIGHT;

        if (content instanceof Region region) {
            if (region.getPrefWidth() > 0) {
                designWidth = region.getPrefWidth();
            }
            if (region.getPrefHeight() > 0) {
                designHeight = region.getPrefHeight();
            }

            /*
             * Lock the designed FXML size. The outer scaler controls the visual size.
             * This avoids a bad mix of "stretching some controls" and "scaling others".
             */
            region.setMinSize(designWidth, designHeight);
            region.setPrefSize(designWidth, designHeight);
            region.setMaxSize(designWidth, designHeight);
        }

        Group scaledGroup = new Group(content);

        StackPane viewport = new StackPane(scaledGroup);
        viewport.setAlignment(Pos.CENTER);
        viewport.getStyleClass().add("scaled-viewport");

        final double baseWidth = designWidth;
        final double baseHeight = designHeight;

        ChangeListener<Number> resizeListener = (observable, oldValue, newValue) -> {
            double availableWidth = viewport.getWidth();
            double availableHeight = viewport.getHeight();

            if (availableWidth <= 0 || availableHeight <= 0) {
                return;
            }

            /*
             * Uniform scale preserves proportions and avoids stretched text.
             * Example: 1200x780 scales a 1000x650 design to 1.2.
             */
            double scale = Math.min(availableWidth / baseWidth, availableHeight / baseHeight);

            /*
             * Prevent the UI from becoming unusably tiny on very small windows.
             * You can reduce this to 0.55 if you want more shrink.
             */
            scale = Math.max(scale, 0.65);

            scaledGroup.setScaleX(scale);
            scaledGroup.setScaleY(scale);
        };

        viewport.widthProperty().addListener(resizeListener);
        viewport.heightProperty().addListener(resizeListener);

        return viewport;
    }
}
