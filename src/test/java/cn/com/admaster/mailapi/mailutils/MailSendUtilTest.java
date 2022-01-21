package cn.com.yourcompany.mailapi.mailutils;

import cn.com.yourcompany.mailapi.PropUtils;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: qeekey
 * Date: 2018/5/14
 * Time: 下午5:40
 * To change this template use File | Settings | File Templates.
 */
public class MailSendUtilTest extends TestCase {

    private static final Log logger = LogFactory.getLog(MailSendUtilTest.class);

    @Override
    protected void setUp() throws Exception {
        initTest();
    }

    public void initTest() throws Exception{
        //为了初始化SMTP参数
        PropUtils propUtils = new PropUtils();
    }

    public void testSendHtmlMail(){
        boolean result = true;

        String receiver = "noreply@xxxxx.com";
        String title = "MailSendUtilTest Title";
        String content = "<div>MailSendUtilTest Content</div><br/><hr/><div>come from ...</div>";
        MailPojo info = new MailPojo();
        info.setReceiver(receiver);
        info.setSubject(title);
        info.setContent(content);

        String  str = MailSendUtil.sendHtmlMail(info);

        if (str.indexOf(":0") !=-1){
            result = true;
        }else {
            result = false;
        }
        assert (result);
    }

    public void testSendTextMail() {
        boolean result = true;

        String receiver = "xxxx@yourcompany.com.cn,xxxx@126.com";
        String title = "MailSendUtilTest Title";
        String content = "MailSendUtilTest Content";
        MailPojo info = new MailPojo();
        info.setReceiver(receiver);
        info.setSubject(title);
        info.setContent(content);

        String str = MailSendUtil.sendTextMail(info);

        if (str.indexOf(":0") !=-1){
            result = true;
        }else {
            result = false;
        }
        assert (result);
    }

}