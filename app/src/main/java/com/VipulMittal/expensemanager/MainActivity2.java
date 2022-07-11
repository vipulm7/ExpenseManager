package com.VipulMittal.expensemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class MainActivity2 extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
		// Set up shared element transition
		findViewById(android.R.id.content).setTransitionName("EXTRA_VIEW");
		setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
		getWindow().setSharedElementEnterTransition(buildContainerTransform(true));
		getWindow().setSharedElementReturnTransition(buildContainerTransform(false));

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);


	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private MaterialContainerTransform buildContainerTransform(boolean entering) {
		MaterialContainerTransform transform = new MaterialContainerTransform();
		transform.setTransitionDirection(entering ? MaterialContainerTransform.TRANSITION_DIRECTION_ENTER : MaterialContainerTransform.TRANSITION_DIRECTION_RETURN);
		transform.setAllContainerColors(MaterialColors.getColor(findViewById(android.R.id.content), R.attr.colorSurface));
		transform.addTarget(android.R.id.content);
		transform.setDuration(400L);
		return transform;
	}
}