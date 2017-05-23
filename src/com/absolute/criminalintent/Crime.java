package com.absolute.criminalintent;

import java.util.UUID;

public class Crime {
	
	private UUID mId;
	private String mTitle;
	
	public Crime(){
		mId = UUID.randomUUID();
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
	
	

}
