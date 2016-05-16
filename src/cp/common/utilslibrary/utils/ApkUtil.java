package cp.common.utilslibrary.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import cp.common.utilslibrary.utils.ShellUtils.CommandResult;

/**
 * apk工具类
 */
public class ApkUtil {
	
	private static final String TAG = "ApkUtil";
	public static final int INSTALL_FAILED_PERMISSION = -100;

	/**
	 * Installation return code<br/>
	 * install success.
	 */
	public static final int INSTALL_SUCCEEDED = 1;
	/**
	 * Installation return code<br/>
	 * the package is already installed.
	 */
	public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;

	/**
	 * Installation return code<br/>
	 * the package archive file is invalid.
	 */
	public static final int INSTALL_FAILED_INVALID_APK = -2;

	/**
	 * Installation return code<br/>
	 * the URI passed in is invalid.
	 */
	public static final int INSTALL_FAILED_INVALID_URI = -3;

	/**
	 * Installation return code<br/>
	 * the package manager service found that the device didn't have enough
	 * storage space to install the app.
	 */
	public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;

	/**
	 * Installation return code<br/>
	 * a package is already installed with the same name.
	 */
	public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;

	/**
	 * Installation return code<br/>
	 * the requested shared user does not exist.
	 */
	public static final int INSTALL_FAILED_NO_SHARED_USER = -6;

	/**
	 * Installation return code<br/>
	 * a previously installed package of the same name has a different signature
	 * than the new package (and the old package's data was not removed).
	 */
	public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;

	/**
	 * Installation return code<br/>
	 * the new package is requested a shared user which is already installed on
	 * the device and does not have matching signature.
	 */
	public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;

	/**
	 * Installation return code<br/>
	 * the new package uses a shared library that is not available.
	 */
	public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;

	/**
	 * Installation return code<br/>
	 * the new package uses a shared library that is not available.
	 */
	public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;

	/**
	 * Installation return code<br/>
	 * the new package failed while optimizing and validating its dex files,
	 * either because there was not enough storage or the validation failed.
	 */
	public static final int INSTALL_FAILED_DEXOPT = -11;

	/**
	 * Installation return code<br/>
	 * the new package failed because the current SDK version is older than that
	 * required by the package.
	 */
	public static final int INSTALL_FAILED_OLDER_SDK = -12;

	/**
	 * Installation return code<br/>
	 * the new package failed because it contains a content provider with the
	 * same authority as a provider already installed in the system.
	 */
	public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;

	/**
	 * Installation return code<br/>
	 * the new package failed because the current SDK version is newer than that
	 * required by the package.
	 */
	public static final int INSTALL_FAILED_NEWER_SDK = -14;

	/**
	 * Installation return code<br/>
	 * the new package failed because it has specified that it is a test-only
	 * package and the caller has not supplied
	 */
	public static final int INSTALL_FAILED_TEST_ONLY = -15;

	/**
	 * Installation return code<br/>
	 * the package being installed contains native code, but none that is
	 * compatible with the the device's CPU_ABI.
	 */
	public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;

	/**
	 * Installation return code<br/>
	 * the new package uses a feature that is not available.
	 */
	public static final int INSTALL_FAILED_MISSING_FEATURE = -17;

	/**
	 * Installation return code<br/>
	 * a secure container mount point couldn't be accessed on external media.
	 */
	public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;

	/**
	 * Installation return code<br/>
	 * the new package couldn't be installed in the specified install location.
	 */
	public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;

	/**
	 * Installation return code<br/>
	 * the new package couldn't be installed in the specified install location
	 * because the media is not available.
	 */
	public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;

	/**
	 * Installation return code<br/>
	 * the new package couldn't be installed because the verification timed out.
	 */
	public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;

	/**
	 * Installation return code<br/>
	 * the new package couldn't be installed because the verification did not
	 * succeed.
	 */
	public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;

	/**
	 * Installation return code<br/>
	 * the package changed from what the calling program expected.
	 */
	public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;

	/**
	 * Installation return code<br/>
	 * the new package is assigned a different UID than it previously held.
	 */
	public static final int INSTALL_FAILED_UID_CHANGED = -24;

