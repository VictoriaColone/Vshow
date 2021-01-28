package com.victoria.vshow.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.victoria.vshow.R;
import static android.os.Process.killProcess;
import static android.os.Process.myPid;

/**
 * 关闭弹窗
 * @Author Victor Colone
 */
public class CloseDialog extends Dialog {

    /** 继续退出按钮*/
    private Button mExitButton;
    /** 关闭退出按钮*/
    private ImageView mCloseButton;
    /** 再玩一会按钮*/
    private Button mContinueButton;


    /**
     * 构造方法，设置风格
     * @param context
     */
    public CloseDialog(@NonNull Context context) {
        super(context, R.style.NoTitleDialog);
        init();
    }

    /**
     * 初始化关闭弹窗
     */
    private void init() {
        setContentView(R.layout.close_dialog);
        setCanceledOnTouchOutside(false);
        mExitButton = findViewById(R.id.exit_button);
        mCloseButton = findViewById(R.id.image_close);
        mContinueButton = findViewById(R.id.continue_button);
        initClickListener();
    }

    /**
     * 初始化监听事件
     */
    private void initClickListener() {

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killProcess(myPid());
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
