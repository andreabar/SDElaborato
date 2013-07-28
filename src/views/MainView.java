package views;

import java.util.ArrayList;
import org.vaadin.risto.stepper.IntStepper;

import view.controllers.ViewController;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import dbutil.IprType;


public class MainView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6826161044686784696L;

	

	private TabSheetView parentView;
	private Panel mainPanel;
	private GridLayout panelLayout;
	private TextField textfield;
	private Button searchButton;
	private IntStepper stepper;
	private NativeSelect typeSelect;
	private NativeSelect languageSelect;
	private ListSelect iprSelector;
	
	private OptionGroup groupSelector;
	private Table searchTable;
	private Button detailsButton;
	private Button deleteButton;
	
	
	public MainView(TabSheetView parent){
		setParentView(parent);
		initViewComponent();		
	}
	
	private void initViewComponent(){
		
		this.setWidth("100%");
		this.setHeight("100%");
		
		this.mainPanel = new Panel("Multimedia Data Crawler");
		
		
		this.panelLayout = new GridLayout(5, 6);
		this.panelLayout.setSizeFull();
		this.panelLayout.setSpacing(true);
		this.panelLayout.setMargin(true);
		this.panelLayout.setColumnExpandRatio(0, 0.20f);
		this.panelLayout.setColumnExpandRatio(1, 0.20f);
		this.panelLayout.setColumnExpandRatio(2, 0.20f);
		this.panelLayout.setColumnExpandRatio(3, 0.20f);
		this.panelLayout.setColumnExpandRatio(4, 0.20f);


		this.textfield = new TextField();
		this.textfield.setImmediate(true);
		this.textfield.setWidth("100%");
		
		this.stepper = new IntStepper("N° result");
		this.stepper.setMinValue(1);
		this.stepper.setValue(1);
		this.stepper.setImmediate(true);
		
		iprSelector = new ListSelect("IPR");
		initIprSelector();
		
		
		this.typeSelect = new NativeSelect("Type");
		this.typeSelect.setNullSelectionAllowed(false);
		initTypeSelect();
		
		this.languageSelect = new NativeSelect("Language");
		this.languageSelect.setNullSelectionAllowed(false);
		initLanguageSelect();

		this.searchButton = new Button("Search");
		this.searchButton.setEnabled(false);
		
		this.detailsButton = new Button("Details");
		this.detailsButton.setEnabled(false);
		
		this.deleteButton = new Button("Delete");
		this.deleteButton.setEnabled(false);
		
		this.groupSelector = new OptionGroup("Source");
		this.groupSelector.setNullSelectionAllowed(false);
		this.groupSelector.setImmediate(true);
		initGroupSelector();
		
		this.searchTable = new Table();

//		this.searchTable.addContainerProperty("Date", Date.class, null);
		this.searchTable.addContainerProperty("Keyword", String.class, null);
		this.searchTable.addContainerProperty("Provider", String.class, null);
		this.searchTable.addContainerProperty("Type", String.class, null);
		this.searchTable.addContainerProperty("Language", String.class, null);
		this.searchTable.addContainerProperty("N° Results", Integer.class, null);
		this.searchTable.setSizeFull();
		this.searchTable.setSelectable(true);
		this.searchTable.setImmediate(true);
		searchTable.setPageLength(10);
		panelLayout.setSizeFull();
		
		this.panelLayout.addComponent(groupSelector, 0, 0);
		this.panelLayout.addComponent(textfield, 0, 1, 2, 1);
		this.panelLayout.addComponent(stepper, 0, 2);
		this.panelLayout.addComponent(typeSelect, 1, 2);
		this.panelLayout.addComponent(languageSelect, 2, 2);
		this.panelLayout.addComponent(iprSelector, 3, 2); 
		this.panelLayout.addComponent(searchButton, 0, 3);
		this.panelLayout.addComponent(detailsButton, 2, 3);
		this.panelLayout.addComponent(deleteButton, 3, 3);

		
		
		this.mainPanel.setContent(panelLayout);
		this.addComponent(mainPanel);
		this.addComponent(searchTable);
		
	}
	
	private void initIprSelector() {

		iprSelector.setMultiSelect(true);
		
		for(String k : IprType.getTypes().keySet()){
			
			iprSelector.addItem(k);
			
		}
		
		
	}

	private void initTypeSelect(){
		ArrayList<String> types = new ArrayList<String>();
		
		types.add("TEXT");
		types.add("VIDEO");
		types.add("IMAGE");
		types.add("SOUND");
		types.add("3D");
		types.add("any");
		
		for(String s : types){
			this.typeSelect.addItem(s);
		}
	
	}
	
	private void initLanguageSelect(){
		String[] typeId = new String[5];
		typeId[0] = "English";
		typeId[1] = "Italian";
		typeId[2] = "French";
		typeId[3] = "German";
		typeId[4] = "any";

		for(String s : typeId){
			this.languageSelect.addItem(s);
		}
	
		
	}
	
	private void initGroupSelector(){
		this.groupSelector.addItem("Europeana");
		this.groupSelector.addItem("Vimeo");
	}
	
	public TextField getTextfield() {
		return textfield;
	}

	public void setTextfield(TextField textfield) {
		this.textfield = textfield;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(Button searchButton) {
		this.searchButton = searchButton;
	}
	
	public IntStepper getStepper() {
		return stepper;
	}

	public void setStepper(IntStepper stepper) {
		this.stepper = stepper;
	}

	public NativeSelect getTypeSelect() {
		return typeSelect;
	}

	public void setTypeSelect(NativeSelect typeSelect) {
		this.typeSelect = typeSelect;
	}
	
	public OptionGroup getGroupSelector() {
		return groupSelector;
	}

	public void setGroupSelector(OptionGroup groupSelector) {
		this.groupSelector = groupSelector;
	}

	public NativeSelect getLanguageSelect() {
		return languageSelect;
	}

	public void setLanguageSelect(NativeSelect languageSelect) {
		this.languageSelect = languageSelect;
	}

	public Table getSearchTable() {
		return searchTable;
	}

	public void setSearchTable(Table searchTable) {
		this.searchTable = searchTable;
	}

	public Button getDetailsButton() {
		return detailsButton;
	}

	public void setDetailsButton(Button detailsButton) {
		this.detailsButton = detailsButton;
	}

	public ListSelect getIprSelector() {
		return iprSelector;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}

	public TabSheetView getParentView() {
		return parentView;
	}

	public void setParentView(TabSheetView parentView) {
		this.parentView = parentView;
	}}
