package com.absolute.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	private static final String JSON_PHOTO = "photo";
	private static final String JSON_SUSPECT ="suspect";
	
	private UUID mId;
	private String mTitle;
	//添加2个变量
	private Date mDate;
	private boolean mSolved;
	private Photo mPhoto;
	private String mSuspect;
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mTitle;
	}

	public Crime(){
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE,mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE,mDate.getTime());
		json.put(JSON_SUSPECT, mSuspect);
		return json;
		
	}
	
	
	public Crime(JSONObject json) throws JSONException{
		mId = UUID.fromString(json.getString(JSON_ID));
		if(json.has(JSON_TITLE)){
			mTitle = json.getString(JSON_TITLE);
		}
		if(json.has(JSON_SUSPECT)){
			mSuspect = json.getString(JSON_SUSPECT);
		}
		
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
		if(mPhoto != null){
			json.put(JSON_PHOTO, mPhoto.toJSON());
		}
		
	}
	

	/**
	 * @return the title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return mId;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return mDate;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		mDate = date;
	}

	/**
	 * @return the solved
	 */
	public boolean isSolved() {
		return mSolved;
	}

	/**
	 * @param solved the solved to set
	 */
	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	/**
	 * @return the photo
	 */
	public Photo getPhoto() {
		return mPhoto;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(Photo photo) {
		mPhoto = photo;
	}
	
	/**
	 * @return the suspect
	 */
	public String getSuspect() {
		return mSuspect;
	}

	/**
	 * @param suspect the suspect to set
	 */
	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}

}
