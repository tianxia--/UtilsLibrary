package cp.common.utilslibrary.view;

import com.example.utilslibrary.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
/**
 * readMoreTextView
 * @author chenpengfei_d
 *
 */
public class ReadMoreTextView extends TextView{
	private static final int DEFAULT_TRIM_LENGTH = 100;
	
	private AttributeSet mAttributeSet;
	private Context mContext;
	private CharSequence text;
	private BufferType bufferType;
	private boolean readMore = true;
	private int trimLength;
	private CharSequence trimCollapsedText;
	private CharSequence trimExpandedText;
	private ReadMoreClickableSpan viewMoreSpan;
	private int colorClickableText;
	private boolean showTrimExpandedText;
	public ReadMoreTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		this.mAttributeSet = attrs;
		init();
	}
	public ReadMoreTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.mAttributeSet = attrs;
		init();
	}
	public ReadMoreTextView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	

	public void init(){
		if (mAttributeSet != null) {
			TypedArray typedArray = mContext.obtainStyledAttributes(mAttributeSet, R.styleable.ReadMoreTextView);
			this.trimLength = typedArray.getInt(R.styleable.ReadMoreTextView_trimlength, DEFAULT_TRIM_LENGTH);
			int resourceIdTrimExpandedText = typedArray.getResourceId(R.styleable.ReadMoreTextView_trimExpandedText, R.string.read_less);
			int resourceIdTrimCollapsedText = typedArray.getResourceId(R.styleable.ReadMoreTextView_trimCollapsedText, R.string.read_more);
			this.trimCollapsedText = getResources().getString(resourceIdTrimCollapsedText);
			this.trimExpandedText = getResources().getString(resourceIdTrimExpandedText);
			this.colorClickableText = typedArray.getColor(R.styleable.ReadMoreTextView_colorClickableText, ContextCompat.getColor(mContext, R.color.accent));
			this.showTrimExpandedText = typedArray.getBoolean(R.styleable.ReadMoreTextView_showTrimExpandedText, true);
			typedArray.recycle();
			viewMoreSpan = new ReadMoreClickableSpan() {
				
				@Override
				void updateDrawState_(TextPaint ds) {
					ds.setColor(colorClickableText);
				}
				
				@Override
				void OnClick_(View widget) {
					readMore = !readMore;
					setText();
				}
			};;
		}
	}
	//通过
	private void setText(){
		super.setText(getTrimmedText(text),bufferType);
		setMovementMethod(LinkMovementMethod.getInstance());
		setHighlightColor(Color.TRANSPARENT);
	}
	//设置文本
	@Override
	public void setText(CharSequence text, BufferType type) {
		this.text = text;
		bufferType = type;
		setText();
	}
	//获取要显示的文本
	public CharSequence getTrimmedText(CharSequence text){
		if (text != null && text.length() > trimLength) {
			if (readMore) {//展开
				return updateExpandedText();
			}else { //缩小
				return updateCollapsedText();
			}
		}
		
		return text;
	}
	//添加扩展的文本
	private CharSequence updateExpandedText(){
		if (showTrimExpandedText) {
			SpannableStringBuilder s = new SpannableStringBuilder(text,0,trimLength + 1).append("...").append(trimCollapsedText);
			return addClickableSpan(s, trimCollapsedText);

		}
		return text;
	}
	//添加缩回去的文本
	public CharSequence updateCollapsedText(){
		if (showTrimExpandedText) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text,0,text.length()).append(trimExpandedText);
			return addClickableSpan(builder, trimExpandedText);
		}
		
		return text;
	}
	//添加clickSpan监听器，也就是给其中的文本添加
	private CharSequence addClickableSpan(SpannableStringBuilder builder,CharSequence sequence){
		builder.setSpan(viewMoreSpan, builder.length()- sequence.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	//设置显示的长度
	public void setTrimLength (int trimLength){
		this.trimLength = trimLength;
		setText();
	}
	
	public void setColorClickableText(int colorClickableText){
		this.colorClickableText = colorClickableText;
	}
	
	public void setTrimCollpsedText(CharSequence trimCollapsedText){
		this.trimCollapsedText = trimCollapsedText;
	}
	
	public void setTrimExpandedText(CharSequence trimExpandedText){
		this.trimExpandedText = trimExpandedText;
	}
	
}
