package hailt.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import hailt.config.mysql.Database;

/**
 * 
 * @author tanhai - 2017/05/01
 *
 */
public class BaseModel {

  public String getTableName() {
    String regex = "([a-z])([A-Z]+)";
    String replacement = "$1_$2";
    return getClass().getSimpleName().replaceAll(regex, replacement).toLowerCase();
  }

  public ArrayList<ObjectRecord> all() {
    String query = "select * from " + getTableName();
    return query(query);
  }

  public ArrayList<ObjectRecord> all(String condition) {
    String query = "select * from " + getTableName() + " where " + condition;
    return query(query);
  }

  public ObjectRecord findBy(HashMap<String, String> object) {
    String condition = "";
    for (String key : object.keySet()) {
      condition += key + " = " + object.get(key);
    }
    ArrayList<ObjectRecord> objects = all(condition);
    if (objects.size() == 0) {
      return null;
    }
    return all(condition).get(0);
  }

  public ArrayList<ObjectRecord> query(String query) {
    ArrayList<ObjectRecord> data = new ArrayList<>();
    try {
      ResultSet result = Database.getInstance().getStatement().executeQuery(query);
      while (result.next()) {
        String attributes[] = new String[result.getMetaData().getColumnCount()];
        for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
          attributes[i - 1] = result.getString(i);
        }
        data.add(new ObjectRecord(attributes));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return data;
  }

  public ObjectRecord find(long id) {
    String condition = "id = " + id;
    ArrayList<ObjectRecord> objects = all(condition);
    if (objects.size() == 0) {
      return null;
    }
    return all(condition).get(0);
  }

  public boolean destroy(String condition) {
    String query = "DELETE * FROM " + getTableName();
    if (!condition.equals("") || condition != null) {
      query += " where " + condition;
    }
    try {
      return Database.getInstance().getConnection().prepareStatement(query).execute();
    } catch (SQLException e) {
      System.out.println(getClass().getSimpleName() + ": " + e.getMessage());
      return false;
    }
  }

  public static void main(String[] args) {
    System.out.println("builded");
  }
}
