package com.VipulMittal.expensemanager.categoryRoom;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.BSDCatViewHolder> {

	public ClickListener cardListener, arrowListener;
	public List<Category> categories;
	public int cID;
	String TAG="Vipul_tag";
	public int who;
	MainActivity mainActivity;


	public CategoryAdapter(MainActivity mainActivity) {
		categories=new ArrayList<>();
		this.mainActivity = mainActivity;
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
				holder.amt.setText("\u20b9"+moneyToString(category.catAmount));
			else
				holder.amt.setText("\u20b9"+moneyToString(-category.catAmount));

			if(category.type==1)
				holder.amt.setTextColor(Color.GREEN);
			else
				holder.amt.setTextColor(Color.RED);


			holder.bgt.setText("\u20b9"+moneyToString(category.catBudget));
			if(category.noOfSubCat>0)
				holder.arrow.setVisibility(View.VISIBLE);
			else
				holder.arrow.setVisibility(View.INVISIBLE);

			if(category.type == 1)
			{
				holder.bgt.setVisibility(View.GONE);
				holder.bgt2.setVisibility(View.GONE);
				holder.progressBar.setVisibility(View.GONE);
			}

			if(category.catBudget!=0) {
				int progress = (int)((-100L*category.catAmount)/category.catBudget);
				Log.d(TAG, "onBindViewHolder: catName = "+category.catName+" progress = "+progress);
				if(progress>100)
					progress = 100;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
					holder.progressBar.setProgress(progress);
				else
					holder.progressBar.setProgress(progress);
			}
			else
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
					holder.progressBar.setProgress(0, true);
				else
					holder.progressBar.setProgress(0);
			}
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
		public TextView name,arrow, bgt,amt, amt2, bgt2;
		ImageView imageView;
		ProgressBar progressBar;

		RecyclerView rv_subcatList;
		public SubCategoryAdapter subCategoryAdapter;
		boolean b3, b4;
		public boolean open;

		public BSDCatViewHolder(@NonNull View itemView) {
			super(itemView);
			if(who==1)
			{
				name = itemView.findViewById(R.id.BSD_Cat);
				arrow = itemView.findViewById(R.id.BSD_Cat_Arrow);

				itemView.setOnClickListener(view -> {
					int position = getAdapterPosition();
					Log.d(TAG, "BSDCatViewHolder: pos for who1 = " + position);
					if (cardListener != null)
						cardListener.onItemClick(this);
				});
			}
			else if(who==2)
			{
				imageView=itemView.findViewById(R.id.IVCat_image);
				name=itemView.findViewById(R.id.TVCat_name);
				arrow=itemView.findViewById(R.id.TVCat_ARROW);
				bgt=itemView.findViewById(R.id.TVCat_budget);
				amt=itemView.findViewById(R.id.TVCat_amt);
				bgt2=itemView.findViewById(R.id.TVCat_BUDGET);
				amt2=itemView.findViewById(R.id.TVCat_AMOUNT);
				rv_subcatList= itemView.findViewById(R.id.rv_SubcatList);
				progressBar = itemView.findViewById(R.id.progressBar);

				subCategoryAdapter = new SubCategoryAdapter();
				subCategoryAdapter.who =2;
				subCategoryAdapter.cID=-1;
				subCategoryAdapter.sID=-1;

				SubCategoryAdapter.ClickListener listenerS = new SubCategoryAdapter.ClickListener() {
					@Override
					public void onItemClick(SubCategoryAdapter.BSDSubCatViewHolder viewHolder) {
						int pos = viewHolder.getAdapterPosition();

						SubCategory subCategorySelected = subCategoryAdapter.subCategories.get(pos);
						TabLayout catTabLayout = mainActivity.categoryFragment.catTabLayout;

						View catView = mainActivity.inflater.inflate(R.layout.category_dialog, null);

						EditText ETForCatN = catView.findViewById(R.id.ETDialogCatName);
						EditText ETForCatIB = catView.findViewById(R.id.ETDialogCatBudget);
//						rg_catDialog=catView.findViewById(R.id.RGCatDialog);

						TextView catTitle = catView.findViewById(R.id.TVDialogCT);
						catTitle.setText("Update SubCategory");

						AlertDialog.Builder builder2;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
							builder2 = new AlertDialog.Builder(mainActivity, android.R.style.ThemeOverlay_Material_Dialog);
						}
						else
							builder2 = new AlertDialog.Builder(mainActivity);
						builder2.setNegativeButton("Cancel", (dialog2, which) -> {

						})
								.setView(catView)
								.setPositiveButton("Update", (dialog2, which) -> {
									int type=catTabLayout.getSelectedTabPosition()+1;
									SubCategory subCategory = new SubCategory(ETForCatN.getText().toString().trim(), subCategorySelected.subCatAmount, Integer.parseInt(ETForCatIB.getText().toString().trim()), subCategorySelected.categoryID, type);
									subCategory.id = subCategorySelected.id;
									mainActivity.subCategoryViewModel.Update(subCategory);
								});
						AlertDialog dialog2 = builder2.create();
						dialog2.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

						ETForCatN.addTextChangedListener(new TextWatcher() {
							@Override
							public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

							}

							@Override
							public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
								b3 = charSequence.toString().trim().length() != 0;
								dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b3 && b4);
							}

							@Override
							public void afterTextChanged(Editable editable) {
							}
						});

						ETForCatIB.addTextChangedListener(new TextWatcher() {
							@Override
							public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

							}

							@Override
							public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
								b4 = charSequence.toString().trim().length() != 0;
								dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b3 && b4);
							}

							@Override
							public void afterTextChanged(Editable editable) {
							}
						});


						dialog2.show();
						dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						ETForCatN.setText(subCategorySelected.name);
						ETForCatIB.setText(""+subCategorySelected.subCatBudget);
						ETForCatN.requestFocus();


						int posT = catTabLayout.getSelectedTabPosition();
						if(posT==0)
						{
							ETForCatIB.setVisibility(View.GONE);
							catView.findViewById(R.id.TVDialogCB).setVisibility(View.GONE);
						}
						else if(posT == 1)
						{
							ETForCatIB.setVisibility(View.VISIBLE);
							catView.findViewById(R.id.TVDialogCB).setVisibility(View.VISIBLE);
						}

						Log.d(TAG, "onItemClick: card clicked "+pos);
					}
				};

				subCategoryAdapter.listener = listenerS;

				rv_subcatList.setNestedScrollingEnabled(false);
				rv_subcatList.setLayoutManager(new LinearLayoutManager(mainActivity.getApplicationContext()));
				rv_subcatList.setAdapter(subCategoryAdapter);

				itemView.setOnClickListener(v->{
					int position = getAdapterPosition();
					Log.d(TAG, "BSDCatViewHolder: pos for who2 = " + position);
					if(cardListener !=null)
						cardListener.onItemClick(this);
				});

				arrow.setOnClickListener(v->{
					if(arrowListener != null)
						arrowListener.onItemClick(this);
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
