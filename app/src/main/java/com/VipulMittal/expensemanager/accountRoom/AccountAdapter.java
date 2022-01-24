package com.VipulMittal.expensemanager.accountRoom;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccViewHolder> {
	ClickListener listener;
	public List<Account> accounts;
	String TAG="Vipul_tag";
	int selected;

	public AccountAdapter(int selected, ClickListener listener) {
		this.listener=listener;
		accounts=new ArrayList<>();
		this.selected=selected;
	}

	@NonNull
	@Override
	public AccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_account_layout_per_item, parent, false);
		return new AccViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AccViewHolder holder, int position) {
		Log.d(TAG, "onBindViewHolder: "+position);
		if(position==selected)
			holder.name.setBackgroundColor(Color.CYAN);
		holder.name.setText(accounts.get(position).name);
	}

	@Override
	public int getItemCount() {
		return accounts.size();
	}


	public class AccViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;
		public AccViewHolder(@NonNull View itemView) {
			super(itemView);
			name=itemView.findViewById(R.id.BSD_Acc);

			itemView.setOnClickListener(v->{
				int position=getAdapterPosition();
				Log.d(TAG, "AccViewHolder: pos = "+position);
				Log.d(TAG, "AccViewHolder: listener = "+listener);
				if(listener!=null) {
					listener.onItemClick(this);
					Log.d(TAG, "AccViewHolder: listener set for pos = "+position);
				}
			});
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
