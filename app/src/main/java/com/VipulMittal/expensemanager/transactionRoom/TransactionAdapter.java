package com.VipulMittal.expensemanager.transactionRoom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransViewHolder> {

    List<Transaction> transactions;
    MainActivity mainActivity;
    private onItemCLickListener listener;

    public TransactionAdapter(MainActivity mainActivity) {
        transactions = new ArrayList<>();
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public TransViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout_per_item, parent, false);
        return new TransViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransViewHolder holder, int position) {
        Transaction transaction=transactions.get(position);

//        holder.TVAccount
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }



    public void setTransactions(List<Transaction> transactions)
    {
        this.transactions=transactions;
//        notifyDataSetChanged();
    }

    public class TransViewHolder extends RecyclerView.ViewHolder
    {
        TextView TVCat, TVSubCat, TVNote, TVAccount, TVAmount;
        public TransViewHolder(@NonNull View itemView) {
            super(itemView);
            TVAmount=itemView.findViewById(R.id.TVLayAmt);
            TVNote=itemView.findViewById(R.id.TVLayNote);
            TVSubCat=itemView.findViewById(R.id.TVLaySubCat);
            TVCat=itemView.findViewById(R.id.TVLayCat);
            TVAccount=itemView.findViewById(R.id.TVLayAcc);

            itemView.setOnClickListener(view -> {
                int position=getAdapterPosition();
                if(listener!=null && position!=RecyclerView.NO_POSITION) //RecyclerView.NO_POSITION is equal to -1
                    listener.onItemClick(this);
            });
        }
    }

    public interface onItemCLickListener {
        void onItemClick(TransViewHolder transViewHolder);
    }

    public void setOnItemClickListener(onItemCLickListener listener)
    {
        this.listener=listener;
    }
}