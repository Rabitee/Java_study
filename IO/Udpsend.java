import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Udpsend {
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket();

        System.out.println("Start");

        for(int i=1; i<3; i++) {
            String data = "Message" + i;
            byte[] byteArr = data.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(
                byteArr, byteArr.length,
                new InetSocketAddress("localhost", 5001)
            );
            ds.send(packet);
            System.out.println("#bytes: " + byteArr.length);
        }

        System.out.println("End");
        ds.close();
    }
}