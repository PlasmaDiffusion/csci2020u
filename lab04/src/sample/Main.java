package sample;

import com.sun.javafx.binding.StringFormatter;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import  javafx.scene.control.Label;
import  javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import javax.swing.text.MaskFormatter;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.text.DecimalFormat;
import java.text.ParseException;


public class Main extends Application {

    private TextField name;
    private PasswordField password;
    private TextField fullName;
    private TextField email;
    private TextField number;
    private DatePicker date;
    private Button register_btn;

    private MaskFormatter numberMask;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Lab 04");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));


        gridPane.setHgap(5);
        gridPane.setVgap(5);

        numberMask = new MaskFormatter("###-###-####");

        //Name label
        Label nameText = new Label("Name: ");

        name = new TextField();
        name.setPromptText("Name...");

        gridPane.add(nameText, 0, 0);
        gridPane.add(name, 1, 0);

        //Password label
        Label passwordText = new Label("Password: ");

        password = new PasswordField();
        password.setPromptText("Password...");

        gridPane.add(passwordText, 0, 1);
        gridPane.add(password, 1, 1);

        //FullName label
        Label fullNameText = new Label("Full Name: ");

        fullName = new TextField();
        fullName.setPromptText("Full Name...");

        gridPane.add(fullNameText, 0, 2);
        gridPane.add(fullName, 1, 2);


        //Email label
        Label emailText = new Label("Email: ");

        email = new TextField();
        email.setPromptText("some@something.com");

        gridPane.add(emailText, 0, 3);
        gridPane.add(email, 1, 3);



        //Phone number
        Label numberText = new Label("Phone #: ");




        number = new TextField();
        number.setPromptText("##########");


        // force the field to be numeric only
        number.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    number.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });



        gridPane.add(numberText, 0, 4);
        gridPane.add(number, 1, 4);


        //Date label
        Label dateText = new Label("Date of Birth: ");

        date = new DatePicker();
        date.setPromptText("Date...");

        gridPane.add(dateText, 0, 5);
        gridPane.add(date, 1, 5);


        //Register button
        register_btn = new Button();

        register_btn.setText("Register");

        gridPane.add(register_btn,0,6);

        register_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                String num = number.getText();

                //Properly format phone number
                if (numberMask != null) {
                    numberMask.setValueContainsLiteralCharacters(false);
                }

                try {
                    number.setText(numberMask.valueToString(num));
                } catch (ParseException e) {
                    //System.out.println("Invalid phone #.");
                    //e.printStackTrace();
                }


                //Output stuff
                System.out.println("Name: " + name.getText());
                System.out.println("Password: " + password.getText());
                System.out.println("Full Name: " + fullName.getText());
                System.out.println("Phone #: " + num);
                System.out.println("Date of Birth: " + date.getValue());


                name.clear();
                password.clear();
                fullName.clear();
                email.clear();
                number.clear();
                date.setValue(null);
            }
        });

        //Set scene
        primaryStage.setScene(new Scene(gridPane, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
