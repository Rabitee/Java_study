import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 {
    public static void main(String[] args) {
        while(true) {
            try(ServerSocket ss = new ServerSocket()) {
                ss.bind(new InetSocketAddress("localhost", 5001));
                System.out.println("Loading...");
                Socket s = ss.accept();
                InetSocketAddress isa = (InetSocketAddress)s.getRemoteSocketAddress();
                System.out.println("Success: " + isa.getHostName());

                byte[] bytes = null;
                String msg = null;

                try(InputStream is = s.getInputStream();
                    OutputStream os = s.getOutputStream()) {
                    bytes = new byte[100];
                    int readByteCnt = is.read(bytes);
                    msg = new String(bytes, 0, readByteCnt, "UTF-8");
                    System.out.println("Client: " + msg);

                    msg = "Hello Client";
                    bytes = msg.getBytes("UTF-8");
                    os.write(bytes);
                    os.flush();
                    System.out.println("Send message");
                }
                catch(IOException e) {

                }

            }
            catch(IOException e) {

            }
        }
    }
}