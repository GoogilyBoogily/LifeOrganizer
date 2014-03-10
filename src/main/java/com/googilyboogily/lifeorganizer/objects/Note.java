package com.googilyboogily.lifeorganizer.objects;

import android.content.Context;

import com.googilyboogily.lifeorganizer.app.MainActivity;
import com.googilyboogily.lifeorganizer.helpers.FileUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Note {
	// Note variables
	private String title;
	private String content;
	private ArrayList<String> tags;
	private long noteID;

	// Activity content
	Context appContext;

	// Constructor
	public Note(MainActivity mainActivity) {
		// Get the application context
		this.appContext = mainActivity.getApplicationContext();

		this.title = "Title";
		this.content = "Content";

		this.tags = new ArrayList<String>();

		this.noteID = FileUtilities.FindAndIncrementNoteID(appContext.getFilesDir().toString());
	} // end Note()

	public Note(MainActivity mainActivity, String title, String content, String tags) {
		// Get the application context
		this.appContext = mainActivity.getApplicationContext();

		this.title = title;
		this.content = content;

		this.tags = getTagsFromString(tags);

		this.noteID = FileUtilities.FindAndIncrementNoteID(appContext.getFilesDir().toString());
	} // end Note()

	public Note(MainActivity mainActivity, JSONObject jsonString) {
		// Get the application context
		this.appContext = mainActivity.getApplicationContext();

		// Set the title to the JSON value
		try {
			this.title = jsonString.get("Title").toString();
		} catch(JSONException e) {
			e.printStackTrace();
		} // end try/catch

		// Set the content to the JSON value
		try {
			this.content = jsonString.get("Content").toString();
		} catch(JSONException e) {
			e.printStackTrace();
		} // end try/catch

		this.noteID = FileUtilities.FindAndIncrementNoteID(appContext.getFilesDir().toString());
	} // end Note()

	public Note(JSONObject jsonString) {
		// Set the title to the JSON value
		try {
			this.title = jsonString.get("Title").toString();
		} catch(JSONException e) {
			e.printStackTrace();
		} // end try/catch

		// Set the content to the JSON value
		try {
			this.content = jsonString.get("Content").toString();
		} catch(JSONException e) {
			e.printStackTrace();
		} // end try/catch

		// Set the noteID to the JSON value
		try {
			this.noteID = Long.parseLong(jsonString.get("NoteID").toString());
		} catch(JSONException e) {
			e.printStackTrace();
		} // end try/catch

		// Set the tags to the JSON value
		try {
			this.tags = new ArrayList<String>(Arrays.asList(jsonString.get("Tags").toString().split(",")));
		} catch(JSONException e) {
			e.printStackTrace();
		} // end try/catch
	} // end Note()

	public void addTag(String tagtoAdd) {
		this.tags.add(tagtoAdd);
	} // end addTag()

	public void removeTag(String tagToRemove) {
		this.tags.remove(tagToRemove);
	} // end removeTag()

	public String getTitle() {
		return this.title;
	} // end getTitle()

	public String getContent() {
		return this.content;
	} // end getContent()

	public ArrayList<String> getTags() {
		return this.tags;
	} // end getTags()

	public String getTagsString() {
		String tagsInString = "";
		if (this.getTags() != null) {
			for(int i = 0; i < this.tags.size(); i++) {
				tagsInString += this.getTags().get(i);

				if (i != (this.tags.size() - 1)) {
					tagsInString += ", ";
				} // end if
			} // end for
		} // end if

		return tagsInString;
	} // end getTags()

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	} // end setTags()

	public void setTagsFromString(String tags) {
		this.tags = new ArrayList<String>(Arrays.asList(tags.split(",")));
	} // end setTagsFromString()

	public long getNoteID() {
		return this.noteID;
	} // end getNoteID()

	public void setTitle(String title) {this.title = title;} // end setTitle()

	public void setContent(String content) {this.content = content;} // end setContent()

	// Note functions

	// Creates an ArrayList from a string of tags (seperated by commas) and returns it
	private static ArrayList<String> getTagsFromString(String stringTags) {
		ArrayList<String> arrayListTags = new ArrayList<String>((Arrays.asList(stringTags.split(","))));

		return arrayListTags;
	} // end getTagsFromString()

} // end class
