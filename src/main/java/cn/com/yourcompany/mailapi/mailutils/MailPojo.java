package cn.com.yourcompany.mailapi.mailutils;

/**
 * Created with IntelliJ IDEA.
 * User: qeekey
 * Date: 2018/5/14
 * Time: 下午5:34
 * To change this template use File | Settings | File Templates.
 */
public class MailPojo {
    //邮箱服务器 如smtp.163.com
    private String host ;

    // 发送邮件的服务器的端口
    private String mailServerPort ;

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    //发件人
    private String sender ;

    //发件人名称
    private String senderName ;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    //用户授权码 不是用户名密码 可以自行查看相关邮件服务器怎么查看
    private String formPassword ;

    //消息回复邮箱
    private String replayAddress ;

    //接收人
    private String receiver ;

    //发送主题
    private String subject ;

    //发送内容
    private String content ;

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getFormPassword() {
        return formPassword;
    }
    public void setFormPassword(String formPassword) {
        this.formPassword = formPassword;
    }
    public String getReplayAddress() {
        return replayAddress;
    }
    public void setReplayAddress(String replayAddress) {
        this.replayAddress = replayAddress;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
