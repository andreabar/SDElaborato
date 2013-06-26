package views;

import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


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
	private Select typeSelect;
	
	public MainView(){
		super("Europeana Crawler");
		initViewComponent();
		
	}
	
	private void initViewComponent(){
		this.mainLayout = new VerticalLayout();
		this.mainLayout.setWidth("100%");
		this.mainLayout.setHeight("100%");
		
		this.mainPanel = new Panel("Europeana Crawler");
		this.setWidth("100%");
		this.setHeight("100%");
		
		this.panelLayout = new GridLayout(2, 3);
		this.panelLayout.setSizeFull();
		this.panelLayout.setSpacing(true);
		
		this.textfield = new TextField();
		this.textfield.setWidth("50%");
		this.textfield.setImmediate(true);
		
		this.stepper = new IntStepper("N° result");
		stepper.setMinValue(1);
		stepper.setValue(1);
		stepper.setImmediate(true);
		
		this.typeSelect = new Select("Type");
		
		
		this.searchButton = new Button("Search");
		
		this.panelLayout.addComponent(textfield, 0, 0, 1, 0);
		this.panelLayout.addComponent(stepper, 0, 1);	
		this.panelLayout.addComponent(searchButton, 0, 2);	
		
		
		this.mainPanel.setContent(panelLayout);
		this.mainLayout.addComponent(mainPanel);
		this.setContent(mainLayout);
		
	}
	
	private void initTypeSelect(){
		
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

	public Select getTypeSelect() {
		return typeSelect;
	}

	public void setTypeSelect(Select typeSelect) {
		this.typeSelect = typeSelect;
	}
	
}
