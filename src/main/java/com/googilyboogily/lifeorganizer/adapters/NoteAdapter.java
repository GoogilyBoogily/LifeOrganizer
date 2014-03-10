package com.googilyboogily.lifeorganizer.adapters;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.googilyboogily.lifeorganizer.app.R;
import com.googilyboogily.lifeorganizer.helpers.FileUtilities;
import com.googilyboogily.lifeorganizer.helpers.GeneralUtilities;
import com.googilyboogily.lifeorganizer.objects.Note;

import java.io.File;
import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
	// Vars
	final Activity context;
	final ArrayList<Note> noteList;
	File activeNotesDir;

	EditText noteTitle;
	EditText noteContent;
	EditText noteTags;

	Button buttonA;
	Button buttonB;
	Button buttonC;

	TextView noteIDLabel;
	TextView noteIDNumber;
	TextView noteTimestamp;

	public NoteAdapter(Activity context, final ArrayList<Note> noteList, File activeNotesDir) {
		this.context = context;
		this.noteList = noteList;
		this.activeNotesDir = activeNotesDir;
	} // end NoteAdapter()

	@Override
	public int getCount() {
		return this.noteList.size();
	} // end getCount()

	// ViewHolder class used for holding recycled views
	static class ViewHolder {
		public EditText editTextTitle;
		public EditText editTextContent;
		public EditText editTextTags;
		public TextWatcher textWatcherTitle;
		public TextWatcher textWatcherContent;
		public TextWatcher textWatcherTags;
	} // end class

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			view = context.getLayoutInflater().inflate(R.layout.note_layout, null);

			// Create vew holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.editTextTitle = (EditText)view.findViewById(R.id.editTextTitle);
			viewHolder.editTextContent = (EditText)view.findViewById(R.id.editTextContent);
			viewHolder.editTextTags = (EditText)view.findViewById(R.id.editTextTags);
			view.setTag(viewHolder);
		} // end if

		ViewHolder holder = (ViewHolder) view.getTag();
		// Remove any existing TextWatcher that will be keyed to the wrong ListItem
		if (holder.textWatcherTitle != null) {
			holder.editTextTitle.removeTextChangedListener(holder.textWatcherTitle);
		} // end if
		if (holder.editTextContent != null) {
			holder.editTextContent.removeTextChangedListener(holder.textWatcherContent);
		} // end if
		if (holder.editTextTags != null) {
			holder.editTextTags.removeTextChangedListener(holder.textWatcherTags);
		} // end if

		noteTitle = (EditText)view.findViewById(R.id.editTextTitle);
		noteContent = (EditText)view.findViewById(R.id.editTextContent);
		noteTags = (EditText)view.findViewById(R.id.editTextTags);

		noteTitle.setText(noteList.get(position).getTitle());
		noteContent.setText(noteList.get(position).getContent());
		noteTags.setText(noteList.get(position).getTagsString());

		buttonA = (Button)view.findViewById(R.id.buttonA);
		buttonB = (Button)view.findViewById(R.id.buttonB);
		buttonC = (Button)view.findViewById(R.id.buttonC);

		noteIDLabel = (TextView)view.findViewById(R.id.textViewNoteIDLabel);
		noteIDNumber = (TextView)view.findViewById(R.id.textViewNoteIDNumber);
		noteTimestamp = (TextView)view.findViewById(R.id.textViewTimestamp);

		// Set the timestamp for the current time
		// TODO: Set the timestamp for when the note was last edited
		noteTimestamp.setText(GeneralUtilities.getCurrentTimeAndDate());

		// Set the noteIDNumber to the notes NoteID
		noteIDNumber.setText(String.valueOf(noteList.get(position).getNoteID()));

		// Event listeners

		// Keep a reference to the TextWatcher so that we can remove it later
		// Textwatcher event for note title
		holder.textWatcherTitle = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!(s.toString().equals(noteList.get(position).getTitle()))) {
					noteList.get(position).setTitle(s.toString());
					FileUtilities.saveNote(activeNotesDir, noteList.get(position));
				} // end if
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		holder.editTextTitle.addTextChangedListener(holder.textWatcherTitle);
		holder.editTextTitle.setText(noteList.get(position).getTitle());

		// Textwatcher event for note content
		holder.textWatcherContent = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!(s.toString().equals(noteList.get(position).getContent()))) {
					noteList.get(position).setContent(s.toString());
					FileUtilities.saveNote(activeNotesDir, noteList.get(position));
				} // end if
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		holder.editTextContent.addTextChangedListener(holder.textWatcherContent);
		holder.editTextContent.setText(noteList.get(position).getContent());


		// Textwatcher event for note tags
		holder.textWatcherTags = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!(s.toString().equals(noteList.get(position).getTagsString()))) {
					noteList.get(position).setTagsFromString(s.toString());
					FileUtilities.saveNote(activeNotesDir, noteList.get(position));
				} // end if
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		holder.editTextTags.addTextChangedListener(holder.textWatcherTags);
		holder.editTextTags.setText(noteList.get(position).getTagsString());


		// Add an onClick listener for buttonA
		buttonA.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				noteList.get(position).setTitle("Hello");
				notifyDataSetChanged();
			} // end onClick()
		});

		// Add an onClick listener for buttonC
		buttonC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Remove the note from the system
				FileUtilities.deleteNote(activeNotesDir, noteList.get(position).getNoteID());

				// Remove the note from noteList
				noteList.remove(position);

				// Update the listview
				notifyDataSetChanged();
			} // end onClick()
		});

		return view;
	} // end getView()

	@Override
	public long getItemId(int position) {
		return position;
	} // end getItemId()

	@Override
	public Note getItem(int index) {
		return noteList.get(index);
	} // end getItem()

}// end class NoteAdapter
