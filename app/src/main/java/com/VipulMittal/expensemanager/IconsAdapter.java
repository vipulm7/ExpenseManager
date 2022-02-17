package com.VipulMittal.expensemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IconsAdapter extends RecyclerView.Adapter<IconsAdapter.IconViewHolder> {
	LayoutInflater inflater;
	int[] icons;
	int selected;
	ClickListener listener;

	public IconsAdapter(int[] icons) {
		this.icons = icons;
	}

	@NonNull
	@Override
	public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if(inflater == null)
			inflater = LayoutInflater.from(parent.getContext());

		View view = inflater.inflate(R.layout.icon_layout_per_item, null);
		return new IconViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
		holder.iv_icon.setImageResource(icons[position]);

		if(position == selected)
			holder.iv_icon.setBackgroundResource(R.drawable.icon_account_selected);
		else
			holder.iv_icon.setBackgroundResource(R.drawable.icon_account_not_selected);
	}

	@Override
	public int getItemCount() {
		return icons.length;
	}

	public class IconViewHolder extends RecyclerView.ViewHolder {
		ImageView iv_icon;
		public IconViewHolder(@NonNull View itemView) {
			super(itemView);

			iv_icon=itemView.findViewById(R.id.iv_icon);

			if(listener != null)
				itemView.setOnClickListener(v -> listener.OnItemClick(this));
		}
	}

	public interface ClickListener{
		void OnItemClick(IconViewHolder viewHolder);
	}
}
