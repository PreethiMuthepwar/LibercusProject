package test.automation.framework;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import static test.automation.framework.Runner.log;

public final class DB {

    private static Connection connection;

    public static List<Map<String, Object>> executeQuery(String sqlQuery) {
        QueryRunner qRunner = new QueryRunner();
        List<Map<String, Object>> mapList = null;
        try {
            mapList = qRunner.query(getConnection(), sqlQuery, new MapListHandler());
        } catch (Exception e) {
            log().log(Level.WARNING, e.getMessage());
        }
        return mapList;
    }

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            DbUtils.loadDriver(Config.getDbDriver());
            connection = DriverManager.getConnection(Config.getDbUrl(), Config.getDbUsername(), Config.getDbPassword());
        } catch (Exception e) {
            log().log(Level.WARNING, e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            DbUtils.closeQuietly(connection);
            connection = null;
        }
    }
}
