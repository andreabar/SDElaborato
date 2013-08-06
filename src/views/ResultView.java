package views;

import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ResultView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5094527720364475453L;
	
	private Panel mainPanel;
	private GridLayout panelLayout;
	private Label info;
	private Table fileTable;
	private Button refreshButton;
	private Button deleteButton;
	
	public ResultView(){
		initViewComponents();
	}
	
	private void initViewComponents(){
		
		this.mainPanel = new Panel("Multimedia Data Crawler");
		
		this.info = new Label("Here you find the data you requested.");
		
		this.refreshButton = new Button("Refresh Table");
		
		this.deleteButton = new Button("Delete");
		this.deleteButton.setEnabled(false);
		
		this.fileTable = new Table("ATTENTION: Deleting a row will not delete the file from your disk");
		this.fileTable.addContainerProperty("Title", String.class, null);
		this.fileTable.addContainerProperty("Type", String.class, null);
		fileTable.addContainerProperty("Status", String.class, null);
		this.fileTable.addContainerProperty("Progress", Component.class, null);
		this.fileTable.addContainerProperty("Date", Date.class, null);

		this.fileTable.setSizeFull();
		this.fileTable.setSelectable(true);
		this.fileTable.setImmediate(true);
		this.fileTable.setPageLength(10);
		
		this.panelLayout = new GridLayout(4, 3);
		this.panelLayout.setSizeFull();
		this.panelLayout.setSpacing(true);
		this.panelLayout.setMargin(true);
		this.panelLayout.setColumnExpandRatio(0, 0.20f);
		this.panelLayout.setColumnExpandRatio(1, 0.20f);
		this.panelLayout.setColumnExpandRatio(2, 0.20f);
		this.panelLayout.setColumnExpandRatio(3, 0.20f);
		this.panelLayout.setSizeFull();
		
		this.panelLayout.addComponent(info, 0, 0, 3, 0);
		this.panelLayout.addComponent(refreshButton, 0, 1);
		this.panelLayout.addComponent(deleteButton, 1, 1);
		
		this.mainPanel.setContent(panelLayout);
		this.addComponent(mainPanel);
		this.addComponent(fileTable);
		
	}
	
	public GridLayout getPanelLayout() {
		return panelLayout;
	}
	public void setPanelLayout(GridLayout panelLayout) {
		this.panelLayout = panelLayout;
	}
	public Panel getMainPanel() {
		return mainPanel;
	}
	public void setMainPanel(Panel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public Table getFileTable() {
		return fileTable;
	}

	public void setFileTable(Table fileTable) {
		this.fileTable = fileTable;
	}

	public Button getRefreshButton() {
		return refreshButton;
	}

	public void setRefreshButton(Button refreshButton) {
		this.refreshButton = refreshButton;
	}

}
