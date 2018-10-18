package servlets;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import helper.Helper;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("name");
        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String desc = request.getParameter("description");
        if (user != null) {
            response.sendRedirect("/profile"); //как он вообще сюда попал
        } else {
            Pattern pattern = Pattern.compile("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{0,19}$");
            Matcher matcher = pattern.matcher(login);
            if (matcher.matches()) {
                //TODO register user in DB
                session.setAttribute("login", login);
                Cookie cookie = new Cookie("login", login);
                cookie.setMaxAge(60 * 10);
                response.addCookie(cookie);
                response.sendRedirect("/login"); //переходим в свой профиль
            } else {
                response.sendRedirect("/login");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Configuration cfg = Helper.getConfig(getServletContext());
        Template tmpl = cfg.getTemplate("registration.html");
        HashMap<String, Object> root = new HashMap<>();
        root.put("form_url", request.getRequestURI());
        try {
            tmpl.process(root, response.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
