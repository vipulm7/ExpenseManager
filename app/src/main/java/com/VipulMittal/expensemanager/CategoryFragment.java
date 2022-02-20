package com.VipulMittal.expensemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.VipulMittal.expensemanager.BSD_Account.BsdAccountsFragment;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

	public CategoryFragment() {
		// Required empty public constructor
	}

	public static final String TAG="Vipul_tag";

	CategoryAdapter categoryAdapter;
	MainActivity mainActivity;
	public TabLayout catTabLayout;
	ViewPager2 viewPager;

	CategoryAdapter categoryAdapter2;
	CategoryViewModel categoryViewModel;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	List<SubCategory> subCategories;
	TransactionViewModel transactionViewModel;
	AccountViewModel accountViewModel;

	boolean b3, b4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_category, container, false);

		catTabLayout = view.findViewById(R.id.catTabLayout);
		viewPager = view.findViewById(R.id.catViewPager);
		mainActivity=(MainActivity) getActivity();
		categoryViewModel= mainActivity.categoryViewModel2;
		transactionViewModel = mainActivity.transactionViewModel;
		accountViewModel = mainActivity.accountViewModel;
		subCategoryViewModel = mainActivity.subCategoryViewModel;

		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add("Income");
		arrayList.add("Expense");

		MainAdapter adapter = new MainAdapter(this);
		viewPager.setAdapter(adapter);

		new TabLayoutMediator(catTabLayout, viewPager, ((tab, position) -> {
			if(position==0)
				tab.setText("Income");
			else
				tab.setText("Expense");
		})).attach();





		CategoryAdapter.ClickListener cardListener = new CategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(CategoryAdapter.BSDCatViewHolder viewHolder) {
				int position = viewHolder.getAdapterPosition();
				Category categorySelected = categoryAdapter.categories.get(position);

				View catView = inflater.inflate(R.layout.category_dialog, null);

				EditText ETForCatN = catView.findViewById(R.id.ETDialogCatName);
				EditText ETForCatIB = catView.findViewById(R.id.ETDialogCatBudget);
//				rg_catDialog=catView.findViewById(R.id.RGCatDialog);
				IconsAdapter iconsAdapter = new IconsAdapter(mainActivity.icon_category_income);

				TextView catTitle = catView.findViewById(R.id.TVDialogCT);
				catTitle.setText("Update Category");

				AlertDialog.Builder builder2;
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
					builder2 = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
				}
				else
					builder2 = new AlertDialog.Builder(getContext());
				builder2.setNegativeButton("Cancel", (dialog2, which) -> {

				})
						.setView(catView)
						.setPositiveButton("Update", (dialog2, which) -> {
							int type=catTabLayout.getSelectedTabPosition()+1;
							Category category = new Category(ETForCatN.getText().toString().trim(), categorySelected.catAmount, Integer.parseInt(ETForCatIB.getText().toString().trim()), categorySelected.noOfSubCat, type, mainActivity.icon_category_income[iconsAdapter.selected]);
							category.catId = categorySelected.catId;
							categoryViewModel.Update(category);
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
				ETForCatN.setText(categorySelected.catName);
				ETForCatIB.setText(""+categorySelected.catBudget);
				ETForCatN.requestFocus();


				int pos = catTabLayout.getSelectedTabPosition();
				if(pos==0)
				{
					ETForCatIB.setVisibility(View.GONE);
					catView.findViewById(R.id.TVDialogCB).setVisibility(View.GONE);
				}
				else if(pos == 1)
				{
					ETForCatIB.setVisibility(View.VISIBLE);
					catView.findViewById(R.id.TVDialogCB).setVisibility(View.VISIBLE);
				}

				Log.d(TAG, "onItemClick: card clicked "+position);
			}
		};

		CategoryAdapter.ClickListener arrowListener = new CategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(CategoryAdapter.BSDCatViewHolder viewHolder) {
				int position = viewHolder.getAdapterPosition();
				Category categorySelected = categoryAdapter.categories.get(position);

				int cID = categorySelected.catId;
				List<SubCategory> subCategories = mainActivity.subcategoriesMap.get(cID);


				if(!viewHolder.open) {
//					viewHolder.subCategoryAdapter.subCategories.addAll(subCategories);
//					viewHolder.subCategoryAdapter.notifyItemRangeInserted(0, subCategories.size());
					for(int i=-1;++i<subCategories.size();)
					{
						viewHolder.subCategoryAdapter.subCategories.add(0, subCategories.get(i));
						viewHolder.subCategoryAdapter.notifyItemInserted(0);
					}
					viewHolder.open=true;
					viewHolder.arrow.setBackgroundResource(R.drawable.ic_arrow_drop_up);
				}
				else
				{
					viewHolder.subCategoryAdapter.subCategories.clear();
					viewHolder.subCategoryAdapter.notifyItemRangeRemoved(0, subCategories.size());
					viewHolder.open=false;
					viewHolder.arrow.setBackgroundResource(R.drawable.ic_arrow_drop_down);
				}

				Log.d(TAG, "onItemClick: arrow clicked "+position);
			}
		};



		categoryAdapter=mainActivity.categoryAdapter;
		categoryAdapter.cardListener =cardListener;
		categoryAdapter.arrowListener = arrowListener;
		categoryAdapter.who=2;
		categoryAdapter.cID=-1;




		CategoryAdapter.ClickListener cardListener2 = new CategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(CategoryAdapter.BSDCatViewHolder viewHolder) {

				int position = viewHolder.getAdapterPosition();
				Category categorySelected = categoryAdapter2.categories.get(position);

				int cID = categorySelected.catId;
				List<SubCategory> subCategories = mainActivity.subcategoriesMap.get(cID);
				if(!viewHolder.open) {
//					viewHolder.subCategoryAdapter.subCategories.addAll(subCategories);
//					viewHolder.subCategoryAdapter.notifyItemRangeInserted(0, subCategories.size());
					for(int i=-1;++i<subCategories.size();)
					{
						viewHolder.subCategoryAdapter.subCategories.add(subCategories.get(i));
						viewHolder.subCategoryAdapter.notifyItemInserted(i);
					}
					viewHolder.open=true;
					viewHolder.arrow.setBackgroundResource(R.drawable.ic_arrow_drop_up);
				}
				else
				{
					viewHolder.subCategoryAdapter.subCategories.clear();
					viewHolder.subCategoryAdapter.notifyItemRangeRemoved(0, subCategories.size());
					viewHolder.open=false;
					viewHolder.arrow.setBackgroundResource(R.drawable.ic_arrow_drop_down);
				}












				/*
				int position = viewHolder.getAdapterPosition();
				Category categorySelected = categoryAdapter2.categories.get(position);

				View catView = inflater.inflate(R.layout.category_dialog, null);

				EditText ETForCatN = catView.findViewById(R.id.ETDialogCatName);
				EditText ETForCatIB = catView.findViewById(R.id.ETDialogCatBudget);
//				rg_catDialog=catView.findViewById(R.id.RGCatDialog);
				IconsAdapter iconsAdapter = new IconsAdapter(mainActivity.icon_category_income);

				TextView catTitle = catView.findViewById(R.id.TVDialogCT);
				catTitle.setText("Update Category");

				AlertDialog.Builder builder2;
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
					builder2 = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
				else
					builder2 = new AlertDialog.Builder(getContext());
				builder2.setNegativeButton("Cancel", (dialog2, which) -> {

				})
						.setView(catView)
						.setPositiveButton("Update", (dialog2, which) -> {
							int type=catTabLayout.getSelectedTabPosition()+1;
							Category category = new Category(ETForCatN.getText().toString().trim(), categorySelected.catAmount, Integer.parseInt(ETForCatIB.getText().toString().trim()), categorySelected.noOfSubCat, type, mainActivity.icon_category_income[iconsAdapter.selected]);
							category.catId = categorySelected.catId;
							categoryViewModel.Update(category);
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
				ETForCatN.setText(categorySelected.catName);
				ETForCatIB.setText(""+categorySelected.catBudget);
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
				recyclerView.setAdapter(iconsAdapter);
				recyclerView.setHasFixedSize(true);
				recyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 2, GridLayoutManager.HORIZONTAL, false));


				int pos = catTabLayout.getSelectedTabPosition();
				if(pos==0)
				{
					ETForCatIB.setVisibility(View.GONE);
					catView.findViewById(R.id.TVDialogCB).setVisibility(View.GONE);
				}
				else if(pos == 1)
				{
					ETForCatIB.setVisibility(View.VISIBLE);
					catView.findViewById(R.id.TVDialogCB).setVisibility(View.VISIBLE);
				}



				Log.d(TAG, "onItemClick: card clicked "+position);

				 */
			}
		};



		CategoryAdapter.ClickListener arrowListener2 = new CategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(CategoryAdapter.BSDCatViewHolder viewHolder) {
				int position = viewHolder.getAdapterPosition();
				Category categorySelected = categoryAdapter2.categories.get(position);

				/*
				int cID = categorySelected.catId;
				List<SubCategory> subCategories = mainActivity.subcategoriesMap.get(cID);
				if(!viewHolder.open) {
//					viewHolder.subCategoryAdapter.subCategories.addAll(subCategories);
//					viewHolder.subCategoryAdapter.notifyItemRangeInserted(0, subCategories.size());
					for(int i=-1;++i<subCategories.size();)
					{
						viewHolder.subCategoryAdapter.subCategories.add(subCategories.get(i));
						viewHolder.subCategoryAdapter.notifyItemInserted(i);
					}
					viewHolder.open=true;
					viewHolder.arrow.setBackgroundResource(R.drawable.ic_arrow_drop_up);
				}
				else
				{
					viewHolder.subCategoryAdapter.subCategories.clear();
					viewHolder.subCategoryAdapter.notifyItemRangeRemoved(0, subCategories.size());
					viewHolder.open=false;
					viewHolder.arrow.setBackgroundResource(R.drawable.ic_arrow_drop_down);
				}

				 */

				PopupMenu popupMenu = new PopupMenu(mainActivity, viewHolder.arrow);
				popupMenu.inflate(R.menu.category);
				popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {

						switch (item.getItemId()) {
							case R.id.m_edit_cat:

								View catView = inflater.inflate(R.layout.category_dialog, null);

								EditText ETForCatN = catView.findViewById(R.id.ETDialogCatName);
								EditText ETForCatIB = catView.findViewById(R.id.ETDialogCatBudget);
//								rg_catDialog=catView.findViewById(R.id.RGCatDialog);
								IconsAdapter iconsAdapter = new IconsAdapter(mainActivity.icon_category_income);

								TextView catTitle = catView.findViewById(R.id.TVDialogCT);
								catTitle.setText("Update Category");

								AlertDialog.Builder builder2;
								if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
									builder2 = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
								else
									builder2 = new AlertDialog.Builder(getContext());
								builder2.setNegativeButton("Cancel", (dialog2, which) -> {

								})
										.setView(catView)
										.setPositiveButton("Update", (dialog2, which) -> {
											int type = catTabLayout.getSelectedTabPosition() + 1;
											Category category = new Category(ETForCatN.getText().toString().trim(), categorySelected.catAmount, Integer.parseInt(ETForCatIB.getText().toString().trim()), categorySelected.noOfSubCat, type, mainActivity.icon_category_income[iconsAdapter.selected]);
											category.catId = categorySelected.catId;
											categoryViewModel.Update(category);
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
								ETForCatN.setText(categorySelected.catName);
								ETForCatIB.setText("" + categorySelected.catBudget);
								ETForCatN.requestFocus();

								RecyclerView recyclerView = catView.findViewById(R.id.rv_icons_category);

								IconsAdapter.ClickListener listener = viewHolder2 -> {
									int pos2 = viewHolder2.getAdapterPosition();

									int a = iconsAdapter.selected;
									iconsAdapter.selected = pos2;

									iconsAdapter.notifyItemChanged(a);
									iconsAdapter.notifyItemChanged(pos2);
								};
								iconsAdapter.listener = listener;
								recyclerView.setAdapter(iconsAdapter);
								recyclerView.setHasFixedSize(true);
								recyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 2, GridLayoutManager.HORIZONTAL, false));


								int pos = catTabLayout.getSelectedTabPosition();
								if (pos == 0) {
									ETForCatIB.setVisibility(View.GONE);
									catView.findViewById(R.id.TVDialogCB).setVisibility(View.GONE);
								} else if (pos == 1) {
									ETForCatIB.setVisibility(View.VISIBLE);
									catView.findViewById(R.id.TVDialogCB).setVisibility(View.VISIBLE);
								}

								return true;

							case R.id.m_delete_cat:
								List<Transaction>transactionsToBeDeleted = transactionViewModel.getAllTransactionsCat(categorySelected.catId);

								AlertDialog.Builder builder;
								if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
									builder = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
								}
								else
									builder = new AlertDialog.Builder(getContext());
								builder.setTitle("Delete Category")
										.setMessage("Are you sure want to delete this category!\nThis will also delete all the sub-categories linked with this category")
										.setNegativeButton("Cancel", (dialog, which) -> {

										})
										.setPositiveButton("Delete", null);
								final AlertDialog[] dialog = {builder.create()};
								dialog[0].getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);
//								dialog.getWindow().getAttributes().windowAnimations = R.style.

								dialog[0].show();
								Dialog dialog1 = dialog[0];

								Button del = dialog[0].getButton(AlertDialog.BUTTON_POSITIVE);
								del.setOnClickListener(v->{
									if(transactionsToBeDeleted.size()==0) {

										viewHolder.subCategoryAdapter.subCategories.clear();
										viewHolder.subCategoryAdapter.notifyDataSetChanged();

										List<SubCategory> subCats = subCategoryViewModel.getSubcats(categorySelected.catId);
										for(int i=-1;++i<subCats.size();)
											subCategoryViewModel.Delete(subCats.get(i));
										categoryViewModel.Delete(categorySelected);
									}
									else
									{
										builder.setTitle("")
												.setMessage("There are "+transactionsToBeDeleted.size()+" transactions with done using this category. What to do with them")
												.setPositiveButton("Delete those transaction", (dialog3, which2) -> {
													for(int i=-1;++i<transactionsToBeDeleted.size();)
													{
														Transaction transaction = transactionsToBeDeleted.get(i);

														transactionViewModel.Delete(transaction);
														accountViewModel.UpdateAmt(-transaction.amount, transaction.accountID);
														if(transaction.type == 3)
															accountViewModel.UpdateAmt(transaction.amount, transaction.catID);
														else {
															categoryViewModel.UpdateAmt(-transaction.amount, transaction.catID);
															if(transaction.subCatID!=-1)
																subCategoryViewModel.UpdateAmt(-transaction.amount, transaction.subCatID);
														}
													}
													mainActivity.transactionAdapter.notifyDataSetChanged();

													viewHolder.subCategoryAdapter.subCategories.clear();
													viewHolder.subCategoryAdapter.notifyDataSetChanged();

													categoryViewModel.Delete(categorySelected);

													List<SubCategory> subCats = subCategoryViewModel.getSubcats(categorySelected.catId);
													for(int i=-1;++i<subCats.size();)
														subCategoryViewModel.Delete(subCats.get(i));
												})
												.setNegativeButton("Choose New Category", (dialog3, which2) -> {

												});
										dialog[0] = builder.create();
										dialog[0].getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

										dialog[0].show();
									}
									dialog1.dismiss();
								});
								return true;
						}
						return false;
					}
				});

				popupMenu.show();

				Log.d(TAG, "onItemClick: arrow clicked "+position);
			}
		};

		categoryAdapter2= mainActivity.categoryAdapter2;
		categoryAdapter2.cardListener =cardListener2;
		categoryAdapter2.arrowListener = arrowListener2;
		categoryAdapter2.who=2;
		categoryAdapter2.cID=-1;

		return view;
	}


	private class MainAdapter extends FragmentStateAdapter
	{
		public MainAdapter(Fragment fragment)
		{
			super(fragment);
		}

		@NonNull
		@Override
		public Fragment createFragment(int position) {
			Fragment catIncomeFragment;
			if(position==0)
				catIncomeFragment = new CatTabFragment(categoryAdapter);
			else
				catIncomeFragment = new CatTabFragment(categoryAdapter2);

			return catIncomeFragment;
		}

		@Override
		public int getItemCount() {
			return 2;
		}
	}
}