	/**
	 * Installation return code<br/>
	 * other reason
	 */
	public static final int INSTALL_FAILED_OTHER = -1000000;

	/**
	 * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
	 * @param pkgCodePath 包路径
	 * @return 应用程序是/否获取Root权限
	 */
	public static boolean upgradeRootPermission(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // 切换到root帐号
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 检查一个APK文件是否是可用的APK。
	 * @param path apk file path
	 * @param context context
	 * @return true文件有效，false文件无效
	 */
	public static boolean isAPKFileValid(String path, Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageArchiveInfo(path, 0);
			return pi != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * shell命令枚举
	 */
	static enum SHELL_CMD {
		CHECK_SU_BINARY(new String[] { "/system/xbin/which", "su" }), ;
		String[] command;
		SHELL_CMD(String[] command) {
			this.command = command;
		}
	}

	/**
	 * 执行shell命令
	 */
	static class ExecShell {
		private static String LOG_TAG = ExecShell.class.getName();
		// ###此方法superuesr不会提示用户###
		public ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
			String line = null;
			ArrayList<String> fullResponse = new ArrayList<String>();
			Process localProcess = null;
			try {
				localProcess = Runtime.getRuntime().exec(shellCmd.command);
			} catch (Exception e) {
				return null;
			}
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
			try {
				while ((line = in.readLine()) != null) {
					Log.d(TAG, shellCmd + "--> Line received: " + line);
					fullResponse.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.d(TAG, shellCmd + "--> Full response was: " + fullResponse);
			return fullResponse;
		}
	}

	/**
	 * 静默安装
	 * @param context 上下文对象
	 * @param filePath 文件路径
	 * @return
	 */
	public static int installSilent(Context context, String filePath) {
		try {
			if (filePath == null || filePath.length() == 0) {
				return INSTALL_FAILED_INVALID_URI;
			}
			File file = new File(filePath);
			if (file == null || file.length() <= 0 || !file.exists()
					|| !file.isFile()) {
				return INSTALL_FAILED_INVALID_URI;
			}
			/**
			 * if context is system app, don,t need root permission, but should
			 * add <uses-permission
			 * android:name="android.permission.INSTALL_PACKAGES" /> in mainfest
			 **/
			StringBuilder command = new StringBuilder().append("pm install -r ").append(filePath.replace(" ", "\\ "));
			CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApplication(context), true);
			if (commandResult.successMsg != null && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
				return INSTALL_SUCCEEDED;
			}
			if (commandResult.errorMsg != null) {
				if (commandResult.errorMsg.contains("denied") || commandResult.errorMsg.contains("Denied")) {
					return INSTALL_FAILED_PERMISSION;
				}
				if (commandResult.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
					return INSTALL_FAILED_ALREADY_EXISTS;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
					return INSTALL_FAILED_INVALID_APK;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
					return INSTALL_FAILED_INVALID_URI;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
					return INSTALL_FAILED_INSUFFICIENT_STORAGE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
					return INSTALL_FAILED_DUPLICATE_PACKAGE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
					return INSTALL_FAILED_NO_SHARED_USER;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
					return INSTALL_FAILED_UPDATE_INCOMPATIBLE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
					return INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
					return INSTALL_FAILED_MISSING_SHARED_LIBRARY;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
					return INSTALL_FAILED_REPLACE_COULDNT_DELETE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
					return INSTALL_FAILED_DEXOPT;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
					return INSTALL_FAILED_OLDER_SDK;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
					return INSTALL_FAILED_CONFLICTING_PROVIDER;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
					return INSTALL_FAILED_NEWER_SDK;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
					return INSTALL_FAILED_TEST_ONLY;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
					return INSTALL_FAILED_CPU_ABI_INCOMPATIBLE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
					return INSTALL_FAILED_MISSING_FEATURE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
					return INSTALL_FAILED_CONTAINER_ERROR;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
					return INSTALL_FAILED_INVALID_INSTALL_LOCATION;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
					return INSTALL_FAILED_MEDIA_UNAVAILABLE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
					return INSTALL_FAILED_VERIFICATION_TIMEOUT;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
					return INSTALL_FAILED_VERIFICATION_FAILURE;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
					return INSTALL_FAILED_PACKAGE_CHANGED;
				} else if (commandResult.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
					return INSTALL_FAILED_UID_CHANGED;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INSTALL_FAILED_OTHER;
	}

	/**
	 * 是否系统应用
	 * @param context 上下文对象
	 * @return
	 */
	public static boolean isSystemApplication(Context context) {
		if (context == null) {
			return false;
		}
		return isSystemApplication(context, context.getPackageName());
	}

	/**
	 * 是否系统应用
	 * @param context 上下文对象
	 * @param packageName 包名
	 * @return
	 */
	public static boolean isSystemApplication(Context context, String packageName) {
		if (context == null) {
			return false;
		}
		return isSystemApplication(context.getPackageManager(), packageName);
	}

	/**
	 * 是否系统应用
	 * @param packageManager 包管理器
	 * @param packageName 包名
	 * @return <ul>
	 *         <li>if packageManager is null, return false</li>
	 *         <li>if package name is null or is empty, return false</li>
	 *         <li>if package name not exit, return false</li>
	 *         <li>if package name exit, but not system app, return false</li>
	 *         <li>else return true</li>
	 *         </ul>
	 */
	public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
		if (packageManager == null || packageName == null || packageName.length() == 0) {
			return false;
		}
		try {
			ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
			return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	interface RootRequestCallBack {
		void onFinished(boolean sucessful);
	}

	/**
	 * 在新线程中请求root权限。
	 * @param context 上下文对象
	 * @param callback 请求回调函数
	 */
	public static void requestRootPrivilege(final Context context, final RootRequestCallBack callback) {
		new Thread() {
			@Override
			public void run() {
				boolean requestRoot = requestRoot();
				if (callback != null) {
					callback.onFinished(requestRoot);
				}
			}
		}.start();
	}

	/**
	 * 是否请求了root权限
	 * @return
	 */
	private static boolean requestRoot() {
		boolean isRootGained = false;
		java.lang.Process process = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			process = Runtime.getRuntime().exec("su");// //"request su";
			OutputStream os = process.getOutputStream();
			InputStream is = process.getInputStream();
			if (os != null && is != null) {
				dos = new DataOutputStream(process.getOutputStream());
				dis = new DataInputStream(process.getInputStream());
				dos.writeBytes("id\n");
				dos.flush();// request flush
				/**
				 * mi-one的结果： uid=0(root) gid=0(root)
				 * groups=1003(graphics),1004(input),1007(log),1009(mount),
				 * 1011(
				 * adb),1015(sdcard_rw),3001(net_bt_admin),3002(net_bt),3003
				 * (inet)
				 */
				String result = dis.readLine();// request readLine
				boolean needExit = false;
				if (result == null) {
					needExit = false;
					isRootGained = false;
				} else if (result.toLowerCase().contains("uid=0")) {
					isRootGained = true;
					needExit = true;
				} else {
					isRootGained = false;
					needExit = true;
				}
				if (needExit) {
					dos.writeBytes("exit\n");
					os.flush();
				}
			}
		} catch (IOException e) {
			android.util.Log.e(TAG, "Request Root Error", e);
		} finally {
			try {
				if (dis != null)
					dis.close();
				if (dos != null)
					dos.close();
				if (process != null)
					process.destroy();
			} catch (Exception e2) {
			}
		}
		return isRootGained;
	}

	/**
	 * 取得渠道号。
	 * 渠道号文件放置在META-INF文件夹中，文件名格式：包名_渠道号
	 * @param context
	 * @return
	 */
	public static String getChannelFromMETAINF(Context context) {
		ApplicationInfo appinfo = context.getApplicationInfo();
		String sourceDir = appinfo.sourceDir;
		String packagename = appinfo.packageName;

		String ret = "";
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(sourceDir);
			Enumeration<?> entries = zipfile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				String entryName = entry.getName();

				if (entryName.contains(packagename)) {
					if(entryName.contains("META-INF/")){
						ret = entryName.replace("META-INF/","");
						break;
					}
					ret = entryName;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		String[] split = ret.split("_");
		if (split != null && split.length >= 2) {
			return ret.substring(split[0].length() + 1);

		} else {
			return "";
		}
	}

}