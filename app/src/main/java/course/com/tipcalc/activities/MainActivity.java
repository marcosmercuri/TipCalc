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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import course.com.tipcalc.R;
import course.com.tipcalc.TipCalcApplication;
import course.com.tipcalc.fragments.TipHistoryListFragment;
import course.com.tipcalc.fragments.TipHistoryListFragmentListener;

public class MainActivity extends AppCompatActivity {
    private static final int TIP_STEP_CHANGE = 1;
    private static final int DEFAULT_TIP_PERCENTAGE = 10;

    @Bind(R.id.inputBill)
    EditText inputBill;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    @Bind(R.id.inputPercentage)
    EditText inputPercentage;
    @Bind(R.id.btnIncrease)
    Button btnIncrease;
    @Bind(R.id.btnDecrease)
    Button btnDecrease;
    @Bind(R.id.btnClear)
    Button btnClear;
    @Bind(R.id.txtTip)
    TextView txtTip;

    TipHistoryListFragmentListener fragmentListener;

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
            double tip = calculatePercentage(totalInputString);
            showTipInFragment(tip);
            showTipInView(tip);
        }
    }

    private void showTipInFragment(double tip) {
        fragmentListener.action(String.valueOf(tip));
    }

    private void showTipInView(double tip) {
        String tipString = String.format(getString(R.string.global_message_tip), tip);
        txtTip.setVisibility(View.VISIBLE);
        txtTip.setText(tipString);
    }

    private double calculatePercentage(String totalInputString) {
        double total = Double.parseDouble(totalInputString);
        int tipPercentage = getTipPercentage();
        return total * tipPercentage / 100;
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

    private void handleTipChange(int change) {
        int tipPercentage = getTipPercentage();
        tipPercentage += change;
        if (tipPercentage > 0) {
            inputPercentage.setText(String.valueOf(tipPercentage));
        }
    }
}
