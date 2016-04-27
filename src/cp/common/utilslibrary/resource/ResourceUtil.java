package cp.common.utilslibrary.resource;

import java.lang.reflect.Field;

import android.content.Context;

/**
 * ��Դ������
 * @author chenpengfei_d
 */
public class ResourceUtil {

	/**
	 * �õ�����id
	 * @param paramContext �����Ķ���
	 * @param paramString �ַ���
	 * @return
	 */
	public static int getAnimId(Context paramContext,String paramString){
		return paramContext.getResources().getIdentifier(paramString, "anim",
				paramContext.getPackageName());
	}
	
	/**
	 * �õ�����id
	 * @param paramContext �����Ķ���
	 * @param paramString �ַ���
	 * @return
	 */
	public static int getAttrId(Context paramContext,String paramString){
		return paramContext.getResources().getIdentifier(paramString, "attr", 
				paramContext.getPackageName());
	}
	
	/**
	 * �õ�����id
	 * @param paramContext �����Ķ���
	 * @param paramString �ַ���
	 * @return
	 */
	public static int getLayoutId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "layout",
				paramContext.getPackageName());
	}

	/**
	 * �õ��ַ���id
	 * @param paramContext �����Ķ���
	 * @param paramString �ַ���
	 * @return
	 */
	public static int getStringId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "string",
				paramContext.getPackageName());
	}

	/**
	 * �õ�ͼƬid
	 * @param paramContext �����Ķ���
	 * @param paramString �ַ���
	 * @return
	 */
	public static int getDrawableId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
	}

	/**
	 * �õ���Դ��ʽ����
	 * @param context �����Ķ���
	 * @param name �ַ���
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
	 * �õ���ʽid
	 * @param context �����Ķ���
	 * @param name �ַ���
	 * @return
	 */
	public static int getStyleableId(Context paramContext, String paramString){
		return paramContext.getResources().getIdentifier(paramString, "styleable", 
				paramContext.getPackageName());
	}
	
	/**
	 * �õ���ʽid
	 * @param context �����Ķ���
	 * @param name �ַ���
	 * @return
	 */
	public static int getStyleId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "style",
				paramContext.getPackageName());
	}

	/**
	 * �õ�id
	 * @param context �����Ķ���
	 * @param name �ַ���
	 * @return
	 */
	public static int getId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "id",
				paramContext.getPackageName());
	}

	/**
	 * �õ���ɫid
	 * @param context �����Ķ���
	 * @param name �ַ���
	 * @return
	 */
	public static int getColorId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "color",
				paramContext.getPackageName());
	}

	/**
	 * �õ�ԭ��id
	 * @param context �����Ķ���
	 * @param name �ַ���
	 * @return
	 */
	public static int getRawId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "raw",
				paramContext.getPackageName());
	}
	
}