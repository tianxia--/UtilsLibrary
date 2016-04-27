package cp.common.utilslibrary.resource;

import android.content.Context;
import android.content.res.Configuration;

public class ScreenUtils {
	public static boolean isScreenChange(Context context) {

		Configuration mConfiguration = context.getResources()
				.getConfiguration(); // ��ȡ���õ�������Ϣ
		int ori = mConfiguration.orientation; // ��ȡ��Ļ����

		if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {

			// ����
			return true;
		} else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {

			// ����
			return false;
		}
		return false;
	}
}
