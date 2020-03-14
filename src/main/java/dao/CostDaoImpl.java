package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Cost;
import util.DbUtils;

public class CostDaoImpl implements CostDao, Serializable {
	public List<Cost> findAll() {
		Connection conn=null;
		try
		{
			conn=DbUtils.getConnection();
			String sql="select * from cost order by id";
			Statement smt=conn.createStatement();
			ResultSet rs=smt.executeQuery(sql);
			List<Cost> list=new ArrayList<Cost>();
			while(rs.next())
			{
				Cost c=new Cost();
				c.setCostId(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setBaseDuration(rs.getInt(3));
				c.setBaseCost(rs.getDouble(4));
				c.setUnitCost(rs.getDouble(5));
				c.setStatus(rs.getString(6));
				c.setDescr(rs.getString(7));
				c.setCreatime(rs.getTimestamp(8));
				c.setStartime(rs.getTimestamp(9));
				c.setCostType(rs.getString(10));
				list.add(c);
			}
			return list;
		}catch(Exception e)
		{
			//记录日志，正式项目需要使用Log4j记录日志
			e.printStackTrace();
			//抛出异常，由调用者处理。可以抛Exception,也可以包装后抛出。RuntimeException抛出去
			//之后可以不处理，但Exception抛出后必须要处理。
			throw new RuntimeException("查询资费失败",e);
		}
		finally
		{
			DbUtils.close(conn);
		}
	}
	
	@Override
	public void save(Cost c) {
		Connection conn=null;
		try
		{
			conn=DbUtils.getConnection();
			String sql="INSERT INTO COST VALUES (cost_seq.nextval,?,?,?,?,1,?,sysdate,null,?)";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1,c.getName());
			ps.setObject(2, c.getBaseDuration());
			ps.setObject(3, c.getBaseCost());
			ps.setObject(4, c.getUnitCost());
			ps.setString(5, c.getDescr());
			ps.setString(6, c.getCostType());
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("插入资费失败",e);
		}
		finally
		{
			DbUtils.close(conn);
		}
	}
	
	public static void main(String[] args)
	{
		CostDao dao=new CostDaoImpl();
		Cost c=new Cost();
		c.setName("test name");
		//c.setBaseDuration(20);
		c.setBaseCost(9.9);
		//c.setUnitCost(10.9);
		c.setDescr("test description");
		c.setCostType("1");
		dao.save(c);
	}
}
