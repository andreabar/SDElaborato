package views;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
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
		
		this.panelLayout = new GridLayout(1, 2);
		this.panelLayout.setSizeFull();
		
		this.textfield = new TextField();
		this.textfield.setWidth("50%");
		this.textfield.setImmediate(true);
		
		
		this.searchButton = new Button("Search");
		
		this.panelLayout.addComponent(textfield, 0, 0);
		this.panelLayout.addComponent(searchButton, 0, 1);	
		
		this.mainPanel.setContent(panelLayout);
		this.mainLayout.addComponent(mainPanel);
		this.setContent(mainLayout);
		
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
	
}
