package com.googilyboogily.lifeorganizer.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.googilyboogily.lifeorganizer.adapters.NoteAdapter;
import com.googilyboogily.lifeorganizer.helpers.FileUtilities;
import com.googilyboogily.lifeorganizer.helpers.JSONUtilities;
import com.googilyboogily.lifeorganizer.objects.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {

	// Vars
	ListView listView;
	final ArrayList<Note> noteList = new ArrayList<Note>();
	NoteAdapter noteAdapter;

	// Quick add vars
	EditText quickAddTitle;
	EditText quickAddContent;
	EditText quickAddTags;
	Button quickAddButton;

	// Directory vars
	File noteIDFile = null;
	File activeNotesDir = null;
	File tagFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Start my logic

	    /*
	    File dir = getFilesDir();
	    ArrayList<String> children;
	    if (dir.isDirectory()) {
		    children = new ArrayList<String>(Arrays.asList(dir.list()));
	    } // end if
	    */

		// Get path for the noteID file
		noteIDFile = new File(getFilesDir().toString() + "/noteID");

		// If noteID doesn't exist, create it!!
		if(!(noteIDFile.exists())) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(noteIDFile);
			} catch(FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} // end try/catch

			// Write the file/data to the storage system
			try {
				fos.write("0".getBytes());
				fos.close();
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} // end try/catch
		} // end if

		// Grab the ActiveNotes directory path
		activeNotesDir = new File(getFilesDir().toString() + "/ActiveNotes");
		// If the ActiveNotes directory doesn't exist, make that shit!
		if(!(activeNotesDir.isDirectory())) {
			activeNotesDir.mkdir();
		} // end if

		// Grab the Tags directory path
		tagFile = new File(getFilesDir().toString() + "/Tags");
		// If the Tags directory doesn't exist, make that shit!
		if(!(tagFile.isDirectory())) {
			tagFile.mkdir();
		} // end if

		// Get a list of the files in the ActiveNotes directory
		File fileList[] = activeNotesDir.listFiles();

		// If there are created files, load them into noteList
		if(!(fileList.length == 0)) {
			FileInputStream fis;
			String result;
			JSONObject note = null;

			for(int count = 0; count < fileList.length; count++) {
				StringBuilder sb = new StringBuilder();

				try {
					fis = new FileInputStream(fileList[count]);
					BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
					String line;

					while ((line = reader.readLine()) != null) {
						sb.append(line).append("\n");
					} // end while

					fis.close();
				} catch(OutOfMemoryError ome){
					ome.printStackTrace();
				} catch(Exception e){
					e.printStackTrace();
				} // end try/catch/catch

				result = sb.toString();

				try {
					note = new JSONObject(result);
				} catch(JSONException je) {
					je.printStackTrace();
				} // end try/catch

				noteList.add(JSONUtilities.JSONToNote(note));
			} // end for
		} // end if/else


		// Grab the quick add widgets
		quickAddTitle = (EditText)findViewById(R.id.editTextQuickAddTitle);
		quickAddContent = (EditText)findViewById(R.id.editTextQuickAddContent);
		quickAddTags = (EditText)findViewById(R.id.editTextQuickAddTags);
		quickAddButton = (Button)findViewById(R.id.buttonQuickAddNote);

		// Grab the listview
		listView = (ListView)findViewById(R.id.listViewNotes);

		// Create the noteAdapter and associate noteList with it
		noteAdapter = new NoteAdapter(this, noteList, activeNotesDir);

		// Set the listView's adapter to noteAdapter
		listView.setAdapter(noteAdapter);


		listView.setStackFromBottom(true);

		// Quick add event
		quickAddButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AddNewNote(quickAddTitle.getText().toString(), quickAddContent.getText().toString(), quickAddTags.getText().toString());
			} // end onClick()
		});

	} // end onCreate()

	public void AddNewNote(String title, String content, String tags) {
		if(!(title.isEmpty() && content.isEmpty())) {
			Note noteToSave = new Note(this, title, content, tags);

			// Add a new note to the noteList
			noteList.add(noteToSave);

			// Save note to device
			FileUtilities.saveNewNote(activeNotesDir, noteIDFile, noteList, noteToSave);

			// Reset the quick add fields
			quickAddTitle.setText("");
			quickAddContent.setText("");
			quickAddTags.setText("");

			noteAdapter.notifyDataSetChanged();
		} // end if

	} // end AddNewNote()

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	} // end onCreateOptionsMenu()

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} // end if

		return super.onOptionsItemSelected(item);
	} // end onOptionsItemSelected()

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		} // end PlaceholderFragment()

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);

			return rootView;
		} // end onCreateView()

	} // end class PlaceholderFragment

} // end class MainActivity
