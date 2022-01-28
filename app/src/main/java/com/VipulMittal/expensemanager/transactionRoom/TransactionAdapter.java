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
import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransViewHolder> {

    public List<Transaction> transactions;
    public onItemCLickListener listener;
    public static final String TAG="Vipul_tag";
    MainActivity mainActivity;
    CategoryAdapter categoryAdapter;
    AccountAdapter accountAdapter;
    SubCategoryAdapter subCategoryAdapter;
    Map<Integer, String> acc;
    Map<Integer, String> cat;
    Map<Integer, String> subcat;

    public TransactionAdapter(MainActivity mainActivity) {
        transactions = new ArrayList<>();
        this.mainActivity=mainActivity;
        categoryAdapter=mainActivity.categoryAdapter;
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

        if(viewType == 1)
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_date_layout_per_item, parent, false);
        else
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout_per_item, parent, false);
        return new TransViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TransViewHolder holder, int position) {
        Transaction transaction=transactions.get(position);
        Log.d(TAG, "onBindViewHolder: "+transaction.catID+" "+transaction.date);

        if(holder.getItemViewType() == 1)
        {
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(transaction.date);
            int d=calendar.get(Calendar.DATE);
            int m=calendar.get(Calendar.MONTH);
            int y=calendar.get(Calendar.YEAR);
            String date=d+" "+getM(m)+", "+y;
            holder.TVCat.setText(date);
            holder.TVSubCat.setText("\u20b9"+transactions.get(position).note);
            holder.TVSubCat.setTextColor(Color.GREEN);
            holder.TVAmount.setText("\u20b9"+transactions.get(position).description);
            holder.TVAmount.setTextColor(Color.RED);
        }
        else
        {
            if(transaction.subCatID!=-1) {
//                holder.TVSubCat.setText(subcat.get(subCategoryAdapter.subCats.get(position).id));
                holder.TVSubCat.setText(subcat.get(transaction.subCatID));
            }
            holder.TVNote.setText(transaction.note);
            if(transaction.amount>=0) {
                holder.TVAmount.setText("\u20b9"+transaction.amount);
                holder.TVAmount.setTextColor(Color.GREEN);
            }
            else
            {
                holder.TVAmount.setText("- \u20b9"+(-transaction.amount));
                holder.TVAmount.setTextColor(Color.RED);
            }
//            holder.TVCat.setText(""+cat.get(categoryAdapter.categories.get(position).catId));
//            holder.TVAccount.setText(""+acc.get(accountAdapter.accounts.get(position).id));



            holder.TVCat.setText("cat");
            holder.TVAccount.setText("acc");


            holder.TVCat.setText(cat.get(transaction.catID));
            holder.TVAccount.setText(acc.get(transaction.accountID));
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
        if(transactions.get(position).catID==-1)
            return 1;
        else
            return 2;
    }


    public void setTransactions(List<Transaction> transactions)
    {
        this.transactions=transactions;

        if(transactions.size()>0) {
            long d = transactions.get(transactions.size() - 1).date;
            long amt[] = new long[2];
            for (int i = transactions.size(); --i >= 0; )
            {
                if (d != transactions.get(i).date)
                {
                    transactions.add(i+1,new Transaction(""+amt[0],0,"",0,-1,0,""+(-amt[1]),0,d));
                    d = transactions.get(i).date;
                    amt[0]=amt[1]=0;
                }
                if(transactions.get(i).amount>=0)
                    amt[0]+=transactions.get(i).amount;
                else
                    amt[1]+=transactions.get(i).amount;
            }
            transactions.add(0,new Transaction(""+amt[0],0,"",0,-1,0,""+(-amt[1]),0,d));
        }

        Log.d(TAG, "acc="+accountAdapter.accounts.size()+"  cat="+categoryAdapter.categories.size()+"  subcat="+subCategoryAdapter.subCats.size());
    }

    public void setAcc() {
        acc.clear();
        for(int i=-1;++i<accountAdapter.accounts.size();)
            acc.put(accountAdapter.accounts.get(i).id, accountAdapter.accounts.get(i).name);
    }

    public void setCat()
    {
        cat.clear();
        for(int i=-1;++i<categoryAdapter.categories.size();)
            cat.put(categoryAdapter.categories.get(i).catId, categoryAdapter.categories.get(i).catName);
    }

    public void setSubcat()
    {
        subcat.clear();
        for(int i=-1;++i<subCategoryAdapter.allSubCats.size();)
            subcat.put(subCategoryAdapter.allSubCats.get(i).id, subCategoryAdapter.allSubCats.get(i).name);
    }

    public class TransViewHolder extends RecyclerView.ViewHolder
    {
        TextView TVCat, TVSubCat, TVNote, TVAccount, TVAmount;
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
            }

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
