package cn.com.yourcompany.mailapi.comm;

import cn.com.yourcompany.mailapi.mailutils.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: qeekey
 * Date: 2018/5/25
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
public class BlackEmailThread extends java.util.TimerTask{

    @Override
    public void run() {
        loadData("conf/blackemaillist.csv");
    }

    public  void loadData(String fileName) {
        try {

            EmailComm.BlackEmailMap.clear();

            InputStream is = (this.getClass().getClassLoader().getResourceAsStream(fileName));
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String loadIgnoreEmailFileTime = DateUtils.DateToString(new Date(),  DateUtils.FORMAT_DAY);

            String inner_email;
            while ((inner_email = in.readLine()) != null) {
                inner_email = inner_email.toLowerCase();
                System.out.println(loadIgnoreEmailFileTime + " inner_email=" + inner_email );
                if (inner_email != null && inner_email.length()>3){
                    EmailComm.BlackEmailMap.put(inner_email , "ignore");
                }
            }

            FileInputStream outIs = new FileInputStream(fileName);
            BufferedReader bufferedReader_outer = new BufferedReader(new InputStreamReader(outIs, "UTF-8"));

            String outer_email;
            while ((outer_email = bufferedReader_outer.readLine()) != null) {
                outer_email = outer_email.toLowerCase();
                System.out.println(loadIgnoreEmailFileTime + " outer_email=" + outer_email );
                if (outer_email != null && outer_email.length()>3){
                    EmailComm.BlackEmailMap.put(outer_email,"ignore");
                }
            }

            System.out.println("EmailComm.BlackEmailMap=" + EmailComm.BlackEmailMap.size() );

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


//    public  void loadOuterData(String fileName) {
//        try {
//            FileInputStream outIs = new FileInputStream(fileName);
//            InputStream inIs = BlackEmailThread.class.getClassLoader().getResourceAsStream(fileName);
//            Properties outPro = new Properties();
//            outPro.load(outIs);
//            Properties inPro = new Properties();
//            inPro.load(inIs);
//            if(StringUtils.isNotEmpty((String) outPro.get("name"))){
//                System.out.println(outPro.get("name"));
//            }
//            System.out.println("name:"+outPro.get("name")+" age:"+outPro.getProperty("age"));
//            System.out.println("name:"+inPro.get("name")+" age:"+inPro.getProperty("age"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
