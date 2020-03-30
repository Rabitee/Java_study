import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatServer extends Application{
    ExecutorService es;
    ServerSocket ss;
    List<Client> connections = new Vector<Client>();

    void startServer() {
        es = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        );

        try {
            ss = new ServerSocket();
            ss.bind(new InetSocketAddress("localhost", 5001));
        }
        catch(Exception e) {
            if(!ss.isClosed()) {
                stopServer();
            }
            return;
        }
        Runnable runnable = new Runnable() {
            public void run() {
                Platform.runLater(() -> {
                    displayText("[server start]");
                    btnStartStop.setText("stop");
                });
                while(true) {
                    try {
                        Socket s = ss.accept();
                        String msg = "Connect: " + s.getRemoteSocketAddress() +
                        ": " + Thread.currentThread().getName();
                        Platform.runLater(() -> displayText(msg));

                        Client client = new Client(s);
                        connections.add(client);
                        Platform.runLater(() -> {
                            displayText("[#connections: " + connections.size() + "]");
                        });
                    }
                    catch(Exception e) {
                        if(!ss.isClosed()) {
                            stopServer();
                        }
                        break;
                    }
                }
            }
        };
        es.submit(runnable);
    }

    void stopServer() {
        try {
            Iterator<Client> itr = connections.iterator();
            while(itr.hasNext()) {
                Client client = itr.next();
                client.s.close();
                itr.remove();
            }
            if(ss != null && !ss.isClosed()) {
                ss.close();
            }
            if(es != null && !es.isShutdown()) {
                es.shutdown();
            }
            Platform.runLater(() -> {
                displayText("[server stop]");
                btnStartStop.setText("start");
            });
        }
        catch(Exception e) {

        }
    }

    class Client {
        Socket s;

        Client(Socket s) {
            this.s = s;
            receive();
        }

        void receive() {
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        while(true) {
                            byte[] byteArr = new byte[100];
                            InputStream is = s.getInputStream();
                            int readByteCnt = is.read(byteArr);

                            if(readByteCnt == -1) {
                                throw new IOException();
                            }

                            String msg = "Request: " + s.getRemoteSocketAddress() +
                            ": " + Thread.currentThread().getName();
                            Platform.runLater(() -> displayText(msg));
                            String data = new String(byteArr, 0, readByteCnt, "UTF-8");
                            
                            for(Client client : connections) {
                                client.send(data);
                            }
                        }
                    }
                    catch(Exception e) {
                        try {
                            connections.remove(Client.this);
                            String msg = "error";
                            Platform.runLater(() -> displayText(msg));
                            s.close();
                        }
                        catch(Exception e2) {

                        }
                    }
                }
            };
            es.submit(runnable);
        }

        void send(String data) {
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        OutputStream os = s.getOutputStream();
                        byte[] byteArr = data.getBytes("UTF-8");
                        os.write(byteArr);
                        os.flush();
                    }
                    catch(Exception e) {

                    }
                }
            };
            es.submit(runnable);
        }
    }
    TextArea txtDisplay;
    Button btnStartStop;

    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPrefSize(500, 300);

        txtDisplay = new TextArea();
        txtDisplay.setEditable(false);
        BorderPane.setMargin(txtDisplay, new Insets(0, 0, 2, 0));
        root.setCenter(txtDisplay);

        btnStartStop = new Button("start");
        btnStartStop.setPrefHeight(30);
        btnStartStop.setMaxWidth(Double.MAX_VALUE);

        btnStartStop.setOnAction(e -> {
            if(btnStartStop.getText().equals("start")) {
                startServer();
            }
            else if(btnStartStop.getText().equals("stop")) {
                stopServer();
            }
        });
        root.setBottom(btnStartStop);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("app.css").toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Server");
        primaryStage.setOnCloseRequest(event -> stopServer());
        primaryStage.show();
    }

    void displayText(String text) {
        txtDisplay.appendText(text + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}