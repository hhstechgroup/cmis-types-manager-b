package com.engagepoint.filters; /**
 * User: stanislav.skrebtsov (stanislav.skrebtsov@engagepoint.com)
 * Date: 29.11.13
 * Time: 18:32
 */

import com.engagepoint.constants.Constants;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginFilter implements Filter {
    public static Deque<String> history = new LinkedList<String>();
    private static final String DEFAULT_ = ".xhtml";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //because we don't need the implementation of this method
    }

    private void addRequestUrlToHistory(ServletRequest request ){
        String requestUrl = ((HttpServletRequest) request).getServletPath();
        requestUrl = requestUrl.substring(0, requestUrl.length()-DEFAULT_.length());
        requestUrl += "?faces-redirect=true";
        while (history.size() > 3) {
            history.removeFirst();
        }
        history.addLast(requestUrl);

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpSession session = ((HttpServletRequest) request).getSession(false);
        String sessionID = (session == null) ? null : (String) session.getAttribute(Constants.Strings.SESSION_ID_DISPLAY_NAME);
        String contextPath = ((HttpServletRequest) request).getContextPath();

        if (StringUtils.isEmpty(sessionID)) {
            ((HttpServletResponse) response).sendRedirect(contextPath + "/login.xhtml");
        }
        addRequestUrlToHistory(request);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //because we don't need the implementation of this method
    }
}