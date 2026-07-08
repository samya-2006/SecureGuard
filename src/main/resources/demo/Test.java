import java.sql.*;

public class Test {

    public static void main(String[] args) throws Exception {

        String id = "10";

        String sql =
                "SELECT * FROM users WHERE id=" + id;

        Statement stmt = null;

        stmt.executeQuery(sql);

    }

}