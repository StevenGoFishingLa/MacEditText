package com.example.steven.macedittext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MacEditText extends LinearLayout {
    private static final int OCTET_NUM = 6;

    private EditText macOctet[] = new EditText[OCTET_NUM];

    public MacEditText(Context context) {
        super(context);
        InitUI();
        checkInput();
    }

    public MacEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitUI();
        checkInput();
    }

    public MacEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitUI();
        checkInput();
    }

    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i))) {//allow only numbers and alphabets
                    return "";
                }
            }
            return null;
        }
    };

    private void InitUI() {
        TextView colonTxt[] = new TextView[OCTET_NUM - 1];
        for (int i = 0; i < OCTET_NUM; i++) {
            macOctet[i] = new EditText(getContext());
            macOctet[i].setTag(i);
            addView(macOctet[i]);
            if (i < OCTET_NUM - 1) {
                colonTxt[i] = new TextView(getContext());
                colonTxt[i].setText(":");
                colonTxt[i].setTextSize(24);
                addView(colonTxt[i]);
            }
        }

        for (int i = 0; i < OCTET_NUM; i++) {
            macOctet[i].setGravity(Gravity.CENTER);
            macOctet[i].setFilters(new InputFilter[]{new InputFilter.LengthFilter(2), filter});//input max length = 2, allow only numbers and alphabets
            LayoutParams params = (LayoutParams) macOctet[i].getLayoutParams();
            macOctet[i].setSingleLine(true);
            macOctet[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);//set keyboard nex button
            params.weight = 1;
            params.width = 0;
        }
    }

    private void checkInput() {
        for (int i = 0; i < OCTET_NUM; i++) {
            macOctet[i].addTextChangedListener(new MyTextWatcher(macOctet[i]) {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    EditText editText = getEditTextView();
                    String trueMacAddress = "[A-Fa-f0-9]";
                    int position = (int) editText.getTag();
                    if (s.length() == 1) {
                        if (!s.toString().matches(trueMacAddress)) {
                            //Toast.makeText(getContext(), "This is not a valid Mac Address", Toast.LENGTH_SHORT).show();
                            addWarnAnim(editText);
                        }
                    } else if (s.length() == 2) {
                        trueMacAddress = "[A-Fa-f0-9]{2}";
                        if (!s.toString().matches(trueMacAddress)) {
                            //Toast.makeText(getContext(), "This is not a valid Mac Address", Toast.LENGTH_SHORT).show();
                            addWarnAnim(editText);
                        } else {
                            if (position < OCTET_NUM - 1) {
                                macOctet[position + 1].requestFocus();
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            macOctet[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        String trueMacAddress = "[A-Fa-f0-9]{2}";
                        if (v.getText().toString().matches(trueMacAddress)) {
                            return false;
                        } else {
                            //Toast.makeText(getContext(), "This is not a valid Mac Address", Toast.LENGTH_SHORT).show();
                            addWarnAnim((EditText) v);
                        }
                    }
                    return true;
                }
            });
        }
    }

    private void addWarnAnim(EditText editText) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setRepeatCount(8);
        TranslateAnimation rightAnim = new TranslateAnimation(0, 30, 0, 0);
        rightAnim.setDuration(60);
        TranslateAnimation leftAnim = new TranslateAnimation(30, 0, 0, 0);
        rightAnim.setDuration(60);
        animationSet.addAnimation(rightAnim);
        animationSet.addAnimation(leftAnim);
        editText.startAnimation(animationSet);
    }

    public String getMacAddress() {
        StringBuffer sb = new StringBuffer();
        String trueMacAddress = "[A-Fa-f0-9]{2}";
        for (int i = 0; i < OCTET_NUM; i++) {
            if (!macOctet[i].getText().toString().matches(trueMacAddress)) {
                //Toast.makeText(getContext(), "This is not a valid Mac Address", Toast.LENGTH_SHORT).show();
                addWarnAnim(macOctet[i]);
                macOctet[i].requestFocus();
                return null;
            }
            sb.append(macOctet[i].getText());
            if (i < OCTET_NUM - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }
}

abstract class MyTextWatcher implements TextWatcher {
    private EditText editText;

    public MyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    public EditText getEditTextView() {
        return editText;
    }

    public ViewParent getParentView() {
        return editText.getParent();
    }

    public View getRootView() {
        return editText.getRootView();
    }
}