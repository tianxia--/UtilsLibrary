package cp.common.utilslibrary.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.SyncStateContract.Constants;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 电话工具类
 */
public class PhoneUtil {

	private static TelephonyManager mTelephonyManager;
	public static final String CHINA_MOBILE = "cm";//移动
	public static final String CHINA_UNICOM = "cu";//联通
	public static final String CHINA_TELCOM = "ct";//电信
	public static final String CHINA_OTHER = "other";
	public enum MNCType {
		ChinaMobile, ChinaUnicom, ChinaTelcom, UNKNOWN, OTHER
	}

	/**
	 * 得到屏幕宽高
	 * @param context 上下文对象
	 * @return
	 */
	public static String getScreenWH(Context context) {
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//		WindowManager wm = (WindowManager) DKPlatform.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		String screenwh = dm.widthPixels + "_" + dm.heightPixels;

		return screenwh;

	}

	/**
	 * 是否有sim卡
	 * @param context 上下文对象
	 * @return
	 */
	public static boolean isSimInserted(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		int status = tm.getSimState();
		if ((status == TelephonyManager.SIM_STATE_UNKNOWN) || (status == TelephonyManager.SIM_STATE_ABSENT)) {
			return false;
		}
		return true;
	}

	/**
	 * 得到imei
	 * @param appcontext 上下文对象
	 * @return
	 */
	public static String getIMEI(Context appcontext) {

		if(appcontext == null){
			return "";
		}
		if (mTelephonyManager == null) {
			mTelephonyManager = (TelephonyManager) appcontext.getSystemService(Context.TELEPHONY_SERVICE);
		}
		String res = mTelephonyManager.getDeviceId();
		if (res != null && res.length() > 0) {
			return res;
		}
		return "";
	}
	
	// 2014-02-25 add start (wounipay)
	/**
	 * 得到imsi
	 * @param context 上下文对象
	 * @return
	 */
	public static String getIMSI(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		return imsi == null ? "" : imsi;
	}
	// 2014-02-25 add end 
	
	public static String getAppVersionName() {
		return "1.0.0";
	}

