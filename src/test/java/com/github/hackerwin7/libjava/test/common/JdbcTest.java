package com.github.hackerwin7.libjava.test.common;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * @author : wenqi.jk
 * @since : 7/24/24, 17:32
 **/
@Slf4j
public class JdbcTest {

  public static final Map<String, String> SECURITY_MYSQL_PARAM_MAPS = ImmutableMap.of(
      "autoDeserialize", "false",
      "allowUrlInLocalInfile", "false",
      "allowLoadLocalInfile", "false",
      "allowLoadLocalInfileInPath", ""
  );
  public static void main(String[] args) {
    dbcpSelect();
  }

  public static void dbcpSelect() {
    // dbcp connect local mysql
    String url = "jdbc:mysql://localhost:3306/test?useSSL=false";
    String username = "root";
    String password = "";
    String query = "SELECT * FROM Persons";
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDefaultAutoCommit(false);
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setInitialSize(5);
    dataSource.setMaxTotal(10);
    dataSource.setMaxIdle(5);
    dataSource.setMinIdle(1);
    SECURITY_MYSQL_PARAM_MAPS.forEach(dataSource::addConnectionProperty);
    try {
      java.sql.Connection conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        System.out.println(rs.getString("PersonID") + " " + rs.getString("LastName"));
      }
      rs.close();
      stmt.close();
      conn.close();
    } catch (Exception e) {
      log.error("dbcp select Error: " + e.getMessage());
    }
  }

  public static void jdbcSelect() {
    // jdbc connect local mysql
    String url = "jdbc:mysql://localhost:3306/test?useSSL=false";
    String username = "root";
    String password = "";
    String query = "SELECT * FROM Persons";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      java.sql.Connection conn = java.sql.DriverManager.getConnection(url, username, password);
      conn.setAutoCommit(false);
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        System.out.println(rs.getString("PersonID") + " " + rs.getString("LastName"));
      }
//      conn.commit();
      rs.close();
      stmt.close();
      conn.close();
    } catch (Exception e) {
      log.error("jdbc select Error: " + e.getMessage());
    }
  }
}
