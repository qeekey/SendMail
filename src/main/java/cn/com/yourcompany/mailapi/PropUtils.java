package cn.com.yourcompany.mailapi;

import cn.com.yourcompany.mailapi.mailutils.MailComm;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: qeekey
 * Date: 2018/5/15
 * Time: 上午12:25
 * To change this template use File | Settings | File Templates.
 */
public class PropUtils {

    public PropUtils() throws Exception{
        String fileName = "conf/smtp.properties";

        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream innerfile_in = PropUtils.class.getClassLoader().getResourceAsStream(fileName);
        // 使用properties对象加载输入流
        properties.load(innerfile_in);
        //获取key对应的value值
        MailComm.smtp_server = properties.getProperty("smtp_server");
        MailComm.smtp_server_port = properties.getProperty("smtp_server_port");
        MailComm.sender = properties.getProperty("sender");
        MailComm.password = properties.getProperty("password");

        System.out.println("innerfile_in MailComm.password=" + MailComm.password);

        FileInputStream exfile_in = new FileInputStream(fileName);
        properties.load(exfile_in);
        MailComm.smtp_server = properties.getProperty("smtp_server");
        MailComm.smtp_server_port = properties.getProperty("smtp_server_port");
        MailComm.sender = properties.getProperty("sender");
        MailComm.password = properties.getProperty("password");

        System.out.println("exfile_in    MailComm.password=" + MailComm.password);

    }

}
