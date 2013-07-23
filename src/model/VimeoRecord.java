package model;

import java.sql.ResultSet;
import java.util.ArrayList;

public class VimeoRecord extends Record {

	public VimeoRecord() {
	
		setType("VIDEO");
		setLanguage("unknown");
		setRights("unknown");
		setProvider("VIMEO");
	}
	
	public VimeoRecord(ResultSet set){
		
		super(set);
		
	}
	
	@Override
	public void loadMoreInfo() {

		ArrayList<String> res = new ArrayList<String>();
		res.add(getUniqueUrl());
		setWebResources(res);
		
	}

	@Override
	public String getShownAt() {

		if(getWebResources().isEmpty())
			loadMoreInfo();
		
		return getWebResources().get(0);
	}

	
	
}