	/**
	 * 得到游戏版本号
	 * @param context 上下文对象
	 * @return
	 */
	public static String getGameVersion(Context context) {
		String version = "";
		PackageManager manager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packInfo != null)
			version = packInfo.versionName;
		if(null == version || "".equals(version))
			version = packInfo.versionCode + "";
		return version;
	}
	
	/**
	 * 得到游戏版本号
	 * @param context 上下文对象
	 * @return
	 */
	public static String getGameVersionCode(Context context) {
		String version = "";
		PackageManager manager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packInfo != null)
			version = packInfo.versionCode + "";
		if(null == version || "".equals(version))
			version = packInfo.versionName;
		return version;
	}

	/**
	 * 得到MNC类型
	 * @param pContext 上下文对象
	 * @return
	 */
	public static synchronized MNCType getMNCType(Context pContext) {
		TelephonyManager tempTelephonyManager = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
		String strOperator = tempTelephonyManager.getSimOperator().trim();
		if (!isSimInserted(pContext)) {
			return MNCType.UNKNOWN;
		}
		if (strOperator.endsWith("00") || strOperator.endsWith("02") || strOperator.endsWith("07")) {
			return MNCType.ChinaMobile;
		} else if (strOperator.endsWith("01")) {
			return MNCType.ChinaUnicom;
		} else if (strOperator.endsWith("03") || strOperator.endsWith("99") || strOperator.endsWith("20404")) {
			return MNCType.ChinaTelcom;
		} else {
			return MNCType.UNKNOWN;
		}
	}

	/**
	 * 根据手机的SimOperator来判断手机SIM卡的类型
	 * @param simOperator 手机卡类型
	 * @return
	 */
	public static synchronized MNCType getMNCType(int simOperator) {
		String strOperator = String.valueOf(simOperator);

		if (strOperator.endsWith("00") || strOperator.endsWith("02") || strOperator.endsWith("07")) {
			return MNCType.ChinaMobile;
		} else if (strOperator.endsWith("01")) {
			return MNCType.ChinaUnicom;
		} else if (strOperator.endsWith("03")) {
			return MNCType.ChinaTelcom;
		} else {
			return MNCType.UNKNOWN;
		}
	}

	/**
	 * 得到手机卡类型
	 * @param context 上下文对象
	 * @return
	 */
	public static synchronized String getDKSIMOperator(Context context){
		String strOperator = "";
		MNCType type = getMNCType(context);
		if(MNCType.ChinaMobile == type){
			strOperator = CHINA_MOBILE;
		} else if(MNCType.ChinaUnicom == type){
			strOperator = CHINA_UNICOM;
		} else if(MNCType.ChinaTelcom == type){
			strOperator = CHINA_TELCOM;
		}
		return strOperator;
	}
	
	/**
	 * 获取手机号
	 * @param context 得到手机号
	 * @return
	 */
	public static String getPhoneNumber(Context context){
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum = telManager.getLine1Number();
		return phoneNum;
	}
	
	/**
	 * Get the phone's identity. Format:IMEI+MAC
	 * @param pContext 上下文对象
	 * @return
	 */
	public static String getPhoneIdentity(Context pContext) {

		TelephonyManager tManager = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);

		StringBuffer sbIdentity = new StringBuffer();

		sbIdentity.append(tManager.getDeviceId());

		return "";
	}
	
	/**
	 * 全局px
	 * @param context 上下文对象
	 * @param pxValue px值
	 * @return
	 */
	public static int globlePx(Context context,float pxValue){
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int)(pxValue*fontScale*2/3);
	}
	
	/**
	 * xp转sp
	 * @param context 上下文对象
	 * @param pxValue px值
	 * @return
	 */
	public static int px2sp(Context context,float pxValue){
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}
	
	/**
	 * sp转px
	 * @param context 上下文对象
	 * @param spValue sp值
	 * @return
	 */
	public static int sp2px(Context context,float spValue){
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int)(spValue * fontScale + 0.5f);
	}
	
	/**
	 * dip转px
	 * @param context 上下文对象
	 * @param dpValue dp值
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转dip
	 * @param context 上下文对象
	 * @param pxValue px值
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 获取经纬度信息 
	 * @param context  上下文对象
	 * @return “纬度_经度”
	 */
	public static String getLBSInfo(Context context){
		String res = "";
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager!=null){
			try{
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if(location != null){
					res = Double.toString(location.getLatitude())+"_"+Double.toString(location.getLongitude());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if("".equals(res)){
			res = getCourseLBSInfo(context);
		}
		return res;
	}
	
	/**
	 * 获取经纬度信息 
	 * @param context 上下文对象
	 * @return “纬度_经度”
	 */
	public static String getCourseLBSInfo(Context context){
		String res = "";
		LocationManager location_manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if(location_manager!=null){
			try{
				Location location = location_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if(location != null){
					res = Double.toString(location.getLatitude())+"_"+Double.toString(location.getLongitude());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return res;
	}
	
	/**
	 * 获取网络类型WIFI、2G、3G、4G
	 * @param context 上下文对象
	 * @return
	 */
	public static String getCurrentNetType(Context context) { 
	    String type = ""; 
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
	    NetworkInfo info = cm.getActiveNetworkInfo(); 
	    if (info == null) { 
	        type = "null"; 
	    } else if (info.getType() == ConnectivityManager.TYPE_WIFI) { 
	        type = "wifi";
	    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) { 
	        int subType = info.getSubtype(); 
	        if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS 
	                || subType == TelephonyManager.NETWORK_TYPE_EDGE) { 
	            type = "2g"; 
	        } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA 
	                || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0 
	                || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) { 
	            type = "3g"; 
	        } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准 
	            type = "4g"; 
	        } 
	    } 
	    return type; 
	}
	
	/**
	 * 获取手机ip地址
	 * @return
	 */ 
	public static String getPhoneIp() { 
	    try { 
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) { 
	            NetworkInterface intf = en.nextElement(); 
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) { 
	                InetAddress inetAddress = enumIpAddr.nextElement(); 
	                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) { 
	                    // if (!inetAddress.isLoopbackAddress() && inetAddress 
	                    // instanceof Inet6Address) { 
	                    return inetAddress.getHostAddress().toString(); 
	                } 
	            } 
	        } 
	    } catch (Exception e) { 
	    } 
	    return ""; 
	}
	
	/**
	 * 获取外网的IP
	 * @return
	 */
	public static String getNetIp(){
		String IP = "";
		try {
			String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setUseCaches(false);

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();

				// 将流转化为字符串
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));

				String tmpString = "";
				StringBuilder retJSON = new StringBuilder();
				while ((tmpString = reader.readLine()) != null) {
					retJSON.append(tmpString + "\n");
				}

				JSONObject jsonObject = new JSONObject(retJSON.toString());
				String code = jsonObject.getString("code");
				if (code.equals("0")) {
					JSONObject data = jsonObject.getJSONObject("data");
					IP = data.getString("ip");

				} else {
					IP = "";
				}
			} else {
				IP = "";
			}
		} catch (Exception e) {
			IP = "";
		}
		return IP;
	}
}
