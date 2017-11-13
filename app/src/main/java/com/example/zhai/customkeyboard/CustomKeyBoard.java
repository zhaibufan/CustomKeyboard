package com.example.zhai.customkeyboard;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;


/**
 * Created by Mastra on 2017/11/4.
 *
 * 自定义软件盘，带数字和英文(以动画的方式控制键盘的显示和隐藏)
 */

public class CustomKeyBoard {

    private static final String TAG = "CustomKeyBoard";
    private Keyboard numberKb; // 数字键盘
    private Keyboard letterKb; // 字母键盘
    private KeyboardView keyboardView;
    private EditText edit;
    private static final int NUMBER_KB_FLAG = 0, LETTER_KB_FLAG = 1;
    private Activity activity;

    public CustomKeyBoard(Activity activity, EditText editText){
        edit = editText;
        this.activity = activity;
        keyboardView = activity.findViewById(R.id.keyboard_view);
        numberKb = new Keyboard(activity, R.xml.number_keyboard);
        letterKb = new Keyboard(activity, R.xml.letter_keyboard);
        keyboardView.setKeyboard(numberKb);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(numberListener);
    }

    private KeyboardView.OnKeyboardActionListener numberListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            displayInputContent(primaryCode, NUMBER_KB_FLAG);
        }
    };

    private KeyboardView.OnKeyboardActionListener letterListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            displayInputContent(primaryCode, LETTER_KB_FLAG);
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    private void displayInputContent(int primaryCode, int flag) {
        Log.d(TAG, "flag="+flag+"   primaryCode="+primaryCode);
        Editable editable = edit.getText();
        int start = edit.getSelectionStart();
        Log.d(TAG, "start="+start);
        if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
            hideKeyboard();
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
            if (editable != null && editable.length() > 0) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) { // 数字键盘切换
            if (flag == NUMBER_KB_FLAG) {
                keyboardView.setKeyboard(letterKb);
                keyboardView.setEnabled(true);
                keyboardView.setPreviewEnabled(false);
                keyboardView.setOnKeyboardActionListener(letterListener);
            }else if (flag == LETTER_KB_FLAG) {
                keyboardView.setKeyboard(numberKb);
                keyboardView.setEnabled(true);
                keyboardView.setPreviewEnabled(false);
                keyboardView.setOnKeyboardActionListener(numberListener);
            }
        } else {
            editable.insert(start, Character.toString((char) primaryCode));
        }
    }

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        Log.d(TAG, "visibility="+visibility);
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
            keyboardView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_bottom_enter));
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
            keyboardView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_bottom_out));
        }
    }
}
