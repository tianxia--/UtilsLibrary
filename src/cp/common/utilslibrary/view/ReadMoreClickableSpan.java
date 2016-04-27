package cp.common.utilslibrary.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
/**
 * readMoreTextView clickSpa¼àÌýÆ÷
 * @author chenpengfei_d
 *
 */
public  abstract class ReadMoreClickableSpan extends ClickableSpan{

	@Override
	public void onClick(View widget) {
		OnClick_(widget);
	}
	
	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		updateDrawState_(ds);
	}
	abstract void OnClick_(View widget);
	abstract void updateDrawState_(TextPaint ds);
}
