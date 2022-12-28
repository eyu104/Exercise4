package com.zy.exercise3.Controller;

import com.zy.exercise3.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class infController {

    @FXML
    private Label LBtnState;

    @FXML
    private Label LInfo;

    @FXML
    private Label LIsSave;

    public int isClose = 0;

    @FXML
    void ok(ActionEvent event) {
        if (isClose != 3){
            HelloApplication.mainStage.close();
            System.exit(0);
        }else {
            HelloApplication.infoStage.close();
        }

    }



    public void show(int i,String msg){
        if (i != 3){
            if (i == 1){
                LBtnState.setText("OK");
                LIsSave.setText("Time saved");
            }

            if (i == 2){
                LBtnState.setText("Cancelled");
                LIsSave.setText("Time not saved");
            }
        }else {
            LBtnState.setText("Apply");
            LIsSave.setText("Time saved");
        }

        try{
            LInfo.setText(HelloApplication.mainViewController.showInfo());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
