import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Udpreceive extends Thread {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Start");
                try(DatagramSocket ds = new DatagramSocket(5001)) {
                    while(true) {
                        DatagramPacket p = new DatagramPacket(new byte[100], 100);
                        ds.receive(p);

                        String data = new String(p.getData(), 0, p.getLength(), "UTF-8");
                        System.out.println("[receive: " + p.getSocketAddress() + "] " + data);
                    }
                }
                catch(Exception e) {

                }
            }
        };
        thread.start();

        Thread.sleep(10000);
    }
}