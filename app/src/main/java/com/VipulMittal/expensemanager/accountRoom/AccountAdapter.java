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
	public ClickListener listener, deleteListener;
	public List<Account> accounts;
	public int aID;
	public int who;
	String TAG = "Vipul_tag";

	public AccountAdapter() {
		accounts = new ArrayList<>();
	}

	@NonNull
	@Override
	public AccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view;
		if (who == 1)
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_account_layout_per_item, parent, false);
		else
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_layout_per_item, parent, false);
		return new AccViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AccViewHolder holder, int position) {
		Log.d(TAG, "onBindViewHolder: accounts name = " + accounts.get(position).name + " id = " + accounts.get(position).id + " amt = " + accounts.get(position).amount + " bal = " + accounts.get(position).initialBalance);
		int amt = accounts.get(position).amount + accounts.get(position).initialBalance;
		if (who == 1) {
			if (accounts.get(position).id == aID) {
				holder.name.setBackgroundColor(Color.parseColor("#C6E6E8"));//cyan
				holder.amount.setBackgroundColor(Color.parseColor("#C6E6E8"));//cyan
//				holder.imageView.setBackgroundColor(Color.CYAN);
			} else {
				holder.name.setBackgroundResource(R.drawable.bsd);
				holder.amount.setBackgroundResource(R.drawable.bsd);
//				holder.imageView.setBackgroundResource(R.drawable.bsd);
			}
			holder.name.setText(accounts.get(position).name);
			if (amt < 0) {
				holder.amount.setTextColor(Color.RED);
				holder.amount.setText("- \u20b9" + moneyToString(-amt));
			} else {
				holder.amount.setTextColor(Color.parseColor("#4fb85f"));//green
				holder.amount.setText("  \u20b9" + moneyToString(amt));
			}
		} else {
			holder.name.setText(accounts.get(position).name);
//			holder.name.setBackgroundColor(Color.BLUE);

			if (amt < 0) {
				holder.amount.setTextColor(Color.RED);
				holder.amount.setText("- \u20b9 " + moneyToString(-amt));
			} else {
				holder.amount.setTextColor(Color.parseColor("#4fb85f"));//green
				holder.amount.setText("  \u20b9 " + moneyToString(amt));
			}
//			holder.amount.setText("\u20b91234567890123456789012345678901234567890");
		}
		holder.imageView.setImageResource(accounts.get(position).imageId);
	}

	@Override
	public int getItemCount() {
		return accounts.size();
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

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public interface ClickListener {
		void onItemClick(AccViewHolder viewHolder);
	}

	public class AccViewHolder extends RecyclerView.ViewHolder {
		TextView name, amount, accDel;
		ImageView imageView;

		public AccViewHolder(@NonNull View itemView) {
			super(itemView);
			if (who == 1) {
				name = itemView.findViewById(R.id.BSD_Acc);
				amount = itemView.findViewById(R.id.BSD_Acc_amt);
				imageView = itemView.findViewById(R.id.IVAccountsLPI);

				itemView.setOnClickListener(v -> {
					int position = getAdapterPosition();
					Log.d(TAG, "AccViewHolder: pos = " + position);
					Log.d(TAG, "AccViewHolder: listener = " + listener);
					if (listener != null) {
						listener.onItemClick(this);
						Log.d(TAG, "AccViewHolder: listener set for pos = " + position);
					}
				});
			} else {
				name = itemView.findViewById(R.id.TVAccounts_nameLPI);
				amount = itemView.findViewById(R.id.TVAccounts_amountLPI);
				imageView = itemView.findViewById(R.id.IVAccountsLPI);
				accDel = itemView.findViewById(R.id.BAccDel);

				itemView.setOnClickListener(v -> {
					if (listener != null) {
						listener.onItemClick(this);
					}
				});

				accDel.setOnClickListener(v -> {
					if (deleteListener != null)
						deleteListener.onItemClick(this);
				});


			}
		}
	}
}
