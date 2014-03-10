package com.googilyboogily.lifeorganizer.helpers;

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

public class FileUtilities {

	public static void saveNewNote(File activeNotesDir, File noteIDFile, final ArrayList<Note> noteList, Note noteToSave) {
		// Get the current noteID
		long curNoteID = findNoteID(noteIDFile);

		// Create the JSON object and set the current NoteID
		JSONObject json = JSONUtilities.NoteToJSON(noteToSave);

		try {
			json.remove("NoteID");
			json.put("NoteID", String.valueOf(curNoteID));
		} catch(JSONException je) {
			je.printStackTrace();
		} // end try/catch

		// Create a new file for the new note and populate it with it's data
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(activeNotesDir + "/note" + curNoteID + ".json");
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} // end try/catch

		// Write the file/data to the storage system
		try {
			fos.write(json.toString().getBytes());
			fos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} // end try/catch
	} // end saveNewNote()

	public static void saveNote(File activeNotesDir, Note noteToSave) {
		// Create the JSON object and put the new notes data in it
		JSONObject json = JSONUtilities.NoteToJSON(noteToSave);

		// Create a new file for the new note and populate it with it's data
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(activeNotesDir + "/note" + noteToSave.getNoteID() + ".json");
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} // end try/catch

		// Write the file/data to the storage system
		try {
			fos.write(json.toString().getBytes());
			fos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} // end try/catch
	} // end saveNote()

	public static void deleteNote(File activeNotesDir, long noteID) {
		File noteToDelete = new File(activeNotesDir.toString() + "/note" + String.valueOf(noteID) + ".json");

		if (noteToDelete.exists()) {
			noteToDelete.delete();
		} // end if
	} // end deleteNote()

	public static long findNoteID(File noteIDFile) {
		// Initilize vars
		FileInputStream fis;
		StringBuilder sb = new StringBuilder();

		// Get the current noteID that we're at
		try {
			fis = new FileInputStream(noteIDFile.toString());
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

		// Return the noteID as a long
		return Long.parseLong(sb.toString());
	} // end findNoteID()

	public static long FindAndIncrementNoteID(String noteIDPath) {
		// Initilize vars
		FileInputStream fis;
		String result;
		StringBuilder sb = new StringBuilder();

		noteIDPath = noteIDPath + "/noteID";
		// Get the current noteID that we're at
		try {
			fis = new FileInputStream(noteIDPath);
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

		// Get the result in a string
		result = sb.toString();

		// Convert the string to a long
		long noteIDNumber = Long.parseLong(result);

		// Increment it
		noteIDNumber++;

		// Convert back to a string
		String noteIDString = String.valueOf(noteIDNumber);

		// Write the new number to the file!
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(noteIDPath);
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} // end try/catch

		try {
			fos.write(noteIDString.getBytes());
			fos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} // end try/catch

		return noteIDNumber--;
	} // end FindAndIncrementNoteID()

} // end class
