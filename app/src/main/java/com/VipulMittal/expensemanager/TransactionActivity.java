package com.VipulMittal.expensemanager;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.VipulMittal.expensemanager.BSD_Account.BsdAccountsFragment;
import com.VipulMittal.expensemanager.BSD_Cat.BsdCatFragment;
import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import java.io.Serializable;
import java.util.Calendar;

public class TransactionActivity extends AppCompatActivity implements Serializable {

	private static final String TAG = "Vipul_tag";
	public int type, amount, request, cID, sID, aID, id;
	public int amountCame, cIDCame, sIDCame, aIDCame, typeCame;
	public TextSwitcher TVDate, TVTime, TVAccount, TVCategory;
	boolean focus;
	RadioGroup radioGroup;
	RadioButton RBIncome, RBExpense, RBTransfer;
	Toast toast;
	String note, description;
	Calendar calendar;
	EditText ETNote, ETDes, ETAmt;
	boolean BNote, BAmt, BAcc, BCat;
	Button save, repeat;
	int[] dateArray = new int[7];
	ImageView iv_acc, iv_cat;
	MainActivity mainActivity;
	ActionBar actionBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
		// Set up shared element transition

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

		Intent intent = getIntent();
		Object objReceived = ((ObjectWrapper) intent.getExtras().getBinder("bind")).getData();
		mainActivity = (MainActivity) objReceived;
		id = intent.getIntExtra("id", -1);
		aID = intent.getIntExtra("aID", -1);
		cID = intent.getIntExtra("cID", -1);
		sID = intent.getIntExtra("sID", -1);
		type = intent.getIntExtra("type", 2);
		request = intent.getIntExtra("request", 1);
		amount = intent.getIntExtra("amount", 0);
		note = intent.getStringExtra("note");
		description = intent.getStringExtra("description");
		focus = intent.getBooleanExtra("focus", false);
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(intent.getLongExtra("calendar", -1));
		dateArray[0] = calendar.get(Calendar.YEAR);
		dateArray[1] = calendar.get(Calendar.MONTH);
		dateArray[2] = calendar.get(Calendar.DATE);
		dateArray[3] = calendar.get(Calendar.HOUR_OF_DAY);
		dateArray[4] = calendar.get(Calendar.MINUTE);
		dateArray[5] = calendar.get(Calendar.SECOND);
		dateArray[6] = calendar.get(Calendar.MILLISECOND);
		amountCame = amount;
		cIDCame = cID;
		sIDCame = sID;
		aIDCame = aID;
		typeCame = type;

		actionBar = getSupportActionBar();

		if (request == 1)
			actionBar.setTitle("Add Transaction");
		else
			actionBar.setTitle("Edit Transaction");

		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#154b5e")));

		if (request == 1 && aIDCame == -1)
			findViewById(android.R.id.content).setTransitionName("EXTRA_VIEW_FAB");
		else if (request == 1)
			findViewById(android.R.id.content).setTransitionName("EXTRA_VIEW_REPEAT");
		else if (request == 2)
			findViewById(android.R.id.content).setTransitionName("EXTRA_VIEW_LIST");
		setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
		getWindow().setSharedElementEnterTransition(buildContainerTransform(true));
		getWindow().setSharedElementReturnTransition(buildContainerTransform(false));

		TVDate = findViewById(R.id.TVDate);
		TVTime = findViewById(R.id.TVTime);
		TVAccount = findViewById(R.id.TVAccount);
		TVCategory = findViewById(R.id.TVCategory);
		radioGroup = findViewById(R.id.RadioGroupType);
		RBExpense = findViewById(R.id.radioCatExpense);
		RBIncome = findViewById(R.id.radioCatIncome);
		RBTransfer = findViewById(R.id.radioCatTransfer);
		ETNote = findViewById(R.id.ETNote);
		ETDes = findViewById(R.id.ETDes);
		ETAmt = findViewById(R.id.ETAmount);
		save = findViewById(R.id.transaction_save_button);
		repeat = findViewById(R.id.transaction_repeat_button);
		iv_acc = findViewById(R.id.IVAccounts);
		iv_cat = findViewById(R.id.IVCategory);

		save.setEnabled(request != 1);
		repeat.setEnabled(request != 1);

