package views;

import com.google.gwt.dom.client.Style.VerticalAlign;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.LoginForm.LoginEvent;

import controllers.LoginController;
import controllers.ViewController;

public class LoginPage extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2728136090258265296L;
	private VerticalLayout loginLayout;
	private TextField username;
	private PasswordField pass;
	private Button login;
	
	public LoginPage() {

		loginLayout = new VerticalLayout();
		loginLayout.setSizeFull();
		username = new TextField("Username: ");
		pass = new PasswordField("Password: ");
		
		login = new Button("Login");
		
		loginLayout.addComponent(username);
		loginLayout.addComponent(pass);
		loginLayout.addComponent(login);
		
		login.addListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 8762852993934098975L;

			public void buttonClick(ClickEvent event) {

				if(pass.getValue() != null && username.getValue() != null){
					int loginResult = LoginController.handleLogin(username.getValue().toString(), pass.getValue().toString());
					if(loginResult != -1){
						Window main = new MainView(loginResult);
						
						getApplication().addWindow(main);
						getApplication().setMainWindow(main);
						
						getApplication().removeWindow(LoginPage.this);
						
					}
					else 
						login.setComponentError(new UserError("Login failed"));
					
					
				}
			}
		});
		
		setContent(loginLayout);
	}
	
}
