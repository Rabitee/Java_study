import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

public class IOex2 {
    public static void main(String[] args) {
        String path = "C:/Users/donghwa/Desktop/local_git/Java_study/IO";

        try(Reader r = new FileReader(path + "/test.txt");
            Writer w = new FileWriter(path + "/copy.txt")) {
            
            char[] buf = new char[100];
            while(r.read(buf, 0, 100) != -1) {
                w.write(buf, 2, 4);
            }

            w.flush();
        }
        catch(Exception e) {
            
        }
    }
}