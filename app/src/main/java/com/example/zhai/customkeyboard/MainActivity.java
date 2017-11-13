package com.example.zhai.customkeyboard;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText mEtInput;
    private KeyboardView mKeyboardView;
    private CustomKeyBoard mCustomKeyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mEtInput = (EditText) findViewById(R.id.et_input);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        mCustomKeyBoard = new CustomKeyBoard(this, mEtInput);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 隐藏系统软键盘
        mEtInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT <= 10) {
                    // 屏蔽默认输入法
                    mEtInput.setInputType(InputType.TYPE_NULL);
                } else {
                    //反射的方法实现避免弹出系统自带的软键盘
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    try {
                        Class<EditText> cls = EditText.class;
                        java.lang.reflect.Method setShowSoftInputOnFocus;
                        if (SDK_INT >= 16) {  // 4.2
                            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                        } else {  // 4.0
                            setShowSoftInputOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                        }
                        setShowSoftInputOnFocus.setAccessible(true);
                        setShowSoftInputOnFocus.invoke(mEtInput, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mCustomKeyBoard.showKeyboard();
                return false;
            }
        });
    }


}
