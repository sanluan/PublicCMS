package com.sanluan.common.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * ErrorToNotFoundDispatcherServlet 请求发生异常时，转换为404错误
 *
 */
public class ErrorToNotFoundDispatcherServlet extends DispatcherServlet {

    private Class<TaskAfterInitServlet>[] taskClasses;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param webApplicationContext
     */
    public ErrorToNotFoundDispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    public void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            super.render(mv, request, response);
        } catch (ServletException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void initFrameworkServlet() throws ServletException {
        super.initFrameworkServlet();
        if (!ObjectUtils.isEmpty(taskClasses)) {
            for (Class<TaskAfterInitServlet> task : taskClasses) {
                getWebApplicationContext().getBean(task).exec();
            }
        }
    }

    /**
     * @param taskClasses
     */
    public void setTaskClasses(Class<TaskAfterInitServlet>[] taskClasses) {
        this.taskClasses = taskClasses;
    }

}
