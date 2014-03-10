package com.googilyboogily.lifeorganizer.helpers;

import com.googilyboogily.lifeorganizer.objects.Note;

import org.json.JSONArray;
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

public class TagUtilities {
	public static ArrayList<String> GetTagNoteIDs(File tagFile, String tagToGet) {
		// Initilize vars
		FileInputStream fis;
		StringBuilder sb = new StringBuilder();
		String result;
		JSONObject jsonTag = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		ArrayList<String> arrayListNoteID = new ArrayList<String>();

		// Get the tagFile
		try {
			fis = new FileInputStream(tagFile.toString() + "/" + tagToGet + ".json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			String line;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			} // end while

			fis.close();
		} catch(OutOfMemoryError ome){
			ome.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} // end try/catch/catch

		// Put the data into result
		result = sb.toString();

		// Make the string into a JSONObject
		try {
			jsonTag = new JSONObject(result);
		} catch(JSONException je) {
			je.printStackTrace();
		} // end try/catch

		// Convert into a JSONArray
		try {
			jsonArray = jsonTag.getJSONArray("NoteIDs");
		} catch(JSONException je) {
			je.printStackTrace();
		} // end try/catch

		// Add each NoteID into arrayListNoteID
		for(int i = 0; i < jsonArray.length(); i++) {
			try {
				arrayListNoteID.add(jsonArray.get(i).toString());
			} catch(JSONException je) {
				je.printStackTrace();
			} // end try/catch
		} // end for

		return arrayListNoteID;
	} // end GetTagNoteIDs()

	public static void UpdateTag(File tagFile, String tagToUpdate, ArrayList<String> noteIDs) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArr = new JSONArray();

		// Push all of the noteIDs into jsonArr
		for(int i = 0; i < noteIDs.size(); i++) {
			jsonArr.put(noteIDs.get(i));
		} // end for

		// Push jsonArr to NoteIDs in the json object
		try {
			jsonObj.put("NoteIDs", jsonArr);
		} catch(JSONException je) {
			je.printStackTrace();
		} // end try/catch

		// Create/Update the new file with the current NoteIDs
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tagFile + "/" + tagToUpdate + ".json");
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} // end try/catch
		// Write the file/data to the storage system
		try {
			fos.write(jsonObj.toString().getBytes());
			fos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} // end try/catch
	} // end UpdateTag()

	public static ArrayList<String> GetNoteIDsFromTag(ArrayList<Note> noteList, String tagName) {
		ArrayList<String> noteIDs = new ArrayList<String>();

		for(int i = 0; i < noteList.size(); i++) {
			if(noteList.get(i).getTags().contains(tagName)) {
				noteIDs.add(String.valueOf(noteList.get(i).getNoteID()));
			} // end if
		} // end for

		return noteIDs;
	} // GetNoteIDsFromTag()

} // end class
