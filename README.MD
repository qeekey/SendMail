#内部邮件服务

###邮件服务    
此邮件服务使用的发件人为：noreply@yourcompany.com.cn

#####Html邮件服务接口：     

    curl -X POST http://mail.yourbusiness.infra/email -d 'addresses=someone@yourcompany.com.cn&subject=title&message=<html>内空正文</html>&production=产品线名称&mailid=324343243433249343'
    或（兼容原来的程序）
    curl -X POST http://mail.yourbusiness.infra/alert -d 'addresses=someone@yourcompany.com.cn&subject=title&message=内空正文&production=产品线名称&mailid=324343243433249343'
    返回值：
    全部发送成功：{"code":0,"message":"someone@yourcompany.com.cn OK;","production":"BusinesNane"}
    或   
    部分发送成功：{"code":-1,"message":"someone3@yourcompany.com.cn javax.mail.SendFailedException: Invalid Addresses;  nested exception is:\tcom.sun.mail.smtp.SMTPAddressFailedException: 550 Mailbox not found or access denied;","production":"BusinesNane"} 
    
#####普通文本邮件服务接口：  
    增加一个text=1的参数
    
    curl -X POST http://xxx.xxxxx.xxx/email -d 'addresses=xxxx@yourcompany.com.cn&subject=title&message=<html>内空正文</html>&production=产品线名称&text=1&mailid=324343243433249343'
    或（兼容原来的程序）
    curl -X POST http://xxx.xxxx.xxxx/alert -d 'addresses=xxxx@yourcompany.com.cn&subject=title&message=内空正文&production=产品线名称&text=1&mailid=324343243433249343'
    
其中addresses支持多个收件人，用英文逗号分割      
其中mailid为此次调用邮件服务的id,可定义为与自己业务相关的ID信息，用于排查问题；   

其中productin, mailid选填，强烈要求填写。它的作用有：    
1、记录日志时会记录产品线名称，方便排查问题    
2、用作发件人显示出来的名称,这个功能如果开放感觉不太好；现在发件人名称都显示为“邮件通知系统”  


#####注意事项： 
    1、发送html邮件需要按照完整的html格式发送；
         开头加上<html> , 结果加上</html>
         如果不加，会导致正文发送不完整。

    2、因为会涉及到一些特殊字符，所以需要url encode后发送，否则发送会失败
         java demo: URLEncoder.encode(你的html邮件正文, "utf-8")
         
    3、在用shell发送正文时，要保证内容是一行的文本
         多行数据发送，后面加上xargs，变成一行 
         shell deml: context=`cat aa.log|xargs` by tanchangwei  
         
    4、非CURL的调用方式（未验证）       
    
    5、返回值的判断
    调用者应该对返回值进行一下解析，如果失败，考虑重发机制；
    目前发现有两种异常返回值：   
     
    {"code":-1,"message":"xxxxx@yourcompany.com.cn javax.mail.SendFailedException: Invalid Addresses;  nested exception is:\tcom.sun.mail.smtp.SMTPAddressFailedException: 550 Mailbox not found or access denied;","production":"production=Site_job_mail\t:subject=SiteMaster 数据任务结果通知:mailid=null"}
    这个结果代表email不存在了，发送失败；
    
    {"code":-1,"message":"xxxxx@yourcompany.com.cn com.sun.mail.smtp.SMTPSendFailedException: 550 Connection frequency limited;","production":"production=TrackFrontend-query:subject=BusinesNane['407521']: '12月HS_M18-44 1+1--收入' 计算完毕:mailid=null"}
    这个结果代表这个email发送频繁，发送失败；
    
    6、腾讯550 Connection frequency limited出错解释
        出错原因：该服务器IP的发信频率超过腾讯邮箱限制。
        
        腾讯邮箱对来自相同IP的外部发信服务器有一定的频率限制：
        1、超过每分钟发信量限制，此IP地址被禁止发信若干分钟。
        2、超过每小时发信量限制，此IP地址被禁止发信若干小时。
        3、超过每日发信量限制，此IP地址本日内禁止再发信。
        4、以上频率限制数值属于腾讯邮箱保密数据，恕不公开。
 
        改善建议：如果您是该服务器IP的管理员，请暂停该服务器IP的发信，稍后降低频率重新尝试发信。
                如果您是个人邮箱用户，请向您的电子邮件提供商报告此情况。