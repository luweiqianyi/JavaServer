package com.example.javaserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LoginController {

    // 从配置文件application.properties中取用户名和密码
    // 暂时先简单处理一下，不加数据库相关的内容
    @Value("${login.uname}")
    String uname;

    @Value("${login.pword}")
    String pword;

    @PostMapping("/login")
    public String login(@RequestParam Map<String,String> user){
        String clientUname = "";
        String clientPword = "";
        for (Map.Entry<String,String> entry:user.entrySet()){
            if("uname".equals(entry.getKey())){
                clientUname = entry.getValue();
            }
            if("pword".equals(entry.getKey())){
                clientPword = entry.getValue();
            }
        }

        if(uname.equals(clientUname) && pword.equals(clientPword)){
            return "redirect:fileupload.html";
        }

        // 下面两种方式都可以,前者可自定义更灵活
        return "redirect:/errorpage/loginforbiden.html"; // 直接返回一个静态页面
        // return "redirect:/forbiden"; //提交forbiden这个函数去处理
    }

    @ResponseBody
    @GetMapping("/forbiden")
    public String forbiden(){
        return "Forbiden";
    }
}
