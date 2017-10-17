package lab.util;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class DataSource {

    private static String url = "jdbc:mysql://172.22.4.16:3306/lab?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static String user = "root";
    //	private static String password = "lab123";
    private static String password = "19901009";

    private static int initCount = 30;
    private static int maxCount = 50;
    private int currentCount = 0;

    /**
     * 初始化connnection连接池
     */
    LinkedList<Connection> connectionsPool = new LinkedList<Connection>();

    /**
     * 创建Connection连接池
     */
    public DataSource() {
        try {
            for (int i = 0; i < initCount; i++) {
                //创建30个与数据库的连接
                this.connectionsPool.addLast(this.createConnection());
                this.currentCount++;
            }
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    //提供connection连接
    public Connection getConnection() throws SQLException {
        synchronized (connectionsPool) {
            //使用一个连接，与数据库的连接就减少一个为什么currentCount没有减减--------------------
            if (this.connectionsPool.size() > 0) {
                //返回和去除第一个元素
                return this.connectionsPool.removeFirst();
            }

//判断连接池是否满了，没有，则可以在继续创建知道满  为什么不是initCount------------------
            if (this.currentCount < maxCount) {
                this.currentCount++;
                return this.createConnection();
            }
            //链接用完时抛出异常
            throw new SQLException("数据库的链接以用完");
        }
    }

    /**
     * 创建连接的方法
     *
     * @return
     * @throws SQLException
     */
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * conn用完后重新放入连接池
     *
     * @param conn
     */
    public void close(Connection conn) {
        //放入连接池的时候没有判断连接池是否满了-----------------------------------------
        this.connectionsPool.addLast(conn);
    }
}
