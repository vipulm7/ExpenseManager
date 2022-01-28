package com.VipulMittal.expensemanager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.dateRoom.DateViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Serializable {

	public static final String TAG="Vipul_tag";

	FloatingActionButton FABAdd;

	public TransactionAdapter transactionAdapter;
	public TransactionViewModel transactionViewModel;
	public AccountAdapter accountAdapter;
	public AccountViewModel accountViewModel;
	public CategoryAdapter categoryAdapter;
	public CategoryViewModel categoryViewModel;
	public SubCategoryAdapter subCategoryAdapter;
	public SubCategoryViewModel subCategoryViewModel;
	public DateViewModel dateViewModel;
	public List<SubCategory> allSubcats;

	NavigationBarView navigationBarView;
	ConstraintLayout layoutForFragment;
	Toast toast;
//	EditText editText;

//	long income, expense, total;
	LiveData<Long> income, expense, total;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		FABAdd=findViewById(R.id.FABAdd);
		navigationBarView=findViewById(R.id.BottomNavigation);
		layoutForFragment=findViewById(R.id.layoutForFragment);

		subCategoryAdapter=new SubCategoryAdapter();
		accountAdapter=new AccountAdapter();
		categoryAdapter=new CategoryAdapter();
		Calendar calendar=Calendar.getInstance();
		allSubcats=new ArrayList<>();

		accountROOM();
		categoryROOM(2);
		subCategoryROOM(0);
		transactionROOM(calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
		subCatROOM();

		transactionAdapter=new TransactionAdapter(this);


		Log.d(TAG, "onCreate: bn_home = "+R.id.bn_home);
		Log.d(TAG, "onCreate: bn_accounts = "+R.id.bn_accounts);
		Log.d(TAG, "onCreate: bn_cat = "+R.id.bn_cat);
		Log.d(TAG, "onCreate: bn_analysis = "+R.id.bn_analysis);

		showFragment(R.id.bn_home);

		navigationBarView.setOnItemSelectedListener(item -> {
			int id=item.getItemId();
			Log.d(TAG, "onCreate: selected = "+id);
			showFragment(id);

			return true;
		});

		navigationBarView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
			@Override
			public void onNavigationItemReselected(@NonNull MenuItem item) {
				int id=item.getItemId();
				String print;

				if(id==R.id.bn_home)
					print="Home";
				else if(id==R.id.bn_cat)
					print="Category";
				else if(id==R.id.bn_accounts)
					print="Accounts";
				else
					print="Analysis";

				if(toast!=null)
					toast.cancel();

				toast=Toast.makeText(MainActivity.this, print, Toast.LENGTH_SHORT);
				toast.show();
			}
		});

		EditText ETForAccAdd=new EditText(this);

		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Add New Account")
				.setNegativeButton("Cancel", (dialog, which) -> {

				})
				.setView(ETForAccAdd)
				.setPositiveButton("Add", (dialog, which) -> {
					accountViewModel.Insert(new Account(ETForAccAdd.getText().toString(),0));
				});
		AlertDialog dialog = builder.create();



		ETForAccAdd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(charSequence.toString().trim().length() != 0);
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		ETForAccAdd.setHint("Add Account name");

		FABAdd.setOnClickListener(v->{

			if(navigationBarView.getSelectedItemId()==R.id.bn_home)
			{
				TransactionFragment transactionFragment=new TransactionFragment(0,"","",Calendar.getInstance(), -1,-1,-1,1,2);
				FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.layoutForFragment, transactionFragment);
				fragmentTransaction.addToBackStack("home_page");
				fragmentTransaction.commit();

				FABAdd.hide();
			}

			else if(navigationBarView.getSelectedItemId()==R.id.bn_accounts)
			{
				((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

				ETForAccAdd.setText("");
				ETForAccAdd.requestFocus();
				dialog.show();
			}
		});

	}

	@Override
	public void onBackPressed() {
		if(navigationBarView.getSelectedItemId()==R.id.bn_home)
			super.onBackPressed();
		else
			navigationBarView.setSelectedItemId(R.id.bn_home);
	}

	public void transactionROOM(int month, int year) {
		transactionViewModel=new ViewModelProvider(this).get(TransactionViewModel.class);
		transactionViewModel.getAllTransactions(month, year).observe(this, transactions -> {
			transactionAdapter.setTransactions(transactions);
			transactionAdapter.notifyDataSetChanged();
			Log.d(TAG, "transactionROOM: transactions = "+transactions.size());
		});
	}

	public void subCategoryROOM(int catID) {
		subCategoryViewModel=new ViewModelProvider(this).get(SubCategoryViewModel.class);
		subCategoryViewModel.getSubs(catID).observe(this, new Observer<List<SubCategory>>() {
			@Override
			public void onChanged(List<SubCategory> subCats) {
				Log.d(TAG, "onChanged: "+subCats);
				subCategoryAdapter.setSubCats(subCats);
				subCategoryAdapter.notifyDataSetChanged();
			}
		});
	}

	public void accountROOM() {
		accountViewModel=new ViewModelProvider(this).get(AccountViewModel.class);
		accountViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
			@Override
			public void onChanged(List<Account> accounts) {
				accountAdapter.setAccounts(accounts);
				accountAdapter.notifyItemInserted(accounts.size()-1);
				Log.d(TAG, "onChanged: account = "+accounts);

				transactionAdapter.setAcc();
				transactionAdapter.notifyDataSetChanged();
			}
		});
	}

	public void subCatROOM()
	{
		subCategoryViewModel.getAllSubcats().observe(this, new Observer<List<SubCategory>>() {
			@Override
			public void onChanged(List<SubCategory> subCategories) {
				subCategoryAdapter.setAllSubCats(subCategories);

				transactionAdapter.setSubcat();
				transactionAdapter.notifyDataSetChanged();
			}
		});
	}

	public void categoryROOM(int type) {
		categoryViewModel=new ViewModelProvider(this).get(CategoryViewModel.class);
		categoryViewModel.getAllCategories(type).observe(this, new Observer<List<Category>>() {
			@Override
			public void onChanged(List<Category> categories) {
				int pos1= posCat(categories, categoryAdapter.categories);
//				Log.d(TAG, "onCreateView: view model called");
//				Log.d(TAG, "onCreateView: type = "+type);
				categoryAdapter.setCategories(categories);
				categoryAdapter.notifyItemInserted(pos1);

				transactionAdapter.setCat();
				transactionAdapter.notifyDataSetChanged();
			}
		});
	}

	private Fragment getFragment(int id) {
		if(id==R.id.bn_home)
			return new HomeFragment();
		else if(id==R.id.bn_cat)
			return new CategoryFragment();
		else if(id==R.id.bn_accounts)
			return new AccountsFragment();
		else
			return new AnalysisFragment();
	}

	public void setActionBarTitle(String title)
	{
		getSupportActionBar().setTitle(title);
	}

	public void showFragment(int id) {
//		navigationBarView.setSelectedItemId(id);
//		if(id==R.id.bn_analysis)
		FABAdd.hide();
		if(id!=R.id.bn_analysis)
			FABAdd.show();


		FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
		if(id==R.id.bn_home)
			fragmentTransaction.replace(R.id.layoutForFragment,new HomeFragment()).commit();
		else if(id==R.id.bn_cat)
			fragmentTransaction.replace(R.id.layoutForFragment,new CategoryFragment()).commit();
		else if(id==R.id.bn_accounts)
			fragmentTransaction.replace(R.id.layoutForFragment,new AccountsFragment()).commit();
		else
			fragmentTransaction.replace(R.id.layoutForFragment,new AnalysisFragment()).commit();
//		getSupportFragmentManager()
//				.beginTransaction()
//				.replace(R.id.layoutForFragment,fragment, fragment.getClass().getSimpleName())
//				.commit();
	}

	private String moneyToString(long money) {
		int a=countDigits(money);
		if(a<4)
			return ""+money;
		else
		{
			char c[]=new char[27];
			for(int i=-1;++i<3;)
			{
				c[i]=(char)(money%10+48);
				money/=10;
			}
			a-=3;

			int b=0;
			int index=3;
			for(;a>0;)
			{
				if(b==0)
					c[index++]=',';
				c[index++]=(char)(money%10+48);
				money/=10;
				b^=1;
				a--;
			}
			String s="";
			for(int i=-1;++i<index;)
				s=c[i]+s;
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

	public long getDate(Calendar calendar)
	{
		long a=calendar.getTimeInMillis()-calendar.get(Calendar.SECOND)*1000-calendar.get(Calendar.MINUTE)*60000-calendar.get(Calendar.MILLISECOND)-calendar.get(Calendar.HOUR_OF_DAY)*3600000;
		return a/1000L;
	}


	private int posCat(List<Category> newCategories, List<Category> oldCategories) {

		int i=-1;
		for(;++i<oldCategories.size();)
		{
			if(newCategories.get(i) != oldCategories.get(i))
				return i;
		}
		return i;
	}
}
