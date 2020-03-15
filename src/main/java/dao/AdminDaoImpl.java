package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entity.Admin;
import util.DbUtils;

public class AdminDaoImpl implements AdminDao, Serializable {

	@Override
	public Admin findByCode(String code) {
		Connection conn=null;
		try
		{
			conn=DbUtils.getConnection();
			String sql="select * from admin_info where admin_code=?";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, code);
			ResultSet rs=ps.executeQuery();
			Admin admin=new Admin();
			if(rs.next())
			{
				admin.setAdminId(rs.getInt(1));
				admin.setAdminCode(rs.getString(2));
				admin.setPassword(rs.getString(3));
				admin.setName(rs.getString(4));
				admin.setTelephone(rs.getString(5));
				admin.setEmail(rs.getString(6));
				admin.setEnrolldate(rs.getTimestamp(7));
				return admin;
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("查询用户出错");
		}
		finally
		{
			DbUtils.close(conn);
		}
	}

	
	public static void main(String[] args)
	{
		AdminDao dao=new AdminDaoImpl();
		Admin a=dao.findByCode("lywme");
		System.out.println(a.getEmail());
	}
}
