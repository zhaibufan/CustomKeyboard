package com.example.zhai.customkeyboard;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


/**
 * Created by Mastra on 2017/11/4.
 *
 * 自定义软件盘，带数字和英文(以popupWindow的方式控制键盘的显示和隐藏)
 */

public class CustomKeyBoard2 {

    private static final String TAG = "CustomKeyBoard2";
    private Keyboard numberKb; // 数字键盘
    private Keyboard letterKb; // 字母键盘
    private KeyboardView keyboardView;
    private EditText edit;
    private static final int NUMBER_KB_FLAG = 0, LETTER_KB_FLAG = 1;
    private PopupWindow popupWindow;
    private RelativeLayout view;
    private Activity activity;

    public CustomKeyBoard2(Activity activity, EditText editText){
        edit = editText;
        this.activity = activity;
        view = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.activity_main, null);
        keyboardView = (KeyboardView) activity.findViewById(R.id.keyboard_view);
        numberKb = new Keyboard(activity, R.xml.number_keyboard);
        letterKb = new Keyboard(activity, R.xml.letter_keyboard);
        keyboardView.setKeyboard(numberKb);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(numberListener);

        // 创建一个PopupWindow，使自定义键盘已PopupWindow方式显示
        createPopupWindow();
    }

    private void createPopupWindow() {
        popupWindow = new PopupWindow(keyboardView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        //设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
        popupWindow.setFocusable(true);
        //这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 这句是点击外部popupWindow消失
        popupWindow.setOutsideTouchable(true);
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
        if (!popupWindow.isShowing()) {
            Log.d(TAG, "show");
            // 必须将keyboardView从父View中移除，否则会出现下面异常
            // The specified child already has a parent. You must call removeView() on the child's parent first
            view.removeView(keyboardView);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(false);
            popupWindow.setTouchable(true);
            popupWindow.showAtLocation(keyboardView, Gravity.BOTTOM, 0, 0);
        }
    }

    public void hideKeyboard() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
