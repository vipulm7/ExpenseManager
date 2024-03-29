package com.VipulMittal.expensemanager;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ViewAnimation {

	public static void expand(final View v, final AnimListener animListener) {
		Animation a = expandAction(v);
		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				animListener.onFinish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		v.startAnimation(a);
	}

	public static void expand(final View v) {
		Animation a = expandAction(v);
		v.startAnimation(a);
	}

	private static Animation expandAction(final View v) {
		v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targtetHeight = v.getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1
						? LayoutParams.WRAP_CONTENT
						: (int) (targtetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
		return a;
	}

	public static void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
	}

	public static void hideFab(View fab) {
		int moveY = 2 * fab.getHeight();
		fab.animate()
				.translationY(moveY)
				.setDuration(300)
				.start();
	}

	public static void showFab(View fab) {
		fab.animate()
				.translationY(0)
				.setDuration(300)
				.start();
	}

	public static void rotate(View view, boolean rotate) {
		view.animate().setDuration(200).rotation(rotate ? 180 : 0);
	}

	public interface AnimListener {
		void onFinish();
	}
}
