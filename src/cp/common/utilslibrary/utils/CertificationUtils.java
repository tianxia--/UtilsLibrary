package cp.common.utilslibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.PatternMatcher;

public class CertificationUtils {
	
	/**
	 *判断是都是闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year){
		if ((year % 4 == 0 && !((year % 100) == 0)) ||year % 400 == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是不是手机号，规则添加了现在还没有出来的手机号开头的号码，如11,12等等
	 * @param number
	 * @return
	 */
	public static boolean isPhoneNumber(String number){
		String regulation = "^((11[0-9])|(12[0-9])|(13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9])|)//d{8}$";
		Pattern pattern = Pattern.compile(regulation);
		Matcher matcher = pattern.matcher(number);
		if (matcher.matches()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断身份证格式是否正确
	 * @param card
	 * @return
	 */
	public static boolean isIdentityCard (String card){
		String regyulation = "(^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$)|(^(\\d{6})(\\d{2})(\\d{2})(\\d{2})(\\d{3})$)";
		Pattern pattern = Pattern.compile(regyulation);
		Matcher matcher = pattern.matcher(card);
		int[] weight={7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
		char[] validate={ '1','0','X','9','8','7','6','5','4','3','2'};    //mod11,对应校验码字符值 
		String [] cards = card.trim().split("");
		String code ;
		if (matcher.matches()) {
			if (card.length() == 15) {
				return true;
			}
			int sum  = 0;
			int result;
			String check_code = cards[cards.length - 1];
			for (int i = 0; i < cards.length - 2; i++) {
				sum = sum + Integer.parseInt(cards[i + 1]) * weight[i];
			}
			
			result = sum % 11;
			code = validate[result]+"";
			
			if (check_code .equals(code)) {
				return true;
			}
		}
		
		return false;
	}
}