		iv_acc.setImageResource(R.drawable.ic_account);
		iv_cat.setImageResource(R.drawable.ic_category);

		TVDate.setFactory(() -> {
			TextView t = new TextView(this);
			t.setTextSize(20);
			return t;
		});
		TVTime.setFactory(() -> {
			TextView t = new TextView(this);
			t.setTextSize(20);
			return t;
		});
		TVAccount.setFactory(() -> {
			TextView t = new TextView(this);
			t.setTextSize(20);
			return t;
		});
		TVCategory.setFactory(() -> {
			TextView t = new TextView(this);
			t.setTextSize(20);
			return t;
		});

		setRadioButton(type);
		doColoring();
		radioGroupSetListener();
		setDateAndTime(calendar);

		if (aID != -1) {
			Account account = mainActivity.accountViewModel.getAcc(aID);
//			Log.d(TAG, "onCreateView: account = "+account);
			Log.d(TAG, "onCreateView: account name = " + account.name);
			TVAccount.setCurrentText(account.name);
			BAcc = true;
			iv_acc.setImageResource(account.imageId);    //icon
		} else
			TVAccount.setCurrentText("Account");

		if (cID != -1) {
			if (type != 3) {
				Category category = mainActivity.categoryViewModel.getCat(cID);
				iv_cat.setImageResource(category.catImageID);
				Log.d(TAG, "onCreateView: category = " + category);
				Log.d(TAG, "onCreateView: category name = " + category.catName);
				if (sID != -1) {
					SubCategory subCategory = mainActivity.subCategoryViewModel.getSubCat(sID);
					Log.d(TAG, "onCreateView: subCategory = " + subCategory);
					Log.d(TAG, "onCreateView: subCategory name = " + subCategory.name);
					TVCategory.setCurrentText(category.catName + " / " + subCategory.name);
					iv_cat.setImageResource(subCategory.subCatImageID);
				} else
					TVCategory.setCurrentText(category.catName);
			} else {
				Account account = mainActivity.accountViewModel.getAcc(cID);
				Log.d(TAG, "onCreateView: account = " + account);
				Log.d(TAG, "onCreateView: account name = " + account.name);
				TVCategory.setCurrentText(account.name);

				iv_cat.setImageResource(account.imageId);
			}

			BCat = true;
		} else
			TVCategory.setCurrentText("Category");

