package com.VipulMittal.expensemanager;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisFragment extends Fragment {

	public static final String TAG="Vipul_tag";
	PieChart pieChart;
	MainActivity mainActivity;
	RadioGroup rg_chart;
	Toast toast;
	ArrayList<PieEntry> pieEntries;
	CategoryAdapter categoryAdapter;
	RadioButton RBI,RBE;
	TextView TVBefore, TVAfter, TVPeriodShown, TVFilter;
	Calendar toShow;
	int viewMode;
	Map <Integer, Integer> catAmount;
	RadioGroup rg_Filter;
	RecyclerView rv_analysis;
	int totalIncome, totalExpense;
	AnalysisAdapter analysisAdapter;

	public AnalysisFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_analysis, container, false);

		pieChart=view.findViewById(R.id.pieChart);
		mainActivity=(MainActivity) getActivity();
		rg_chart=view.findViewById(R.id.RGChart);
		TVAfter=view.findViewById(R.id.TVafter);
		TVBefore=view.findViewById(R.id.TVbefore);
		TVPeriodShown =view.findViewById(R.id.TVDateChange);
		toShow = Calendar.getInstance();
		TVFilter=view.findViewById(R.id.TVFilter);
		rv_analysis=view.findViewById(R.id.rv_analysis);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		viewMode = sharedPreferences.getInt("viewAnalysis", R.id.RBM);
		SharedPreferences.Editor editor= sharedPreferences.edit();
		catAmount = new HashMap<>();
		toast=mainActivity.toast;
		RBE=view.findViewById(R.id.radioCatExpenseChart);
		RBI=view.findViewById(R.id.radioCatIncomeChart);
		analysisAdapter = new AnalysisAdapter();

		AnalysisAdapter.ClickListener listener = new AnalysisAdapter.ClickListener() {
			@Override
			public void OnItemClick(AnalysisAdapter.AnalysisViewHolder viewHolder) {
				int pos = viewHolder.getAdapterPosition();

			}
		};

		rv_analysis.setAdapter(analysisAdapter);
		rv_analysis.setNestedScrollingEnabled(false);
		rv_analysis.setLayoutManager(new LinearLayoutManager(getContext()));

		radioGroupSetListener();
		setRadioButton(2);

		transactionROOM();
		setDate();

		View filterView = inflater.inflate(R.layout.filter_dialog, null);
		rg_Filter =filterView.findViewById(R.id.RGFilter);

		TextView viewTitle = new TextView(getContext());
		viewTitle.setText("View Mode");
		viewTitle.setGravity(Gravity.CENTER_HORIZONTAL);
		viewTitle.setPadding(2,16,2,10);
		viewTitle.setTextSize(22);
		viewTitle.setTypeface(null, Typeface.BOLD);

		AlertDialog.Builder builder;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			builder = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
		}
		else
			builder = new AlertDialog.Builder(getContext());
		builder.setCustomTitle(viewTitle)
				.setView(filterView);
		AlertDialog dialog = builder.create();
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);


		TVAfter.setText(">");
		TVBefore.setText("<");

		TVAfter.setOnClickListener(v->{
			if(viewMode == R.id.RBM)
				toShow.add(Calendar.MONTH, 1);
			else if(viewMode == R.id.RBD)
				toShow.add(Calendar.DATE, 1);
			else if(viewMode == R.id.RBW)
				toShow.add(Calendar.WEEK_OF_YEAR, 1);
			transactionROOM();
			setDate();
		});

		TVBefore.setOnClickListener(v->{
			if(viewMode == R.id.RBM)
				toShow.add(Calendar.MONTH, -1);
			else if(viewMode == R.id.RBD)
				toShow.add(Calendar.DATE, -1);
			else if(viewMode == R.id.RBW)
				toShow.add(Calendar.WEEK_OF_YEAR, -1);
			transactionROOM();
			setDate();
		});

		RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
				editor.putInt("viewAnalysis", checkedID);
				editor.apply();
				viewMode=checkedID;
				Log.d(TAG, "onCheckedChanged: viewMode = "+viewMode);
				Log.d(TAG, "onCheckedChanged: shared = "+sharedPreferences.getInt("viewAnalysis", -1));
				dialog.dismiss();

				toShow.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
				transactionROOM();
				setDate();
				rg_Filter.setOnCheckedChangeListener(null);
			}
		};

		TVFilter.setOnClickListener(v->{
			dialog.show();
			rg_Filter.check(viewMode);
			rg_Filter.setOnCheckedChangeListener(listener1);
		});




		pieChart.setUsePercentValues(true);
		pieChart.setHoleColor(Color.TRANSPARENT);
		pieChart.setHighlightPerTapEnabled(true);
		pieChart.setEntryLabelTextSize(12);


		pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
			@Override
			public void onValueSelected(Entry e, Highlight h) {

			}

			@Override
			public void onNothingSelected() {

			}
		});




		return view;
	}

	private void setRadioButton(int type) {
		int id=-1;
		if(type==1)
			id=R.id.radioCatIncomeChart;
		else if(type==2)
			id=R.id.radioCatExpenseChart;

		if(id!=-1)
			rg_chart.check(id);
		else
		{
			if(toast!=null)
				toast.cancel();
			toast= Toast.makeText(getContext(), "Error in chart type selection", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void radioGroupSetListener() {
		rg_chart.setOnCheckedChangeListener((radioGroup, type) -> {
			if(type==R.id.radioCatIncomeChart)
			{
				pieData(1);
				pieChart.invalidate();

				RBI.setTextColor(Color.parseColor("#154b5e"));
				RBI.setTextSize(25);
				RBE.setTextColor(Color.parseColor("#1a74a1"));
				RBE.setTextSize(20);
			}
			else if(type==R.id.radioCatExpenseChart)
			{
				pieData(2);
				pieChart.invalidate();

				RBE.setTextColor(Color.parseColor("#154b5e"));
				RBE.setTextSize(25);
				RBI.setTextColor(Color.parseColor("#1a74a1"));
				RBI.setTextSize(20);
			}
		});
	}

	public void pieData(int type)
	{
		pieEntries=new ArrayList<>();

		List<Integer> cat = new ArrayList<>(catAmount.keySet());
		double s=0;
		List<Cat> percent = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00");
		double a=0;

		if(type ==1)
		{
			for(int i=-1;++i<cat.size();) {
				Category category = mainActivity.categoryViewModel.getCat(cat.get(i));
				if(catAmount.get(cat.get(i))>0) {
					pieEntries.add(new PieEntry(catAmount.get(cat.get(i)), category.catName));
					a = (double)catAmount.get(cat.get(i)) / totalIncome;
					s+=a;
					percent.add(new Cat(a, category.catName, category.catImageID));
				}
				Log.d(TAG, "pieData: amt = "+catAmount.get(cat.get(i))+" name = "+category.catName);
			}

			if(percent.size()!=0) {
				s -= a;
				a = 1 - s;
				percent.set(percent.size() - 1, new Cat(a, percent.get(percent.size() - 1).catName, percent.get(percent.size() - 1).imageID));
			}

			pieChart.setNoDataText("No Income Entries");
		}
		else
		{
			for(int i=-1;++i<cat.size();) {
				Category category = mainActivity.categoryViewModel.getCat(cat.get(i));
				if(catAmount.get(cat.get(i))<0) {
					pieEntries.add(new PieEntry(-catAmount.get(cat.get(i)), category.catName));
					a = (double)catAmount.get(cat.get(i)) / totalExpense;
					s+=a;
					percent.add(new Cat(a, category.catName, category.catImageID));
				}
			}

			if(percent.size()!=0) {
				s -= a;
				a = 1 - s;
				percent.set(percent.size() - 1, new Cat(a, percent.get(percent.size() - 1).catName, percent.get(percent.size() - 1).imageID));
			}

			pieChart.setNoDataText("No Expense Entries");
		}

		analysisAdapter.percent = percent;
		analysisAdapter.notifyDataSetChanged();

		Log.d(TAG, "radioGroupSetListener: pie size="+pieEntries.size());

		PieDataSet pieDataSet=new PieDataSet(pieEntries, "");
		pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
		pieDataSet.setDrawValues(true);

		PieData pieData = new PieData(pieDataSet);
		pieData.setValueFormatter(new PercentFormatter());
		pieData.setValueTextSize(12f);

		if(pieEntries.size()>0)
			pieChart.setData(pieData);
		else
			pieChart.setData(null);
		pieChart.animateY(1000);
//				pieChart.getDescription().setText("Trans2 chart");
//				pieChart.getDescription().setTextColor(Color.CYAN);
		pieChart.getDescription().setEnabled(false);

		pieChart.invalidate();
	}

	public void transactionROOM() {
		int date=toShow.get(Calendar.DATE);
		int week = toShow.get(Calendar.WEEK_OF_YEAR);
		int month=toShow.get(Calendar.MONTH);
		int year=toShow.get(Calendar.YEAR);
		if(viewMode == R.id.RBM)
		{
			mainActivity.transactionViewModel2.getAllTransactionsMONTH(month, year).observe(getViewLifecycleOwner(), transactions -> {
				Log.d(TAG, "transactionROOM: transactions month = " + transactions.size());
				setCatAmount(transactions);
				int check=rg_chart.getCheckedRadioButtonId();
				rg_chart.clearCheck();
				rg_chart.check(check);
			});
		}
		else if(viewMode == R.id.RBW)
		{
			mainActivity.transactionViewModel2.getAllTransactionsWEEK(week, year).observe(getViewLifecycleOwner(), transactions -> {
				Log.d(TAG, "transactionROOM: transactions week = " + transactions.size());
				setCatAmount(transactions);
				int check=rg_chart.getCheckedRadioButtonId();
				rg_chart.clearCheck();
				rg_chart.check(check);
			});
		}
		else if(viewMode == R.id.RBD)
		{
			mainActivity.transactionViewModel2.getAllTransactionsDAY(date, month, year).observe(getViewLifecycleOwner(), transactions -> {
				Log.d(TAG, "transactionROOM: transactions day = " + transactions.size());
				setCatAmount(transactions);
				int check=rg_chart.getCheckedRadioButtonId();
				rg_chart.clearCheck();
				rg_chart.check(check);
			});
		}
	}

	public void setCatAmount(List<Transaction> transactions)
	{
		catAmount.clear();
		totalExpense = 0;
		totalIncome = 0;

		for(int i=-1;++i<transactions.size();)
		{
			if(transactions.get(i).type==3)
				continue;

			if(!catAmount.containsKey(transactions.get(i).catID))
				catAmount.put(transactions.get(i).catID, 0);
			Integer a=catAmount.get(transactions.get(i).catID);
			a+=transactions.get(i).amount;
			catAmount.put(transactions.get(i).catID, a);
			if(transactions.get(i).type ==1)
				totalIncome+=transactions.get(i).amount;
			else
				totalExpense+=transactions.get(i).amount;
		}
	}

	private void setDate() {
		int date=toShow.get(Calendar.DATE);
		int week = toShow.get(Calendar.WEEK_OF_YEAR);
		int month=toShow.get(Calendar.MONTH);
		int year=toShow.get(Calendar.YEAR);

		if(viewMode == R.id.RBM)
			TVPeriodShown.setText(getM(month)+", "+year);
		else if(viewMode == R.id.RBW) {
			Calendar calendar=Calendar.getInstance();
			calendar.setTimeInMillis(toShow.getTimeInMillis());
			calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_WEEK)+1);
			String s=calendar.get(Calendar.DATE)+" "+getM(calendar.get(Calendar.MONTH))+" - ";
			calendar.add(Calendar.DATE,6);


			TVPeriodShown.setText(s+calendar.get(Calendar.DATE)+" "+getM(calendar.get(Calendar.MONTH)));
		}
		else if(viewMode == R.id.RBD)
			TVPeriodShown.setText(date+" "+getM(month)+", "+year);
	}

	private String getM(int m) {
		String a[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		return a[m];
	}
}