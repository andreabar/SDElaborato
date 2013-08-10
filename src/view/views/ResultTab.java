package view.views;

import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ResultTab extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5094527720364475453L;
	
	private VerticalLayout vertical;
	private Panel mainPanel;
	private GridLayout panelLayout;
	private Label info;
	private Table fileTable;
	private Button clear;
	
	public ResultTab(){
		initViewComponents();
	}
	
	private void initViewComponents(){
		
		this.setCaption("My Results");

		
		this.vertical = new VerticalLayout();
		
		this.mainPanel = new Panel("Multimedia Data Crawler");
		
		this.info = new Label("Here you can check out the progess of the data you requested.");
		
		this.clear = new Button("Clear");
		this.clear.setDescription("Remove from the table all 'Not downloadable' files.");
		this.clear.setEnabled(false);
				
		this.fileTable = new Table();
		this.fileTable.addContainerProperty("Title", String.class, null);
		this.fileTable.addContainerProperty("Type", String.class, null);
		this.fileTable.addContainerProperty("Keyword", String.class, null);
		this.fileTable.addContainerProperty("Provider", String.class, null);
		this.fileTable.addContainerProperty("Status", String.class, null);
		this.fileTable.addContainerProperty("Progress", Component.class, null);
		this.fileTable.addContainerProperty("Date Query", Date.class, null);
		this.fileTable.addContainerProperty("Date Download", Date.class, null);

		this.fileTable.setSizeFull();
		this.fileTable.setSelectable(true);
		this.fileTable.setImmediate(true);
		this.fileTable.setPageLength(12);
		
		this.panelLayout = new GridLayout(4, 2);
		this.panelLayout.setSizeFull();
		this.panelLayout.setSpacing(true);
		this.panelLayout.setMargin(true);
		this.panelLayout.setColumnExpandRatio(0, 0.20f);
		this.panelLayout.setColumnExpandRatio(1, 0.20f);
		this.panelLayout.setColumnExpandRatio(2, 0.20f);
		this.panelLayout.setColumnExpandRatio(3, 0.20f);
		this.panelLayout.setSizeFull();
		
		this.panelLayout.addComponent(info, 0, 0, 3, 0);
		this.panelLayout.addComponent(clear, 0, 1);
		
		this.mainPanel.setContent(panelLayout);
		
		this.vertical.addComponent(mainPanel);
		this.vertical.addComponent(fileTable);

		this.addComponent(vertical);
		
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

	public VerticalLayout getVertical() {
		return vertical;
	}

	public void setVertical(VerticalLayout vertical) {
		this.vertical = vertical;
	}

	public Button getClear() {
		return clear;
	}

	public void setClear(Button clear) {
		this.clear = clear;
	}

}
