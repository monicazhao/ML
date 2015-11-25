package com.sboss.knowledge.base;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



/**
 * 上传文件或者流到服务器，返回文件路径
 * 
 * @author yuwj2@asiainfo.com
 *
 */
public class SocketHttpRequesterStr {
	/**
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     *   <FORM METHOD=POST ACTION="http://127.0.0.1:8080/fileup/fileupload" enctype="multipart/form-data">
            <INPUT TYPE="text" NAME="name">
            <INPUT TYPE="text" NAME="id">
            <input type="file" name="imagefile"/>
            <input type="file" name="zip"/>
         </FORM>
     * @param path 上传路径
     * @param params 请求参数 key为参数名,value为参数值
     * @param file 上传文件
     */
	//private String result ="";
    public static String post(String path, Map<String, String> params, String ins,HashMap<String, String> urlmap) throws Exception{     
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志
        String strline = null;
        String puburl = null;
        boolean flag = false;
        String result =null;
        
        int fileDataLength = 0;
        
        //String ss = null;
        //ss = convertStreamToString(ins);
        //for(FormFile uploadFile : files){//得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
             fileExplain.append("--");
             fileExplain.append(BOUNDARY);
             fileExplain.append("\r\n");
             //fileExplain.append("<form action = \"\" ENCTYPE=\"multipart/form-data\"></form>");
             fileExplain.append("Content-Disposition: form-data;name=\""+ params.get("form-data")+"\";filename=\""+ params.get("filename") + "\"\r\n");
             fileExplain.append("Content-Type: "+ params.get("ContentType")+"\r\n\r\n");
             fileExplain.append("\r\n");
             fileDataLength += fileExplain.length();

             //fileDataLength += ins.length()+6;
             fileDataLength += ins.getBytes().length;

        //}
        StringBuilder textEntity = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length;
        
        URL url = new URL(path);
        int port = url.getPort()==-1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        //socket.setSoTimeout(10);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        /*String froms ="<form action = \"\" ENCTYPE=\"multipart/form-data\"></form>";
        outStream.write(froms.getBytes());*/
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: "+ dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: "+ url.getHost() +":"+ port +"\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());           
        //把所有文件类型的实体数据发送出来
        //for(FormFile uploadFile : files){
            StringBuilder fileEntity = new StringBuilder();
             fileEntity.append("--");
             fileEntity.append(BOUNDARY);
             fileEntity.append("\r\n");
             fileEntity.append("Content-Disposition: form-data;name=\""+ params.get("form-data")+"\";filename=\""+ params.get("filename") + "\"\r\n");
             fileEntity.append("Content-Type: "+ params.get("ContentType")+"\r\n\r\n");
             outStream.write(fileEntity.toString().getBytes());

                 /*byte[] buffer = new byte[1024];
                 int len = 0;
                 while((len = files.read(0, 1024, buffer, 0))!=-1){
                     outStream.write(buffer, 0, len);
                 }*/
             //convertStreamToString(files);
             //System.out.println("ins:"+getWordCount(ins)+"inst.length:"+ins.length()+":"+ins+"ins.getBytes(UTF-8).length:"+ins.getBytes("UTF-8").length);
             //System.out.println(ins.getBytes());
             /*for (int i =0 ;i< getWordCount(ins);i++){
            	 System.out.println("i="+i+":"+ins.getBytes()[i]);
            	 
             }*/
             //outStream.write(ins.getBytes(),0,getWordCount(ins));
            //BufferedReader br = new BufferedReader(new StringReader(ins));

    		ByteArrayInputStream bis=new ByteArrayInputStream(ins.getBytes("utf-8"));
    		//BufferedReader br=new BufferedReader(new InputStreamReader(bis,"UTF-8"));
    		
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = bis.read(buffer))!=-1){
                //System.out.println("len:="+len);
            	outStream.write(buffer, 0, len);
            }
            //outStream.write(ins.getBytes("utf-8"));
            //bis.close();

     		/*String line="";
    		while ((line = br.readLine()) != null) {
    			System.out.println("line:"+line+" bytes:"+line.getBytes()+" : "+line.getBytes().length);
    			outStream.write(line.getBytes(),0,line.getBytes().length-0);
    			//outStream.write("\r\n".getBytes());// 补上换行符
    			//System.out.println(line);
    			//ss += "\r\n"; // 补上换行符
    		}
    		br.close();*/
             //outStream.write(ins.getBytes(),0,getWordCount(ins)-1);
             

            
    		outStream.write("\r\n".getBytes());// 补上换行符
        //}
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());
        //System.out.println("upload 2111:"+new Date());
        //socket.setSoTimeout(3000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //System.out.println("upload 2112:"+new Date());
        while ((strline=reader.readLine())!=null){
        	if(strline.indexOf("200")>=0){
        		flag = true;
        	}
        	if(strline.indexOf("puburl:")>=0){
        		puburl = strline.replaceAll("puburl:","");
        		//urlmap.put("puburl", strline.replaceAll("puburl:",""));
        		//System.out.println("puburl211111:"+puburl);
                //reader.close();
        		break;
        	}
        }
        urlmap.put("puburl",puburl);
        result =new String(urlmap.get("puburl"));
        //System.out.println("post:"+result);
        if(flag==false){//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
            outStream.flush();
            outStream.close();
            reader.close();
            socket.close();
        	return result;	
        }
        //System.out.println("upload 222:"+new Date()+urlmap.get("puburl"));
        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        return result;
    }
    
    public static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
      	 
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    public static int getWordCount(String s)
    {

        int length = 0;
        for(int i = 0; i < s.length(); i++)
        {
            int ascii = Character.codePointAt(s, i);
            if(ascii >= 0 && ascii <=255)
                length++;
            else
                length += 2;
                
        }
        return length;
        
    }
    
    /* upload string to server
     * @param str 要上传的字符串
     * @param urlmap 空
     * @param filename 保存的文件名，文件名随意取，其实只要后缀
     * */
	public static String  uploadStr(String str, HashMap<String, String> urlmap,
			String filename) throws Exception {
		String requestUrl = "http://171.221.254.231:9000/fileup/fileupload";
		Map<String, String> params = new HashMap<String, String>();
		params.put("filename", filename);
		params.put("form-data", "text");
		params.put("ContentType", "multipart/form-data");
		String res = "";
		res = post(requestUrl, params, str, urlmap);
		//System.out.println("res:"+res);
		return res;
	}

	/**
	 * 上传文件到服务器
	 * 
	 * @param imageFile 包含路径
	 * @throws Exception
	 */
	public static String uploadFile(File iFile, HashMap<String, String> urlmap)
			throws Exception {
		String requestUrl = "http://171.221.254.231:9000/fileup/fileupload";
		// 请求普通信息
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileName", iFile.getName());
		// 上传文件
		FormFile formfile = new FormFile(iFile.getName(), iFile, "image",
				"application/octet-stream");
		SocketHttpRequester.post(requestUrl, params, formfile, urlmap);
		return urlmap.get("puburl");
	}
    
}