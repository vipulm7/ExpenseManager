package com.VipulMittal.expensemanager;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements Serializable {

	public static final String TAG = "Vipul_tag";
	public static final int notifID = 2;
	public static int[] amount;
	public static int sum_amounts;
	public static NotificationManagerCompat notificationManager;
	public static PendingIntent pendingIntent;
	public final String CHANNEL_ID = "1";
	public TransactionAdapter transactionAdapter;
	public TransactionViewModel transactionViewModel;
	public AccountAdapter accountAdapter;
	public AccountViewModel accountViewModel;
	public CategoryAdapter categoryAdapter, categoryAdapter2;
	public CategoryViewModel categoryViewModel, categoryViewModel2;
	public SubCategoryAdapter subCategoryAdapter;
	public SubCategoryViewModel subCategoryViewModel;
	public List<SubCategory> allSubcats;
	public Toast toast;
	public int expense, income;
	public HomeFragment homeFragment;
	public CategoryFragment categoryFragment;
	public long systemTimeInMillies;
	public int[] icon_account, icon_category_income, icon_category_expense;
	public Executor executor;
	public BiometricPrompt biometricPrompt;
	public BiometricPrompt.PromptInfo promptInfo;
	public LayoutInflater inflater;
	ExtendedFloatingActionButton FABAdd;
	NavigationBarView navigationBarView;
	ConstraintLayout layoutForFragment;
	Calendar toShow;
	AccountsFragment accountsFragment;
	AnalysisFragment analysisFragment;
	View accView, catView;
	boolean b1, b2, b3, b4;
	boolean first_time;
	ActivityResultLauncher<Intent> abc;
	boolean areNotifAllowed;
	ActionBar actionBar;
	int viewMode;
	boolean exit, login, menuShow;
	SharedPreferences sharedPreferences;
	SwitchPreference notifSwitchPreference;
	SettingsFragment settingsFragment;
	Map<Integer, List<SubCategory>> subcategoriesMap;
	IconsAdapter iconsAdapterCat, iconsAdapter;
	List<Transaction> transactions;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		splashScreen.setKeepOnScreenCondition(() -> transactions == null);

		FABAdd = findViewById(R.id.FABAdd);
		navigationBarView = findViewById(R.id.BottomNavigation);
		layoutForFragment = findViewById(R.id.layoutForFragment);
		settingsFragment = new SettingsFragment();
		notifSwitchPreference = settingsFragment.notif;

		subCategoryAdapter = new SubCategoryAdapter();
		accountAdapter = new AccountAdapter();
		categoryAdapter = new CategoryAdapter(this);
		categoryAdapter2 = new CategoryAdapter(this);
		allSubcats = new ArrayList<>();
		toShow = Calendar.getInstance();
		subcategoriesMap = new HashMap<>();
		inflater = LayoutInflater.from(this);
		icon_account = new int[]{R.drawable.ia_airtel_money, R.drawable.ia_amazon, R.drawable.ia_american_express, R.drawable.ia_apple_pay,
				R.drawable.ia_bitcoin_cash, R.drawable.ia_cash, R.drawable.ia_dogecoin, R.drawable.ia_ethereum,
				R.drawable.ia_facebook_pay, R.drawable.ia_freecharge, R.drawable.ia_gift_card, R.drawable.ia_google_pay,
				R.drawable.ia_google_wallet, R.drawable.ia_litecoin, R.drawable.ia_maestro, R.drawable.ia_master,
				R.drawable.ia_mobikwik, R.drawable.ia_ola_money, R.drawable.ia_paypal, R.drawable.ia_paytm,
				R.drawable.ia_payu, R.drawable.ia_payzapp, R.drawable.ia_rupay, R.drawable.ia_samsung_pay,
				R.drawable.ia_uaevisa, R.drawable.ia_visa};

		icon_category_income = new int[]{R.drawable.is_airport, R.drawable.is_android, R.drawable.is_apartment, R.drawable.is_app_store,
				R.drawable.is_baby_stroller, R.drawable.is_bank, R.drawable.is_books, R.drawable.is_bread,
				R.drawable.is_bus, R.drawable.is_cake, R.drawable.is_car, R.drawable.is_card,
				R.drawable.is_cruise_ship, R.drawable.is_desk, R.drawable.is_discount, R.drawable.is_dish,
				R.drawable.is_dishwasher, R.drawable.is_doctors_bag, R.drawable.is_electrical, R.drawable.is_energy_drink,
				R.drawable.is_engine_oil, R.drawable.is_fill_color, R.drawable.is_fish, R.drawable.is_flip_flops,
				R.drawable.is_food_wine, R.drawable.is_gas_station, R.drawable.is_glasses, R.drawable.is_hamburger,
				R.drawable.is_headphones, R.drawable.is_home, R.drawable.is_ice_cream, R.drawable.is_iphone,
				R.drawable.is_kebab, R.drawable.is_lipstick, R.drawable.is_maintenance, R.drawable.is_mannequin,
				R.drawable.is_milk_bottle, R.drawable.is_money, R.drawable.is_motorcycle, R.drawable.is_netflix,
				R.drawable.is_orange, R.drawable.is_paint_roller, R.drawable.is_parking_meter, R.drawable.is_perfume,
				R.drawable.is_popcorn, R.drawable.is_potato, R.drawable.is_protection_mask, R.drawable.is_raquet,
				R.drawable.is_refund, R.drawable.is_rent, R.drawable.is_roller, R.drawable.is_safe,
				R.drawable.is_sale, R.drawable.is_sandals, R.drawable.is_shopping_cart, R.drawable.is_shirt,
				R.drawable.is_soap_dispenser, R.drawable.is_soccer, R.drawable.is_sofa, R.drawable.is_subway,
				R.drawable.is_taxi, R.drawable.is_tetra_pak, R.drawable.is_ticket, R.drawable.is_tie,
				R.drawable.is_toilet_paper, R.drawable.is_treatment_list, R.drawable.is_tshirt, R.drawable.is_two_tickets,
				R.drawable.is_umbrella, R.drawable.is_vegetarian_food, R.drawable.is_wallet, R.drawable.is_wallet2,
				R.drawable.is_wardrobe, R.drawable.is_washing_machine, R.drawable.is_cafe, R.drawable.is_eggs,
				R.drawable.is_pills, R.drawable.is_mcdonalds, R.drawable.is_swiggy, R.drawable.is_zomato,
				R.drawable.is_tea,

				R.drawable.ia_airtel_money, R.drawable.ia_amazon, R.drawable.ia_american_express, R.drawable.ia_apple_pay,
				R.drawable.ia_bitcoin_cash, R.drawable.ia_cash, R.drawable.ia_dogecoin, R.drawable.ia_ethereum,
				R.drawable.ia_facebook_pay, R.drawable.ia_freecharge, R.drawable.ia_gift_card, R.drawable.ia_google_pay,
				R.drawable.ia_google_wallet, R.drawable.ia_litecoin, R.drawable.ia_maestro, R.drawable.ia_master,
				R.drawable.ia_mobikwik, R.drawable.ia_ola_money, R.drawable.ia_paypal, R.drawable.ia_paytm,
				R.drawable.ia_payu, R.drawable.ia_payzapp, R.drawable.ia_rupay, R.drawable.ia_samsung_pay,
				R.drawable.ia_uaevisa, R.drawable.ia_visa};

		icon_category_expense = new int[]{};

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		viewMode = sharedPreferences.getInt("view", R.id.RBM);
		first_time = sharedPreferences.getBoolean("first_time", true);
		Log.d(TAG, "onCreate: viewMode = " + viewMode);
		menuShow = true;

		actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#154b5e")));

		if (first_time) {
			int size = 1000;
			amount = new int[size];
			for (int i = -1; ++i < size; ) {
				double random = Math.random();
				if (random < 0.5)
					amount[i] = -(int) (Math.random() * 10000);
				else
					amount[i] = (int) (Math.random() * 10000);
				sum_amounts += amount[i];
			}

			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean("first_time", false);


			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 20);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			editor.putLong("notifTime", calendar.getTimeInMillis());
			editor.apply();

			Log.d(TAG, "createNotif: notiftime = " + sharedPreferences.getLong("notifTime", -1));
		}


		accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
		categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
		categoryViewModel2 = new ViewModelProvider(this).get(CategoryViewModel.class);
		subCategoryViewModel = new ViewModelProvider(this).get(SubCategoryViewModel.class);
		transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

		accountROOM();
		categoryROOM();
		subCategoryROOM(0);
		subCatROOM();
		transactionROOM();
		notification();

		Calendar calendar = Calendar.getInstance();
		Log.d(TAG, "onCreate: telldate day = " + calendar.get(Calendar.DAY_OF_MONTH));
		Log.d(TAG, "onCreate: telldate date = " + calendar.get(Calendar.DATE));
		Log.d(TAG, "onCreate: telldate DAY_OF_WEEK = " + calendar.get(Calendar.DAY_OF_WEEK));

		transactionAdapter = new TransactionAdapter(this);

		Log.d(TAG, "onCreate: bn_home = " + R.id.bn_home);
		Log.d(TAG, "onCreate: bn_accounts = " + R.id.bn_accounts);
		Log.d(TAG, "onCreate: bn_cat = " + R.id.bn_cat);
		Log.d(TAG, "onCreate: bn_analysis = " + R.id.bn_analysis);


		if (sharedPreferences.getBoolean("fingerprint", false))
			fingerprint();

