package model;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.JSONObject;

import util.AppData;

public class VimeoRecord extends Record {

	private JSONObject metadata;
	public VimeoRecord() {

		setType(AppData.VIDEO);
		setLanguage("unknown");
		setRights("unknown");
		setProvider(AppData.VIMEO);
		setDataProvider("unknown");
		setDataProviderDescr("unknown");
		
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

	public void setMetadata(JSONObject jsonItem) {
		metadata = jsonItem;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

}
