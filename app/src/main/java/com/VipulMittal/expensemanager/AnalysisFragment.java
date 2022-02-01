package com.VipulMittal.expensemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AnalysisFragment extends Fragment {

	public static final String TAG="Vipul_tag";
	PieChart pieChart;
	MainActivity mainActivity;
	RadioGroup rg_chart;
	Toast toast;
	ArrayList<PieEntry> pieEntries;
	CategoryAdapter categoryAdapter;
	RadioButton RBI,RBE;

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

		toast=mainActivity.toast;
		RBE=view.findViewById(R.id.radioCatExpenseChart);
		RBI=view.findViewById(R.id.radioCatIncomeChart);

		radioGroupSetListener();
		setRadioButton(2);

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
				categoryAdapter= mainActivity.categoryAdapter;
				pieEntries=new ArrayList<>();
				for(int i=-1;++i<categoryAdapter.categories.size();) {
					if(categoryAdapter.categories.get(i).catAmount>0)
						pieEntries.add(new PieEntry(categoryAdapter.categories.get(i).catAmount));
				}

				Log.d(TAG, "radioGroupSetListener: pie size="+pieEntries.size());

				PieDataSet pieDataSet=new PieDataSet(pieEntries, "b");
				pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
				pieDataSet.setDrawValues(true);
				Log.d(TAG, "radioGroupSetListener: pieEntries.size() = "+pieEntries.size());
//				if(pieEntries.size()>0)
					pieChart.setData(new PieData(pieDataSet));
				pieChart.animateY(1000);
//				pieChart.getDescription().setText("Trans2 chart");
//				pieChart.getDescription().setTextColor(Color.CYAN);
				pieChart.setNoDataText("No data");


				RBI.setTextColor(Color.parseColor("#a912db"));
				RBI.setTextSize(25);
				RBE.setTextColor(Color.parseColor("#db4002"));
				RBE.setTextSize(20);

			}
			else if(type==R.id.radioCatExpenseChart)
			{
				categoryAdapter= mainActivity.categoryAdapter2;
				pieEntries=new ArrayList<>();
				for(int i=-1;++i<categoryAdapter.categories.size();) {
					if(categoryAdapter.categories.get(i).catAmount<0)
						pieEntries.add(new PieEntry(-categoryAdapter.categories.get(i).catAmount*1f));
				}
				Log.d(TAG, "radioGroupSetListener: pie size="+pieEntries.size());
				PieDataSet pieDataSet=new PieDataSet(pieEntries, "");
				pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
				pieDataSet.setDrawValues(true);
				pieChart.setData(new PieData(pieDataSet));
				pieChart.animateY(1000);
//				pieChart.getDescription().setText("Trans2 chart");
//				pieChart.getDescription().setTextColor(Color.CYAN);


				RBE.setTextColor(Color.parseColor("#a912db"));
				RBE.setTextSize(25);
				RBI.setTextColor(Color.parseColor("#db4002"));
				RBI.setTextSize(20);
			}
		});
	}
}