package sa.stira.mousePosition;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;


public class Controller implements NativeKeyListener {
    @FXML TextArea labelTextArea;
    int xLocation;
    int yLocation;
    int displayKey=41;
    int delayInSeconds=5;

public Controller(){
    createHook();
    }

@FXML public void exitApplication(ActionEvent event){
    try {
        // here you starts the hook
        GlobalScreen.registerNativeHook();
    } catch (NativeHookException ex) {
        //TODO Exception handling
    }
    // here you add the listener for the hook
    GlobalScreen.removeNativeKeyListener((NativeKeyListener) this);
    Platform.exit();
}

    private String getMousePointColor() {
        xLocation = MouseInfo.getPointerInfo().getLocation().x;
        yLocation = MouseInfo.getPointerInfo().getLocation().y;
        Robot r;
        try {
            r = new Robot();
            Color color=r.getPixelColor(xLocation,yLocation);
            String colorString="R:"+color.getRed()+" G:"+color.getGreen()+" B:"+color.getBlue();
            return (colorString + "\nX:"+xLocation+" Y:"+yLocation);
        } catch (AWTException ex) {
            //TODO Exception handling
            return ex.getMessage();
        }

    }

    private void createHook() {
        try {
            // here you starts the hook
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            //TODO Exception handling
        }
        // here you add the listener for the hook
        GlobalScreen.addNativeKeyListener((NativeKeyListener) this);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }
    private boolean timerStarted=false;
    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int inputKey = nativeKeyEvent.getKeyCode();

        if(inputKey==displayKey){
            Timer timer=new Timer();
            Platform.runLater( () -> {
                Platform.runLater( () ->Main.mainStage.toFront());
                Main.mainStage.setOpacity(Main.mainStageOpacity);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int x=MouseInfo.getPointerInfo().getLocation().x;
                int y=MouseInfo.getPointerInfo().getLocation().y;

                if(x>0) {
                    if (x > screenSize.getWidth() - Main.mainStage.getWidth()) {

                        x = (int) (screenSize.getWidth() - Main.mainStage.getWidth() - 10);
                    }
                }
                else {
                    if (Math.abs(x) < Main.mainStage.getWidth()) {
                        x = (int) ((-1)* ( Main.mainStage.getWidth() + 10));
                    }
                }

                if(Math.abs(y) >  screenSize.getHeight()- Main.mainStage.getHeight())
                {
                    y = (int)(screenSize.getHeight()-Main.mainStage.getHeight()-30);
                }

                Main.mainStage.setX(x+3);
                Main.mainStage.setY(y);
                labelTextArea.setText(getMousePointColor());
                if (isTimerStarted()){Platform.runLater(()->timer.cancel());}
                timerStarted = true;
                timer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        Platform.runLater(()->Main.mainStage.setOpacity(0));
                        timerStarted=false;
                    }
                }, delayInSeconds*1000);
            });
        }
    }
    public boolean isTimerStarted() {
        return this.timerStarted;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}
