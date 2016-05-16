package cp.common.utilslibrary.utils;

import java.io.File;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * �豸������
 */
public class DeviceUtil {
	
	/**
	 * �ж�sdk�汾���Ƿ���ڵ���9
	 * @return
	 */
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= 9 ;//Build.VERSION_CODES.GINGERBREAD;
	}
	
	/**
	 * �õ����ÿռ�
	 * @return ���ÿռ��С
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
     * �õ����ÿռ�
     * @param path �ļ�·��
     * @return ���ļ����ÿռ��С
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
