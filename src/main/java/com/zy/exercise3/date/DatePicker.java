package com.zy.exercise3.date;



        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.geometry.Pos;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.ComboBox;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextField;
        import javafx.scene.layout.*;
        import javafx.scene.paint.Color;
        import javafx.stage.Modality;
        import javafx.stage.Stage;
        import javafx.stage.StageStyle;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
/**
 * 通用日历组件
 * @author 徐志林
 * @createTime 2020-04-21 23:18
 */
public class DatePicker {
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH)+1;
    int date = c.get(Calendar.DATE);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    int second = c.get(Calendar.SECOND);
    Stage stage;
    HBox timeCombobox;
    ComboBox hourCombobox;
    ComboBox minuteCombobox;
    ComboBox secondCombobox;
    Boolean isFirstComing;
    //日期选择
    HBox datePick;

    Label yearLabel;
    Label monthLabel;
    public TextField timeField;
    public Boolean withSecond;
    //日期控件是否需要强行转换为  如果circleType为false则：控件一定为矩形 ，为true时还要进行withSecond判断
    public Boolean circleType;
    double x,y;

    /**
     * @param withSecond 是否带时分秒  true有 false无
     * @param isCircleType 是否为圆形框，亦或方形框
     */
    public DatePicker(Boolean withSecond, Boolean isCircleType){
        this.withSecond = withSecond;
        this.circleType = isCircleType;
        isFirstComing = true;
        timeField = new TextField();
        datePick = new HBox();
        yearLabel = new Label(timeFilter(year));
        monthLabel = new Label(timeFilter(month));
    }

    /**
     * 初始化当前日期时间
     */
    public void setCalendarTime(){
        Calendar calendar = Calendar.getInstance();//可以对每个时间域单独修改
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        date = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdfSecond = new SimpleDateFormat("yyyy-MM-dd");
    public void showDatePicker() {
        String timeValStr = timeField.getText();
        String timeVal = timeValStr==null?"":timeValStr.trim();
        //初始化当前日期时间
        if(timeField!=null && timeVal.length()>0){
            try {
                Date dateTime;
                if(withSecond){
                    dateTime = sdf.parse(timeVal);
                }else {
                    dateTime = sdfSecond.parse(timeVal);
                }
                c.setTime(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            setCalendarTime();
        }
        stage = new Stage();
        //以下两个同时设置可
        stage.initModality(Modality.APPLICATION_MODAL);//设置弹框为APPLICATION_MODAL模式
        stage.initStyle(StageStyle.TRANSPARENT);//隐藏默认标题栏
        try {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-border-radius: 4;");
            //主舞台
            VBox layout = new VBox();
            layout.setAlignment(Pos.CENTER);
            layout.setId("layout");
            layout.getChildren().add(root);
            VBox layoutMain = new VBox(10);
            layoutMain.setStyle("-fx-background-color:transparent;");
            layoutMain.getChildren().addAll(layout);
            layoutMain.setAlignment(Pos.CENTER);
            layout.maxHeightProperty().bind(layoutMain.heightProperty().subtract(15));
            layout.minHeightProperty().bind(layoutMain.heightProperty().subtract(15));
            layout.minWidthProperty().bind(layoutMain.widthProperty().subtract(15));
            layout.maxWidthProperty().bind(layoutMain.widthProperty().subtract(15));
            root.minHeightProperty().bind(layout.heightProperty());
            root.maxWidthProperty().bind(layout.widthProperty());
            root.minHeightProperty().bind(layout.heightProperty());
            root.maxWidthProperty().bind(layout.widthProperty());
            Scene scene = new Scene(layoutMain, 365, 505-(withSecond?0:110));
            scene.getStylesheets().add("/css/datePicker.css");

            //日期（年月日）主体
            VBox upMain = new VBox();
            upMain.setStyle("-fx-min-height: 330;-fx-max-height: 330;-fx-min-width:350;-fx-max-width:350;-fx-alignment: center;");
            setUpMain(upMain);
            root.setTop(upMain);

            if(withSecond){
                //时间（时分秒）主体
                VBox downMain = new VBox();
                setDownMain(downMain);
                root.setCenter(downMain);
            }

            //确认按钮
            VBox confirmBox = new VBox();
            setBtnBox(confirmBox);
            root.setBottom(confirmBox);


            //设置默认时间
            setCurrentDateTime();

            scene.setFill(Color.rgb(255,255,255,0.01));
            stage.setScene(scene);
            stage.setX(x+(withSecond?15:25));
            stage.setY(y-(withSecond?10:15));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpMain(VBox upMain){
        //时间box
        HBox timeLabelBox = new HBox();
        timeLabelBox.setStyle("-fx-min-height: 40;-fx-max-height: 40;-fx-min-width:350;-fx-alignment: CENTER_LEFT;-fx-padding: 10; ");
        timeLabelBox.getChildren().add(new Label("日期"));
        //日期头部拖动
//        DragUtil.addDragListener(stage, timeLabelBox);

        HBox main = new HBox();
        main.setStyle("-fx-min-height: 290;-fx-max-height: 290;-fx-min-width:350;-fx-max-width:350;-fx-alignment: CENTER;");
        VBox innerMainBox = new VBox();
        innerMainBox.setStyle("-fx-min-height: 290;-fx-max-height: 290;-fx-min-width:335;-fx-max-width:335;-fx-alignment: CENTER;");
        //年月选择
        HBox yearMonthBox = new HBox();
        yearMonthBox.setId("yearMonthBox");
        yearMonthBox.setStyle("-fx-min-height: 35;-fx-max-height: 35;-fx-min-width:310;-fx-max-width:310;-fx-alignment: CENTER; ");
        setYearMonthContent(yearMonthBox);
        //星期
        HBox weekBox = new HBox();
        weekBox.setStyle("-fx-min-height: 35;-fx-max-height: 35;-fx-min-width:335;-fx-max-width:335;-fx-alignment: CENTER;");
        setWeekContent(weekBox);
        //留白5px
        HBox whiteBox = new HBox();
        whiteBox.setStyle("-fx-min-height: 5;-fx-max-height: 5;");
        //日期选择
        datePick.setId("datePick");
        datePick.setStyle("-fx-min-height: 225;-fx-max-height: 225;-fx-min-width:310;-fx-max-width:310;");

        setDatePickContent();

        innerMainBox.getChildren().addAll(yearMonthBox,weekBox,whiteBox,datePick);
        main.getChildren().add(innerMainBox);
        upMain.getChildren().addAll(timeLabelBox,main);
    }

    public void setDownMain(VBox downMain){
        downMain.setStyle("-fx-min-height: 150;-fx-max-height: 150;-fx-min-width:350;-fx-max-width:350;-fx-alignment: center;");

        HBox emptyBox1 = new HBox();
        emptyBox1.setStyle("-fx-min-height: 5;-fx-max-height: 5;-fx-min-width:330;-fx-max-width:330;-fx-border-style:  hidden hidden solid hidden;-fx-border-color:#EBEBEB; ");
        //时间box
        HBox timeLabelBox = new HBox();
        timeLabelBox.setStyle("-fx-min-height: 40;-fx-max-height: 40;-fx-min-width:350;-fx-alignment: CENTER_LEFT;-fx-padding: 10; ");
        timeLabelBox.getChildren().add(new Label("时间"));
        //时间选择combobox
        timeCombobox = new HBox();
        timeCombobox.setStyle("-fx-min-height: 30;-fx-max-height: 30;-fx-min-width:350;-fx-alignment: center;");
        setComboBoxs();
        HBox emptyBox2 = new HBox();
        emptyBox2.setStyle("-fx-min-height: 35;-fx-max-height: 35;-fx-min-width:350;");
        downMain.getChildren().addAll(emptyBox1,timeLabelBox,timeCombobox,emptyBox2);
    }

    public void setBtnBox(VBox box){
        //取消和确认按钮box
        HBox btnBox = new HBox();
        btnBox.setStyle("-fx-min-height: 40;-fx-max-height: 40;-fx-min-width:350;-fx-border-style: solid hidden hidden hidden;-fx-border-color:#EBEBEB;-fx-alignment: center;");
        setBtns(btnBox);
        box.getChildren().add(btnBox);
    }

    final String HOVERED_BUTTON_STYLE = "-fx-text-fill: white;-fx-background-color: #5b8cff;";
    final String NORMAL_BUTTON_STYLE = "-fx-text-fill: black;-fx-background-color: transparent;";
    private void setDatePickContent(){
        FlowPane flow = new FlowPane();
        flow.setVgap(2);
        flow.setHgap(10);
        flow.setPrefWrapLength(310);
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

        for(int i=(curDay-1);i>-1;i--){
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

        int size = allDayList.size();
        int subDate = (size-lastDay-1);
        c.setTime(new Date());
        int realYear = c.get(Calendar.YEAR);
        int realMonth = c.get(Calendar.MONTH)+1;
        for (int i = 0; i < size; i++) {
            int k = allDayList.get(i);
            Button btn = new Button(String.valueOf(k));
            if(year==realYear && month==realMonth && k==date)
                btn.setStyle(HOVERED_BUTTON_STYLE);
            if(i>=curDay && i<= subDate){
                btn.setOnMouseEntered(e -> btn.setStyle(HOVERED_BUTTON_STYLE));
                btn.setOnMouseExited(e -> btn.setStyle(NORMAL_BUTTON_STYLE));
                btnList.add(btn);
                btn.setOnAction(oa->{
                    for(Button b:btnList){
                        b.setStyle(NORMAL_BUTTON_STYLE);
                    }
                    btn.setOnMouseEntered(null);
                    btn.setOnMouseExited(null);
                    btn.setStyle(HOVERED_BUTTON_STYLE);
                    date = k;
                    //设置当前选择日期的时间
                    setCurrentDateTime();
                    System.out.println("点击了日期："+date);
                });
            }else{
                btn.setDisable(true);
                btn.setStyle("-fx-background-color: #F4F4F4;");
            }
            flow.getChildren().add(btn);
        }
        datePick.getChildren().clear();
        datePick.getChildren().add(flow);
    }

    private void setYearMonthContent(HBox yearMonthBox){
        HBox yearLeft = new HBox();
        yearLeft.setStyle("-fx-min-height: 35;-fx-max-height: 35;-fx-min-width:30;-fx-max-width:30;-fx-alignment: CENTER_RIGHT;");
        Button bt1 = new Button();
        bt1.setStyle("-fx-background-image: url('/image/yearLeftArrow.png');-fx-background-size:50%;-fx-background-position:center;-fx-background-repeat:no-repeat;-fx-background-color: transparent;");
        yearLeft.getChildren().add(bt1);

        HBox yearRight = new HBox();
        yearRight.setStyle("-fx-min-height: 35;-fx-max-height: 35;-fx-min-width:30;-fx-max-width:30;-fx-alignment: CENTER_LEFT;");
        Button bt2 = new Button();
        bt2.setStyle("-fx-background-image: url('/image/yearRightArrow.png');-fx-background-size:50%;-fx-background-position:center;-fx-background-repeat:no-repeat;-fx-background-color: transparent;");
        yearRight.getChildren().add(bt2);

        HBox monthLeft = new HBox();
        monthLeft.setStyle("-fx-min-height: 35;-fx-max-height: 35;-fx-min-width:65;-fx-max-width:65;-fx-alignment: CENTER_RIGHT; ");
        Button bt3 = new Button();
        bt3.setStyle("-fx-background-image: url('/image/monthLeftArrow.png');-fx-background-size:80%;-fx-background-position:center;-fx-background-repeat:no-repeat;-fx-background-color: transparent;");
        monthLeft.getChildren().add(bt3);

        HBox monthRight = new HBox();
        monthRight.setStyle("-fx-min-height: 35;-fx-max-height: 35;-fx-min-width:65;-fx-max-width:65;-fx-alignment: CENTER_LEFT;");
        Button bt4 = new Button();
        bt4.setStyle("-fx-background-image: url('/image/monthRightArrow.png');-fx-background-size:80%;-fx-background-position:center;-fx-background-repeat:no-repeat;-fx-background-color: transparent;");
        monthRight.getChildren().add(bt4);

        HBox yearAndMonth = new HBox();
        yearAndMonth.setStyle("-fx-min-height: 35;-fx-max-height: 35;-fx-min-width:120;-fx-max-width:120;-fx-alignment:center; ");
        Label yearTitle = new Label("年");
        Label monthTitle = new Label("月");

        bt1.setOnAction(oa->{
            int val = Integer.valueOf(monthLabel.getText())-1;
            month = getRightMonth(val);
            //月减一
            monthLabel.setText(timeFilter(month));
            setDatePickContent();
        });
        bt2.setOnAction(oa->{
            //月加一
            int val = Integer.valueOf(monthLabel.getText())+1;
            month = getRightMonth(val);
            monthLabel.setText(timeFilter(month));
            setDatePickContent();
        });
        bt3.setOnAction(oa->{
            //年减一
            int val = Integer.valueOf(yearLabel.getText())-1;
            year = val;
            yearLabel.setText(String.valueOf(val));
            setDatePickContent();
        });
        bt4.setOnAction(oa->{
            //年加一
            int val = Integer.valueOf(yearLabel.getText())+1;
            year = val;
            yearLabel.setText(String.valueOf(val));
            setDatePickContent();
        });

        yearAndMonth.getChildren().addAll(yearLabel,yearTitle,monthLabel,monthTitle);

        yearMonthBox.getChildren().addAll(monthLeft,yearLeft,yearAndMonth,yearRight,monthRight);
    }

    private int getRightMonth(int val){
        if(val>12)
            val = 1;
        if(val<1)
            val = 12;
        month = val;
        return val;
    }

    private void setWeekContent(HBox weekBox){
        weekBox.setId("weekBox");
        FlowPane flowPane = new FlowPane();
        flowPane.setStyle("-fx-alignment: center;");
        flowPane.setVgap(20);
        flowPane.setHgap(31);
        flowPane.setPrefWrapLength(335);
        flowPane.getChildren().add(new Label("日"));
        flowPane.getChildren().add(new Label("一"));
        flowPane.getChildren().add(new Label("二"));
        flowPane.getChildren().add(new Label("三"));
        flowPane.getChildren().add(new Label("四"));
        flowPane.getChildren().add(new Label("五"));
        flowPane.getChildren().add(new Label("六"));

        weekBox.getChildren().add(flowPane);
    }

    private void setBtns(HBox btnBox){
        Button confirmBtn = new Button("确认");
        Button cancelBtn = new Button("取消");
        confirmBtn.setId("confirmBtn");
        cancelBtn.setId("cancelBtn");
        confirmBtn.setOnAction(oa->{
            System.out.println("你选择的日期是："+currentDateTime);
            timeField.setText(currentDateTime);
            stage.close();
        });
        cancelBtn.setOnAction(oa->{
            timeField.clear();
            stage.close();
        });
        btnBox.getChildren().addAll(cancelBtn,confirmBtn);
    }

    private String currentDateTime;
    private void setCurrentDateTime(){
        if(isFirstComing){
            isFirstComing = false;
            currentDateTime = "";
            currentDateTime = year +"-"+ timeFilter(month)+"-"+timeFilter(date);
            if(withSecond){
                currentDateTime = currentDateTime+" "+timeFilter(hour)+":"+timeFilter(minute)+":"+timeFilter(second);
            }
            return;
        }
        currentDateTime = year +"-"+ timeFilter(month)+"-"+timeFilter(date);
        if(withSecond && hourCombobox!=null && hourCombobox.getValue()!=null){
            currentDateTime = currentDateTime+" "+hourCombobox.getValue().toString()+":"+minuteCombobox.getValue().toString()+":"+secondCombobox.getValue().toString();
        }else{
            System.out.println("你选择的日期是："+currentDateTime);
            timeField.setText(currentDateTime);
        }
    }

    private String timeFilter(int val){
        String str;
        if(val<10){
            str = "0"+val;
        }else{
            str = ""+val;
        }
        return str;
    }

    private void setComboBoxs(){
        timeCombobox.getChildren().addAll(getCombo("时"),getCombo("分"),getCombo("秒"));
    }

    private HBox getCombo(String text){
        HBox main = new HBox();
        main.setStyle("-fx-alignment: center;");
        main.prefWidthProperty().bind(timeCombobox.widthProperty().subtract(20).divide(3));
        main.prefHeightProperty().bind(timeCombobox.heightProperty());
        ComboBox comboBox = new ComboBox();
        comboBox.prefWidthProperty().bind(main.widthProperty().multiply(2).divide(3));
        comboBox.prefHeightProperty().bind(main.heightProperty());
        Label label = new Label("  "+text);
        label.prefWidthProperty().bind(main.widthProperty().multiply(1).divide(3));
        label.prefHeightProperty().bind(main.heightProperty());
        comboBox.getSelectionModel().selectedIndexProperty().addListener((ov,oldv,newv)->{
            if(text.equals("时") && newv.intValue()!=hour){
                setCurrentDateTime();
            }else if(text.equals("分") && newv.intValue()!=minute){
                setCurrentDateTime();
            }else if(text.equals("秒") && newv.intValue()!=second){
                setCurrentDateTime();
            }
        });
        main.getChildren().addAll(comboBox,label);

        ObservableList<String> items = FXCollections.observableArrayList();
        if(text.equals("时")){
            setRange(24,items);
            comboBox.setValue(timeFilter(hour));
            hourCombobox = comboBox;
        }else if(text.equals("分")){
            setRange(60,items);
            comboBox.setValue(timeFilter(minute));
            minuteCombobox = comboBox;
        }else if(text.equals("秒")){
            setRange(60,items);
            comboBox.setValue(timeFilter(second));
            secondCombobox = comboBox;
        }
        comboBox.setItems(items);
        return main;
    }

    private void setRange(int range, ObservableList<String> items){
        int k =0;
        while (k<range){
            if(k<10){
                items.add("0"+k);
            }else{
                items.add(String.valueOf(k));
            }
            k++;
        }
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
    //错误提示区
    public HBox errBox;
    public Label errLabel;
    public HBox getDateTimeTool(){
        timeField.setEditable(false);
        if(withSecond){
            timeField.setMinWidth(200);
            timeField.setMaxWidth(200);
        }else {
            timeField.setMinWidth(150);
            timeField.setMaxWidth(150);
        }
        //错误提示区
        errBox = new HBox();
        errBox.setStyle("-fx-background-color: #59A1FC;-fx-max-height: 25;-fx-opacity: 0;");
        errLabel = new Label("这是错误信息");
        errLabel.setStyle("-fx-text-fill: white;");
        HBox arrow = new HBox();
        arrow.translateYProperty().bind(errBox.heightProperty().multiply(-0.25));
        arrow.setStyle("-fx-translate-x:8;-fx-background-color: #59A1FC;-fx-max-width:30;-fx-max-height:10;-fx-shape: 'M 0 10 L 10 0 L 20 10 z';");
        errBox.translateYProperty().bind(errBox.heightProperty());

        if(circleType){
            timeField.setStyle("-fx-border-radius: 20;-fx-background-radius: 20;");
        }

        HBox hBox = new HBox();
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_RIGHT);
        Button btn = new Button();
        String style = ("-fx-translate-x:-10;")+
                "-fx-background-color:transparent;-fx-pref-width:22;-fx-pref-height:18;-fx-cursor:hand;" +
                "-fx-background-image: url('/image/date-bg.png');-fx-background-size:70%;-fx-background-repeat:no-repeat;-fx-background-position:center;";
        btn.setStyle(style);//背景偏移 x=1 y=3
        btn.setOnMouseClicked(oa->{
            x = oa.getScreenX()-oa.getX();
            y = oa.getScreenY()-oa.getY();

            //先取消显示错误信息
            showDateErrorInfo("");
            showDatePicker();
        });

        Button btnCancel = new Button();
        String cancelStyle = ("-fx-translate-x:-30;-fx-opacity:1;")+
                "-fx-background-color:transparent;-fx-pref-width:22;-fx-cursor:hand;" +
                "-fx-background-image: url('/image/forbidden.png');-fx-background-repeat:no-repeat;-fx-background-position:center;";
        btnCancel.setStyle(cancelStyle);
        btnCancel.setOnMouseClicked(oa->{
            setCalendarTime();
            timeField.clear();
        });

        StackPane errGraphic = new StackPane();
        errGraphic.setAlignment(Pos.TOP_LEFT);
        errGraphic.setStyle("-fx-padding: 3;");
        errGraphic.getChildren().add(arrow);
        errGraphic.getChildren().add(errLabel);
        errBox.getChildren().add(errGraphic);

        //第一层为：textField
        stackPane.getChildren().add(timeField);
        //第二层为：日期弹框按钮
        stackPane.getChildren().add(btn);
        //第三层为：清除按钮
        stackPane.getChildren().add(btnCancel);
        if(!withSecond){
            //第四层为：错误提示
            stackPane.getChildren().add(errBox);
        }
        hBox.getChildren().add(stackPane);
        return hBox;
    }

    //formatType 1 表示按年月日+时分秒格式化 2表示按年月日格式化
    public String isTimeCorrect(String startTimeStr, String endTimeStr, int formatType, Boolean isStart){
        Date start = null;
        Date end = null;
        Date now = new Date();
        String message = "";
        SimpleDateFormat formater = formatType==1?sdf:sdfSecond;
        try {
            if(startTimeStr.length()==0 && endTimeStr.length()==0){
                //都为空则不用判断
                return message;
            }
            now = formater.parse(formater.format(now));
            if(startTimeStr.length()>0){
                start = formater.parse(startTimeStr);
                if(start.getTime()>now.getTime() && isStart)
                    return "起始时间必须小于当前时间";
            }
            if(endTimeStr.length()>0){
                end = formater.parse(endTimeStr);
                if(end.getTime()>now.getTime() && !isStart)
                    return "结束时间必须小于当前时间";
            }

            if(start!=null && end!=null){
                if(start.getTime()>end.getTime())
                    return isStart?"起始时间必须小于结束时间":"结束时间必须大于起始时间";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void showDateErrorInfo(String message){
        if("".equals(message)){
            this.errBox.setOpacity(0);
        }else{
            this.errBox.setOpacity(1);
            this.errLabel.setText(message);
        }
    }

}
