package sa.stira.mousePosition;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main extends Application {
    public static Stage mainStage;
    public static Double mainStageOpacity=0.7;
    public static Parent root;
    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("mainUI.fxml"));
        mainStage=new Stage();

        createTrayIcon(mainStage);
        firstTime = true;
        mainStage.setOpacity(0);
        mainStage.setAlwaysOnTop(true);
        mainStage.initStyle(StageStyle.UTILITY);
      //  mainStage.setTitle("");
        mainStage.setTitle("Pointer");
        mainStage.setScene(new Scene(root, 130, 60));
        mainStage.show();
    }

    private boolean firstTime;
    private TrayIcon trayIcon;

    public void createTrayIcon(final Stage stage) {
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = null;
            try {
                image = ImageIO.read(new java.io.File("C:\\Users\\touseef.elahi\\IdeaProjects\\MousePositionIndicator\\src\\Images\\stiraLogo.jpg"));
            } catch (IOException ex) {
                System.out.println(ex);
            }

            stage.setOnCloseRequest(event -> {
                hide(stage);
                event.consume();
            });

            // create a action listener to listen for default action executed on the tray icon
            final ActionListener closeListener = e -> {
                showProgramIsExitingSafely();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            };


            // create a popup menu
            PopupMenu popup = new PopupMenu();
            ActionListener showListener = e -> Platform.runLater(() ->
            {
                stage.toFront();
                stage.setOpacity(mainStageOpacity);
            }
            );

            ActionListener helpListener = e -> Platform.runLater(() -> showHelp());

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem helpItem = new MenuItem("Info");
            helpItem.addActionListener(helpListener);
            popup.add(helpItem);

            MenuItem closeItem = new MenuItem("Exit");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);

            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Pointer Info", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(showListener);

            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            showStartupMessage();
        }
    }

    private void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Pointer location and color indicator utility");
        alert.setContentText("Press ~ key to get mouse position and pixel value \n" +
                "For Suggestions and Bug report contact touseef@stira.sa, touseefelahi@ymail.com");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Pointer Info",
                    "App running normally in background",
                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }
    public void showStartupMessage() {
            trayIcon.displayMessage("Pointer Info",
                    "Mouse Position indicator key '~'",
                    TrayIcon.MessageType.INFO);
    }

    public void showProgramIsExitingSafely() {
        if (firstTime) {
            trayIcon.displayMessage("Pointer Info",
                    "Closing all services",
                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }

    private void hide(final Stage stage) {
        Platform.runLater(() -> {
            if (SystemTray.isSupported()) {
                stage.toBack();
                showProgramIsMinimizedMsg();
            } else {
                System.exit(0);
            }
        });
    }


@Override public void stop(){
    System.out.println("Exiting Application");

}
    public static void main(String[] args) {
        launch(args);
    }
}
