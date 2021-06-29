/*
package com.lw.api;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lw.po.CrawlerPo;
import com.sun.deploy.net.HttpResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import sun.net.www.http.HttpClient;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.sql.DataSource;
import javax.swing.text.Document;

import org.apache.http.util.EntityUtils;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


*/
/**
 * @ClassName CrawlerApi
 * @Description TODO
 * @Author Li Wang
 * @Date 2021/6/29/029 14:35
 **//*


public class CrawlerApi {

    //log4j的是使用，不会的请看之前写的文章
    static final Log logger = LogFactory.getLog(JdongMain.class);
    public static void main(String[] args) throws Exception {
        //初始化一个httpclient
        HttpClient client = new DefaultHttpClient();
        //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
        String url="http://search.jd.com/Search?keyword=Python&enc=utf-8&book=y&wq=Python&pvid=33xo9lni.p4a1qb";
        //抓取的数据
        List<CrawlerPo> bookdatas=URLFecter.URLParser(client, url);
        //循环输出抓取的数据
        for (CrawlerPo jd:bookdatas) {
            logger.info("bookID:"+jd.getBookID()+"\t"+"bookPrice:"+jd.getBookPrice()+"\t"+"bookName:"+jd.getBookName());
        }
        //将抓取的数据插入数据库
        MYSQLControl.executeInsert(bookdatas);
    }



    public static List<CrawlerPo> URLParser (HttpClient client, String url) throws Exception {
        //用来接收解析的数据
        List<CrawlerPo> JingdongData = new ArrayList<CrawlerPo>();
        //获取网站响应的html，这里调用了HTTPUtils类
        HttpResponse response = getRawHtml(client, url);
        //获取响应状态码
        int StatusCode = response.getStatusLine().getStatusCode();
        //如果状态响应码为200，则获取html实体内容或者json文件
        if(StatusCode == 200){
            String entity = EntityUtils.toString (response.getEntity(),"utf-8");
            JingdongData = getData(entity);
            EntityUtils.consume(response.getEntity());
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
        return JingdongData;
    }


    public static HttpResponse getRawHtml(HttpClient client, String personalUrl) {
        //获取响应文件，即html，采用get方法获取响应数据
        HttpGet getMethod = new HttpGet(personalUrl);
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        try {
            //执行get方法
            response = client.execute(getMethod);
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            // getMethod.abort();
        }
        return response;
    }


    public static List<CrawlerPo> getData (String html) throws Exception{
        //获取的数据，存放在集合中
        List<CrawlerPo> data = new ArrayList<CrawlerPo>();
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);
        //获取html标签中的内容
        Elements elements=doc.select("ul[class=gl-warp clearfix]").select("li[class=gl-item]");
        for (Element ele:elements) {
            String bookID=ele.attr("data-sku");
            String bookPrice=ele.select("div[class=p-price]").select("strong").select("i").text();
            String bookName=ele.select("div[class=p-name]").select("em").text();
            //创建一个对象，这里可以看出，使用Model的优势，直接进行封装
            CrawlerPo jdModel=new CrawlerPo();
            //对象的值
            jdModel.setBookID(bookID);
            jdModel.setBookName(bookName);
            jdModel.setBookPrice(bookPrice);
            //将每一个对象的值，保存到List集合中
            data.add(jdModel);
        }
        //返回数据
        return data;
    }


    public static DataSource getDataSource(String connectURI){
        BasicDataSource ds = new BasicDataSource();
        //MySQL的jdbc驱动
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("root");              //所要连接的数据库名
        ds.setPassword("112233");                //MySQL的登陆密码
        ds.setUrl(connectURI);
        return ds;
    }


    //根据自己的数据库地址修改
    static DataSource ds = MyDataSource.getDataSource("jdbc:mysql://127.0.0.1:3306/moviedata");
    static QueryRunner qr = new QueryRunner(ds);
    //第一类方法
    public static void executeUpdate(String sql){
        try {
            qr.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //第二类数据库操作方法
    public static void executeInsert(List<CrawlerPo> jingdongdata) throws SQLException {
        */
/*
         * 定义一个Object数组，行列
         * 3表示列数，根据自己的数据定义这里面的数字
         * params[i][0]等是对数组赋值，这里用到集合的get方法
         *
         *//*

        Object[][] params = new Object[jingdongdata.size()][3];
        for ( int i=0; i<params.length; i++ ){
            params[i][0] = jingdongdata.get(i).getBookID();
            params[i][1] = jingdongdata.get(i).getBookName();
            params[i][2] = jingdongdata.get(i).getBookPrice();
        }
        qr.batch("insert into jingdongbook (bookID, bookName, bookPrice)"
                + "values (?,?,?)", params);
        System.out.println("执行数据库完毕！"+"成功插入数据："+jingdongdata.size()+"条");

    }

}
*/
