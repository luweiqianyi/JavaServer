package com.example.javaserver.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 20220922
 * 尝试实现一个过滤器对请求进行编码转换(暂时尝试失败)
 * 当客户端上传gbk的中文时，该过滤器无法正确地将其转化成正确的串
 * 我的目的是：将客户端上传来的gbk的数据转成utf-8
 */
// 需要在JavaServerApplication主程序类上加入@ServletComponentScan注解过滤器才能有效
// @WebFilter(filterName = "requestFilter",urlPatterns = {"/*"})
public class CharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("RequestFilter init");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        System.out.println("请求的字符集编码："+request.getCharacterEncoding());
//        Enumeration<String> names = request.getParameterNames();
//        while(names.hasMoreElements()){
//            System.out.print(names.nextElement()+" ");
//        }
//        System.out.println();

        Map<String, String[]> map = request.getParameterMap();
        for(Map.Entry<String,String[]> entry : map.entrySet()){
            // System.out.println(entry.getKey());
            for (int i=0;i<entry.getValue().length;i++){
                // System.out.println("  "+entry.getValue()[i]);
                // 转化还是有问题
//                byte[] bytes = entry.getValue()[i].getBytes(Charset.forName("GBK"));
//                String uftString = new String(bytes, StandardCharsets.UTF_8);
//                entry.getValue()[i] = uftString;

//                String str = new String(entry.getValue()[i].getBytes("UTF-8"),"UTF-8");//GBK
//                entry.getValue()[i] = str;
                if(entry.getKey().equals("hobby"))
                {
                    // 尝试打印出来字节数据看看是不是和客户端的一样
                    //byte[] bytes = entry.getValue()[i].getBytes(Charset.forName("UTF-8"));
                    byte[] bytes = entry.getValue()[i].getBytes(Charset.forName("GBK"));

                    for(int k=0;k< bytes.length;k++){
                        System.out.println(Integer.toHexString(bytes[k]));
                    }
                }
            }
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        System.out.println("RequestFilter destroy");
    }
}
