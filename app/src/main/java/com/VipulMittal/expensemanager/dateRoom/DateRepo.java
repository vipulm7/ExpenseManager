package com.VipulMittal.expensemanager.dateRoom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DateRepo {
	private DateDAO dateDAO;
	private LiveData<List<Date>> dates;

	public DateRepo(Application application)
	{
		DateDatabase dateDatabase=DateDatabase.getInstance(application);
		dateDAO=dateDatabase.dateDAO();
		dates=dateDAO.getDates();
	}

	public void Insert(Date date)
	{
		new InsertNoteASyncTask(dateDAO).execute(date);
	}

	public void Delete(Date date)
	{
		new DeleteNoteASyncTask(dateDAO).execute(date);
	}

	public void Update(Date date)
	{
		new UpdateNoteASyncTask(dateDAO).execute(date);
	}

	public LiveData<List<Date>> getDates()
	{
		return dates;
	}

	private static class InsertNoteASyncTask extends AsyncTask<Date, Void, Void>
	{
		private DateDAO dateDAO;

		private InsertNoteASyncTask(DateDAO dateDAO)
		{
			this.dateDAO=dateDAO;
		}

		@Override
		protected Void doInBackground(Date... dates) {
			this.dateDAO.Insert(dates[0]);
			return null;
		}
	}

	private static class DeleteNoteASyncTask extends AsyncTask<Date, Void, Void>
	{
		private DateDAO dateDAO;

		private DeleteNoteASyncTask(DateDAO dateDAO)
		{
			this.dateDAO=dateDAO;
		}

		@Override
		protected Void doInBackground(Date... dates) {
			this.dateDAO.Delete(dates[0]);
			return null;
		}
	}

	private static class UpdateNoteASyncTask extends AsyncTask<Date, Void, Void>
	{
		private DateDAO dateDAO;

		private UpdateNoteASyncTask(DateDAO dateDAO)
		{
			this.dateDAO=dateDAO;
		}

		@Override
		protected Void doInBackground(Date... dates) {
			this.dateDAO.Update(dates[0]);
			return null;
		}
	}

}
