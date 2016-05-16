package cp.common.utilslibrary.utils;

import java.io.File;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * 设备工具类
 */
public class DeviceUtil {
	
	/**
	 * 判断sdk版本号是否大于等于9
	 * @return
	 */
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= 9 ;//Build.VERSION_CODES.GINGERBREAD;
	}
	
	/**
	 * 得到可用空间
	 * @return 可用空间大小
	 */
    public static long getUsableSpace() {
    	long ret = -1 ;
    	try {
        	File path = Environment.getExternalStorageDirectory().getAbsoluteFile();
        	ret = getUsableSpace(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
       return ret ;
    }
    
    /**
     * 得到可用空间
     * @param path 文件路径
     * @return 该文件可用空间大小
     */
	private static long getUsableSpace(File path) {
		try {
			long ret = -1;
			if (hasGingerbread()) {
				return path.getUsableSpace();
			}
			final StatFs stats = new StatFs(path.getPath());
			ret = (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
			return ret;
		} catch (Exception e) {
			return -1 ;
		}
		
	}
    

}
