import java.net.InetSocketAddress;
import java.net.Socket;

public class ChatClient {

    void startClient() {
        Thread t = new Thread() {
            public void run() {
                try(Socket s = new Socket()) {
                    s.connect(new InetSocketAddress("localhost", 5001));
                }
                catch(Exception e) {

                }
                receive();
            }
        };
        t.start();
    }

    void stopClient() {
        
    }

    void receive() {
        while(true) {
            
        }
    }

    void send(String data) {
        
    }
}