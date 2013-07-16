package views;


import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DetailsView extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7067858500866643855L;

	private VerticalLayout layout;
	private Table recordsTable;
	
	public DetailsView(){
		initViewComponent();
	}
	
	private void initViewComponent(){
		this.setWidth("50%");
		this.setHeight("50%");
		
		this.layout = new VerticalLayout();
		
		this.recordsTable = new Table();
		this.recordsTable.addContainerProperty("Title", String.class, null);
		this.recordsTable.addContainerProperty("Language", String.class, null);
		this.recordsTable.addContainerProperty("Type", String.class, null);
		this.recordsTable.addContainerProperty("URL", String.class, null);
		this.recordsTable.setSelectable(true);
		this.recordsTable.setSizeFull();
		
		this.layout.addComponent(recordsTable);
		
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
