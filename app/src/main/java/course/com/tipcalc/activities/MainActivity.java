package course.com.tipcalc.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import course.com.tipcalc.R;
import course.com.tipcalc.TipCalcApplication;
import course.com.tipcalc.fragments.TipHistoryListFragment;
import course.com.tipcalc.fragments.TipHistoryListFragmentListener;
import course.com.tipcalc.model.TipRecord;

public class MainActivity extends AppCompatActivity {
    private static final int TIP_STEP_CHANGE = 1;
    private static final int DEFAULT_TIP_PERCENTAGE = 10;

    @Bind(R.id.inputBill)
    EditText inputBill;
    @Bind(R.id.inputPercentage)
    EditText inputPercentage;
    @Bind(R.id.txtTip)
    TextView txtTip;

    private TipHistoryListFragmentListener fragmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        TipHistoryListFragment fragment =
                (TipHistoryListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentList);
        fragment.setRetainInstance(true);
        fragmentListener = fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            about();
        }
        return super.onOptionsItemSelected(item);
    }

    private void about() {
        Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
        openBrowserIntent.setData(Uri.parse(TipCalcApplication.ABOUT_URL));
        startActivity(openBrowserIntent);
    }

    @OnClick(R.id.btnSubmit)
    public void handleClickSubmit() {
        Log.d(getLocalClassName(), "Click in submit");
        hideKeyboard();
        processPercentageFromInput();
    }

    private void processPercentageFromInput() {
        String totalInputString = inputBill.getText().toString().trim();
        if ( ! totalInputString.isEmpty()) {
            TipRecord tipRecord = createTipRecord(totalInputString);
            addToFragment(tipRecord);
            double tip = tipRecord.getTip();
            showTipInView(tip);
        }
    }

    private TipRecord createTipRecord(String totalInputString) {
        TipRecord tipRecord = new TipRecord();
        tipRecord.setBill(Double.parseDouble(totalInputString));
        tipRecord.setTipPercentage(getTipPercentage());
        tipRecord.setCreatedDate(new Date());
        return tipRecord;
    }

    private void addToFragment(TipRecord tipRecord) {
        fragmentListener.addToList(tipRecord);
    }

    private void showTipInView(double tip) {
        String tipString = String.format(getString(R.string.global_message_tip), tip);
        txtTip.setVisibility(View.VISIBLE);
        txtTip.setText(tipString);
    }

    public int getTipPercentage() {
        int tipPercentage = DEFAULT_TIP_PERCENTAGE;
        String inputTipPercentage = inputPercentage.getText().toString().trim();
        if ( ! inputTipPercentage.isEmpty()) {
            tipPercentage = Integer.parseInt(inputTipPercentage);
        } else {
            inputPercentage.setText(String.valueOf(tipPercentage));
        }
        return tipPercentage;
    }

    private void hideKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException npe) {
            Log.e(getLocalClassName(), Log.getStackTraceString(npe));
        }
    }

    @OnClick(R.id.btnIncrease)
    public void handleClickIncrease() {
        hideKeyboard();
        handleTipChange(TIP_STEP_CHANGE);
    }

    @OnClick(R.id.btnDecrease)
    public void handleClickDecrease() {
        hideKeyboard();
        handleTipChange( - TIP_STEP_CHANGE);
    }

    @OnClick(R.id.btnClear)
    public void handleClickClear() {
        fragmentListener.clearList();
    }

    private void handleTipChange(int change) {
        int tipPercentage = getTipPercentage();
        tipPercentage += change;
        if (tipPercentage > 0) {
            inputPercentage.setText(String.valueOf(tipPercentage));
        }
    }
}
