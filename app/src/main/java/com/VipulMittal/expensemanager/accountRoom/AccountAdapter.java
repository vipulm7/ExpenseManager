package com.VipulMittal.expensemanager.accountRoom;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccViewHolder> {
	public ClickListener listener;
	public List<Account> accounts;
	String TAG="Vipul_tag";
	public int selected;
	public int who;

	public AccountAdapter() {
		accounts=new ArrayList<>();
	}

	@NonNull
	@Override
	public AccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view;
		if(who==1)
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_account_layout_per_item, parent, false);
		else
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_layout_per_item, parent, false);
		return new AccViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AccViewHolder holder, int position) {
		Log.d(TAG, "onBindViewHolder: "+position);
		if(who==1) {
			if (position == selected)
				holder.name.setBackgroundColor(Color.CYAN);
			else
				holder.name.setBackgroundColor(Color.WHITE);
			holder.name.setText(accounts.get(position).name);
		}
		else
		{
			holder.name.setText(accounts.get(position).name);
//			holder.name.setBackgroundColor(Color.BLUE);
			if(accounts.get(position).amount<0)
				holder.amount.setTextColor(Color.RED);
			else
				holder.amount.setTextColor(Color.GREEN);
//			holder.amount.setText("\u20b91234567890123456789012345678901234567890");
			holder.amount.setText("\u20b9"+accounts.get(position).amount);
		}
	}

	@Override
	public int getItemCount() {
		return accounts.size();
	}


	public class AccViewHolder extends RecyclerView.ViewHolder
	{
		TextView name, amount;
		ImageView imageView;
		public AccViewHolder(@NonNull View itemView) {
			super(itemView);
			if(who==1) {
				name = itemView.findViewById(R.id.BSD_Acc);

				itemView.setOnClickListener(v -> {
					int position = getAdapterPosition();
					Log.d(TAG, "AccViewHolder: pos = " + position);
					Log.d(TAG, "AccViewHolder: listener = " + listener);
					if (listener != null) {
						listener.onItemClick(this);
						Log.d(TAG, "AccViewHolder: listener set for pos = " + position);
					}
				});
			}
			else
			{
				name=itemView.findViewById(R.id.TVAccounts_nameLPI);
				amount=itemView.findViewById(R.id.TVAccounts_amountLPI);
				imageView=itemView.findViewById(R.id.IVAccountsLPI);

//				itemView.setOnClickListener();
			}
		}
	}

	public interface ClickListener
	{
		void onItemClick(AccViewHolder viewHolder);
	}

	public void setAccounts(List<Account> accounts)
	{
		this.accounts=accounts;
	}
}
