package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AdminDao;
import dao.AdminDaoImpl;
import dao.CostDao;
import dao.CostDaoImpl;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

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
		else if("/toUpdateCost.do".equals(path))
		{
			toUpdateCost(req,res);
		}
		else if("/toLogin.do".equals(path))
		{
			toLogin(req,res);
		}
		else if("/login.do".equals(path))
		{
			login(req,res);
		}
		else if("/toIndex.do".equals(path))
		{
			toIndex(req,res);
		}
		else if("/logOut.do".equals(path))
		{
			logout(req,res);
		}
		else if("/createImg.do".equals(path))
		{
			createImg(req,res);
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
	
	protected void toUpdateCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		String requestId=req.getParameter("id");
		if(requestId!=null && !requestId.equals(""))
		{
			CostDao dao=new CostDaoImpl();
			Cost c=dao.findById(Integer.parseInt(requestId));
			req.setAttribute("cost", c);
			req.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(req,res);
		}
		else
		{
			throw new RuntimeException("查询时id为空");
		}
	}
	
	protected void toLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req,res);
	}
	
	protected void login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		String name=req.getParameter("name");
		String pwd=req.getParameter("pwd");
		String valicode=req.getParameter("valicode");
		
		HttpSession session=req.getSession();
		String sessionCode=(String)session.getAttribute("imgcode");
		
		if(!valicode.equalsIgnoreCase(sessionCode))
		{
			//验证码不合法
			req.setAttribute("error", "验证码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req,res);
		}
		else
		{
			AdminDao dao=new AdminDaoImpl();
			Admin admin=dao.findByCode(name);
			if(admin!=null)
			{
				if(admin.getPassword().equals(pwd))
				{
					//credentials are correct
					session.setAttribute("isLogin", name);
					res.sendRedirect("toIndex.do");
				}
				else
				{
					//password is incorrect
					req.setAttribute("error", "密码错误");
					req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req,res);
				}
			}
			else
			{
				//Can't find user
				req.setAttribute("error", "账号错误");
				req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req,res);
			}
		}
	}
	
	protected void toIndex(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		req.getRequestDispatcher("WEB-INF/main/index.jsp").forward(req,res);
	}
	
	protected void logout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		//invalidate session
		HttpSession session=req.getSession();
		session.invalidate();
		res.sendRedirect("toLogin.do");
	}
	
	protected void createImg(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		Object[] objs=ImageUtil.createImage();
		String code=(String)objs[0];
		//验证码保存到session
		HttpSession session=req.getSession();
		session.setAttribute("imgcode", code);
		//将图片输出给浏览器
		BufferedImage image=(BufferedImage)objs[1];
		res.setContentType("image/png");
		OutputStream os=res.getOutputStream();
		ImageIO.write(image,"png",os);
		os.close();
	}
}
