package views;


import model.Record;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class DetailsView extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7067858500866643855L;

	private VerticalLayout layout;
	private Table recordsTable;
	private Button seeOnline, verify;
	
	public DetailsView(){
		initViewComponent();
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
				String url = selected.getWebResources().get(0);
				DetailsView.this.open(new ExternalResource(url), "_blank");
				
				
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
