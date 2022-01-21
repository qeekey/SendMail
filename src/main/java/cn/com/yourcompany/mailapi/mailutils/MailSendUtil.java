package cn.com.yourcompany.mailapi.mailutils;

/**
 * Created with IntelliJ IDEA.
 * User: qeekey
 * Date: 2018/5/14
 * Time: 下午5:35
 * To change this template use File | Settings | File Templates.
 */
import cn.com.yourcompany.mailapi.comm.EmailComm;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;


public class MailSendUtil {

    public static String sendMail(String addresses, String subject, String message, String senderName , String text){
        String  str = "";
        MailPojo info = new MailPojo();
        info.setSenderName(senderName);
        info.setReceiver(addresses);
        info.setSubject(subject);
        info.setContent(message);
        if (text.equals("1")){
            str = sendTextMail(info);
        }else {
            str = sendHtmlMail(info); //默认发Html
        }
        return str;
    }

    public static String sendHtmlMail(MailPojo mailPojo){
        MailResult mailResult = null;
        StringBuffer sb = new StringBuffer();
        int idx = 0 ;
        boolean allOK = true;

        String[] receives = mailPojo.getReceiver().split(",",-1);
        for (idx=0;idx<receives.length;idx++) {
            try {
                if (ignoreThisEmail(receives[idx])){
                    sb.append(receives[idx] + " Ignore" + ";");
                }else {
                    mailPojo.setHost(MailComm.smtp_server);
                    mailPojo.setMailServerPort(MailComm.smtp_server_port);
                    mailPojo.setSender(MailComm.sender);
                    mailPojo.setFormPassword(MailComm.password);   //网易邮箱的授权码~不一定是密码

                    // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                    Multipart mainPart = new MimeMultipart();
                    // 创建一个包含HTML内容的MimeBodyPart
                    BodyPart html = new MimeBodyPart();
                    // 设置HTML内容
                    html.setContent(mailPojo.getContent(), "text/html; charset=utf-8");
                    mainPart.addBodyPart(html);
                    // 将MiniMultipart对象设置为邮件内容
                    Message message = getMessage(mailPojo, receives[idx], mailPojo.getSenderName() );
                    message.setContent(mainPart);
                    Transport.send(message);
                    sb.append(receives[idx] + " OK" + ";");
                }
            }catch (Exception e){
                e.printStackTrace();
                String str = e.toString().replaceAll("\n","");
                sb.append(receives[idx] +  " " + str + ";");
                allOK = false ;
            }
        }

        if (allOK){
            mailResult = new MailResult(0 , sb.toString() );
        }else {
            mailResult = new MailResult(-1, sb.toString() );
        }
        return JsonUtil.bean2JsonStr(mailResult);
    }

    public static String sendTextMail(MailPojo mailPojo) {
        MailResult mailResult = null;
        StringBuffer sb = new StringBuffer();
        int idx = 0 ;
        boolean allOK = true;

        String[] receives = mailPojo.getReceiver().split(",",-1);
        for (idx=0;idx<receives.length;idx++) {
            try {
                if (ignoreThisEmail(receives[idx])) {
                    sb.append(receives[idx] + " Ignore" + ";");
                }else {
                    mailPojo.setHost(MailComm.smtp_server);
                    mailPojo.setMailServerPort(MailComm.smtp_server_port);
                    mailPojo.setSender(MailComm.sender);
                    mailPojo.setFormPassword(MailComm.password);   //网易邮箱的授权码~不一定是密码
                    Message message = getMessage(mailPojo, receives[idx], mailPojo.getSenderName() );
                    //消息发送的内容
                    message.setText(mailPojo.getContent());

                    Transport.send(message);
                    sb.append(receives[idx] + " OK" + ";");
                }
            }catch (Exception e){
                String str = e.toString().replaceAll("\n","");
                sb.append(receives[idx] +  " " + str + ";");
                allOK = false ;
            }
        }
        if (allOK){
            mailResult = new MailResult(0 , sb.toString() );
        }else {
            mailResult = new MailResult(-1, sb.toString() );
        }
        return JsonUtil.bean2JsonStr(mailResult);
    }

    private static Message getMessage(MailPojo mailPojo,String receive , String senderName) throws Exception{
        final Properties p = System.getProperties() ;
        p.setProperty("mail.smtp.host", mailPojo.getHost());

        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.setProperty("mail.smtp.port", mailPojo.getMailServerPort());
        p.setProperty("mail.smtp.socketFactory.port", mailPojo.getMailServerPort());

        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.user", mailPojo.getSender());
        p.setProperty("mail.smtp.pass", mailPojo.getFormPassword());

        p.setProperty("mail.smtp.socketFactory.fallback","true");
        p.setProperty("mail.smtp.starttls.enable","true");



        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getInstance(p, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(p.getProperty("mail.smtp.user"),p.getProperty("mail.smtp.pass"));
            }
        });
//        session.setDebug(true);
        Message message = new MimeMessage(session);
        //消息发送的主题
        message.setSubject(mailPojo.getSubject());
        //消息的发送者
//        message.setFrom(new InternetAddress(p.getProperty("mail.smtp.user"), senderName ));
        message.setFrom(new InternetAddress(p.getProperty("mail.smtp.user"), MailComm.senderName ));

//        String[] receivers = mailPojo.getReceiver().split(",",-1);
//        InternetAddress[] sendTo = new InternetAddress[receivers.length];
//        for (int i = 0; i < receivers.length; i++) {
//            sendTo[i] = new InternetAddress(receivers[i]);
//        }
//        //创建邮件的接收者地址，并设置到邮件消息中
//        message.setRecipients(Message.RecipientType.TO, sendTo);  //发适多人

        message.setRecipient( Message.RecipientType.TO, new InternetAddress(receive));//发适单人


        // 消息发送的时间
        message.setSentDate(new Date());

        return message ;
    }


    //忽略此收件人地址，不向他发邮件
    public static boolean ignoreThisEmail(String receiver){
        receiver = receiver.toLowerCase();  //转成小写，因为在BlackEmailMap都是小写的keys
        if (EmailComm.BlackEmailMap.get(receiver) != null){
            return true;
        }else {
            return false;
        }
    }

}