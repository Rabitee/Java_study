import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOex3 {
    public static void main(String[] args) {
        String path = "C:/Users/donghwa/Desktop/local_git/Java_study/IO";

        try(FileOutputStream fos = new FileOutputStream(path + "/test2.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(new Integer(10));
            oos.writeObject(new String("ABC"));
        }
        catch(Exception e) {

        }

        try(FileInputStream fis = new FileInputStream(path + "/test2.txt");
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            Integer obj1 = (Integer)ois.readObject();
            String obj2 = (String)ois.readObject();

            System.out.println(obj1);
            System.out.println(obj2);
        }
        catch(Exception e) {

        }
    }
}