package com.lw;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.security.Key;
import java.util.Base64;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        String tom010804lw = encrypt("tom010804lw", "tom010804lw");
        System.out.println(tom010804lw);
    }

    /*public static void main(String[] args) {
        String decrypt = decrypt("b4frjTDqQvA=", "6kF6PeeuqT5+qHSxHYiWOQ==");
        System.out.println(decrypt);
    }*/

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data 待加密字符串
     * @return 加密后内容
     */
    public static String encrypt(String password, String data) {
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            return new String(Base64.getEncoder().encode(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data 待解密字符串
     * @return 解密后内容
     */
    public static String decrypt(String password, String data) {
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(CHARSET))), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }



    /**
     * @Description //TODO 操作xml文件
     **/
    /*public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Document doc = new SAXReader().read(new File("D:/Ia_Quarter_template.xml"));
        *//**
         * 删除
         *//*
        //1.1 删除标签
        *//*Element conElem = doc.getRootElement().element("file");
        conElem.detach();*//* //自杀
        //conElem.getParent().remove(conElem); //他杀,利用他的父节点删除他

        //1.2 删除属性
        Attribute idAttr = doc.getRootElement().element("Cell").attribute("StyleID");
        idAttr.detach();

        //1.2 把文档写出到xml文件中
        OutputStream out = new FileOutputStream("D:/Ia_Quarter_templates.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter writer = new XMLWriter(out,format);
        writer.write(doc);
    }*/

    public static void main(String[] args) throws Exception {
        //1-获取XML-IO流
        InputStream xmlInputStream = getXmlInputStream("D:/Ia_Quarter_template.xml");
        //2-解析XML-IO流 ，获取Document 对象，以及Document对象 的根节点
        Element rootElement = getRootElementFromIs(xmlInputStream);
        //3~5-从根元素解析得到元素
        parseElementFromRoot(rootElement);

        //控制台输出：
        //name == HelloWorld
        //className == com.huishe.HelloWord
        //propertyEle: name == textone
        //propertyEle: value == Hello World!
        //propertyEle: name == texttwo
        //propertyEle: value == Hello SUN!
    }

    //1-获取XML-IO流
    private static InputStream  getXmlInputStream(String xmlPath){
        InputStream inputStream = null;
        try {
            //1-把要解析的 XML 文档转化为输入流，以便 DOM 解析器解析它
            inputStream= new FileInputStream(xmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
    //2-解析XML-IO流 ，获取Document 对象，以及Document对象 的根节点
    private static Element getRootElementFromIs(InputStream inputStream) throws Exception {
        if(inputStream == null){
            return  null;
        }
        /*
         * javax.xml.parsers 包中的DocumentBuilderFactory用于创建DOM模式的解析器对象 ，
         * DocumentBuilderFactory是一个抽象工厂类，它不能直接实例化，但该类提供了一个newInstance方法 ，
         * 这个方法会根据本地平台默认安装的解析器，自动创建一个工厂的对象并返回。
         */
        //2-调用 DocumentBuilderFactory.newInstance() 方法得到创建 DOM 解析器的工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //3-调用工厂对象的 newDocumentBuilder方法得到 DOM 解析器对象。
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        //4-调用 DOM 解析器对象的 parse() 方法解析 XML 文档，得到代表整个文档的 Document 对象，进行可以利用DOM特性对整个XML文档进行操作了。
        Document doc = (Document) docBuilder.parse(inputStream);
        //5-得到 XML 文档的根节点
        Element root = (Element) doc.getDocument();
        //6-关闭流
        if(inputStream != null){
            inputStream.close();
        }
        return root;
    }

    //3-从根元素解析得到元素
    private static void parseElementFromRoot(Element root) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                //4-从元素解析得到属性值
                getDataFromElement(ele);
                //5-从元素解析特定子元素并解析(以property为例)
                getCertainElementFromParentElement(ele);
            }
        }
    }

    //4-从元素解析得到属性值
    private static void getDataFromElement(Element ele) {
        String name = ele.getAttribute("name");//根据属性名称读取属性值
        System.out.println("name == " + name);
        String className = ele.getAttribute("class");
        System.out.println("className == " + className);
    }
    //5-从元素解析特定子元素并解析(以property为例)
    private static void getCertainElementFromParentElement(Element ele) {
        NodeList propertyEleList = ele.getElementsByTagName("property");//根据标签名称获取标签元素列表
        for (int i = 0; i < propertyEleList.getLength(); i++) {
            Node node = propertyEleList.item(i);
            if (node instanceof Element) {
                Element propertyEle = (Element) node;
                String name = propertyEle.getAttribute("name");
                System.out.println("propertyEle: name == " + name);
                String value = propertyEle.getAttribute("value");
                System.out.println("propertyEle: value == " + value);
            }
        }

    }
}
