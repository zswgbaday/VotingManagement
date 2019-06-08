package com.zsw.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsw.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JSONUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);


    /**
     * response 输出JSON
     */
    public static void out(ServletResponse response, Result result) {

        response.setContentType("application/json; charset=utf-8");

        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();

            ObjectMapper mapper = new ObjectMapper();
            // Convert object to JSON string
            String Json = mapper.writeValueAsString(result);

            out.println(Json);
        } catch (Exception e) {
            LOGGER.debug("输出JSON报错。");
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }
    
    public static final int UNAUTHORIZED  = HttpServletResponse.SC_UNAUTHORIZED;

    public static void outWithStatus(ServletResponse response, Result result, int status) {

        response.setContentType("application/json; charset=utf-8");
        ((HttpServletResponse)response).setStatus(status);
                
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();

            ObjectMapper mapper = new ObjectMapper();
            // Convert object to JSON string
            String Json = mapper.writeValueAsString(result);

            out.println(Json);
        } catch (Exception e) {
            LOGGER.debug("输出JSON报错。");
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }


}
