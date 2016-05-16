package cp.common.utilslibrary.utils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * app工具类
 */
public class AppUtil {
	
    
    /** 16进制数组 */
    static final char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
    /**
     * 将字节数组转换为16进制字符串
     * @param byt 要转换的字节
     * @return 字符串
     */
    public static String toHexString(byte[] byt) {
        StringBuilder sb = new StringBuilder(byt.length * 2);
        for (int i = 0; i < byt.length; i++) {
            sb.append(HEXCHAR[(byt[i] & 0xf0) >>> 4]);// SUPPRESS CHECKSTYLE : magic number
            sb.append(HEXCHAR[byt[i] & 0x0f]);// SUPPRESS CHECKSTYLE : magic number
        }
        return sb.toString();
    }
    
    /**
	 * 得到所有的app的集合信息
	 * @param context 上下文对象
	 * @return the 所有的app的集合信息
	 */
	@SuppressLint("NewApi")
	public static List<GPNativeApkInfo> getInstalledApps(Context context) {
		List<GPNativeApkInfo> list = new ArrayList<GPNativeApkInfo>();
		if (null == context) {
			return list;
		}
		try {
			List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(PackageManager.GET_SIGNATURES);
			for (int i = 0; i < packageInfoList.size(); i++) {
				GPNativeApkInfo apkInfo = new GPNativeApkInfo();
				PackageInfo pi = packageInfoList.get(i);
				apkInfo.setPackageName(pi.packageName);//获取apk包名
				apkInfo.setMd5(FileHelper.getFileMd5(context.getPackageCodePath()));//获取apk md5指纹
				list.add(apkInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 得到所有安装app的包名集合信息
	 * @param context 上下文对象
	 * @return 所有安装app的包名集合信息
	 */
	public static List<String> getInstalledPackageNames(Context context) {
		List<String> list = new ArrayList<String>();
		if (null == context) {
			return list;
		}
		try {
			List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(0);
			for (int i = 0; i < packageInfoList.size(); i++) {
				PackageInfo pi = packageInfoList.get(i);
				list.add(pi.packageName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 得到指定的安装包的所有包名信息
	 * @param context 上下文对象
	 * @return 指定的安装包的所有包名信息
	 */
	public static List<String> getInstalledPackageNames(List<GPNativeApkInfo> GList) {
		List<String> list = new ArrayList<String>();
		for (GPNativeApkInfo gA : GList) {
			list.add(gA.getPackageName());
		}
		return list;
	}
	

}
