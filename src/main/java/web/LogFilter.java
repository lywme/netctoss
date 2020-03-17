package web;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)req;
		HttpServletResponse response=(HttpServletResponse)res;
		String path=request.getServletPath();
	
		
		Set<String> ruleOutSet=new HashSet<String>();
		ruleOutSet.add("/login.do");
		ruleOutSet.add("/createImg.do");
		ruleOutSet.add("/toLogin.do");
		
		if(ruleOutSet.contains((String)path))
		{
			chain.doFilter(request, response);
			return;
		}
		
		//check whether has the session
		HttpSession session=request.getSession();
		String code=(String)session.getAttribute("isLogin");
		if(code==null)
		{
			//havn't stored session
			response.sendRedirect("/netctoss/toLogin.do");
		}
		else
		{
			//have login
			chain.doFilter(request, response);
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
