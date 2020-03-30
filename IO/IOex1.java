import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IOex1 {
    public static void main(String[] args) {
        String path = "C:/Users/donghwa/Desktop/local_git/Java_study/IO";

        try(InputStream is = new FileInputStream(path + "/test.jpg");
            OutputStream os = new FileOutputStream(path + "/copy.jpg")) {
            
            int data = 0;
            while((data = is.read()) != -1) {
                os.write(data);
            }

            os.flush();
        }
        catch(Exception e) {

        }
    }
}