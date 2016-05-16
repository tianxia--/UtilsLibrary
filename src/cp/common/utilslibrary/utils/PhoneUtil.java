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
 * �绰������
 */
public class PhoneUtil {

	private static TelephonyManager mTelephonyManager;
	public static final String CHINA_MOBILE = "cm";//�ƶ�
	public static final String CHINA_UNICOM = "cu";//��ͨ
	public static final String CHINA_TELCOM = "ct";//����
	public static final String CHINA_OTHER = "other";
	public enum MNCType {
		ChinaMobile, ChinaUnicom, ChinaTelcom, UNKNOWN, OTHER
	}

	/**
	 * �õ���Ļ���
	 * @param context �����Ķ���
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
	 * �Ƿ���sim��
	 * @param context �����Ķ���
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
	 * �õ�imei
	 * @param appcontext �����Ķ���
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
	 * �õ�imsi
	 * @param context �����Ķ���
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
	 * �õ���Ϸ�汾��
	 * @param context �����Ķ���
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
	 * �õ���Ϸ�汾��
	 * @param context �����Ķ���
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
	 * �õ�MNC����
	 * @param pContext �����Ķ���
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
	 * �����ֻ���SimOperator���ж��ֻ�SIM��������
	 * @param simOperator �ֻ�������
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
	 * �õ��ֻ�������
	 * @param context �����Ķ���
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
	 * ��ȡ�ֻ���
	 * @param context �õ��ֻ���
	 * @return
	 */
	public static String getPhoneNumber(Context context){
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum = telManager.getLine1Number();
		return phoneNum;
	}
	
	/**
	 * Get the phone's identity. Format:IMEI+MAC
	 * @param pContext �����Ķ���
	 * @return
	 */
	public static String getPhoneIdentity(Context pContext) {

		TelephonyManager tManager = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);

		StringBuffer sbIdentity = new StringBuffer();

		sbIdentity.append(tManager.getDeviceId());

		return "";
	}
	
	/**
	 * ȫ��px
	 * @param context �����Ķ���
	 * @param pxValue pxֵ
	 * @return
	 */
	public static int globlePx(Context context,float pxValue){
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int)(pxValue*fontScale*2/3);
	}
	
	/**
	 * xpתsp
	 * @param context �����Ķ���
	 * @param pxValue pxֵ
	 * @return
	 */
	public static int px2sp(Context context,float pxValue){
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}
	
	/**
	 * spתpx
	 * @param context �����Ķ���
	 * @param spValue spֵ
	 * @return
	 */
	public static int sp2px(Context context,float spValue){
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int)(spValue * fontScale + 0.5f);
	}
	
	/**
	 * dipתpx
	 * @param context �����Ķ���
	 * @param dpValue dpֵ
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * pxתdip
	 * @param context �����Ķ���
	 * @param pxValue pxֵ
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * ��ȡ��γ����Ϣ 
	 * @param context  �����Ķ���
	 * @return ��γ��_���ȡ�
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
	 * ��ȡ��γ����Ϣ 
	 * @param context �����Ķ���
	 * @return ��γ��_���ȡ�
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
	 * ��ȡ��������WIFI��2G��3G��4G
	 * @param context �����Ķ���
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
	        } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE��3g��4g�Ĺ��ɣ���3.9G��ȫ���׼ 
	            type = "4g"; 
	        } 
	    } 
	    return type; 
	}
	
	/**
	 * ��ȡ�ֻ�ip��ַ
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
	 * ��ȡ������IP
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

				// ����ת��Ϊ�ַ���
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
