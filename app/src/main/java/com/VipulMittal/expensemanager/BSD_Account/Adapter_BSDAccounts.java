package com.VipulMittal.expensemanager.BSD_Account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.accountRoom.Account;

import java.util.ArrayList;
import java.util.List;

public class Adapter_BSDAccounts extends RecyclerView.Adapter<Adapter_BSDAccounts.BSDAccViewHolder> {
	ClickListener listener;
	List<Account> accounts;

	@NonNull
	@Override
	public BSDAccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_layout_per_item, parent, false);
		return new BSDAccViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BSDAccViewHolder holder, int position) {
		holder.name.setText(accounts.get(position).name);
	}

	@Override
	public int getItemCount() {
		return accounts.size();
	}


	public class BSDAccViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;
		public BSDAccViewHolder(@NonNull View itemView) {
			super(itemView);
			name=itemView.findViewById(R.id.BSD_Acc);

			itemView.setOnClickListener(v->{
				int posititon=getAdapterPosition();
				if(listener!=null && posititon!=-1)
					itemView.setOnClickListener(view -> {
						listener.onItemClick(this);
					});
			});
		}
	}

	public interface ClickListener
	{
		void onItemClick(BSDAccViewHolder viewHolder);
	}
}
