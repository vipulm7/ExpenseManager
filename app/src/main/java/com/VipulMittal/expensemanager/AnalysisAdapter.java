package com.VipulMittal.expensemanager;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.AnalysisViewHolder> {

	LayoutInflater inflater;
	ClickListener listener;
	public static final String TAG="Vipul_tag";
	DecimalFormat df;
	List<Cat> percent;

	public AnalysisAdapter() {
		df=new DecimalFormat("0.00");
	}

	@NonNull
	@Override
	public AnalysisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if(inflater == null)
			inflater = LayoutInflater.from(parent.getContext());

		View view = inflater.inflate(R.layout.cat_analysis_layout_per_item, parent, false);
		return new AnalysisViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AnalysisViewHolder holder, int position) {

		holder.TVCatName.setText(percent.get(position).catName);
		double a = percent.get(position).catPercent;
		Log.d(TAG, "onBindViewHolder: analysis percent="+a);
		a*=100d;
		holder.TVpercent.setText(""+df.format(a)+"%");
		int b=(int)a;
		holder.progressBar.setProgress(b);
		holder.imageView.setImageResource(percent.get(position).imageID);
	}

	@Override
	public int getItemCount() {
		return percent.size();
	}

	public class AnalysisViewHolder extends RecyclerView.ViewHolder{

		ImageView imageView;
		TextView TVCatName, TVpercent;
		ProgressBar progressBar;

		public AnalysisViewHolder(@NonNull View itemView) {
			super(itemView);

			imageView = itemView.findViewById(R.id.iv_icon);
			TVpercent = itemView.findViewById(R.id.TVpercent);
			TVCatName = itemView.findViewById(R.id.TVCatName);
			progressBar = itemView.findViewById(R.id.progressBar);

			if(listener != null)
				listener.OnItemClick(this);
		}
	}

	public interface ClickListener{
		void OnItemClick(AnalysisViewHolder viewHolder);
	}
}
