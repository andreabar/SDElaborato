package view.views;

import java.util.Date;

import refresher.RefreshableTable;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
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
	private Table fileProgressTable;
	private Table downloadedFileTable;
	private Button clear, deleteSelected, seeMetadata, deleteFile;

	

	public Button getSeeMetadata() {
		return seeMetadata;
	}

	public ResultTab(TabSheetView tabSheetView){
		initViewComponents();
		
	}
	
	private void initViewComponents(){
		
		this.setCaption("My Results");
		
		this.vertical = new VerticalLayout();
		
		HorizontalLayout hl = new HorizontalLayout();
		
		this.mainPanel = new Panel("Multimedia Data Crawler");
		
		this.info = new Label("Here you can check out the progess of the data you requested.");
		
		this.clear = new Button("Clear all 'Not downloadable' items");
		this.clear.setEnabled(false);
		
		deleteSelected = new Button("Delete Selected Row");
		deleteSelected.setEnabled(false);
		
		seeMetadata = new Button("See metadata");
		seeMetadata.setEnabled(false);
		this.fileProgressTable = new Table("Files in Download");
		this.fileProgressTable.addContainerProperty("Title", String.class, null);
		this.fileProgressTable.addContainerProperty("Type", String.class, null);
		this.fileProgressTable.addContainerProperty("Keyword", String.class, null);
		this.fileProgressTable.addContainerProperty("Provider", String.class, null);
		this.fileProgressTable.addContainerProperty("URL to process", Link.class, null);
		
		this.fileProgressTable.addContainerProperty("Status", String.class, null);
		this.fileProgressTable.addContainerProperty("Progress", Component.class, null);
		this.fileProgressTable.addContainerProperty("Date Query", Date.class, null);
		this.fileProgressTable.addContainerProperty("Date Download", Date.class, null);

		this.fileProgressTable.setSizeFull();
		this.fileProgressTable.setSelectable(true);
		this.fileProgressTable.setImmediate(true);
		this.fileProgressTable.setPageLength(12);

		fileProgressTable.setColumnWidth("Status", 150);
		fileProgressTable.setColumnWidth("Progress", 150);
		
		this.downloadedFileTable = new Table("Downloaded Files");
		this.downloadedFileTable.addContainerProperty("Title", String.class, null);
		this.downloadedFileTable.addContainerProperty("Type", String.class, null);
		this.downloadedFileTable.addContainerProperty("Source", String.class, null);
		this.downloadedFileTable.addContainerProperty("Keyword", String.class, null);
		this.downloadedFileTable.addContainerProperty("Status", String.class, null);
		this.downloadedFileTable.addContainerProperty("Link", Component.class, null);

		this.downloadedFileTable.addContainerProperty("Provider", String.class, null);
		this.downloadedFileTable.addContainerProperty("Data Provider", String.class, null);
		this.downloadedFileTable.addContainerProperty("Source link", Link.class, null);
		this.downloadedFileTable.addContainerProperty("Resource link", Link.class, null);

		this.downloadedFileTable.addContainerProperty("Language", String.class, null);
		this.downloadedFileTable.addContainerProperty("Date Query", Date.class, null);
		this.downloadedFileTable.addContainerProperty("Date Download", Date.class, null);
		
		this.downloadedFileTable.setSizeFull();
		this.downloadedFileTable.setSelectable(true);
		this.downloadedFileTable.setImmediate(true);
		this.downloadedFileTable.setPageLength(12);
		
		this.deleteFile = new Button("Delete a file");
		this.deleteFile.setEnabled(false);
		
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
		this.panelLayout.addComponent(deleteSelected, 0, 1);
		
		
		this.mainPanel.setContent(panelLayout);
		
		hl.addComponent(seeMetadata);
		hl.addComponent(deleteFile);
		hl.addComponent(clear);
		
		this.vertical.addComponent(mainPanel);
		this.vertical.addComponent(fileProgressTable);
		this.vertical.addComponent(hl);
		this.vertical.addComponent(downloadedFileTable);

		this.addComponent(vertical);
		
	}
	
	public Button getDeleteSelected() {
		return deleteSelected;
	}

	public void setDeleteSelected(Button deleteSelected) {
		this.deleteSelected = deleteSelected;
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
		return fileProgressTable;
	}

	public void setFileTable(Table fileTable) {
		this.fileProgressTable = fileTable;
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

	public Table getDownloadedFileTable() {
		return downloadedFileTable;
	}

	public void setDownloadedFileTable(Table downloadedFileTable) {
		this.downloadedFileTable = downloadedFileTable;
	}
	
	public Button getDeleteFile() {
		return deleteFile;
	}

	public void setDeleteFile(Button deleteFile) {
		this.deleteFile = deleteFile;
	}

}
