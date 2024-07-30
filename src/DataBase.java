import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.sqlite.SQLiteDataSource;

public class DataBase {
    private static Connection conn;
    private static SQLiteDataSource ds;

    public static void dbInit() {
        ds = new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite:QuizDB.db");
        try {
            conn = ds.getConnection();
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "userID INTEGER PRIMARY KEY,"
                    + "username TEXT NOT NULL,"
                    + "email TEXT NOT NULL,"
                    + "password TEXT NOT NULL);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS question ("
                    + "QuestionID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Question TEXT,"
                    + "Option1 TEXT,"
                    + "Option2 TEXT,"
                    + "Option3 TEXT,"
                    + "Option4 TEXT,"
                    + "Answer TEXT);");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void addUser(int userID, String username, String email, String password) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users(userID, username, email, password) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, userID);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.executeUpdate();
        }
    }

    public static boolean validatePassword(String id, String password) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT password FROM users WHERE userID = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
        }
        return false;
    }

    public static void addQuestion(String question, String[] options, String answer) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO question (Question, Option1, Option2, Option3, Option4, Answer) VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, question);
            ps.setString(2, options[0]);
            ps.setString(3, options[1]);
            ps.setString(4, options[2]);
            ps.setString(5, options[3]);
            ps.setString(6, answer);
            ps.executeUpdate();
        }
    }

    public static void delete(String id, String tableName) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tableName + " WHERE QuestionID = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        }
    }

    public static ArrayList<Question> getQuestionAns() throws SQLException {
        ArrayList<Question> questions = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM question")) {
            while (rs.next()) {
                String que = rs.getString("Question");
                String op1 = rs.getString("Option1");
                String op2 = rs.getString("Option2");
                String op3 = rs.getString("Option3");
                String op4 = rs.getString("Option4");
                String ans = rs.getString("Answer");
                questions.add(new Question(que, op1, op2, op3, op4, ans));
            }
        }
        return questions;
    }
}
