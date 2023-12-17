package com.VipulMittal.expensemanager;

import android.os.Binder;

public class ObjectWrapper extends Binder {
	Object data;

	public ObjectWrapper(Object object) {
		data = object;
	}

	public Object getData() {
		return data;
	}
}
