package com.engagepoint.filters;

/**
 * User: stanislav.skrebtsov (stanislav.skrebtsov@engagepoint.com)
 * Date: 29.11.13
 * Time: 18:32
 */

import com.engagepoint.view.LoginBean;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpSession session = ((HttpServletRequest) request).getSession(false);
        String sessionID = (session == null) ? null : (String) session.getAttribute(LoginBean.SESSION_ID);
        String contextPath = ((HttpServletRequest) request).getContextPath();

        if (StringUtils.isEmpty(sessionID)) {
            ((HttpServletResponse) response).sendRedirect(contextPath + "/login.xhtml");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}