import com.wuzx.io.webserver.connector.ConnectorUtils;
import com.wuzx.io.webserver.connector.HttpStatus;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        final PrintWriter writer = servletResponse.getWriter();

        writer.println(ConnectorUtils.renderStatus(HttpStatus.SC_OK));
        writer.println("what time is it now ");

        writer.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("销毁");
    }
}
