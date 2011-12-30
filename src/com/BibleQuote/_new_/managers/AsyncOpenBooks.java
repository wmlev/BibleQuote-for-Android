package com.BibleQuote._new_.managers;

import com.BibleQuote._new_.listeners.ChangeBooksEvent;
import com.BibleQuote._new_.listeners.IChangeBooksListener.ChangeCode;
import com.BibleQuote._new_.models.Module;
import com.BibleQuote.exceptions.ModuleNotFoundException;
import com.BibleQuote.utils.AsyncTaskManager;
import com.BibleQuote.utils.Log;
import com.BibleQuote.utils.Task;

public class AsyncOpenBooks extends Task {
	private final String TAG = "AsyncTaskChapterOpen";
	
	private ChangeBooksEvent event;
	private Librarian librarian;
	private Module module;
	AsyncTaskManager asyncTaskManager;
	
	public AsyncOpenBooks(String message, Librarian librarian, Module module, AsyncTaskManager asyncTaskManager) {
		super(message);
		this.librarian = librarian;
		this.module = module;
		this.asyncTaskManager = asyncTaskManager;
	}

	
	@Override
	protected Boolean doInBackground(String... arg0) {
		Log.i(TAG, String.format("Load books for module with moduleID=%1$s", module.getID()));
		try {
			librarian.getModuleBooksList(module.getID());
			event = new ChangeBooksEvent(ChangeCode.BooksAdded, module, null);
		} catch (ModuleNotFoundException e) {
			Log.e(TAG, e);
		}
		return true;
	}
	
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}

	
	public ChangeBooksEvent getEvent() {
		return event;
	}
}
