package de.bht.fpa.mail.gruppe15.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of the project which initializes the main Window.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public class FpaMailer extends Application {

    @Override
    public void start(final Stage stage) throws Exception {
        final Parent root;
        root = FXMLLoader.load(getClass().getResource("/de/bht/fpa/mail/gruppe15/view/MainWindow.fxml"));

        final Scene scene;
        scene = new Scene(root);
        stage.setTitle("FPA Mail application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

}
