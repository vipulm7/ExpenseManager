package com.VipulMittal.expensemanager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.dateRoom.Date;
import com.VipulMittal.expensemanager.dateRoom.DateViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

	public static final String TAG="Vipul_tag";

	FloatingActionButton FABAdd;

	TransactionAdapter transactionAdapter;
	TransactionViewModel transactionViewModel;
	AccountAdapter accountAdapter;
	AccountViewModel accountViewModel;
	CategoryAdapter categoryAdapter;
	CategoryViewModel categoryViewModel;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	DateViewModel dateViewModel;

	NavigationBarView navigationBarView;
	ConstraintLayout layoutForFragment;
	Toast toast;

	long income, expense, total;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		FABAdd=findViewById(R.id.FABAdd);
		transactionAdapter=new TransactionAdapter(this);
		navigationBarView=findViewById(R.id.BottomNavigation);
		layoutForFragment=findViewById(R.id.layoutForFragment);


		showFragment(new HomeFragment(),R.id.bn_home);

		navigationBarView.setOnItemSelectedListener(item -> {
			int id=item.getItemId();
			showFragment(getFragment(id),id);

			return false;
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






		transactionViewModel=new ViewModelProvider(this).get(TransactionViewModel.class);
		transactionViewModel.getAllData().observe(this, transactions -> {
			transactionAdapter.setTransactions(transactions);
			transactionAdapter.notifyDataSetChanged();
		});







		ActivityResultLauncher<Intent> arl=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
				  new ActivityResultCallback<ActivityResult>() {
					  @Override
					  public void onActivityResult(ActivityResult result) {
						  if(result.getResultCode() == Activity.RESULT_OK)
						  {
						  	Intent data=result.getData();

						  	Transaction transaction=new Transaction("",1,"1",
									  "","","","",100000, 2);

						  	transactionViewModel.Insert(transaction);
						  }
						  else if(result.getResultCode() == -2)
						  {

						  }
						  else if(result.getResultCode() == Activity.RESULT_CANCELED)
						  {
						     if(toast!=null)
						        toast.cancel();
						     toast=Toast.makeText(MainActivity.this,"Cancelled",Toast.LENGTH_SHORT);
						     toast.show();
						  }
					  }
				  });

		FABAdd.setOnClickListener(v->{
			Intent intent=new Intent(MainActivity.this, TransactionActivity.class);
			intent.putExtra("amount",0);
			intent.putExtra("note","");
			intent.putExtra("description","");
			Calendar calendar=Calendar.getInstance();
			Bundle bundle=new Bundle();
			bundle.putSerializable("date",calendar);
//			bundle.put("main", this);
			intent.putExtra("bundle",bundle);
			Log.d(TAG, "onCreate: Adapter passed");
			intent.putExtra("account",-1);
			intent.putExtra("cat",-1);
			intent.putExtra("subCat",-1);
			intent.putExtra("request",1);
			intent.putExtra("type",2);

			arl.launch(intent);
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

	public void showFragment(Fragment fragment, int id) {
		navigationBarView.setSelectedItemId(id);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.layoutForFragment,fragment, fragment.getClass().getSimpleName())
				.commit();
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
}
