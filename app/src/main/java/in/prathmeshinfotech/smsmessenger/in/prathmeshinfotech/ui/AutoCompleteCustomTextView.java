package in.prathmeshinfotech.smsmessenger.in.prathmeshinfotech.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.HashMap;

import in.prathmeshinfotech.smsmessenger.R;


public class AutoCompleteCustomTextView extends MultiAutoCompleteTextView implements AdapterView.OnItemClickListener {

    private final String TAG = "MultiAutoTextBox";
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count >= 1) {
                if (s.charAt(start) == ',') {
                    setReciept();
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public AutoCompleteCustomTextView(Context context) {
        super(context);
        init(context);
    }


    public AutoCompleteCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoCompleteCustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOnItemClickListener(this);
        addTextChangedListener(textWatcher);
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        HashMap<String, String> selectHashmap = (HashMap<String, String>) selectedItem;
        return selectHashmap.get("txtGroupName");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setReciept();
    }

    public void setReciept() {
        if (getText().toString().contains(",")) {

            SpannableStringBuilder spanStringBuilder = new SpannableStringBuilder(getText());
            String reciepts[] = getText().toString().trim().split(",");

            int count = 0;

            for (String recieptName : reciepts) {
                LayoutInflater parent = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                TextView txtView = (TextView) parent.inflate(R.layout.txtreciept, null);
                txtView.setText(recieptName);
                setFlags(txtView, recieptName);

                int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                txtView.measure(spec, spec);
                txtView.layout(0, 0, txtView.getMeasuredWidth(), txtView.getMeasuredHeight());

                Bitmap b = Bitmap.createBitmap(txtView.getWidth(), txtView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(b);
                canvas.translate(-txtView.getScrollX(), -txtView.getScrollY());
                txtView.draw(canvas);
                txtView.setDrawingCacheEnabled(true);
                Bitmap cacheBmp = txtView.getDrawingCache();
                Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
                txtView.destroyDrawingCache();  // destory drawable
                // create bitmap drawable for imagespan
                BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
                bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
                // create and set imagespan
                spanStringBuilder.setSpan(new ImageSpan(bmpDrawable), count, count + recieptName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                count = count + recieptName.length() + 1;
            }
            // set chips span
            setText(spanStringBuilder);
            // move cursor to last
            setSelection(getText().length());
            Log.e(TAG, "inside");

        }

    }

    private void setFlags(TextView txtView, String image) {
        if (image != null)
            txtView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_launcher, 0);
    }

}
