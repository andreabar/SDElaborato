package view.views;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

public class LoginPage extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2728136090258265296L;
	private Panel loginPanel;
	private VerticalLayout loginLayout;
	private TextField username;
	private PasswordField pass;
	private Button login, register;
	private Label l1, l2;
	
	public LoginPage() {
		initViewComponents();
	}

	private void initViewComponents(){
		
		loginPanel = new Panel("Multimedia Data Crawler");
		loginPanel.setSizeFull();
		
		loginLayout = new VerticalLayout();
		loginLayout.setSizeFull();
		loginLayout.setSpacing(true);
		username = new TextField("Email: ");
		pass = new PasswordField("Password: ");
		
		login = new Button("Login");
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		
		l1 = new Label(" Or ");
		l2 = new Label(" if you don't have any account yet.");
		
		register = new Button("Register");
		register.setStyleName(BaseTheme.BUTTON_LINK);
		
		hl.addComponent(login);
		hl.addComponent(l1);
		hl.addComponent(register);
		hl.addComponent(l2);
		
		loginLayout.addComponent(username);
		loginLayout.addComponent(pass);
		loginLayout.addComponent(hl);
		
		loginPanel.addComponent(loginLayout);
		
		setContent(loginPanel);
	}

	public VerticalLayout getLoginLayout() {
		return loginLayout;
	}

	public void setLoginLayout(VerticalLayout loginLayout) {
		this.loginLayout = loginLayout;
	}

	public TextField getUsername() {
		return username;
	}

	public void setUsername(TextField username) {
		this.username = username;
	}

	public PasswordField getPass() {
		return pass;
	}

	public void setPass(PasswordField pass) {
		this.pass = pass;
	}

	public Button getLogin() {
		return login;
	}

	public void setLogin(Button login) {
		this.login = login;
	}

	public Panel getLoginPanel() {
		return loginPanel;
	}

	public void setLoginPanel(Panel loginPanel) {
		this.loginPanel = loginPanel;
	}

	public Button getRegister() {
		return register;
	}

	public void setRegister(Button register) {
		this.register = register;
	}
	
}


