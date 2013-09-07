package view.views;


import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
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
	private Button keepAll, seeOnline, verify;
	
	public DetailsView(){
		initViewComponent();
	}
	
	private void initViewComponent(){

		setSizeFull();
		
		
		this.layout = new VerticalLayout();
		
		
		keepAll = new Button("Keep All");
		verify = new Button("Verify & Download");
		seeOnline = new Button("See Online");

		
		
		this.recordsTable = new Table();
		this.recordsTable.addContainerProperty("Title", String.class, null);
		this.recordsTable.addContainerProperty("Provider", String.class, null);
		this.recordsTable.addContainerProperty("Language", String.class, null);
		this.recordsTable.addContainerProperty("Type", String.class, null);
		this.recordsTable.addContainerProperty("IPR", String.class, null);
		this.recordsTable.addContainerProperty("Keep", CheckBox.class, null);
		
		
		this.recordsTable.setSelectable(true);
		this.recordsTable.setSizeFull();
		
		this.layout.addComponent(recordsTable);
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(keepAll);
		hl.addComponent(seeOnline);
		hl.addComponent(verify);
		
		hl.setComponentAlignment(keepAll, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(seeOnline, Alignment.BOTTOM_CENTER);
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

	public Button getSeeOnline() {
		return seeOnline;
	}

	public void setSeeOnline(Button seeOnline) {
		this.seeOnline = seeOnline;
	}

	public Button getVerify() {
		return verify;
	}

	public void setVerify(Button verify) {
		this.verify = verify;
	}

	public Button getKeepAll() {
		return keepAll;
	}

	public void setKeepAll(Button keepAll) {
		this.keepAll = keepAll;
	}
	
}
