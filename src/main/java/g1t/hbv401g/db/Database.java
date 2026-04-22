package g1t.hbv401g.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// heldur utan um tengingar í gagnagrunnunum þremur
public final class Database {

    private static final String TEAM_D_URL = "jdbc:sqlite:teamD.db";
    private static final String TEAM_F_URL = "jdbc:sqlite:teamF.db";
    // hardcoded hjá H, þarf að passa við það
    private static final Path TEAM_H_PATH = Path.of("1H", "sqlite", "db.db");
    private static final String TEAM_H_URL = "jdbc:sqlite:" + TEAM_H_PATH.toString().replace('\\', '/');

    private static Connection teamD;
    private static Connection teamF;
    private static Connection teamH;

    private Database() {}

    public static void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("[db] sqlite-jdbc driver not on classpath: " + e.getMessage());
            return;
        }
        teamD = open(TEAM_D_URL, "teamD");
        teamF = open(TEAM_F_URL, "teamF");
        try {
            Path parent = TEAM_H_PATH.getParent();
            if (parent != null) Files.createDirectories(parent);
        } catch (IOException e) {
            System.err.println("[db] failed to create teamH dir: " + e.getMessage());
        }
        teamH = open(TEAM_H_URL, "teamH");
    }

    public static Connection teamD() { return teamD; }
    public static Connection teamF() { return teamF; }
    public static Connection teamH() { return teamH; }

    public static void close() {
        closeQuietly(teamD, "teamD");
        closeQuietly(teamF, "teamF");
        closeQuietly(teamH, "teamH");
    }

    private static Connection open(String url, String label) {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("[db] failed to open " + label + ": " + e.getMessage());
            return null;
        }
    }

    private static void closeQuietly(Connection c, String label) {
        if (c == null) return;
        try {
            c.close();
        } catch (SQLException e) {
            System.err.println("[db] failed to close " + label + ": " + e.getMessage());
        }
    }
}
