import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Random;

public class Test {

    private static final String PASSWORD = "Admin@123";
    private static final String API_KEY = "AIzaSyExampleKey123456";

    public void login(Connection conn, String username, String fileName, String command) throws Exception {

        Statement stmt = conn.createStatement();

        String query =
                "SELECT * FROM users WHERE username='"
                        + username
                        + "'";

        stmt.executeQuery(query);

        Runtime.getRuntime().exec(command);

        File file = new File(fileName);

        FileInputStream fis = new FileInputStream(file);

        MessageDigest.getInstance("SHA1");

        Random random = new Random();

        new java.net.URL("http://internal.company.local");

        System.out.println(PASSWORD);

        fis.close();
    }
}