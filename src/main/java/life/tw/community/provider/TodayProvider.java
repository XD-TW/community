package life.tw.community.provider;


import com.alibaba.fastjson.JSON;
import life.tw.community.dto.TodayDTO;
import org.springframework.stereotype.Component;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.BufferedReader;
import net.sf.json.JSONObject;



/**
 *历史上的今天调用示例代码 － 聚合数据
 *在线接口文档：http://www.juhe.cn/docs/63
 **/
@Component
public class TodayProvider {

    private static final String DEF_CHARSET = "UTF-8";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;
    private static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    private static final String APP_KEY ="de9465aa6846910c409d05511e46e67c";

    //1.事件列表
    //获得 历史上的今天 发生的事件
    public static TodayDTO getHistoryOnToday(){
        String result =null;
        String url ="http://api.juheapi.com/japi/toh";//请求接口地址
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String[] split = simpleDateFormat.format(new Date().getTime()).split("-");
        Map<String, Object> params = new HashMap<>();//请求参数
        params.put("key",APP_KEY);//应用APP_KEY(应用详细页查询)
        params.put("v","1.0");//版本，当前：1.0
        params.put("month",split[0]);//月份，如：10
        params.put("day",split[1]);//日，如：1

        try {
            result =net(url, params);
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                System.out.println();
                List<TodayDTO> todayDTOS = JSON.parseArray(object.get("result").toString(), TodayDTO.class);
                return today(todayDTOS);
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//        //2.根据ID查询事件详情
//        public static void getRequest2(){
//            String result =null;
//            String url ="http://api.juheapi.com/japi/tohdet";//请求接口地址
//            Map params = new HashMap();//请求参数
//            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
//            params.put("v","");//版本，当前：1.0
//            params.put("id","");//事件ID
//
//            try {
//                result =net(url, params, "GET");
//                JSONObject object = JSONObject.fromObject(result);
//                if(object.getInt("error_code")==0){
//                    System.out.println(object.get("result"));
//                }else{
//                    System.out.println(object.get("error_code")+":"+object.get("reason"));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }



    public static void main(String[] args) {
        getHistoryOnToday();
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @return  网络请求字符串
     * @throws Exception
     */
    private static String net(String strUrl, Map<String, Object> params) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if("GET" ==null || "GET".equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if("GET" ==null || "GET".equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && "GET".equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHARSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    private static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static TodayDTO today( List<TodayDTO> todayDTOS){
        TodayDTO todayDTO = null;
        Integer random = todayDTOS.size();
        Random r = new Random();
        random = r.nextInt(random);
        for (int i = 0; i < todayDTOS.size(); i++) {
            if(random.equals(i)){
                todayDTO = todayDTOS.get(i);
            }
        }
        return todayDTO;
    }
}

