import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client1 {
    public static void main(String[] args) {
        try(Socket s = new Socket()) {
            System.out.println("Request...");
            s.connect(new InetSocketAddress("localhost", 5001));
            System.out.println("Success");

            byte[] bytes = null;
            String msg = null;

            try(OutputStream os = s.getOutputStream();
                InputStream is = s.getInputStream()) {
                msg = "Hello Server";
                bytes = msg.getBytes("UTF-8");
                os.write(bytes);
                os.flush();
                System.out.println("Send message");
        
                bytes = new byte[100];
                int readByteCnt = is.read(bytes);
                msg = new String(bytes, 0, readByteCnt, "UTF-8");
                System.out.println("Server: " + msg);
            }
            catch(IOException e) {

            }
        }
        catch(IOException e) {
            
        }
    }
}