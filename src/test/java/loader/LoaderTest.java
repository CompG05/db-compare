package loader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import structure.Schema;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoaderTest {
    static String url;
    static String catalog;
    static String username;
    static String password;
    static Loader loader;

    @BeforeAll
    public static void setUp() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String configFilename = "src/test/java/loader/config.json";
            JsonNode jsonNode = objectMapper.readTree(new FileReader(configFilename));

            url = jsonNode.get("url").asText();
            catalog = jsonNode.get("catalog").asText();
            username = jsonNode.get("username").asText();
            password = jsonNode.get("password").asText();

            String fullUrl = "jdbc:mysql://" + url + "/";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(fullUrl, username, password);

            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(new FileReader(System.getProperty("user.dir") + "/src/test/java/loader/test-script.sql"));

            loader = new MyLoader(url, catalog, username, password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTest() {
        assertTrue(true);
    }

    @Test
    public void test01() {
        Schema s = loader.loadSchema();
        System.out.println(s);
    }
}