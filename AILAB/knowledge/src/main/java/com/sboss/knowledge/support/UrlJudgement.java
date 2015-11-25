package com.sboss.knowledge.support;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlJudgement {
	public boolean isURL(String str) {
		str = str.toLowerCase();
		String regex = "^((https|http|ftp|rtsp|mms)?://)"
				+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
				+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
				+ "|" // 允许IP和DOMAIN（域名）
				+ "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
				+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
				+ "[a-z]{2,6})" // first level domain- .com or .museum
				+ "(:[0-9]{1,4})?" // 端口- :80
				+ "((/?)|" // a slash isn't required if there is no file name
				+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher isUrl = pattern.matcher(str);
		return isUrl.matches();
	}

	/*public static void main(String[] args) {
		UrlEffectiveJudgement uej = new UrlEffectiveJudgement();
		UrlJudgement urlj = new UrlJudgement();
		URL url;
		try {
			url = new URL("http://www.uestc.edu");
			System.out.println(uej.isEffective(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println(urlj.isURL("http://www.uestc.edu"));
	}*/
}
