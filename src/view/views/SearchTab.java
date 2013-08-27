package view.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.vaadin.risto.stepper.IntStepper;

import util.AppData;
import util.Languages;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

import dbutil.IprType;

public class SearchTab extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6826161044686784696L;

	private TabSheetView parentView;
	private Panel mainPanel, europeanaPanel;

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
	private Button logoutButton;
	private Label logged, eu;

	public SearchTab(TabSheetView parent) {
		setParentView(parent);
		initViewComponent();
	}

	private void initViewComponent() {

		this.setCaption("Search");

		this.setWidth("100%");
		this.setHeight("100%");

		this.mainPanel = new Panel("Multimedia Data Crawler");

		this.europeanaPanel = new Panel();

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
		this.stepper.setValue(100);
		this.stepper.setImmediate(true);

		iprSelector = new ListSelect("IPR");
		initIprSelector();

		this.typeSelect = new NativeSelect("Type");
		this.typeSelect.setNullSelectionAllowed(false);
		initTypeSelect();

		this.languageSelect = new NativeSelect("Language");
		this.languageSelect.setNullSelectionAllowed(false);
		initLanguageSelect();
		this.languageSelect.setValue("any");

		this.searchButton = new Button("Search");
		this.searchButton.setDisableOnClick(true);
		
		this.detailsButton = new Button("Details");
		this.detailsButton.setEnabled(false);

		this.deleteButton = new Button("Delete");
		this.deleteButton.setEnabled(false);

		this.logoutButton = new Button("Logout");
		this.logoutButton.setStyleName(BaseTheme.BUTTON_LINK);

		this.logged = new Label();
		this.eu = new Label("Europeana only");

		this.groupSelector = new OptionGroup("Source");
		this.groupSelector.setNullSelectionAllowed(false);
		this.groupSelector.setImmediate(true);
		groupSelector.setMultiSelect(true);
		initGroupSelector();

		this.searchTable = new Table();

		// this.searchTable.addContainerProperty("Date", Date.class, null);
		this.searchTable.addContainerProperty("Keyword", String.class, null);
		this.searchTable.addContainerProperty("Provider", String.class, null);
		this.searchTable.addContainerProperty("Type", String.class, null);
		this.searchTable.addContainerProperty("Language", String.class, null);
		this.searchTable
				.addContainerProperty("N° Results", Integer.class, null);
		this.searchTable.setSizeFull();
		this.searchTable.setSelectable(true);
		this.searchTable.setImmediate(true);
		searchTable.setPageLength(10);
		panelLayout.setSizeFull();

		this.europeanaPanel.addComponent(eu);
		this.europeanaPanel.addComponent(typeSelect);
		this.europeanaPanel.addComponent(languageSelect);
		this.europeanaPanel.addComponent(iprSelector);

		this.panelLayout.addComponent(groupSelector, 0, 0);
		this.panelLayout.addComponent(logged, 3, 0);
		this.panelLayout.addComponent(logoutButton, 4, 0);
		this.panelLayout.addComponent(textfield, 0, 1, 2, 1);
		this.panelLayout.addComponent(stepper, 0, 2);
		this.panelLayout.addComponent(europeanaPanel, 1, 2, 3, 2);
		this.panelLayout.addComponent(searchButton, 0, 3);
		this.panelLayout.addComponent(detailsButton, 2, 3);
		// this.panelLayout.addComponent(deleteButton, 3, 3);

		this.mainPanel.setContent(panelLayout);
		this.addComponent(mainPanel);
		this.addComponent(searchTable);

	}

	private void initIprSelector() {

		iprSelector.setMultiSelect(true);

		for (String k : IprType.getTypes()) {

			iprSelector.addItem(k);

		}

	}

	private void initTypeSelect() {
		ArrayList<String> types = new ArrayList<String>();

		types.add(AppData.TEXT);
		types.add(AppData.VIDEO);
		types.add(AppData.IMAGE);
		types.add(AppData.SOUND);
		types.add(AppData._3D);
		types.add(AppData.ANY_TYPE);

		for (String s : types) {
			this.typeSelect.addItem(s);
		}

		typeSelect.setValue(AppData.ANY_TYPE);
	}

	private void initLanguageSelect() {

		ResultSet languages = Languages.getAll();

		try {
			while (languages.next()) {
				this.languageSelect.addItem(languages
						.getString("full_language"));
			}
		} catch (SQLException e) {

		}

		languageSelect.addItem(AppData.ANY_TYPE);
		languageSelect.setValue(AppData.ANY_TYPE);

	}

	private void initGroupSelector() {
		this.groupSelector.addItem(AppData.EUROPEANA);
		this.groupSelector.addItem(AppData.VIMEO);

		this.groupSelector.select(AppData.EUROPEANA);
		this.groupSelector.select(AppData.VIMEO);

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
	}

	public Button getLogoutButton() {
		return logoutButton;
	}

	public void setLogoutButton(Button logoutButton) {
		this.logoutButton = logoutButton;
	}

	public Label getLogged() {
		return logged;
	}

	public void setLogged(Label logged) {
		this.logged = logged;
	}

	public Panel getEuropeanaPanel() {
		return europeanaPanel;
	}

	public void setEuropeanaPanel(Panel europeanaPanel) {
		this.europeanaPanel = europeanaPanel;
	}

}
