package views;

import java.util.ArrayList;
import java.util.Date;

import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import controllers.ViewController;


public class MainView extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6826161044686784696L;

	

	private VerticalLayout mainLayout;
	private Panel mainPanel;
	private GridLayout panelLayout;
	private TextField textfield;
	private Button searchButton;
	private IntStepper stepper;
	private NativeSelect typeSelect;
	private NativeSelect languageSelect;
	private OptionGroup groupSelector;
	private CheckBox downloadable;
	private Table searchTable;
	private Button detailsButton;
	private int userID;
	
	public MainView(int uid){
		super("Multimedia Data Crawler");
		userID = uid;
		initViewComponent();
		ViewController controller = new ViewController(this);
		
	}
	
	private void initViewComponent(){
		
		this.mainLayout = new VerticalLayout();
		this.mainLayout.setWidth("100%");
		this.mainLayout.setHeight("100%");
		
		this.mainPanel = new Panel("Multimedia Data Crawler");
		this.setWidth("100%");
		this.setHeight("100%");
		this.setScrollable(true);
		
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
		
		this.typeSelect = new NativeSelect("Type");
		this.typeSelect.setNullSelectionAllowed(false);
		initTypeSelect();
		
		this.languageSelect = new NativeSelect("Language");
		this.languageSelect.setNullSelectionAllowed(false);
		initLanguageSelect();

		this.searchButton = new Button("Search");
		
		this.detailsButton = new Button("Details");
		this.detailsButton.setEnabled(false);
		
		this.groupSelector = new OptionGroup("Source");
		this.groupSelector.setNullSelectionAllowed(false);
		this.groupSelector.setImmediate(true);
		initGroupSelector();
		
		this.downloadable = new CheckBox("Downloadable Content");
		this.downloadable.setImmediate(true);
		
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
		this.panelLayout.addComponent(downloadable, 0, 3);	
		this.panelLayout.addComponent(searchButton, 0, 4);
		this.panelLayout.addComponent(detailsButton, 3, 4);
		this.panelLayout.addComponent(searchTable, 0, 5, 3, 5);
		
		
		this.mainPanel.setContent(panelLayout);
		this.mainLayout.addComponent(mainPanel);
		this.setContent(mainLayout);
		
	}
	
	private void initTypeSelect(){
		String[] typeId = new String[3];
		typeId[0] = "TEXT";
		typeId[1] = "VIDEO";
		typeId[2] = "IMAGE";
		for(String s : typeId){
			this.typeSelect.addItem(s);
		}
		this.typeSelect.setItemCaption(typeId[0], "Text");
		this.typeSelect.setItemCaption(typeId[1], "Video");
		this.typeSelect.setItemCaption(typeId[2], "Image");
		this.typeSelect.setValue(typeId[0]);
	}
	
	private void initLanguageSelect(){
		String[] typeId = new String[4];
		typeId[0] = "en";
		typeId[1] = "it";
		typeId[2] = "fr";
		typeId[3] = "de";
		for(String s : typeId){
			this.languageSelect.addItem(s);
		}
		this.languageSelect.setItemCaption(typeId[0], "English");
		this.languageSelect.setItemCaption(typeId[1], "Italian");
		this.languageSelect.setItemCaption(typeId[2], "French");
		this.languageSelect.setItemCaption(typeId[3], "German");

		this.languageSelect.setValue(typeId[0]);
	}
	
	private void initGroupSelector(){
		this.groupSelector.addItem("Europeana");
		this.groupSelector.addItem("Vimeo");
		this.groupSelector.setValue("Europeana");
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

	public CheckBox getDownloadable() {
		return downloadable;
	}

	public void setDownloadable(CheckBox downloadable) {
		this.downloadable = downloadable;
	}

	public Button getDetailsButton() {
		return detailsButton;
	}

	public void setDetailsButton(Button detailsButton) {
		this.detailsButton = detailsButton;
	}

	public int getUserID() {
		return userID;
	}

	

}
