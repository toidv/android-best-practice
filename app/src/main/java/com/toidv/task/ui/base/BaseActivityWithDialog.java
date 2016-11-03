package com.toidv.task.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.toidv.task.R;
import com.toidv.task.utils.ApplicationUtils;


/**
 * Created by TOIDV on 4/4/2016.
 */
public abstract class BaseActivityWithDialog extends BaseActivity implements BaseMvpView {

    protected MaterialDialog progressDialog, alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createProgressDialog();
        createAlertDialog();
        setupDialogTitle();
    }


    @Override
    public void createProgressDialog() {
        progressDialog = ApplicationUtils.createProgress(this, getString(R.string.app_name));
    }

    @Override
    public void createAlertDialog() {
        alertDialog = ApplicationUtils.createAlertDialog(this, getString(R.string.app_name));
    }

    @Override
    public void showProgressDialog(boolean value) {
        if (value) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showAlertDialog(String errorMessage) {
        alertDialog.setContent(errorMessage);
        alertDialog.show();
    }

    @Override
    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            alertDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    protected abstract void setupDialogTitle();

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}
