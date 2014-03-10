package com.googilyboogily.lifeorganizer.helpers;

import com.googilyboogily.lifeorganizer.objects.Note;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtilities {

	// Converts a JSON object to a Note object
	public static Note JSONToNote(JSONObject JSONToConvert) {
		Note noteFromJSON = new Note(JSONToConvert);

		return noteFromJSON;
	} // end JSONToNote()

	// Converts a Note object to a JSON object
	public static JSONObject NoteToJSON(Note noteToConvert) {
		JSONObject JSONFromNote = new JSONObject();

		try {
			JSONFromNote.put("NoteID", noteToConvert.getNoteID());
		} catch(JSONException je) {
			je.printStackTrace();
		} // end try/catch

		try {
			JSONFromNote.put("Tags", noteToConvert.getTagsString());
		} catch(JSONException je) {
			je.printStackTrace();
		} // end try/catch

		try {
			JSONFromNote.put("Content", noteToConvert.getContent());
		} catch(JSONException je) {
			je.printStackTrace();
		} // end try/catch

		try {
			JSONFromNote.put("Title", noteToConvert.getTitle());
		} catch(JSONException je) {
			je.printStackTrace();
		} // end try/catch

		return JSONFromNote;
	} // end NoteToJSON()

} // end class
