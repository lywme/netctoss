package util;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbUtils {
	static String driver;
	static String url;
	static String userName;
	static String passWord;
	
	//读取文件中的数据库连接参数
	static{
		//初始化静态属性
		try{
			//1>利用Properties读取配置文件
			Properties cfg=new Properties();
			//2>从配置文件中查找相应参数值
			InputStream in=DbUtils.class.getClassLoader().getResourceAsStream("db.properties");
			cfg.load(in);
			//System.out.println(cfg);
			
			driver=cfg.getProperty("jdbc.driver");
			url=cfg.getProperty("jdbc.url");
			userName=cfg.getProperty("jdbc.userName");
			passWord=cfg.getProperty("jdbc.passWord");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 封装创建数据库连接的过程
	 * 简化数据库连接
	 */
	public static Connection getConnection()
	{
		try{
			//注册驱动
			Class.forName(driver);
			//连接到数据库
			//getConnection()方法查找并尝试连接到数据库，如果不成功将出现异常
			Connection conn=DriverManager.getConnection(url, userName, passWord);
			return conn;   //方法的调用者，要么得到一个明确的结果，要么得到一个异常
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);   //有异常给调用者返回一个异常
		}
	}
	
	public static void close(Connection conn)
	{
		if(conn!=null)
		{
			try
			{
				conn.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//Test DbUtils
	public static void main(String[] args)
	{
		Connection con=DbUtils.getConnection();
		System.out.println(con);
		DbUtils.close(con);
	}
}
