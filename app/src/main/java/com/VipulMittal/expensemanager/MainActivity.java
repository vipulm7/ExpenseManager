package com.VipulMittal.expensemanager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

	public static final String TAG="Vipul_tag";
	TextView TVMainExpense,TVMainIncome,TVMainTotal;
	FloatingActionButton FABAdd;
	RecyclerView RVTransactions;
	TransactionAdapter transactionAdapter;
	TransactionViewModel transactionViewModel;

	Toast toast;

	long income, expense, total;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TVMainIncome=findViewById(R.id.TVIncomeAmt);
		TVMainExpense=findViewById(R.id.TVExpenseAmt);
		TVMainTotal=findViewById(R.id.TVTotalAmt);
		FABAdd=findViewById(R.id.FABAdd);
		RVTransactions =findViewById(R.id.RecyclerViewID);
		RVTransactions.setLayoutManager(new LinearLayoutManager(this));
		transactionAdapter=new TransactionAdapter(this);
		RVTransactions.setAdapter(transactionAdapter);
		RVTransactions.setNestedScrollingEnabled(false);




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
