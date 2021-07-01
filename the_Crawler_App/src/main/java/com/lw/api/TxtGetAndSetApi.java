package com.lw.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * @ClassName TxtGetAndSetApi
 * @Description TODO
 * @Author Li Wang
 * @Date 2021/7/1/001 16:25
 **/
@RestController
@RequestMapping("/txt")
public class TxtGetAndSetApi {

    /**
     * @Description //TODO 文本替换  导出文本
     **/
    @PostMapping("/txtGetAndSet")
    public String txtGetAndSet(String filePath) {
        File file = new File(filePath);
        String s = "";
        s = txt2String(file);
        for (int i = 1; i < 2000; i++) {
            String b = "MergeAcross=" + '"' + i + '"';
            s = s.replaceAll(b, "");
            System.out.println(b);
        }
        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(filePath);
            fwriter.write(s);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "处理完成";
    }

    /**
     * @Description //TODO 获取文本内容
     **/
    public static String txt2String(File file) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result = result + s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