		enableDisableSaveButton();


		Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_down);
		Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_up);
		TVCategory.setInAnimation(in);
		TVCategory.setOutAnimation(out);
		TVAccount.setInAnimation(in);
		TVAccount.setOutAnimation(out);

		if (request == 2) {
			if (amount >= 0)
				ETAmt.setText(String.valueOf(amount));
			else
				ETAmt.setText(String.valueOf(-amount));
		}

		ETNote.setText(note);
		ETDes.setText(description);

		TVAccount.setOnClickListener(v -> {
			BottomSheetDialogFragment bottomSheetDialogFragment = new BsdAccountsFragment(aID, cID, 1, type, this, null, mainActivity);
			bottomSheetDialogFragment.show(getSupportFragmentManager(), "BSD_Accounts");
		});

		TVCategory.setOnClickListener(v -> {
			if (type != 3) {
				BottomSheetDialogFragment bottomSheetDialogFragment = new BsdCatFragment(cID, sID, type, this, null, null, mainActivity);
				bottomSheetDialogFragment.show(getSupportFragmentManager(), "BSD_Category");
			} else {
				BottomSheetDialogFragment bottomSheetDialogFragment = new BsdAccountsFragment(cID, aID, 2, type, this, null, mainActivity);
				bottomSheetDialogFragment.show(getSupportFragmentManager(), "BSD_Accounts2");
			}
		});

		if (focus) {
			ETNote.requestFocus();
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInput(InputMethodManager.RESULT_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private MaterialContainerTransform buildContainerTransform(boolean entering) {
		MaterialContainerTransform transform = new MaterialContainerTransform();
		transform.setTransitionDirection(entering ? MaterialContainerTransform.TRANSITION_DIRECTION_ENTER : MaterialContainerTransform.TRANSITION_DIRECTION_RETURN);
		transform.setAllContainerColors(MaterialColors.getColor(findViewById(android.R.id.content), R.attr.colorSurface));
		transform.addTarget(android.R.id.content);
		transform.setDuration(500L);
		return transform;
	}


	private void enableDisableSaveButton() {

		save.setOnClickListener(v -> {
			Log.d(TAG, "enableDisableSaveButton: string before = " + ETAmt.getText().toString().trim());
			String s = amt(ETAmt.getText().toString().trim());
			Log.d(TAG, "enableDisableSaveButton: string after = " + s);
			int a = Integer.parseInt(s);
			if (type != 1)
				a = -a;
			calendar.set(dateArray[0], dateArray[1], dateArray[2], dateArray[3], dateArray[4]);
			calendar.set(Calendar.SECOND, dateArray[5]);
			calendar.set(Calendar.MILLISECOND, dateArray[6]);
			Log.d(TAG, "enableDisableSaveButton: month = " + (dateArray[1] - 1) + " y= " + dateArray[0]);
			Log.d(TAG, "enableDisableSaveButton: starting");

			if (request == 1)
				mainActivity.transactionViewModel.Insert(new Transaction(ETNote.getText().toString().trim(), a, aID, cID, sID, ETDes.getText().toString().trim(), type, calendar.getTimeInMillis() - dateArray[3] * 3600000L - dateArray[4] * 60000L - dateArray[5] * 1000L - dateArray[6], calendar.getTimeInMillis()));
			else {
				Transaction transaction = new Transaction(ETNote.getText().toString().trim(), a, aID, cID, sID, ETDes.getText().toString().trim(), type, calendar.getTimeInMillis() - dateArray[3] * 3600000L - dateArray[4] * 60000L - dateArray[5] * 1000L - dateArray[6], calendar.getTimeInMillis());
				transaction.id = id;
				mainActivity.transactionViewModel.Update(transaction);
			}
			Log.d(TAG, "enableDisableSaveButton: ended");

			if (aIDCame != -1)
				mainActivity.accountViewModel.UpdateAmt(-amountCame, aIDCame);
			mainActivity.accountViewModel.UpdateAmt(a, aID);
			if (type != 3) {
				mainActivity.categoryViewModel.UpdateAmt(a, cID);
				if (sID != -1)
					mainActivity.subCategoryViewModel.UpdateAmt(a, sID);
			} else {
				mainActivity.accountViewModel.UpdateAmt(-a, cID);//cid has aid2 data
			}

			if (typeCame != 3) {
				if (cIDCame != -1)
					mainActivity.categoryViewModel.UpdateAmt(-amountCame, cIDCame);
				if (sIDCame != -1)
					mainActivity.subCategoryViewModel.UpdateAmt(-amountCame, sIDCame);
			} else {
				//amountCame - a
				if (cIDCame != -1)
					mainActivity.accountViewModel.UpdateAmt(amountCame, cIDCame);//cid has aid2 data
			}

			onBackPressed();
		});

		repeat.setOnClickListener(v -> {
//			Log.d(TAG, "enableDisableSaveButton: started");
			String s = amt(ETAmt.getText().toString().trim());
			int a = Integer.parseInt(s);
			if (type == 2)
				a = -a;
			calendar.set(dateArray[0], dateArray[1], dateArray[2], dateArray[3], dateArray[4]);
			calendar.set(Calendar.SECOND, dateArray[5]);
			calendar.set(Calendar.MILLISECOND, dateArray[6]);

			if (request == 1)
				mainActivity.transactionViewModel.Insert(new Transaction(ETNote.getText().toString().trim(), a, aID, cID, sID, ETDes.getText().toString().trim(), type, calendar.getTimeInMillis() - dateArray[3] * 3600000L - dateArray[4] * 60000L - dateArray[5] * 1000L - dateArray[6], calendar.getTimeInMillis()));
			else {
				Transaction transaction = new Transaction(ETNote.getText().toString().trim(), a, aID, cID, sID, ETDes.getText().toString().trim(), type, calendar.getTimeInMillis() - dateArray[3] * 3600000L - dateArray[4] * 60000L - dateArray[5] * 1000L - dateArray[6], calendar.getTimeInMillis());
				transaction.id = id;
				mainActivity.transactionViewModel.Update(transaction);
			}

			mainActivity.accountViewModel.UpdateAmt(a - amountCame, aID);
			if (type != 3) {
				mainActivity.categoryViewModel.UpdateAmt(a - amountCame, cID);
				if (sID != -1)
					mainActivity.subCategoryViewModel.UpdateAmt(a - amountCame, sID);
			} else
				mainActivity.accountViewModel.UpdateAmt(amountCame - a, cID);//cid has aid2 data

//			Log.d(TAG, "enableDisableSaveButton: ended");

			Intent intent = new Intent(TransactionActivity.this, TransactionActivity.class);
			intent.putExtra("EXTRA_DURATION", 500L);
			intent.putExtra("calendar", Calendar.getInstance().getTimeInMillis());
			intent.putExtra("note", "");
			intent.putExtra("description", "");
			intent.putExtra("aID", aID);
			intent.putExtra("cID", cID);
			intent.putExtra("sID", sID);
			intent.putExtra("type", type);
			intent.putExtra("focus", true);

			Bundle bundle = new Bundle();
			bundle.putBinder("bind", new ObjectWrapper(mainActivity));
			intent.putExtras(bundle);

			ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(TransactionActivity.this, repeat, "EXTRA_VIEW_REPEAT");
			startActivity(intent, options.toBundle());
			finish();
		});

		ETNote.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				String s = charSequence.toString().trim();
				BNote = s.length() != 0;
				save.setEnabled(BNote && BAmt && BAcc && BCat);
				repeat.setEnabled(BNote && BAmt && BAcc && BCat);
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		ETAmt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				String s = charSequence.toString().trim();
				BAmt = s.length() != 0;
				save.setEnabled(BNote && BAmt && BAcc && BCat);
				repeat.setEnabled(BNote && BAmt && BAcc && BCat);
				Log.d(TAG, "onTextChanged: note=" + BNote);
				Log.d(TAG, "onTextChanged: amt=" + BAmt);
				Log.d(TAG, "onTextChanged: acc=" + BAcc);
				Log.d(TAG, "onTextChanged: cat=" + BCat);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

	}

	public void saveSelectedAccount(int aID, Account accountSelected) {
		if (aID == this.aID)
			return;
		this.aID = aID;
		TVAccount.setText(accountSelected.name);
		BAcc = true;
		iv_acc.setImageResource(accountSelected.imageId);

		save.setEnabled(BNote && BAmt && BAcc && BCat);
		repeat.setEnabled(BNote && BAmt && BAcc && BCat);
	}

	public void saveSelectedCategoryWithName(int cID, String name, int imageID) {
		if (cID == this.cID && this.sID == -1)
			return;

		this.cID = cID;
		TVCategory.setText(name);
		BCat = true;
		this.sID = -1;
		save.setEnabled(BNote && BAmt && BAcc && BCat);
		repeat.setEnabled(BNote && BAmt && BAcc && BCat);
		iv_cat.setImageResource(imageID);
	}

	public void saveSelectedCategoryWithoutName(int cID) {
		this.cID = cID;
	}

	public void saveSelectedSubCategory(int cID, int sID, String name, int imageID) {
		if (cID == this.cID && sID == this.sID)
			return;

		TVCategory.setText(name);
		BCat = true;
		save.setEnabled(BNote && BAmt && BAcc && BCat);
		repeat.setEnabled(BNote && BAmt && BAcc && BCat);
		this.sID = sID;
		this.cID = cID;
		iv_cat.setImageResource(imageID);
	}


	private void radioGroupSetListener() {
		radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
			if (i == R.id.radioCatIncome) {
				TVCategory.setCurrentText("Category");
				rSelected(RBIncome);
				rNotSelected(RBExpense);
				rNotSelected(RBTransfer);
				type = 1;
				cID = -1;
				sID = -1;
				BCat = false;
				save.setEnabled(false);
				repeat.setEnabled(false);
				iv_cat.setImageResource(R.drawable.ic_category);
			} else if (i == R.id.radioCatExpense) {
				TVCategory.setCurrentText("Category");
				rSelected(RBExpense);
				rNotSelected(RBIncome);
				rNotSelected(RBTransfer);
				type = 2;
				cID = -1;
				sID = -1;
				BCat = false;
				save.setEnabled(false);
				repeat.setEnabled(false);
				iv_cat.setImageResource(R.drawable.ic_category);
			} else if (i == R.id.radioCatTransfer) {
				TVCategory.setCurrentText("Account");
				rSelected(RBTransfer);
				rNotSelected(RBExpense);
				rNotSelected(RBIncome);
				type = 3;
				cID = -1;
				sID = -1;
				BCat = false;
				save.setEnabled(false);
				repeat.setEnabled(false);
				iv_cat.setImageResource(R.drawable.ic_account);
			}
		});
	}

	private void rSelected(RadioButton selected) {
		selected.setTextSize(27);
		selected.setTextColor(Color.parseColor("#154b5e"));
	}

	private void rNotSelected(RadioButton notSelected) {
		notSelected.setTextSize(20);
		notSelected.setTextColor(Color.parseColor("#1a74a1"));
	}

	private void doColoring() {
		int i = radioGroup.getCheckedRadioButtonId();
		if (i == R.id.radioCatIncome) {
			rSelected(RBIncome);
			rNotSelected(RBExpense);
			rNotSelected(RBTransfer);
		} else if (i == R.id.radioCatExpense) {
			rSelected(RBExpense);
			rNotSelected(RBIncome);
			rNotSelected(RBTransfer);
		} else if (i == R.id.radioCatTransfer) {
			rSelected(RBTransfer);
			rNotSelected(RBExpense);
			rNotSelected(RBIncome);
		}
	}


	private void setRadioButton(int type) {
		int id = -1;
		if (type == 1)
			id = R.id.radioCatIncome;
		else if (type == 2)
			id = R.id.radioCatExpense;
		else if (type == 3)
			id = R.id.radioCatTransfer;
		if (id != -1)
			radioGroup.check(id);
		else {
			if (toast != null)
				toast.cancel();
			toast = Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public String amt(String s) {
		int i = -1;
		while (++i < s.length() && s.indexOf(i) == '0') {
		}
		i--;
		String s1 = "";
		while (++i < s.length()) {
			s1 += s.charAt(i);
		}
		return s1;
	}

	private void setDateAndTime(Calendar calendar) {
		TVDate.setCurrentText(datePrint(dateArray[2], dateArray[1], dateArray[0]));
		TVTime.setCurrentText(timePrint(dateArray[3], dateArray[4]));

		Animation in = AnimationUtils.loadAnimation(mainActivity, R.anim.slide_down);
		Animation out = AnimationUtils.loadAnimation(mainActivity, R.anim.slide_up);
		TVDate.setInAnimation(in);
		TVDate.setOutAnimation(out);
		TVTime.setInAnimation(in);
		TVTime.setOutAnimation(out);

		TVDate.setOnClickListener(v -> {
			DatePickerDialog datePickerDialog = new DatePickerDialog(this,
					(datePicker, y, m, d) -> {
						if (dateArray[0] != y || dateArray[1] != m || dateArray[2] != d) {
							dateArray[0] = y;
							dateArray[1] = m;
							dateArray[2] = d;
							TVDate.setText(datePrint(d, m, y));
						}

					}, dateArray[0], dateArray[1], dateArray[2]);
			datePickerDialog.show();
		});

		TVTime.setOnClickListener(v -> {
			TimePickerDialog timePickerDialog = new TimePickerDialog(this,
					(timePicker, h, m) -> {
						if (dateArray[3] != h || dateArray[4] != m) {
							dateArray[3] = h;
							dateArray[4] = m;
							TVTime.setText(timePrint(h, m));
						}
					}, dateArray[3], dateArray[4], false);
			timePickerDialog.show();
		});
	}

	private String timePrint(int hour, int minute) {
		String amOrPm;
		if (hour < 12) {
			amOrPm = "AM";
		} else {
			hour -= 12;
			amOrPm = "PM";
		}
		if (hour == 0)
			hour = 12;

		String minut = String.valueOf(minute);
		if (minute < 10)
			minut = "0" + minut;
		return hour + ":" + minut + " " + amOrPm;
	}

	private String datePrint(int date, int month, int year) {
		String monthName = getMonth(month);
		return date + " " + monthName + ", " + year;
	}

	private String getMonth(int month) {
		String[] name = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		return name[month];
	}
}