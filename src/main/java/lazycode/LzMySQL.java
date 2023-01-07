package lazycode;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class LzMySQL {
    Connection connection = null;
    String database = null;

    public LzMySQL() {
    }

    public LzMySQL init(String user, String password, String database,
                        String host, int port, int version) throws ClassNotFoundException, SQLException {
        this.database = database;
        if (version < 8) {
            Class.forName("com.mysql.jdbc.Driver");
        } else {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        String url = String.format(
                "jdbc:mysql://%s:%d/%s?useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
                host, port, database);
        this.connection = DriverManager.getConnection(url, user, password);
        return this;
    }

    public void close(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Statement st) {
        try {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 增,删,改 操作, 传入sql和数据参数列表
     */
    public boolean update(String sql, Object... args) {
        boolean flag = true;
        PreparedStatement preparedStatement = null;
        try {
            // 获取操作数据库的prepareStatement对象,预编译sql
            preparedStatement = this.connection.prepareStatement(sql);
            // 将预编译sql中的占位符替换为具体的数据
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]); // 注意preparedStatement占位符是从1开始数的
            }
            flag = preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            // 关闭相关的资源
            this.close(preparedStatement);
        }
        return flag;
    }

    public List<Map<Object, Object>> query(String sql, Object... args) {
        ArrayList<Map<Object, Object>> list = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount(); // 获取结果集的数据列数
            while (resultSet.next()) {
                HashMap<Object, Object> map = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(columnName);
                    map.put(columnName, columnValue);
                }
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(preparedStatement);
            this.close(resultSet);
        }
        return null;
    }

    /**
     * 查询数据, 传入sql和一条记录的包装类, 数据参数列表
     * 返回查询的结果集合
     */
    public <T> List<T> query(String sql, Class<T> clazz, Object... args) {
        ArrayList<T> list = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.connection.prepareStatement(sql);
            // 将预编译sql中的占位符替换为具体的数据
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            // 执行查询语句
            resultSet = preparedStatement.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount(); // 获取结果集的数据列数
            // 循环获取所有结果记录
            while (resultSet.next()) {
                // 通过反射获取类实例
                T instance = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    // 1. 获取列的别名
                    // 注意：获取结果集中（相当于数据表）列的名称（别名）, 列从1开始数
                    String columnName = metaData.getColumnLabel(i + 1);
                    // 根据列名获取当前行记录对应列的数据
                    Object columnValue = resultSet.getObject(columnName);
                    // 使用反射对实例属性进行赋值
                    Field declaredField = clazz.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(instance, columnValue);
                }
                list.add(instance);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(preparedStatement);
            this.close(resultSet);
        }
        return null;
    }

    public List<Map<Object, Object>> tableMateData(String tableName) {
        String sql = "SELECT\n" +
                "    a.TABLE_SCHEMA as dbname, -- 0 数据库名\n" +
                "    a.TABLE_NAME as tbname,     -- 1 表名\n" +
                "    b.TABLE_COMMENT tbcomment,  -- 2 表注释\n" +
                "\n" +
                "    a.COLUMN_NAME as colname,       -- 3 列名\n" +
                "    a.COLUMN_COMMENT as colcomment, -- 4 列注释\n" +
                "    a.DATA_TYPE as col_datatype,    -- 5 数据类型 'varchar'\n" +
                "    a.COLUMN_TYPE as coltype,       -- 6 列数据类型(全) 'varchar(255)'\n" +
                "    a.IS_NULLABLE as isnull,        -- 7 是否可以为空 'YES' , 'NO'\n" +
                "    a.ORDINAL_POSITION as  col_position,    -- 8 列所在位置 1\n" +
                "    c.CONSTRAINT_NAME as cs_type,   -- 9 列约束类型 None, PRIMARY, ... \n" +
                "    c.COLUMN_NAME as cs_col_name,   -- 10 约束列名\n" +
                "    c.REFERENCED_TABLE_SCHEMA as cs_dbname,      -- 11 外键主表数据库名\n" +
                "    c.REFERENCED_TABLE_NAME as cs_tbname,        -- 12 外键主表表名\n" +
                "    c.REFERENCED_COLUMN_NAME as cs_tb_col_name \t -- 13 外键主表关联列名\n" +
                "FROM\n" +
                "    (\n" +
                String.format("    SELECT * FROM information_schema.COLUMNS WHERE TABLE_SCHEMA= \"%s\" -- 数据库名\n", this.database) +
                "    ) AS a\n" +
                "LEFT JOIN information_schema.TABLES AS b\n" +
                "    ON a.TABLE_NAME=b.TABLE_NAME AND a.TABLE_SCHEMA=b.TABLE_SCHEMA\n" +
                "LEFT JOIN information_schema.KEY_COLUMN_USAGE as c\n" +
                "    ON c.CONSTRAINT_SCHEMA = a.TABLE_SCHEMA \n" +
                "        AND c.TABLE_NAME = a.TABLE_NAME \n" +
                "        AND c.COLUMN_NAME = a.COLUMN_NAME\n" +
                String.format("WHERE a.TABLE_NAME like \"%s\" -- 需要选择的表格, 默认全部表格\n", tableName) +
                "order by a.TABLE_NAME, a.ORDINAL_POSITION \n" +
                ";";
        return this.query(sql);
    }
}
