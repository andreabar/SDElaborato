package views;


import java.sql.SQLException;
import java.util.ArrayList;

import model.Record;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import controllers.VerificationHandler;

public class DetailsView extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7067858500866643855L;

	private VerticalLayout layout;
	private Table recordsTable;
	private Button seeOnline, verify;
	private int userID;
	
	public DetailsView(int userID){
		initViewComponent();
		this.userID = userID;
	}
	
	private void initViewComponent(){
		this.setWidth("50%");
		this.setHeight("50%");
		
		this.layout = new VerticalLayout();
		
		
		
		verify = new Button("Verify & Download");
		seeOnline = new Button("See Online");
		seeOnline.addListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				
				Record selected = ((Record)recordsTable.getValue());
				try {
					DetailsView.this.open(new ExternalResource(selected.getShownAt()), "_blank");
				} catch (Exception e) {
					getApplication().getMainWindow().showNotification("Server Error", Window.Notification.TYPE_ERROR_MESSAGE);
				}
				
				
			}
		});
		this.recordsTable = new Table();
		recordsTable.setPageLength(10);
		this.recordsTable.addContainerProperty("Title", String.class, null);
		this.recordsTable.addContainerProperty("Language", String.class, null);
		this.recordsTable.addContainerProperty("Type", String.class, null);
		this.recordsTable.addContainerProperty("IPR", String.class, null);
		this.recordsTable.addContainerProperty("Keep", CheckBox.class, new CheckBox(null, false));
		
		
		this.recordsTable.setSelectable(true);
		this.recordsTable.setSizeFull();
		
		this.layout.addComponent(recordsTable);
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(seeOnline);
		hl.addComponent(verify);
		
		hl.setComponentAlignment(seeOnline, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(verify, Alignment.BOTTOM_RIGHT);
		
		verify.addListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1533022295485889710L;

			public void buttonClick(ClickEvent event) {
				
				ArrayList<Record> toKeep = new ArrayList<Record>();
				for(Object row : recordsTable.getItemIds()){
					
					if(((CheckBox)recordsTable.getItem(row).getItemProperty("Keep").getValue()).booleanValue())
						toKeep.add((Record)row);
					
				}
					
				if(toKeep.isEmpty())
					return;
				
				try {
					VerificationHandler handler = new VerificationHandler(toKeep, userID);
					handler.initializeResources();
					
					removeAllComponents();
					addComponent(new Label("Your request is being processed"));
					
				} catch (Exception e) {
					e.printStackTrace();
					getApplication().getMainWindow().showNotification("Server Error", Window.Notification.TYPE_ERROR_MESSAGE);
					return;
				}
				
			}
		});
		
		layout.addComponent(hl);
		hl.setSpacing(true);
		layout.setSpacing(true);
		this.addComponent(layout);
		
	}

	public VerticalLayout getLayout() {
		return layout;
	}

	public void setLayout(VerticalLayout layout) {
		this.layout = layout;
	}

	public Table getRecordsTable() {
		return recordsTable;
	}

	public void setRecordsTable(Table recordsTable) {
		this.recordsTable = recordsTable;
	}
	
}
