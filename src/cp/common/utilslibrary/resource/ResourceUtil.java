package cp.common.utilslibrary.resource;

import java.lang.reflect.Field;

import android.content.Context;

/**
 * 资源工具类
 * @author chenpengfei_d
 */
public class ResourceUtil {

	/**
	 * 得到动画id
	 * @param paramContext 上下文对象
	 * @param paramString 字符串
	 * @return
	 */
	public static int getAnimId(Context paramContext,String paramString){
		return paramContext.getResources().getIdentifier(paramString, "anim",
				paramContext.getPackageName());
	}
	
	/**
	 * 得到属性id
	 * @param paramContext 上下文对象
	 * @param paramString 字符串
	 * @return
	 */
	public static int getAttrId(Context paramContext,String paramString){
		return paramContext.getResources().getIdentifier(paramString, "attr", 
				paramContext.getPackageName());
	}
	
	/**
	 * 得到布局id
	 * @param paramContext 上下文对象
	 * @param paramString 字符串
	 * @return
	 */
	public static int getLayoutId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "layout",
				paramContext.getPackageName());
	}

	/**
	 * 得到字符串id
	 * @param paramContext 上下文对象
	 * @param paramString 字符串
	 * @return
	 */
	public static int getStringId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "string",
				paramContext.getPackageName());
	}

	/**
	 * 得到图片id
	 * @param paramContext 上下文对象
	 * @param paramString 字符串
	 * @return
	 */
	public static int getDrawableId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
	}

	/**
	 * 得到资源样式数组
	 * @param context 上下文对象
	 * @param name 字符串
	 * @return
	 */
	public static final int[] getResourceDeclareStyleableIntArray( Context context, String name ){
	    try {
	        //use reflection to access the resource class
	        Field[] fields2 = Class.forName( context.getPackageName() + ".R$styleable" ).getFields();
	        //browse all fields
	        for ( Field f : fields2 ) {
	            //pick matching field
	            if ( f.getName().equals( name ) ) {
	                //return as int array
	                int[] ret = (int[])f.get( null );
	                return ret;
	            }
	        }
	    } catch ( Throwable t ) {
	    	t.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 * 得到样式id
	 * @param context 上下文对象
	 * @param name 字符串
	 * @return
	 */
	public static int getStyleableId(Context paramContext, String paramString){
		return paramContext.getResources().getIdentifier(paramString, "styleable", 
				paramContext.getPackageName());
	}
	
	/**
	 * 得到样式id
	 * @param context 上下文对象
	 * @param name 字符串
	 * @return
	 */
	public static int getStyleId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "style",
				paramContext.getPackageName());
	}

	/**
	 * 得到id
	 * @param context 上下文对象
	 * @param name 字符串
	 * @return
	 */
	public static int getId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "id",
				paramContext.getPackageName());
	}

	/**
	 * 得到颜色id
	 * @param context 上下文对象
	 * @param name 字符串
	 * @return
	 */
	public static int getColorId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "color",
				paramContext.getPackageName());
	}

	/**
	 * 得到原生id
	 * @param context 上下文对象
	 * @param name 字符串
	 * @return
	 */
	public static int getRawId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "raw",
				paramContext.getPackageName());
	}
	
}