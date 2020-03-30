import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatClient extends Application{
    Socket s;

    void startClient() {
        Thread t = new Thread() {
            public void run() {
                try {
                    s = new Socket();
                    s.connect(new InetSocketAddress("localhost", 5001));
                    Platform.runLater(() -> {
                        displayText("[success: " + s.getRemoteSocketAddress() + "]");
                        btnConn.setText("stop");
                        btnSend.setDisable(false);
                    });
                }
                catch(Exception e) {
                    Platform.runLater(() -> displayText("failed"));
                    if(!s.isClosed()) {
                        stopClient();
                    }
                    return;
                }
                receive();
            }
        };
        t.start();
    }

    void stopClient() {
        try {
            Platform.runLater(() -> {
                displayText("[disconnect]");
                btnConn.setText("start");
                btnSend.setDisable(true);
            });
            
            if(s != null && !s.isClosed()) {
                s.close();
            }
        }
        catch(Exception e) {

        }
    }

    void receive() {
        while(true) {
            try {
                byte[] byteArr = new byte[100];
                InputStream is = s.getInputStream();
                int readByteCnt = is.read(byteArr);
                if(readByteCnt == -1) {
                    throw new IOException();
                }
                String data = new String(byteArr, 0, readByteCnt, "UTF-8");

                Platform.runLater(() -> displayText("[receive]: " + data));
            }
            catch(Exception e) {
                Platform.runLater(() -> displayText("[failed]"));
                stopClient();
                break;
            }
        }
    }

    void send(String data) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    byte[] byteArr = data.getBytes("UTF-8");
                    OutputStream os = s.getOutputStream();
                    os.write(byteArr);
                    os.flush();
                    Platform.runLater(() -> displayText("[send]"));
                }
                catch(Exception e) {
                    Platform.runLater(() -> displayText("[failed]"));
                    stopClient();
                }
            }
        };
        thread.start();
    }

    TextArea txtDisplay;
    TextField txtInput;
    Button btnConn, btnSend;

    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPrefSize(500, 300);

        txtDisplay = new TextArea();
        txtDisplay.setEditable(false);
        BorderPane.setMargin(txtDisplay, new Insets(0, 0, 2, 0));
        root.setCenter(txtDisplay);

        BorderPane bottom = new BorderPane();
        txtInput = new TextField();
        txtInput.setPrefSize(60, 30);
        BorderPane.setMargin(txtInput, new Insets(0, 1, 1, 1));

        btnConn = new Button("start");
        btnConn.setPrefSize(60, 30);
        btnConn.setOnAction(e -> {
            if(btnConn.getText().equals("start")) {
                startClient();
            }
            else if(btnConn.getText().equals("stop")) {
                stopClient();
            }
        });

        btnSend = new Button("send");
        btnSend.setPrefSize(60, 30);
        btnSend.setDisable(true);
        btnSend.setOnAction(e->send(txtInput.getText()));

        bottom.setCenter(txtInput);
        bottom.setLeft(btnConn);
        bottom.setRight(btnSend);
        root.setBottom(bottom);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("app.css").toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.setOnCloseRequest(event -> stopClient());
        primaryStage.show();
    }

    void displayText(String text) {
        txtDisplay.appendText(text + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}