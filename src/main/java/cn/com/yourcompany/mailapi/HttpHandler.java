package cn.com.yourcompany.mailapi;
import cn.com.yourcompany.mailapi.mailutils.*;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;
import cn.com.yourcompany.mailapi.mailutils.DateUtils;
/**
 * Created with IntelliJ IDEA.
 * User: qeekey
 * Date: 2018/5/14
 * Time: 下午5:09
 * To change this template use File | Settings | File Templates.
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> { // 1
    private static final Log logger = LogFactory.getLog(HttpHandler.class);

    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String remoteAddr = insocket.getAddress().getHostAddress();
        MDC.put("ip", remoteAddr);  //for log4j

        String requestTime = DateUtils.DateToString(new Date(),  DateUtils.FORMAT_DAY);

        final String uri = request.uri();
        System.out.println(requestTime + " uri=" + uri);


        if(uri == null || uri.equalsIgnoreCase("/")) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }else if(uri.equalsIgnoreCase("/email")  || uri.equalsIgnoreCase("/alert") ) {

            Map<String, String> parmMap = new RequestParser(request).parse(); // 将GET, POST所有请求参数转换成Map对象
            String addresses    = parmMap.get("addresses");
            String subject      = parmMap.get("subject");
            String message      = parmMap.get("message");
            String text         = parmMap.get("text");
            String mailid       = parmMap.get("mailid");

            MailResult mailResult = null ;
            String production   = parmMap.get("production");
            if (production == null || production.length()==0){
                production = MailComm.senderName ;
            }


            //为了做点message的记录，截断一些字符输出
            String shortMessage = "";
            StringBuffer stringBuffer = new StringBuffer();
            if (message != null ){
                shortMessage = message ;
                if (message.length()>100){
                    shortMessage = message.substring(0,100);
                }
            }

            stringBuffer.append("mailid=").append(mailid).append("; production=").append(production).append("; addresses=").append(addresses).append("; subject=").append(subject).append("; shortMessage=").append(shortMessage).append("; text=").append(text);
            System.out.println(requestTime + " " + stringBuffer.toString());

            if (text == null){
                text = "0";
            }else {
                text = "1";
            }

            String result = "{code:-1,msg:Connection timed out or 503 Error}"; //初始化
            int time = 0 ;         //重新次数
            int MaxTime = 4 ;      //最多3次重试

            //地址必须传入且符合邮件格式
            if (addresses != null && addresses.length()>3 && addresses.indexOf("@") != -1 ){
                while (result.indexOf("-1") !=-1 && time<MaxTime && needRetrySend(result) ){
                    result = MailSendUtil.sendMail(addresses, subject, message, production ,text);
                    mailResult = (MailResult)JsonUtil.jsonStr2Bean(result, MailResult.class);
                    mailResult.setProduction("production=" + production + ":subject=" + subject + ":mailid=" +  mailid );

                    if (result.indexOf("-1") !=-1){
                        logger.error(JsonUtil.bean2JsonStr(mailResult));
                    }else {
                        logger.info(JsonUtil.bean2JsonStr(mailResult));
                    }

                    time ++;
                    Thread.sleep(1000 * time ); //重试间隔变长
                }
            }else {
                mailResult = new MailResult();
                mailResult.setCode(-1);
                mailResult.setMessage("email address error or missing.");
                mailResult.setProduction("production=" + production + ":subject=" + subject + ":mailid=" +  mailid );
                logger.error(JsonUtil.bean2JsonStr(mailResult));
            }

            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(JsonUtil.bean2JsonStr(mailResult).getBytes())); // 2

            HttpHeaders heads = response.headers();
            heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
            heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            ctx.write(response);
        }else {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
    }

    public boolean needRetrySend(String message){
        boolean ret = false;
        if (message.contains("503 Error") || message.contains("Connection timed out") ) {
            ret = true ;
        }
        return ret;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush(); // 4
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(null != cause) cause.printStackTrace();
        if(null != ctx) ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    private static void setContentTypeHeader(HttpResponse response, File file){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file.getPath()));
    }

}
