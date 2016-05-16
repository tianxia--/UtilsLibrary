package cp.common.utilslibrary.utils;

import java.io.Serializable;

/**
 * 本地apk信息类
 */
@SuppressWarnings("serial")
public class GPNativeApkInfo implements Serializable{
	private String packageName;
	private String signature;
	private String md5;
	
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		return "GPBaseInfo [packageName=" + packageName + ", signature="
				+ signature + ", md5=" + md5 + "]";
	}
	
	
}
