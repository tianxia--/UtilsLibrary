package cp.common.utilslibrary.utils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * app������
 */
public class AppUtil {
	
    
    /** 16�������� */
    static final char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
    /**
     * ���ֽ�����ת��Ϊ16�����ַ���
     * @param byt Ҫת�����ֽ�
     * @return �ַ���
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
	 * �õ����е�app�ļ�����Ϣ
	 * @param context �����Ķ���
	 * @return the ���е�app�ļ�����Ϣ
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
				apkInfo.setPackageName(pi.packageName);//��ȡapk����
				apkInfo.setMd5(FileHelper.getFileMd5(context.getPackageCodePath()));//��ȡapk md5ָ��
				list.add(apkInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * �õ����а�װapp�İ���������Ϣ
	 * @param context �����Ķ���
	 * @return ���а�װapp�İ���������Ϣ
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
	 * �õ�ָ���İ�װ�������а�����Ϣ
	 * @param context �����Ķ���
	 * @return ָ���İ�װ�������а�����Ϣ
	 */
	public static List<String> getInstalledPackageNames(List<GPNativeApkInfo> GList) {
		List<String> list = new ArrayList<String>();
		for (GPNativeApkInfo gA : GList) {
			list.add(gA.getPackageName());
		}
		return list;
	}
	

}
