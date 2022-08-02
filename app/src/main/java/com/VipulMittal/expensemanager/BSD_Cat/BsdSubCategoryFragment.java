package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionActivity;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;

import java.util.List;

public class BsdSubCategoryFragment extends Fragment {

	public BsdSubCategoryFragment(int cID, int cIDCame, int sID, int type, Category categorySelected, List<Transaction> transactionsToBeModified, CategoryAdapter.BSDCatViewHolder viewHolder, MainActivity mainActivity) {
		this.sID = sID;
		this.type=type;
		this.categorySelected = categorySelected;
		this.cID = cID;
		sIDCame=sID;
		this.transactionsToBeModified=transactionsToBeModified;
		this.cIDCame=cIDCame;
		viewHolderCat=viewHolder;
		this.mainActivity=mainActivity;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVSubCategories;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	Category categorySelected;
	int sID, type, cID, sIDCame, cIDCame;
	Toast toast;
	List<Transaction> transactionsToBeModified;
	CategoryViewModel categoryViewModel;
	TransactionViewModel transactionViewModel;
	CategoryAdapter.BSDCatViewHolder viewHolderCat;
	MainActivity mainActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_bsd_sub_category, container, false);

		RVSubCategories=view.findViewById(R.id.bsd_rv_subcategories);
		BsdCatFragment bsdCatFragment = (BsdCatFragment) getParentFragment();
		TransactionActivity transactionActivity = bsdCatFragment.transactionActivity;
		transactionViewModel=mainActivity.transactionViewModel;
		subCategoryViewModel=mainActivity.subCategoryViewModel;
		categoryViewModel=mainActivity.categoryViewModel;
		toast=mainActivity.toast;

		Log.d(TAG, "onCreateView: cID in BsdSubCategoryFragment = "+cID);

		subCategoryAdapter= mainActivity.subCategoryAdapter;
//		mainActivity.subCategoryROOM(cID);

		Log.d(TAG, "onCreateView: subCategories.size() = "+subCategoryAdapter.subCategories.size());

		SubCategoryAdapter.ClickListener listener=new SubCategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(SubCategoryAdapter.BSDSubCatViewHolder viewHolder) {
				int position=viewHolder.getAdapterPosition();
				SubCategory subCategorySelected=subCategoryAdapter.subCategories.get(position);
				sID = subCategorySelected.id;

				if(transactionActivity !=null) {
					transactionActivity.saveSelectedSubCategory(cID, sID, categorySelected.catName + " / " + subCategoryAdapter.subCategories.get(position).name, subCategoryAdapter.subCategories.get(position).subCatImageID);
					bsdCatFragment.dismiss();
				}
				else if(sID==sIDCame)
				{
					toast=mainActivity.toast;
					if(toast!=null)
						toast.cancel();
					toast= Toast.makeText(mainActivity, "Can't select same SubCategory!", Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{
					Category categoryToBeDeleted=categoryViewModel.getCat(cIDCame);
					for(int i=-1;++i<transactionsToBeModified.size();)
					{
						Transaction transaction = transactionsToBeModified.get(i);
						if(transaction.type == 3)
							continue;

						transaction.catID=cID;
						transaction.subCatID=sID;
						transactionViewModel.Update(transaction);

						categoryViewModel.UpdateAmt(transaction.amount, cID);
						subCategoryViewModel.UpdateAmt(transaction.amount, sID);
					}

					List<SubCategory> subCats = subCategoryViewModel.getSubcats(categoryToBeDeleted.catId);
					for(int i=-1;++i<subCats.size();)
						subCategoryViewModel.Delete(subCats.get(i));
					categoryViewModel.Delete(categoryToBeDeleted);

					int x=viewHolderCat.subCategoryAdapter.subCategories.size();
					viewHolderCat.subCategoryAdapter.subCategories.clear();
					viewHolderCat.subCategoryAdapter.notifyItemRangeRemoved(0, x);
					bsdCatFragment.dismiss();
				}
			}
		};

		subCategoryAdapter.listener=listener;
		subCategoryAdapter.sID = sID;
		subCategoryAdapter.catSelected = categorySelected;

		RVSubCategories.setLayoutManager(new LinearLayoutManager((getContext())));
		RVSubCategories.setAdapter(subCategoryAdapter);
		RVSubCategories.setNestedScrollingEnabled(false);

		return view;
	}
}