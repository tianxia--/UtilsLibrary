package cp.common.utilslibrary.resource;

import android.content.Context;
import android.content.res.Configuration;

public class ScreenUtils {
	public static boolean isScreenChange(Context context) {

		Configuration mConfiguration = context.getResources()
				.getConfiguration(); // 获取设置的配置信息
		int ori = mConfiguration.orientation; // 获取屏幕方向

		if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {

			// 横屏
			return true;
		} else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {

			// 竖屏
			return false;
		}
		return false;
	}
}
