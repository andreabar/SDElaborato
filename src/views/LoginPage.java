package views;


import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class LoginPage extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2728136090258265296L;
	private Panel loginPanel;
	private VerticalLayout loginLayout;
	private TextField username;
	private PasswordField pass;
	private Button login;
	
	public LoginPage() {
		initViewComponents();
	}

	private void initViewComponents(){
		
		loginPanel = new Panel("Multimedia Data Crawler");
		loginPanel.setSizeFull();
		
		loginLayout = new VerticalLayout();
		loginLayout.setSizeFull();
		loginLayout.setSpacing(true);
		username = new TextField("Username: ");
		pass = new PasswordField("Password: ");
		
		login = new Button("Login");
		
		loginLayout.addComponent(username);
		loginLayout.addComponent(pass);
		loginLayout.addComponent(login);
		
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
	
}


