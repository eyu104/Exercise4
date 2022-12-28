package com.zy.exercise3.Controller;

import com.zy.exercise3.HelloApplication;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;


import java.net.URL;
import java.util.*;

public class mainController implements Initializable {

    int flag = 1;
    public int btnState = 0;
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH)+1;
    int date = c.get(Calendar.DATE);
    int timeZoneChange = 0;
//    int hour = c.get(Calendar.HOUR_OF_DAY);
//    int minute = c.get(Calendar.MINUTE);
//    int second = c.get(Calendar.SECOND);

    Timer timer = new Timer();
    final String HOVERED_BUTTON_STYLE = "-fx-text-fill: white;-fx-background-color: #5b8cff;";
    final String NORMAL_BUTTON_STYLE = "-fx-text-fill: black;-fx-background-color: transparent;";
    String[] monthItems = new String[]{"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    String[] timeZones = new String[]{
            "(GMT -12:00)",
            "(GMT -11:00)",
            "(GMT -10:00)",
            "(GMT -9:00)",
            "(GMT -8:00)",
            "(GMT -7:00)",
            "(GMT -6:00)",
            "(GMT -5:00)",
            "(GMT -4:00)",
            "(GMT -3:00)",
            "(GMT -2:00)",
            "(GMT -1:00)",
            "(GMT)",
            "(GMT +1:00)",
            "(GMT +2:00)",
            "(GMT +3:00)",
            "(GMT +4:00)",
            "(GMT +5:00)",
            "(GMT +6:00)",
            "(GMT +7:00)",
            "(GMT +8:00)",
            "(GMT +9:00)",
            "(GMT +10:00)",
            "(GMT +11:00)",
            "(GMT +12:00)"
    };
    @FXML
    private AnchorPane datePane;
    @FXML
    private AnchorPane TimeZonePane;
    @FXML
    private ChoiceBox<String> CBMonth;
    @FXML
    private Spinner<Integer> SpinYear;
    @FXML
    private ChoiceBox<String> CBTimeZone;
    @FXML
    private CheckBox isChange;
    @FXML
    private Label LDateTime;
    @FXML
    private Spinner<Integer> SpinMin;
    @FXML
    private Spinner<Integer> SpinSec;
    @FXML
    private Spinner<Integer> SpinHour;
    @FXML
    private FlowPane FPDate;
    @FXML
    private Label LTimeZone;
    @FXML
    private Button okBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button applyBtn;
    @FXML
    private ImageView IMGTimeZone;
    TimerTask timerTaskBegin;
    ObservableList<String> monthList = FXCollections.observableArrayList();
    ObservableList<String> timeZoneList = FXCollections.observableArrayList();

    @FXML
    void apply(ActionEvent event) {
        if (flag != 1){
            timerTaskBegin.cancel();
            HelloApplication.infViewController.isClose = 3;
            HelloApplication.infViewController.show(3,showInfo());
            HelloApplication.infoStage.show();
            clockBegin();
            flag = 1;
        }

    }

    @FXML
    void cancel(ActionEvent event) {
        timer.cancel();
        HelloApplication.infViewController.show(2,showInfo());
        HelloApplication.infViewController.isClose = 2;
        HelloApplication.infoStage.show();
    }

    @FXML
    void ok(ActionEvent event) {
        HelloApplication.infViewController.isClose = 1;
        HelloApplication.infViewController.show(1,showInfo());
        timer.cancel();
        HelloApplication.infoStage.show();
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) throws NullPointerException{
        Date date = new Date();
        SpinHour.setValueFactory(new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int i) {
                Integer time = SpinHour.getValue();
                timerTaskBegin.cancel();flag = 0;
                if(time == 0){
                    SpinHour.getValueFactory().setValue(23);
                }else {
                    SpinHour.getValueFactory().setValue(time - 1);
                }
            }
            @Override
            public void increment(int i) {
                Integer time = SpinHour.getValue();
                timerTaskBegin.cancel();flag = 0;
                if (time == 23){
                    SpinHour.getValueFactory().setValue(0);
                }else {
                    SpinHour.getValueFactory().setValue(time + 1);
                }
            }
        });

        SpinMin.setValueFactory(new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int i) {
                Integer time = SpinMin.getValue();
                timerTaskBegin.cancel();flag = 0;
                if(time == 0){
                    SpinMin.getValueFactory().setValue(59);
                }else {
                    SpinMin.getValueFactory().setValue(time - 1);
                }
            }
            @Override
            public void increment(int i) {
                Integer time = SpinMin.getValue();
                timerTaskBegin.cancel();flag = 0;
                if (time == 60){
                    SpinMin.getValueFactory().setValue(0);
                }else {
                    SpinMin.getValueFactory().setValue(time + 1);
                }
            }
        });

        SpinSec.setValueFactory(new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int i) {
                Integer time = SpinSec.getValue();
                timerTaskBegin.cancel();flag = 0;
                if(time == 0){
                    SpinSec.getValueFactory().setValue(59);
                }else {
                    SpinSec.getValueFactory().setValue(time - 1);
                }
            }
            @Override
            public void increment(int i) {
                Integer time = SpinSec.getValue();
                timerTaskBegin.cancel();flag = 0;
                if (time == 60){
                    SpinSec.getValueFactory().setValue(0);
                }else {
                    SpinSec.getValueFactory().setValue(time + 1);
                }
            }
        });
        SpinHour.getValueFactory().setValue(date.getHours());
        SpinMin.getValueFactory().setValue(date.getMinutes());
        SpinSec.getValueFactory().setValue(date.getSeconds());

