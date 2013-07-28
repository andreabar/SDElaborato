package view.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import controllers.TaskController;

import util.AppData;
import views.ResultView;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;


public class ResultViewController {
	
	private ResultView resultView;
	
	public ResultViewController(ResultView r){
		setResultView(r);
		this.getResultView().getRefreshButton().addListener(new RefreshButtonListener(this));
		loadResultTable();
	}
	
	protected void loadResultTable(){
		this.resultView.getFileTable().removeAllItems();
		try {
			ResultSet result = TaskController.getResults(AppData.userID);
			while(result.next()){
				String title = result.getString("title");
				String type = result.getString("type");
				String status = result.getString("status");
				Integer scheduledTaskId = result.getInt("id");
				
				Object rowItem[] = new Object[]{title, type, status};
				this.resultView.getFileTable().addItem(rowItem, scheduledTaskId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultView getResultView() {
		return resultView;
	}

	public void setResultView(ResultView resultView) {
		this.resultView = resultView;
	}

}

class RefreshButtonListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6367448598090263990L;
	private ResultViewController resultViewController;
	
	public RefreshButtonListener(ResultViewController rvc){
		this.resultViewController = rvc;
	}

	public void buttonClick(ClickEvent event) {
		this.resultViewController.loadResultTable();
	}
	
}


