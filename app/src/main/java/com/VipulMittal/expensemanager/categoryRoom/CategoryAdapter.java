package com.VipulMittal.expensemanager.categoryRoom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.BSD_Account.BsdAccountsFragment;
import com.VipulMittal.expensemanager.BSD_Cat.BsdCatFragment;
import com.VipulMittal.expensemanager.Cat;
import com.VipulMittal.expensemanager.IconsAdapter;
import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
	TransactionViewModel transactionViewModel;
	SubCategoryViewModel subCategoryViewModel;
	AccountViewModel accountViewModel;
	CategoryViewModel categoryViewModel;
	Toast toast;


	public CategoryAdapter(MainActivity mainActivity) {
		categories=new ArrayList<>();
		this.mainActivity = mainActivity;
		transactionViewModel = mainActivity.transactionViewModel;
		categoryViewModel = mainActivity.categoryViewModel;
		accountViewModel = mainActivity.accountViewModel;
		subCategoryViewModel = mainActivity.subCategoryViewModel;
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
		Log.d(TAG, "onBindViewHolder: categoryv = "+category.catName+"         id = "+category.catId);
		if(who==1)
		{
			holder.name.setText(category.catName);
			Log.d(TAG, "onBindViewHolder: ofCat position=" + position);
			Log.d(TAG, "onBindViewHolder: ofCat name=" + category.catName + " noOfSubCat=" + category.noOfSubCat + " id=" + category.catId);
			if (category.catId == cID)
				holder.name.setBackgroundColor(Color.parseColor("#C6E6E8"));//cyan
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
				holder.amt.setTextColor(Color.parseColor("#4fb85f"));//green
			else
				holder.amt.setTextColor(Color.RED);


			holder.bgt.setText("\u20b9"+moneyToString(category.catBudget));

			holder.arrow.setVisibility(View.VISIBLE);

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
					holder.progressBar.setProgress(progress, true);
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

			holder.imageView.setImageResource(category.catImageID);
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

				TransactionViewModel transactionViewModel = mainActivity.transactionViewModel;
				CategoryViewModel categoryViewModel = mainActivity.categoryViewModel;
				AccountViewModel accountViewModel = mainActivity.accountViewModel;
				SubCategoryViewModel subCategoryViewModel = mainActivity.subCategoryViewModel;

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
						IconsAdapter iconsAdapter = new IconsAdapter(mainActivity.icon_category_income);

						AlertDialog.Builder builder2;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
							builder2 = new AlertDialog.Builder(mainActivity, android.R.style.ThemeOverlay_Material_Dialog);
						}
						else
							builder2 = new AlertDialog.Builder(mainActivity);
						builder2.setNegativeButton("Cancel", (dialog2, which) -> {

						})
								.setView(catView)
								.setPositiveButton("Update", null);
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
						Button del1=dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
						del1.setOnClickListener(view->{
							if(possible(ETForCatIB.getText().toString().trim())) {
								int type=catTabLayout.getSelectedTabPosition()+1;
								SubCategory subCategory = new SubCategory(ETForCatN.getText().toString().trim(), subCategorySelected.subCatAmount, Integer.parseInt(ETForCatIB.getText().toString().trim()), subCategorySelected.categoryID, type, mainActivity.icon_category_income[iconsAdapter.selected]);
								subCategory.id = subCategorySelected.id;
								subCategoryViewModel.Update(subCategory);
								subCategoryAdapter.subCategories.set(pos, subCategory);
								subCategoryAdapter.notifyItemChanged(pos);
								dialog2.dismiss();
							}
							else
							{
								if(toast!=null)
									toast.cancel();

								toast= Toast.makeText(mainActivity, "Only 0-9 characters allowed", Toast.LENGTH_SHORT);
								toast.show();
							}
						});
						dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						ETForCatN.setText(subCategorySelected.name);
						ETForCatIB.setText(""+subCategorySelected.subCatBudget);
						ETForCatN.requestFocus();

						RecyclerView recyclerView = catView.findViewById(R.id.rv_icons_category);

						IconsAdapter.ClickListener listener = viewHolder2 -> {
							int pos2 = viewHolder2.getAdapterPosition();

							int a=iconsAdapter.selected;
							iconsAdapter.selected=pos2;

							iconsAdapter.notifyItemChanged(a);
							iconsAdapter.notifyItemChanged(pos2);
						};
						iconsAdapter.listener = listener;
						iconsAdapter.selected = findIndex(iconsAdapter.icons, subCategorySelected.subCatImageID);
						recyclerView.setAdapter(iconsAdapter);
						recyclerView.setHasFixedSize(true);
						recyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 2, GridLayoutManager.HORIZONTAL, false));


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

				SubCategoryAdapter.ClickListener deleteListenerS = viewHolder -> {

					int pos= viewHolder.getAdapterPosition();
					SubCategory subCategory = subCategoryAdapter.subCategories.get(pos);

					Log.d(TAG, "onItemClick: transactionViewModel = "+transactionViewModel);
					List<Transaction>transactionsToBeDeleted = transactionViewModel.getAllTransactionsSubCat(subCategory.id);

					AlertDialog.Builder builder;
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
						builder = new AlertDialog.Builder(mainActivity, android.R.style.ThemeOverlay_Material_Dialog);
					else
						builder = new AlertDialog.Builder(mainActivity);
					builder.setTitle("Delete Sub-Category")
							.setMessage("Are you sure want to delete this Sub-Category!")
							.setNegativeButton("Cancel", (dialog, which) -> {

							})
							.setPositiveButton("Delete", null);
					final AlertDialog[] dialog = {builder.create()};
					dialog[0].getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

					dialog[0].show();
					Dialog dialog1 = dialog[0];

					Button del = dialog[0].getButton(AlertDialog.BUTTON_POSITIVE);

					del.setOnClickListener(v->{
						if(transactionsToBeDeleted.size()==0) {
							subCategoryViewModel.Delete(subCategory);
							if(this.open)
							{
								subCategoryAdapter.subCategories.remove(pos);
								subCategoryAdapter.notifyItemRemoved(pos);
							}
						}
						else
						{
							String msg2;
							if(transactionsToBeDeleted.size()==1)
								msg2="There is 1 transaction done using this category. What to do with it";
							else
								msg2="There are "+transactionsToBeDeleted.size()+" transactions done using this category. What to do with them";
							builder.setTitle("")
									.setMessage(msg2)
									.setPositiveButton("Delete those transaction", (dialog2, which2) -> {
										for(int i=-1;++i<transactionsToBeDeleted.size();)
										{
											Transaction transaction = transactionsToBeDeleted.get(i);

											transactionViewModel.Delete(transaction);
											accountViewModel.UpdateAmt(-transaction.amount, transaction.accountID);
											if(transaction.type == 3)
												accountViewModel.UpdateAmt(transaction.amount, transaction.catID);
											else
											{
												categoryViewModel.UpdateAmt(-transaction.amount, transaction.catID);
												if(transaction.subCatID!=-1)
													subCategoryViewModel.UpdateAmt(-transaction.amount, transaction.subCatID);
											}
										}
										mainActivity.transactionAdapter.notifyDataSetChanged();
										subCategoryViewModel.Delete(subCategory);
										if(this.open)
										{
											subCategoryAdapter.subCategories.remove(pos);
											subCategoryAdapter.notifyItemRemoved(pos);
										}
//										categoryViewModel.catDeleted(subCategory.categoryID);
									})
									.setNegativeButton("", (dialog2, which2) -> {
//										BottomSheetDialogFragment bottomSheetDialogFragment = new BsdCatFragment(subCategory.categoryID, subCategory.id, subCategory.type, null, transactionsToBeDeleted, null);
//										bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Category");
									});
							dialog[0] = builder.create();
							dialog[0].getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

							dialog[0].show();
						}
						dialog1.dismiss();
					});
				};

				subCategoryAdapter.listener = listenerS;
				subCategoryAdapter.deleteListener = deleteListenerS;

				rv_subcatList.setNestedScrollingEnabled(false);
				rv_subcatList.setLayoutManager(new LinearLayoutManager(mainActivity));
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

		public void setProgressBar(int progress)
		{
			progressBar.setProgress(progress);
//			if(progress<11)
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

	private int findIndex(int[] icon_category, int imageId) {
		for(int i=-1;++i<icon_category.length;)
			if(icon_category[i] == imageId)
				return i;

		return -1;
	}

	private boolean possible(String trim) {
		int n=trim.length();
		for(int i=-1;++i<n;)
		{
			if(trim.charAt(i)>='0' && trim.charAt(i)<='9')
				continue;
			return false;
		}
		return true;
	}


	public void setCategories(List<Category> categories)
	{
		this.categories=categories;
	}
}
