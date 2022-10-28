package com.example.javaserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
public class FileUploadController {
    @Value("${file.upload.path}") // application.properties中配置好路径
    private String path;

    /**
     * 文件上传+表单数据上传
     * @param file 上传的单一文件
     * @param params 上传的表单数据
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@NonNull @RequestPart MultipartFile file, @RequestParam Map<String,Object> params) throws IOException {
        // path是服务器上传文件的存储路径，路径不存在要创建路径，由于可能是多级目录，必须调用mkdirs，不能调用mkdir
        File dir = new File(path);
        if(!dir.exists())
        {
            // 有返回值，添加下面这句可以解除编辑器警告
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        // 打印看看客户端传上来表单数据的key,value
        for(Map.Entry<String,Object> entry:params.entrySet()){
            System.out.println("Key=" + entry.getKey() + ", Value=" + entry.getValue());
        }

        if(!file.isEmpty())
        {
            // 查看上传的文件的类型，需要客户端自定义设置，客户端不设置就打印null
            // 客户端设置了就按客户端设置的显示
            //  客户端在http协议数据里指定"Content-Type: image/png"时，这里显示image/png
            System.out.println(file.getContentType());

            // 上传文件的绝对路径：服务器端的上传目录+分隔符+上传的文件原始名字
            String serverFileName = dir.getAbsolutePath()+File.separator+file.getOriginalFilename();
            System.out.println(serverFileName);//打印出文件在服务器端的存储路径
            file.transferTo(new File(serverFileName));
        }

        return "success";
    }

    /**
     * 仅上传表单数据
     * @param headers HTTP请求头
     * @param params 请求参数，要求客户端上传的请求数据必须是utf-8类型的,因为我当前没有找到在服务器端成功转化字符集的方式
     *               当我的客户端以gbk的编码的字符串上传中文时，我服务器端无法正确解析出来，暂时不知道怎么解决
     *               期间我尝试过RequestFilter来接收字节流，然后进行编码格式转化，但都尝试失败，所以只能要求客户端上传
     *               中文时必须先转成utf-8的串再上传
     * @return 返回响应
     */
    @PostMapping("upload2")
    @ResponseBody
    public String upload2(@RequestHeader Map<String,Object> headers,@RequestParam Map<String,Object> params) {
        // 打印请求头
        System.out.println("请求头：");
        for(Map.Entry<String,Object> entry: headers.entrySet()){
            System.out.println("key="+entry.getKey()+",value="+entry.getValue());
        }

        // 打印请求数据
        System.out.println("post请求上传的数据：");
        for(Map.Entry<String,Object> entry: params.entrySet()){
            System.out.println("key="+entry.getKey()+",value="+entry.getValue());
        }
        return "success";
    }
}
