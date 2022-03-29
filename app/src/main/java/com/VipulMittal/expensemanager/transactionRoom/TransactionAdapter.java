package com.VipulMittal.expensemanager.transactionRoom;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransViewHolder> {

	public List<Transaction> transactions, transactionsToBeDeleted;
	public CLickListener listener, longListener;
	public static final String TAG="Vipul_tag";
	MainActivity mainActivity;
	AccountAdapter accountAdapter;
	SubCategoryAdapter subCategoryAdapter;
	public Map<Integer, String> acc;
	public Map<Integer, String> cat;
	public Map<Integer, String> subcat;
	public int dates;
	LayoutInflater inflater;
	public boolean selectionModeOn;
	public boolean[] select;

	public TransactionAdapter(MainActivity mainActivity) {
		transactions = new ArrayList<>();
		transactionsToBeDeleted = new ArrayList<>();
		this.mainActivity=mainActivity;
		accountAdapter= mainActivity.accountAdapter;
		subCategoryAdapter= mainActivity.subCategoryAdapter;
		acc=new HashMap<>();
		cat=new HashMap<>();
		subcat=new HashMap<>();
	}

	@NonNull
	@Override
	public TransViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view;
		Log.d(TAG, "onCreateViewHolder: viewType = "+viewType);

		if(inflater==null)
			inflater = LayoutInflater.from(parent.getContext());

		if(viewType == 1)
			view= inflater.inflate(R.layout.transaction_date_layout_per_item, parent, false);
		else
			view= inflater.inflate(R.layout.transaction_layout_per_item, parent, false);
		return new TransViewHolder(view, viewType);
	}

	@Override
	public void onBindViewHolder(@NonNull TransViewHolder holder, int position) {
		Transaction transaction=transactions.get(position);
//        Log.d(TAG, "onBindViewHolder: transaction cid = "+transaction.catID+" date"+transaction.date);

		Log.d(TAG, "onBindViewHolder: transaction name="+transaction.note+ "cid="+transaction.catID+" sid="+transaction.subCatID);

		if(holder.getItemViewType() == 1)
		{
			Calendar calendar=Calendar.getInstance();
			calendar.setTimeInMillis(transaction.date);
			int d=calendar.get(Calendar.DATE);
			int m=calendar.get(Calendar.MONTH);
			int y=calendar.get(Calendar.YEAR);
			String date=d+" "+getM(m)+", "+y;
			holder.TVCat.setText(date);
			if(!transactions.get(position).note.equals("0"))
				holder.TVSubCat.setText("Income : \u20b9"+transactions.get(position).note);
			else
				holder.TVSubCat.setText("");
			holder.TVSubCat.setTextColor(Color.parseColor("#4fb85f"));//green

			if(!transactions.get(position).description.equals("0"))
				holder.TVAmount.setText("Expense : \u20b9"+transactions.get(position).description);
			else
				holder.TVAmount.setText("");
			holder.TVAmount.setTextColor(Color.RED);

			Log.d(TAG, "onBindViewHolder: Tnote = "+transaction.note);
			Log.d(TAG, "onBindViewHolder: Ttime = "+transaction.date);
		}
		else
		{
			if(transaction.subCatID!=-1)
				holder.TVSubCat.setText(subcat.get(transaction.subCatID));
			else
				holder.TVSubCat.setText("");
			holder.TVNote.setText(transaction.note);
			if(transaction.amount>=0) {
				holder.TVAmount.setText("\u20b9"+mainActivity.moneyToString(transaction.amount));
				holder.TVAmount.setTextColor(Color.parseColor("#4fb85f"));//green
			}
			else
			{
				holder.TVAmount.setText("- \u20b9"+mainActivity.moneyToString(-transaction.amount));
				holder.TVAmount.setTextColor(Color.RED);
			}
			Log.d(TAG, "onBindViewHolder: note = "+transaction.note);
			Log.d(TAG, "onBindViewHolder: cat map = "+cat);
			Log.d(TAG, "onBindViewHolder: cat id "+transaction.catID);
			Log.d(TAG, "onBindViewHolder: cat from map "+cat.get(transaction.catID));

			if(transaction.type!=3) {
				holder.TVCat.setText(cat.get(transaction.catID));
				holder.TVAccount.setText(acc.get(transaction.accountID));
			}
			else {
				holder.TVCat.setText("Transfer");
				holder.TVAccount.setText(acc.get(transaction.accountID) +" -> "+acc.get(transaction.catID));
				holder.TVAmount.setTextColor(Color.BLUE);
				holder.TVAmount.setText("\u20b9"+mainActivity.moneyToString(-transaction.amount));
			}

//			Log.d(TAG, "onBindViewHolder: holder.selected = "+select[position]);
//			Log.d(TAG, "onBindViewHolder: holderv = "+holder);

			if(selectionModeOn)
			{
				if(position==transactions.size()-1 || transactions.get(position+1).catID==-1)
				{
					if(select[position])
						holder.view.setBackgroundResource(R.drawable.rc_below_selected);
					else
						holder.view.setBackgroundResource(R.drawable.rc_below);
				}
				else
				{
					if(select[position])
						holder.view.setBackgroundResource(R.drawable.rc_mid_selected);
					else
						holder.view.setBackgroundResource(R.drawable.rc_mid);
				}
			}
			else
			{
				if(position==transactions.size()-1 || transactions.get(position+1).catID==-1)
					holder.view.setBackgroundResource(R.drawable.rc_below);
				else
					holder.view.setBackgroundResource(R.drawable.rc_mid);
			}


		}
	}

	private String getM(int m) {
		String a[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		return a[m];
	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount: transactions.size() = "+transactions.size());
		return transactions.size();
	}

	@Override
	public int getItemViewType(int position) {
		Log.d(TAG, "getItemViewType: transactions.get(position).catID = "+transactions.get(position).catID);
		if(transactions.get(position).id==-1)
			return 1;
		else
			return 2;
	}


	public class TransViewHolder extends RecyclerView.ViewHolder
	{
		TextView TVCat, TVSubCat, TVNote, TVAccount, TVAmount;
		View view;

		public TransViewHolder(@NonNull View itemView, int viewType) {
			super(itemView);
			Log.d(TAG, "TransViewHolder: getItemViewType = "+getItemViewType());

			if(viewType == 1)
			{
				TVCat = itemView.findViewById(R.id.TD_date_lpi);
				TVSubCat = itemView.findViewById(R.id.TD_income_lpi);
				TVAmount = itemView.findViewById(R.id.TD_expense_lpi);
			} else {
				TVAmount = itemView.findViewById(R.id.TVLayAmt);
				TVNote = itemView.findViewById(R.id.TVLayNote);
				TVSubCat = itemView.findViewById(R.id.TVLaySubCat);
				TVCat = itemView.findViewById(R.id.TVLayCat);
				TVAccount = itemView.findViewById(R.id.TVLayAcc);
				view = itemView;

				itemView.setOnClickListener(view -> {
					int position=getAdapterPosition();
					if(listener!=null && position!=RecyclerView.NO_POSITION) //RecyclerView.NO_POSITION is equal to -1
						listener.onItemClick(this);
				});

				itemView.setOnLongClickListener((View.OnLongClickListener) v -> {
					if(longListener != null) {
						longListener.onItemClick(this);
						return true;
					}
					return false;
				});
			}
		}

		public void changeSelection()
		{
			int position = getAdapterPosition();
			Transaction transaction = transactions.get(position);

			select[position] = ! select[position];
			if(select[position])
				transactionsToBeDeleted.add(transaction);
			else
				transactionsToBeDeleted.remove(transaction);

			mainActivity.homeFragment.actionMode.setTitle(""+transactionsToBeDeleted.size());
			notifyItemChanged(position);
		}

	}

	//    android:background="@drawable/icon_account_not_selected"
	public interface CLickListener {
		void onItemClick(TransViewHolder viewHolder);
	}

	public void setTransactions(List<Transaction> transactions)
	{
		this.transactions=transactions;
		dates=0;

		int ex=0,in=0;
		if(transactions.size()>0) {
			long d = transactions.get(transactions.size() - 1).date;
			long amt[] = new long[2];
			for (int i = transactions.size(); --i >= 0; )
			{
				if (d != transactions.get(i).date)
				{
					Transaction transaction = new Transaction(""+mainActivity.moneyToString(amt[0]),0,0,-1,0,""+mainActivity.moneyToString(-amt[1]),0,d,0);
					transaction.id=-1;
					transactions.add(i+1, transaction);
					dates++;
					d = transactions.get(i).date;
					amt[0]=amt[1]=0;
				}
				if(transactions.get(i).type==3)
					continue;
				if(transactions.get(i).amount>=0) {
					amt[0] += transactions.get(i).amount;
					in+=transactions.get(i).amount;
				}
				else {
					amt[1] += transactions.get(i).amount;
					ex+=transactions.get(i).amount;
				}
			}
			Transaction transaction = new Transaction(""+mainActivity.moneyToString(amt[0]),0,0,-1,0,""+mainActivity.moneyToString(-amt[1]),0,d,0);
			transaction.id=-1;
			transactions.add(0, transaction);
			dates++;
		}
		mainActivity.income=in;
		mainActivity.expense=ex;

//        Log.d(TAG, "acc="+accountAdapter.accounts.size()+"  cat="+categoryAdapter.categories.size()+"  subcat="+subCategoryAdapter.subCategories.size());
	}

	public void setAcc() {
//        acc.clear();
		for(int i=-1;++i<accountAdapter.accounts.size();)
			acc.put(accountAdapter.accounts.get(i).id, accountAdapter.accounts.get(i).name);
	}

	public void setCat(int type)
	{
		CategoryAdapter categoryAdapter;
//        cat.clear();
		if(type == 1)
		{
			categoryAdapter=mainActivity.categoryAdapter;
			for (int i = -1; ++i < categoryAdapter.categories.size(); )
				cat.put(categoryAdapter.categories.get(i).catId, categoryAdapter.categories.get(i).catName);
		}
		else {
			categoryAdapter=mainActivity.categoryAdapter2;
			for (int i = -1; ++i < categoryAdapter.categories.size(); )
				cat.put(categoryAdapter.categories.get(i).catId, categoryAdapter.categories.get(i).catName);
		}
	}

	public void setSubcat()
	{
//        subcat.clear();
		for(int i=-1;++i<subCategoryAdapter.allSubCats.size();)
			subcat.put(subCategoryAdapter.allSubCats.get(i).id, subCategoryAdapter.allSubCats.get(i).name);
	}
}
