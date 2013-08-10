package model;

import java.sql.ResultSet;
import java.util.ArrayList;

import util.AppData;

public class VimeoRecord extends Record {

	public VimeoRecord() {

		setType(AppData.VIDEO);
		setLanguage("unknown");
		setRights("unknown");
		setProvider(AppData.VIMEO);
	}

	public VimeoRecord(ResultSet set) {

		super(set);

	}

	@Override
	public void loadMoreInfo() {

		ArrayList<String> res = new ArrayList<String>();
		res.add(getUniqueUrl());
		setWebResources(res);

	}

	@Override
	public String getShownAt() throws Exception {

		if (getWebResources().isEmpty())
			loadMoreInfo();

		return getWebResources().get(0);
	}

}
