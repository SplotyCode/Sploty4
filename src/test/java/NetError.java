import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetError {

    public static void main(String[] args){
        try {
            URL url = new URL("http://132.321.32.23");
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            is.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
