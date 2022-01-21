package com.VipulMittal.expensemanager.dateRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DateViewModel extends AndroidViewModel {

	private DateRepo repo;
	private LiveData<List<Date>> dates;

	public DateViewModel(@NonNull Application application) {
		super(application);
		repo=new DateRepo(application);
		dates= repo.getDates();
	}

	public void Insert(Date date)
	{
		repo.Insert(date);
	}

	public void Delete(Date date)
	{
		repo.Delete(date);
	}

	public void Update(Date date)
	{
		repo.Update(date);
	}

	public LiveData<List<Date>> getDates()
	{
		return dates;
	}
}
