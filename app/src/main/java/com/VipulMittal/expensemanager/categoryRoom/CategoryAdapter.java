package com.VipulMittal.expensemanager.categoryRoom;

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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.BSDCatViewHolder> {

	public ClickListener listener;
	public List<Category> categories;
	public int cID;
	String TAG="Vipul_tag";
	public int who;

	public CategoryAdapter() {
		categories=new ArrayList<>();
	}

	@NonNull
	@Override
	public BSDCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view;
		if(who==1)
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_cat_layout_per_item, parent, false);
		else
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout_per_item, parent, false);
		return new BSDCatViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BSDCatViewHolder holder, int position) {
		Category category = categories.get(position);
		if(who==1)
		{
			holder.name.setText(category.catName);
			Log.d(TAG, "onBindViewHolder: ofCat position=" + position);
			Log.d(TAG, "onBindViewHolder: ofCat name=" + category.catName + " noOfSubCat=" + category.noOfSubCat + " id=" + category.catId);
			if (category.catId == cID)
				holder.name.setBackgroundColor(Color.CYAN);
			else
				holder.name.setBackgroundResource(R.drawable.bsd);

			if (category.noOfSubCat == 0)
				holder.arrow.setVisibility(View.INVISIBLE);
			else
				holder.arrow.setVisibility(View.VISIBLE);
		}
		else if(who==2)
		{
			holder.name.setText(category.catName);
			if(category.catAmount>=0)
				holder.amt.setText(""+moneyToString(category.catAmount));
			else
				holder.amt.setText(""+moneyToString(-category.catAmount));

			if(category.type==1)
				holder.amt.setTextColor(Color.GREEN);
			else
				holder.amt.setTextColor(Color.RED);


			holder.bgt.setText(""+moneyToString(category.catBudget));
			if(category.noOfSubCat>0)
				holder.arrow.setVisibility(View.VISIBLE);
			else
				holder.arrow.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount: categories.size() = "+categories.size());
		Log.d(TAG, "getItemCount: categories = "+categories);
		return categories.size();
	}


	public class BSDCatViewHolder extends RecyclerView.ViewHolder
	{
		TextView name,arrow, bgt,amt;
		ImageView imageView;
		public BSDCatViewHolder(@NonNull View itemView) {
			super(itemView);
			if(who==1)
			{
				name = itemView.findViewById(R.id.BSD_Cat);
				arrow = itemView.findViewById(R.id.BSD_Cat_Arrow);

				itemView.setOnClickListener(view -> {
					int position = getAdapterPosition();
					Log.d(TAG, "BSDCatViewHolder: pos for who1 = " + position);
//				if(listener!=null && position!=-1)
					if (listener != null)
						listener.onItemClick(this);
				});
			}
			else if(who==2)
			{
				imageView=itemView.findViewById(R.id.IVCat_image);
				name=itemView.findViewById(R.id.TVCat_name);
				arrow=itemView.findViewById(R.id.TVCat_ARROW);
				bgt=itemView.findViewById(R.id.TVCat_budget);
				amt=itemView.findViewById(R.id.TVCat_amt);

				itemView.setOnClickListener(v->{
					int position = getAdapterPosition();
					Log.d(TAG, "BSDCatViewHolder: pos for who2 = " + position);
					if(listener!=null)
						listener.onItemClick(this);
				});
			}
		}
	}

	public interface ClickListener
	{
		void onItemClick(BSDCatViewHolder viewHolder);
	}


	public String moneyToString(long money) {
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


	public void setCategories(List<Category> categories)
	{
		this.categories=categories;
	}
}
