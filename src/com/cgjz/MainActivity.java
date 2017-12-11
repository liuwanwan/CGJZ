package com.cgjz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.*;
import android.content.*;

public class MainActivity extends Activity {
	private Fragment[] fragments;
	private RadioGroup bottomMenuRG, showOrNotRG;;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	//private TextView stockMoneyTV
	TextView todayProfitTV, mainStockMoneyTV,
			mainStockProfitTV;
	private EditText set_expenseRateET, set_stamptaxRateET, set_transferfeeET;
	private double expenseRate = 0, stamptaxRate = 0, transferfeeRate = 0;
	private boolean PROMT_SHOW;
	private RadioButton showRB;
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	public DailyStockInfoSqlite myStockDB;
	private Cursor myStockCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		myStockDB = new DailyStockInfoSqlite(this);
		myStockCursor = myStockDB.select();
		initView();
		setMenuRadioGroup();
	}

	public DailyStockInfoSqlite getStockDB() {
		return myStockDB;
	}

	public Cursor getStockCursor() {
		return myStockCursor;
	}

	public SharedPreferences getSharedPreferences() {
		return mySharedPreferences;
	}

	private void setMenuRadioGroup() {

		bottomMenuRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(fragments[0]).hide(fragments[1])
						.hide(fragments[2]).hide(fragments[3])
						.hide(fragments[4]);
				switch (checkedId) {
				case R.id.assetBT:
					fragmentTransaction.show(fragments[0]).commit();
					//mainStockMoneyTV.setText(stockMoneyTV.getText().toString());
					mainStockProfitTV.setText(todayProfitTV.getText()
							.toString());
					break;
				case R.id.stockBT:
					fragmentTransaction.show(fragments[1]).commit();
					break;
				case R.id.mainBT:
					fragmentTransaction.show(fragments[2]).commit();
					break;
				case R.id.profitBT:
					fragmentTransaction.show(fragments[3]).commit();

					break;
				case R.id.insetBT:
					fragmentTransaction.show(fragments[4]).commit();
					break;
				default:
					break;
				}
			}
		});
	}

	private void initView() {
		mySharedPreferences = getSharedPreferences("myPreferenceSet",
				Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		//stockMoneyTV = (TextView) findViewById(R.id.totle_stockTV);
		todayProfitTV = (TextView) findViewById(R.id.totleStockProfitTV);
		mainStockMoneyTV = (TextView) findViewById(R.id.main_totle_stockTV);
		mainStockProfitTV = (TextView) findViewById(R.id.main_today_profitTV);
		bottomMenuRG = (RadioGroup) findViewById(R.id.bottom_menuBG);
		PROMT_SHOW = mySharedPreferences.getBoolean("showOrNot", false);
		if (PROMT_SHOW == false)
			promptDialog();
		fragments = new Fragment[5];
		fragmentManager = getFragmentManager();
		fragments[0] = fragmentManager.findFragmentById(R.id.fragment_asset);
		fragments[1] = fragmentManager.findFragmentById(R.id.fragment_stock);
		fragments[2] = fragmentManager.findFragmentById(R.id.fragment_main);
		fragments[3] = fragmentManager.findFragmentById(R.id.fragment_profit);
		fragments[4] = fragmentManager.findFragmentById(R.id.fragment_inset);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(fragments[0]).hide(fragments[1]).hide(fragments[2])
				.hide(fragments[3]).hide(fragments[4]);
		fragmentTransaction.show(fragments[2]).commit();
	}

	private void promptDialog() {
		final View layout = LayoutInflater.from(this).inflate(
				R.layout.promt_dialog,
				(ViewGroup) findViewById(R.id.setShowOrNotLayout), false);
		showOrNotRG = (RadioGroup) layout.findViewById(R.id.showornotRG);
		showRB = (RadioButton) layout.findViewById(R.id.showRButton);
		showOrNotRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == showRB.getId()) {
					PROMT_SHOW = true;

				} else {
					PROMT_SHOW = false;
				}
				editor.putBoolean("showOrNot", PROMT_SHOW);
				editor.commit();
			}
		});

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("T").setView(layout)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			final View layout = LayoutInflater.from(this).inflate(
					R.layout.set_layout,
					(ViewGroup) findViewById(R.id.mySetDialogLayout), false);
			set_expenseRateET = (EditText) layout
					.findViewById(R.id.stock_expensesET);
			set_stamptaxRateET = (EditText) layout
					.findViewById(R.id.stock_taxET);
			set_transferfeeET = (EditText) layout
					.findViewById(R.id.stock_transferfeeET);
			set_expenseRateET.setText(mySharedPreferences.getFloat(
					"expenseRate", 0.25F) + "");
			set_stamptaxRateET.setText(mySharedPreferences.getFloat(
					"stamptaxRate", 0.5F) + "");
			set_transferfeeET.setText(mySharedPreferences.getFloat(
					"stamptransferfeeRate", 0.02F) + "");
			AlertDialog.Builder setDialog = new AlertDialog.Builder(this);
			setDialog
					.setTitle("交易税费")
					.setView(layout)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									String tempExpense = set_expenseRateET
											.getText().toString();
									String tempStamptax = set_stamptaxRateET
											.getText().toString();
									String tempTransferfeetax = set_transferfeeET
											.getText().toString();
									if (tempExpense.length() > 0
											&& tempStamptax.length() > 0
											&& tempTransferfeetax.length() > 0) {
										expenseRate = Double
												.parseDouble(tempExpense);
										stamptaxRate = Double
												.parseDouble(tempStamptax);
										transferfeeRate = Double
												.parseDouble(tempTransferfeetax);
										editor.putFloat("expenseRate",
												(float) expenseRate);
										editor.putFloat("stamptaxRate",
												(float) stamptaxRate);
										editor.putFloat("transferfeeRate",
												(float) transferfeeRate);

										editor.commit();
									}

								}

							}).setNegativeButton("Cancel", null).show();
			break;
		case R.id.action_exit:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