//		showFragment(R.id.bn_home);
		actionBar = getSupportActionBar();

		navigationBarView.setOnItemSelectedListener(item -> {
			int id = item.getItemId();
			Log.d(TAG, "onCreate: selected navigation bar id = " + id);
			showFragment(id);

			return true;
		});


		navigationBarView.setSelectedItemId(R.id.bn_home);

		navigationBarView.setOnItemReselectedListener(item -> {
			int id = item.getItemId();
			String print;

			if (id == R.id.bn_home)
				print = "Home";
			else if (id == R.id.bn_cat)
				print = "Category";
			else if (id == R.id.bn_accounts)
				print = "Accounts";
			else
				print = "Analysis";

			if (toast != null)
				toast.cancel();

			toast = Toast.makeText(MainActivity.this, print, Toast.LENGTH_SHORT);
			toast.show();
		});

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		accView = layoutInflater.inflate(R.layout.account_dialog, null);


		EditText ETForAccN = accView.findViewById(R.id.ETDialogAccName);
		EditText ETForAccIB = accView.findViewById(R.id.ETDialogAccBalance);

		TextView accTitle = new TextView(this);
		accTitle.setText("Add New Account");
		accTitle.setGravity(Gravity.CENTER_HORIZONTAL);
		accTitle.setPadding(2, 16, 2, 10);
		accTitle.setTextSize(22);
		accTitle.setTypeface(null, Typeface.BOLD);

		AlertDialog.Builder builder;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
			builder = new AlertDialog.Builder(this, android.R.style.ThemeOverlay_Material_Dialog);
		else
			builder = new AlertDialog.Builder(this);
		builder.setCustomTitle(accTitle)
				.setNegativeButton("Cancel", (dialog, which) -> {

				})
				.setView(accView)
				.setPositiveButton("Add", null);
		AlertDialog dialog = builder.create();
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

		ETForAccN.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				b1 = charSequence.toString().trim().length() != 0;
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b1 && b2);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		ETForAccIB.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				b2 = charSequence.toString().trim().length() != 0;
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b1 && b2);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});


		catView = layoutInflater.inflate(R.layout.category_dialog, null);

		EditText ETForCatN = catView.findViewById(R.id.ETDialogCatName);
		EditText ETForCatIB = catView.findViewById(R.id.ETDialogCatBudget);

		TextView catTitle = catView.findViewById(R.id.TVDialogCT);
		AlertDialog.Builder builder2;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			builder2 = new AlertDialog.Builder(this, android.R.style.ThemeOverlay_Material_Dialog);
		} else
			builder2 = new AlertDialog.Builder(this);
		builder2.setNegativeButton("Cancel", (dialog2, which) -> {

				})
				.setView(catView)
				.setPositiveButton("Add", null);

		AlertDialog dialog2 = builder2.create();
		dialog2.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

		ETForCatN.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				b3 = s.toString().trim().length() != 0;
				dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b3 && b4);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		ETForCatIB.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				b4 = s.toString().trim().length() != 0;
				dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b3 && b4);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		MainActivity mainActivity = this;

		FABAdd.setOnClickListener(v -> {

			systemTimeInMillies = 0;
			if (navigationBarView.getSelectedItemId() == R.id.bn_home) {
				Intent intent = new Intent(this, TransactionActivity.class);
				intent.putExtra("EXTRA_DURATION", 500L);
				intent.putExtra("calendar", Calendar.getInstance().getTimeInMillis());
				intent.putExtra("note", "");
				intent.putExtra("description", "");

				Bundle bundle = new Bundle();
				bundle.putBinder("bind", new ObjectWrapper(mainActivity));
				intent.putExtras(bundle);

				ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, FABAdd, "EXTRA_VIEW_FAB");
				startActivity(intent, options.toBundle());
			} else if (navigationBarView.getSelectedItemId() == R.id.bn_accounts) {
				dialog.show();

				Button del1 = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				del1.setOnClickListener(view -> {
					if (possible(ETForAccIB.getText().toString().trim())) {
						accountViewModel.Insert(new Account(ETForAccN.getText().toString(), 0, Integer.parseInt(ETForAccIB.getText().toString().trim()), icon_account[iconsAdapter.selected]));
						dialog.dismiss();
					} else {
						if (toast != null)
							toast.cancel();

						toast = Toast.makeText(this, "Only 0-9 characters allowed", Toast.LENGTH_SHORT);
						toast.show();
					}
				});
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				ETForAccN.setText("");
				ETForAccIB.setText("");
				ETForAccN.requestFocus();

				iconsAdapter = new IconsAdapter(icon_account);
				RecyclerView recyclerView = accView.findViewById(R.id.rv_icons_account);

				IconsAdapter.ClickListener listener = viewHolder -> {
					int pos = viewHolder.getAdapterPosition();

					int a = iconsAdapter.selected;
					iconsAdapter.selected = pos;

					iconsAdapter.notifyItemChanged(a);
					iconsAdapter.notifyItemChanged(pos);
				};
				iconsAdapter.listener = listener;
				recyclerView.setHasFixedSize(true);
				recyclerView.setAdapter(iconsAdapter);
				recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
			} else if (navigationBarView.getSelectedItemId() == R.id.bn_cat) {
				dialog2.show();
				Button del2 = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
				del2.setOnClickListener(view -> {
					if (possible(ETForCatIB.getText().toString().trim())) {
						int type = categoryFragment.catTabLayout.getSelectedTabPosition() + 1;
						categoryViewModel.Insert(new Category(ETForCatN.getText().toString().trim(), 0, Integer.parseInt(ETForCatIB.getText().toString().trim()), 0, type, icon_category_income[iconsAdapterCat.selected]));
						dialog2.dismiss();
					} else {
						if (toast != null)
							toast.cancel();

						toast = Toast.makeText(this, "Only 0-9 characters allowed", Toast.LENGTH_SHORT);
						toast.show();
					}
				});
				dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				ETForCatN.setText("");
				ETForCatIB.setText("");
				ETForCatN.requestFocus();


				int pos = categoryFragment.catTabLayout.getSelectedTabPosition();
				if (pos == 0) {
					catTitle.setText("Add New Income Category");
					ETForCatIB.setVisibility(View.GONE);
					ETForCatIB.setText("0");
					catView.findViewById(R.id.TVDialogCB).setVisibility(View.GONE);

					iconsAdapterCat = new IconsAdapter(icon_category_income);
					RecyclerView recyclerView = catView.findViewById(R.id.rv_icons_category);

					IconsAdapter.ClickListener listener = viewHolder -> {
						int pos2 = viewHolder.getAdapterPosition();

						int a = iconsAdapterCat.selected;
						iconsAdapterCat.selected = pos2;

						iconsAdapterCat.notifyItemChanged(a);
						iconsAdapterCat.notifyItemChanged(pos2);
					};
					iconsAdapterCat.listener = listener;
					recyclerView.setAdapter(iconsAdapterCat);
					recyclerView.setHasFixedSize(true);
					recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
				} else if (pos == 1) {
					catTitle.setText("Add New Expense Category");
					ETForCatIB.setVisibility(View.VISIBLE);
					catView.findViewById(R.id.TVDialogCB).setVisibility(View.VISIBLE);

					iconsAdapterCat = new IconsAdapter(icon_category_income);
					RecyclerView recyclerView = catView.findViewById(R.id.rv_icons_category);

					IconsAdapter.ClickListener listener = viewHolder -> {
						int pos2 = viewHolder.getAdapterPosition();

						int a = iconsAdapterCat.selected;
						iconsAdapterCat.selected = pos2;

						iconsAdapterCat.notifyItemChanged(a);
						iconsAdapterCat.notifyItemChanged(pos2);
					};
					iconsAdapterCat.listener = listener;
					recyclerView.setAdapter(iconsAdapterCat);
					recyclerView.setHasFixedSize(true);
					recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
				}

				Log.d(TAG, "onCreateView: dialog created");
			}
		});


		FABAdd.setOnLongClickListener(v -> {

			if (FABAdd.isExtended())
				FABAdd.shrink();
			else
				FABAdd.extend();

			return true;
		});


		boolean showNotif = sharedPreferences.getBoolean("notifs", true);

		sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {
			if (key.equals("notifs")) {
				SharedPreferences.Editor editor = sharedPreferences.edit();

				Calendar calendar1 = Calendar.getInstance();
				calendar1.set(Calendar.HOUR_OF_DAY, 20);
				calendar1.set(Calendar.MINUTE, 0);
				calendar1.set(Calendar.SECOND, 0);
				calendar1.set(Calendar.MILLISECOND, 0);

				editor.putLong("notifTime", calendar1.getTimeInMillis());
				editor.apply();

				if (sharedPreferences1.getBoolean(key, true))
					createNotif();
				else
					stopNotif();
			}
		});
	}

	private boolean possible(String trim) {
		int n = trim.length();
		for (int i = -1; ++i < n; ) {
			if (trim.charAt(i) >= '0' && trim.charAt(i) <= '9')
				continue;
			return false;
		}
		return true;
	}

	private void fingerprint() {
		executor = ContextCompat.getMainExecutor(this);
		biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
			@Override
			public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
				super.onAuthenticationError(errorCode, errString);
				if (toast != null)
					toast.cancel();
				toast = Toast.makeText(getApplicationContext(), "User not identified!", Toast.LENGTH_SHORT);
				toast.show();

				systemTimeInMillies = System.currentTimeMillis();
				exit = true;
				onBackPressed();
			}

			@Override
			public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
				super.onAuthenticationSucceeded(result);
				if (toast != null)
					toast.cancel();
				toast = Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT);
				toast.show();
				login = true;
			}

			@Override
			public void onAuthenticationFailed() {
				super.onAuthenticationFailed();
//				if(toast!=null)
//					toast.cancel();
//				toast=Toast.makeText(getApplicationContext(), "Not user!", Toast.LENGTH_SHORT);
//				toast.show();
			}
		});

		promptInfo = new BiometricPrompt.PromptInfo.Builder()
				.setTitle("Biometric Login")
				.setSubtitle("Log in using your biometrics")
				.setNegativeButtonText("Exit")
				.build();

		biometricPrompt.authenticate(promptInfo);
	}


	public void notification() {
		abc = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
			notificationManager = NotificationManagerCompat.from(this);
			createNotificationChannelForOreoAndAbove();
			areNotifAllowed = notificationManager.areNotificationsEnabled();

			notifSwitchPreference.setChecked(areNotifAllowed);
		});

		notificationManager = NotificationManagerCompat.from(this);
		createNotificationChannelForOreoAndAbove();
		areNotifAllowed = notificationManager.areNotificationsEnabled();

		if (!areNotifAllowed) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean("notifs", false);
			editor.apply();
		} else
			createNotif();
	}

	private void createNotif() {

		Intent intent = new Intent(this, Receiver.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		else
			pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		intent.setData((Uri.parse("custom://" + System.currentTimeMillis())));

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		alarmManager.cancel(pendingIntent);
		if (alarmManager != null) {
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sharedPreferences.getLong("notifTime", -1), AlarmManager.INTERVAL_DAY, pendingIntent);
			Log.d(TAG, "createNotif: notiftime = " + sharedPreferences.getLong("notifTime", -1));
		}
	}

	private void stopNotif() {
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(this, "Notif turned off", Toast.LENGTH_SHORT);
		toast.show();
	}

	public void openNotifSettings(SwitchPreference notifSwitchPreference) {
		Intent intent = new Intent();
		intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
		intent.putExtra("app_package", getPackageName());
		intent.putExtra("app_uid", getApplicationInfo().uid);

		intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

		this.notifSwitchPreference = notifSwitchPreference;
		abc.launch(intent);
	}

	public void createNotificationChannelForOreoAndAbove() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notif", NotificationManager.IMPORTANCE_HIGH);
			channel.setDescription("DesForNotifChannel");

			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "onBackPressed: came here");
		Log.d(TAG, "onBackPressed: getBackStackEntryCount = " + getSupportFragmentManager().getBackStackEntryCount());
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			getSupportFragmentManager().popBackStack();
			Fragment fragment = getSupportFragmentManager().findFragmentByTag("repeat");
			if (fragment != null)
				getSupportFragmentManager().beginTransaction().remove(fragment).commit();
			FABAdd.show();
			navigationBarView.setVisibility(View.VISIBLE);
			setActionBarTitle("Expense Manager");
			showMenu();
			actionBar.setDisplayHomeAsUpEnabled(false);
		} else {
			if (navigationBarView.getSelectedItemId() == R.id.bn_home) {
				Log.d(TAG, "onBackPressed: System.currentTimeMillis() - systemTimeInMillies = " + (System.currentTimeMillis() - systemTimeInMillies));
				if (exit || System.currentTimeMillis() - systemTimeInMillies < 2000) {
					super.onBackPressed();
				} else {
					systemTimeInMillies = System.currentTimeMillis();
					if (toast != null)
						toast.cancel();
					toast = Toast.makeText(this, "Press again to exit!", Toast.LENGTH_LONG);
					toast.show();
				}
			} else {
				navigationBarView.setSelectedItemId(R.id.bn_home);
				setActionBarTitle("Expense Manager");
			}
		}
	}

	public void showTransactions() {
		int date = toShow.get(Calendar.DATE);
		int week = toShow.get(Calendar.WEEK_OF_YEAR);
		int month = toShow.get(Calendar.MONTH);
		int year = toShow.get(Calendar.YEAR);

		List<Transaction> showTransactions = new ArrayList<>();

		if (transactions == null)
			return;

		for (int i = -1; ++i < transactions.size(); ) {
			if (viewMode == R.id.RBM) {
				if (transactions.get(i).month == month && transactions.get(i).year == year) {
					showTransactions.add(transactions.get(i));
				}
			} else if (viewMode == R.id.RBW) {
				if (transactions.get(i).week == week && transactions.get(i).year == year) {
					showTransactions.add(transactions.get(i));
				}
			} else if (viewMode == R.id.RBD) {
				if (transactions.get(i).dateOfMonth == date && transactions.get(i).month == month && transactions.get(i).year == year) {
					showTransactions.add(transactions.get(i));
				}
			}
		}

		transactionAdapter.setTransactions(showTransactions);
		transactionAdapter.notifyDataSetChanged();

		homeFragment.TVMainIncome.setText("\u20b9" + moneyToString(income));
		homeFragment.TVMainExpense.setText("\u20b9" + moneyToString(-expense));
		if (income + expense >= 0) {
			homeFragment.TVMainTotal.setText("\u20b9" + moneyToString(income + expense));
			homeFragment.TVMainTotal.setTextColor(Color.parseColor("#4fb85f"));//green
		} else {
			homeFragment.TVMainTotal.setText("\u20b9" + moneyToString(-(income + expense)));
			homeFragment.TVMainTotal.setTextColor(Color.RED);
		}

		if (showTransactions.size() == 0)
			homeFragment.TVNoTransFound.setVisibility(View.VISIBLE);
		else
			homeFragment.TVNoTransFound.setVisibility(View.INVISIBLE);
	}

	public void transactionROOM() {
		transactionViewModel.getAllTransactions().observe(this, transactions -> {
			this.transactions = transactions;

			showTransactions();
		});
	}

	public void subCategoryROOM(int catID) {
		subCategoryViewModel.getSubcategories(catID).observe(this, new Observer<List<SubCategory>>() {
			@Override
			public void onChanged(List<SubCategory> subCats) {
				Log.d(TAG, "onChanged: " + subCats);
				Log.d(TAG, "onChanged: subCats.size = " + subCats.size());
				subCategoryAdapter.setSubCategories(subCats);
				subCategoryAdapter.notifyDataSetChanged();
			}
		});
	}

	public void accountROOM() {

		accountViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
			@Override
			public void onChanged(List<Account> accounts) {
				accountAdapter.setAccounts(accounts);
//				accountAdapter.notifyItemInserted(accounts.size()-1);
				accountAdapter.notifyDataSetChanged();
				Log.d(TAG, "onChanged: account = " + accounts);

				transactionAdapter.setAcc();
				transactionAdapter.notifyDataSetChanged();
			}
		});
	}

	public void subCatROOM() {
		subCategoryViewModel.getAllSubcats().observe(this, new Observer<List<SubCategory>>() {
			@Override
			public void onChanged(List<SubCategory> subCategories) {
				subCategoryAdapter.setAllSubCats(subCategories);

				subcategoriesMap.clear();
				for (int i = -1; ++i < subCategories.size(); ) {
					if (!subcategoriesMap.containsKey(subCategories.get(i).categoryID))
						subcategoriesMap.put(subCategories.get(i).categoryID, new ArrayList<>());

					subcategoriesMap.get(subCategories.get(i).categoryID).add(subCategories.get(i));
				}

				transactionAdapter.setSubcat();
				transactionAdapter.notifyDataSetChanged();
				categoryAdapter.notifyDataSetChanged();
			}
		});
	}

	public void categoryROOM() {

		categoryViewModel.getAllCategories(1).observe(this, new Observer<List<Category>>() {
			@Override
			public void onChanged(List<Category> categories) {
//				int pos1 = posCat(categories, categoryAdapter.categories);
//				Log.d(TAG, "onCreateView: view model called");
//				Log.d(TAG, "onCreateView: type = "+type);
				categoryAdapter.setCategories(categories);
//				categoryAdapter.notifyItemInserted(pos1);
				categoryAdapter.notifyDataSetChanged();

				transactionAdapter.setCat(1);
				transactionAdapter.notifyDataSetChanged();
			}
		});

		categoryViewModel.getAllCategories(2).observe(this, new Observer<List<Category>>() {
			@Override
			public void onChanged(List<Category> categories) {
//				int pos1 = posCat(categories, categoryAdapter2.categories);
//				Log.d(TAG, "onCreateView: view model called");
//				Log.d(TAG, "onCreateView: type = "+type);
				Log.d(TAG, "onChanged: categories2.size()= " + categories.size());
				categoryAdapter2.setCategories(categories);
//				categoryAdapter2.notifyItemInserted(pos1);
				categoryAdapter2.notifyDataSetChanged();

				transactionAdapter.setCat(2);
				transactionAdapter.notifyDataSetChanged();
			}
		});
	}


	private Fragment getFragment(int id) {
		if (id == R.id.bn_home)
			return new HomeFragment();
		else if (id == R.id.bn_cat)
			return new CategoryFragment();
		else if (id == R.id.bn_accounts)
			return new AccountsFragment();
		else
			return new AnalysisFragment();
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return super.onSupportNavigateUp();
	}

	public void setActionBarTitle(String title) {
		actionBar.setTitle(title);
	}

	public void showFragment(int id) {
		FABAdd.hide();

		if (id != R.id.bn_analysis) {
			FABAdd.show();
			FABAdd.shrink();
		}

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
		if (id == R.id.bn_home) {
			homeFragment = new HomeFragment();
			fragmentTransaction.replace(R.id.layoutForFragment, homeFragment).commit();
			Log.d(TAG, "showFragment: home");
			setActionBarTitle("Expense Manager");
			FABAdd.setText("Add Transaction");
		} else if (id == R.id.bn_cat) {
			categoryFragment = new CategoryFragment();
			fragmentTransaction.replace(R.id.layoutForFragment, categoryFragment).commit();
			Log.d(TAG, "showFragment: category");
			systemTimeInMillies = 0;
			setActionBarTitle("Categories ->");
			FABAdd.setText("Add Category");
		} else if (id == R.id.bn_accounts) {
			accountsFragment = new AccountsFragment();
			fragmentTransaction.replace(R.id.layoutForFragment, accountsFragment).commit();
			Log.d(TAG, "showFragment: accounts");
			systemTimeInMillies = 0;
			setActionBarTitle("Expense Manager");
			FABAdd.setText("Add Account");
		} else if (id == R.id.bn_analysis) {
			analysisFragment = new AnalysisFragment();
			fragmentTransaction.replace(R.id.layoutForFragment, analysisFragment).commit();
			Log.d(TAG, "showFragment: analysis");
			systemTimeInMillies = 0;
			setActionBarTitle("Expense Manager");
		} else {
			Log.d(TAG, "showFragment: else");
			if (toast != null)
				toast.cancel();
			toast = Toast.makeText(this, "Slow Down", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public String moneyToString(long money) {
		int a = countDigits(money);
		if (a < 4)
			return String.valueOf(money);
		else {
			char[] c = new char[27];
			for (int i = -1; ++i < 3; ) {
				c[i] = (char) (money % 10 + 48);
				money /= 10;
			}
			a -= 3;

			int b = 0;
			int index = 3;
			while (a > 0) {
				if (b == 0)
					c[index++] = ',';
				c[index++] = (char) (money % 10 + 48);
				money /= 10;
				b ^= 1;
				a--;
			}
			String s = "";
			for (int i = -1; ++i < index; )
				s = c[i] + s;
			return s;
		}
	}

	int countDigits(long l) {
		if (l >= 1000000000000000000L) return 19;
		if (l >= 100000000000000000L) return 18;
		if (l >= 10000000000000000L) return 17;
		if (l >= 1000000000000000L) return 16;
		if (l >= 100000000000000L) return 15;
		if (l >= 10000000000000L) return 14;
		if (l >= 1000000000000L) return 13;
		if (l >= 100000000000L) return 12;
		if (l >= 10000000000L) return 11;
		if (l >= 1000000000L) return 10;
		if (l >= 100000000L) return 9;
		if (l >= 10000000L) return 8;
		if (l >= 1000000L) return 7;
		if (l >= 100000L) return 6;
		if (l >= 10000L) return 5;
		if (l >= 1000L) return 4;
		if (l >= 100L) return 3;
		if (l >= 10L) return 2;
		return 1;
	}

	public long getDate(Calendar calendar) {
		long a = calendar.getTimeInMillis() - calendar.get(Calendar.SECOND) * 1000 - calendar.get(Calendar.MINUTE) * 60000 - calendar.get(Calendar.MILLISECOND) - calendar.get(Calendar.HOUR_OF_DAY) * 3600000;
		return a;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return menuShow;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.addToBackStack("settings");
			fragmentTransaction.replace(R.id.layoutForFragment, settingsFragment)
					.commit();

			FABAdd.hide();
			navigationBarView.setVisibility(View.INVISIBLE);
			hideMenu();
			setActionBarTitle("Settings");

			return true;
		} else if (id == R.id.action_backup) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.addToBackStack("backup");
			fragmentTransaction.replace(R.id.layoutForFragment, new BackupRestoreFragment())
					.commit();

			FABAdd.hide();
			navigationBarView.setVisibility(View.INVISIBLE);
			hideMenu();
			setActionBarTitle("Backup and Restore");

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private int posCat(List<Category> newCategories, List<Category> oldCategories) {

		int i = -1;
		while (++i < oldCategories.size()) {
			if (newCategories.get(i) != oldCategories.get(i))
				return i;
		}
		return i;
	}

	public void tellDate() {
		Log.d(TAG, "tellDate: DATE = " + toShow.get(Calendar.DATE));
		Log.d(TAG, "tellDate: MONTH = " + toShow.get(Calendar.MONTH));
		Log.d(TAG, "tellDate: WEEK_OF_YEAR = " + toShow.get(Calendar.WEEK_OF_YEAR));
		Log.d(TAG, "tellDate: ================================================================");
//		Log.d(TAG, "tellDate: DAY_OF_WEEK = "+toShow.get(Calendar.DAY_OF_WEEK));
	}

	@Override
	protected void onDestroy() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(null);

		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume: called");

		if (!login && sharedPreferences.getBoolean("fingerprint", false))
			fingerprint();

		notificationManager = NotificationManagerCompat.from(getApplicationContext());
		createNotificationChannelForOreoAndAbove();
		areNotifAllowed = notificationManager.areNotificationsEnabled();

//		if(!areNotifAllowed)
//			notifSwitchPreference.setChecked(false);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop: ");
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause: ");
	}

	public void hideMenu() {
		menuShow = false;
		invalidateOptionsMenu();
	}

	public void showMenu() {
		menuShow = true;
		invalidateOptionsMenu();
	}
}
