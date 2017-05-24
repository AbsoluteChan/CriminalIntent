package com.absolute.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
	
	private UUID mId;
	private String mTitle;
	//添加2个变量
	private Date mDate;
	private boolean mSolved;
	
	
	public Crime(){
		mId = UUID.randomUUID();
		mDate = new Date();
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
	
	

}
