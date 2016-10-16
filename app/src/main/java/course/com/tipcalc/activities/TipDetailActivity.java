package course.com.tipcalc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import course.com.tipcalc.R;

public class TipDetailActivity extends AppCompatActivity {
    public static final String TIP_KEY = "tip";
    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String TOTAL_KEY = "total";

    @Bind(R.id.txtBillTotal)
    TextView txtBillTotal;
    @Bind(R.id.txtTip)
    TextView txtTip;
    @Bind(R.id.txtTimestamp)
    TextView txtTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String total = String.format(getString(R.string.tipdetail_message_bill),
                intent.getDoubleExtra(TOTAL_KEY, 0d)
        );
        txtBillTotal.setText(total);

        String tip = String.format(getString(R.string.global_message_tip),
                intent.getDoubleExtra(TIP_KEY, 0d)
        );
        txtTip.setText(tip);

        txtTimestamp.setText(intent.getStringExtra(TIMESTAMP_KEY));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon action bar is clicked; go to parent activity
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
