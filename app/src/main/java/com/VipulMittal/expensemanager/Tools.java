package com.VipulMittal.expensemanager;


import android.view.View;
import androidx.core.widget.NestedScrollView;

public class Tools {


	public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
		nested.post(new Runnable() {
			@Override
			public void run() {
				nested.scrollTo(500, targetView.getBottom());
			}
		});
	}
}
