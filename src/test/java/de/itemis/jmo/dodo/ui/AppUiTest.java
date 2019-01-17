package de.itemis.jmo.dodo.ui;

import static org.testfx.assertions.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
@RunWith(JUnitPlatform.class)
public class AppUiTest {

    private Button button;

    @Start
    void onStart(Stage stage) {
        button = new Button("click me!");
        button.setId("myButton");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    @Test
    void should_contain_button(FxRobot robot) {
        assertThat(button).hasText("click me!");
        assertThat(robot.lookup("#myButton").queryAs(Button.class)).hasText("click me!");
        assertThat(robot.lookup(".button").queryAs(Button.class)).hasText("click me!");
        assertThat(robot.lookup(".button").queryButton()).hasText("click me!");
        robot.clickOn(button);
        assertThat(robot.lookup(".button").queryButton()).hasText("clicked!");
    }
}
