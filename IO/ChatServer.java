import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    ExecutorService es;
    List<Client> connections = new Vector<Client>();

    void startServer() {
        es = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        );

        try(ServerSocket ss = new ServerSocket()) {
            ss.bind(new InetSocketAddress("localhost", 5001));
            Runnable runnable = new Runnable() {
                public void run() {
                    while(true) {
                        try(Socket s = ss.accept()) {
                            String msg = "Connect: " + s.getRemoteSocketAddress() +
                            ": " + Thread.currentThread().getName();

                            Client client = new Client(s);
                            connections.add(client);
                        }
                    }
                }
            };
            es.submit(runnable);
        }
        catch(Exception e) {

        }
    }

    void stopServer() {

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
                    try(InputStream is = s.getInputStream()) {
                        while(true) {
                            byte[] byteArr = new byte[100];
                            int readByteCnt = is.read(byteArr);

                            if(readByteCnt == -1) {
                                throw new IOException();
                            }

                            String msg = "Request: " + s.getRemoteSocketAddress() +
                            ": " + Thread.currentThread().getName();
                            String data = new String(byteArr, 0, readByteCnt, "UTF-8");
                            
                            for(Client client : connections) {
                                client.send(data);
                            }
                        }
                    }
                    catch(Exception e) {
                        
                    }
                }
            };
            es.submit(runnable);
        }

        void send(String data) {
            Runnable runnable = new Runnable() {
                public void run() {
                    try(OutputStream os = s.getOutputStream()) {
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
}