//        setDatePickContent();
//        datePane.getChildren().add(datePick);

        clockBegin();


        for (String M:monthItems) {
            monthList.add(M);
        }
        for (String TZ: timeZones
             ) {
            timeZoneList.add(TZ);
        }

        CBTimeZone.setItems(timeZoneList);
        CBTimeZone.setValue(timeZones[20]);
        CBTimeZone.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                LTimeZone.setText(t1);
                int old = 20;
                for (int i = 0; i < timeZones.length; i++) {
                    if (timeZones[i].equals(s)){
                        old = i;
                    }
                }
                for (int i = 0; i < timeZones.length; i++) {
                    if (timeZones[i].equals(t1)){

                        timeZoneChange += i - old;
                        System.out.println(timeZoneChange);
                        moveTimeZoneImg(i);
                    }
                }

                if (isChange.isSelected()){
                    SpinHour.getValueFactory().setValue(SpinHour.getValue() + timeZoneChange);
                    timeZoneChange = 0;
                }

            }
        });

        CBMonth.setItems(monthList);
        CBMonth.setValue(monthItems[month - 1 ]);
        CBMonth.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                for (int i = 0; i < 12; i++) {
                    if (monthItems[i] == t1){
                        System.out.println(i + 1);
                        month = i + 1;
                    }
                    setDatePickContent();
                }
                System.out.println(t1);
            }
        });

        SpinYear.setValueFactory(new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int i) {
                int tem = SpinYear.getValue();
                tem--;
                if (tem == 1921){
                    return;
                }else {
                    SpinYear.getValueFactory().setValue(tem);
                    year = tem;
                    setDatePickContent();
                }

            }

            @Override
            public void increment(int i) {
                int tem = SpinYear.getValue();
                tem++;
                if (tem == 2122){
                    return;
                }else {
                    SpinYear.getValueFactory().setValue(tem);
                    year = tem;
                    setDatePickContent();
                }
            }
        });

        SpinYear.getValueFactory().setValue(year);
        setDatePickContent();
        LTimeZone.setText(timeZones[20]);
        IMGTimeZone.setX(20 * 18.2);
    }

    private void setDatePickContent(){
        flag = 0;
//        FlowPane flow = new FlowPane();
//        flow.setVgap(2);
//        flow.setHgap(10);
//        flow.setPrefWrapLength(310);
        FPDate.getChildren().clear();
        List<Button> btnList = new ArrayList<>();
        //当月总天数
        int monthDays = getDaysOfMonth(month);
        //前一个月总天数
        int lastMonthDays = getDaysOfMonth(month-1);

        //判断当月第一天是星期几, curDay为多少则前一个月所在当前的天数就为多少
        int curDay = getWeekNumber(1);
        //getWeekNumber(monthDays)最后一天是星期几
        // 6-getWeekNumber(monthDays) 就为下个月有几天在当前日历
        int lastDay = 6-getWeekNumber(monthDays);

        List<Integer> allDayList = new ArrayList<>();



        for(int i = (curDay-1);i>-1;i--){
            //加入上个月在本日历的几天
            allDayList.add(lastMonthDays-i);
        }


        for(int i=1;i<=monthDays;i++){
            //加入本月的日历
            allDayList.add(i);
        }
        for(int i=1;i<=lastDay;i++){
            //加入下个月的几天
            allDayList.add(i);
        }
//
        int size = allDayList.size();
        int subDate = (size-lastDay-1);
        c.setTime(new Date());
        int realYear = c.get(Calendar.YEAR);
        int realMonth = c.get(Calendar.MONTH)+1;
        for (int i = 0; i < size; i++) {
            int k = allDayList.get(i);
            Button btn = new Button(String.valueOf(k));
            btn.setPrefWidth(34);
            if(year==realYear && month==realMonth && k==date)
                btn.setStyle(HOVERED_BUTTON_STYLE);
            if(i>=curDay && i<= subDate){
                btn.setStyle(NORMAL_BUTTON_STYLE);
                btn.setOnMouseEntered(e -> btn.setStyle(HOVERED_BUTTON_STYLE));
                btn.setOnMouseExited(e -> btn.setStyle(NORMAL_BUTTON_STYLE));
                btnList.add(btn);

                if (k == date){
                    btn.setStyle(HOVERED_BUTTON_STYLE);
                }
                btn.setOnAction(event->{
                    for(Button b:btnList){
                        b.setStyle(NORMAL_BUTTON_STYLE);
                    }
                    btn.setOnMouseEntered(null);
                    btn.setOnMouseExited(null);
                    btn.setStyle(HOVERED_BUTTON_STYLE);
                    date = k;
                    //设置当前选择日期的时间
//                    setCurrentDateTime();
                    System.out.println("点击了日期："+date);
                });
            }else{
                btn.setDisable(true);
                btn.setStyle("-fx-background-color: #F4F4F4;");
            }
//            flow.getChildren().add(btn);
            FPDate.getChildren().add(btn);
        }
//        datePick.getChildren().clear();
//        datePick.getChildren().add(flow);
    }



    //获取某月中天数
    private int getDaysOfMonth(Integer curMonth) {
        Date dateTime = new Date(curMonth==0?(year-1):year,curMonth==0?(12):curMonth,0);
        return dateTime.getDate();
    }

    //获取某一天是星期几
    private int getWeekNumber(int day){
        Date dateTime = new Date();
        dateTime.setYear(year);
        dateTime.setMonth(month-1);
        dateTime.setDate(day);
        c.setTime(dateTime);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-2;
        if(dayOfWeek<0)
            dayOfWeek = 6;
        return dayOfWeek;
    }
    //开始计时
    public void clockBegin(){
        timerTaskBegin = new TimerTask() {
            @Override
            public void run() {
                Integer hour = SpinHour.getValue();
                Integer minute = SpinMin.getValue();
                Integer second = SpinSec.getValue();
                second++;
                if (second == 60){
                    second = 0;
                    minute++;
                }
                if (minute == 60){
                    minute = 0;
                    hour++;
                }
                if (hour == 24){

                }

                SpinHour.getValueFactory().setValue(hour);
                SpinMin.getValueFactory().setValue(minute);
                SpinSec.getValueFactory().setValue(second);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Integer hour = SpinHour.getValue();
                        Integer minute = SpinMin.getValue();
                        Integer second = SpinSec.getValue();
                        LDateTime.setText(year + "    " + monthItems[month - 1] + "  "+ date + "      " + hour + " : " + minute + " : " + second);
                    }
                });

            }
        };
        timer.schedule(timerTaskBegin,0,1000);
    }

    void moveTimeZoneImg(int i){

        double x = IMGTimeZone.getX();
        IMGTimeZone.setX(i * 18.2);
        System.out.println(IMGTimeZone.getX());

    }

    public String showInfo(){
        String msg = "";
        msg = "Year = " + year + "\n" +
              "Month = " + month + "\n"+
                "Day = " + date + "\n" +
                "Hour = " + SpinHour.getValue() + "\n" +
                "Minute = " + SpinMin.getValue() + "\n" +
                "Second = " + SpinSec.getValue() + "\n" +
                "TimeZone = " + CBTimeZone.getValue() + "\n" +
                "Auto Daylight = " + isChange.isSelected();

        return msg;
    }
}
