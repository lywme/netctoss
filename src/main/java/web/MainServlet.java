package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CostDao;
import dao.CostDaoImpl;
import entity.Cost;

public class MainServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String path=req.getServletPath();
		if("/findCost.do".equals(path))
		{
			findCost(req,res);
		}
		else if("/toAddCost.do".equals(path))
		{
			toAddCost(req,res);
		}
		else if("/addCost.do".equals(path))
		{
			addCost(req,res);
		}
		else if("/modiCost.do".equals(path))
		{
			modiCost(req,res);
		}
		else
		{
			throw new RuntimeException("Can't find this page");
		}
	}
	
	//查询资费
	protected void findCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		CostDao dao=new CostDaoImpl();
		List<Cost> list=dao.findAll();
		req.setAttribute("costs",list);
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req,res);
	}
	
	//增加资费
	protected void toAddCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req,res);
	}
	
	protected void addCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		//接收参数，传入有可能为中文，所以需要设置编码
		req.setCharacterEncoding("utf-8");
		String name=req.getParameter("name");
		String costType=req.getParameter("costType");
		String baseDuration=req.getParameter("baseDuration");
		String baseCost=req.getParameter("baseCost");
		String unitCost=req.getParameter("unitCost");
		String descr=req.getParameter("descr");
		Cost c=new Cost();
		c.setName(name);
		c.setCostType(costType);
		//防止输入的数据是空，可能会造成转换时异常
		if(baseDuration!=null && !baseDuration.equals(""))
			c.setBaseDuration(new Integer(baseDuration));
		if(baseCost!=null && !baseCost.equals(""))
			c.setBaseCost(new Double(baseCost));
		if(unitCost!=null && !unitCost.equals(""))
			c.setUnitCost(new Double(unitCost));
		c.setDescr(req.getParameter("descr"));
		CostDao dao=new CostDaoImpl();
		dao.save(c);
		res.sendRedirect("findCost.do");
	}
}
