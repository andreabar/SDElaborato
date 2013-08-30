package view.controllers;

import java.sql.SQLException;

import util.AppData;
import view.views.LoginPage;
import view.views.RegisterPopUp;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

import dbutil.DBHelper;

public class LoginController {
	
	private LoginPage loginPage;
	
	public LoginController(LoginPage p){
		this.setLoginPage(p);
		this.getLoginPage().getLogin().addListener(new LoginButtonListener(this));
		this.getLoginPage().getRegister().addListener(new RegisterListener(this));
		this.getLoginPage().getLogin().setClickShortcut(KeyCode.ENTER);
	}



	public LoginPage getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(LoginPage loginPage) {
		this.loginPage = loginPage;
	}

	
}

class LoginButtonListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3909562668554729141L;

	private LoginController loginController;
	
	public LoginButtonListener(LoginController l){
		this.loginController = l;
	}
	
	public void buttonClick(ClickEvent event) {


		if(loginController.getLoginPage().getPass().getValue() != null && 
				loginController.getLoginPage().getUsername().getValue() != null){
			int loginResult = DBHelper.handleLogin(loginController.getLoginPage().getUsername().getValue().toString(), 
					loginController.getLoginPage().getPass().getValue().toString());
			if(loginResult != -1){
				AppData.userID = loginResult;
				TabViewController tvc = new TabViewController();
				Window main = tvc.getTabSheetView();
				
				this.loginController.getLoginPage().getApplication().addWindow(main);
				this.loginController.getLoginPage().getApplication().setMainWindow(main);
				
				this.loginController.getLoginPage().getApplication().
					removeWindow(this.loginController.getLoginPage());
				
			}
			else 
				this.loginController.getLoginPage().getLogin().setComponentError(new UserError("Login failed"));
				
		}
		
	}
	
}

class RegisterListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7346192404697774539L;
	
	private LoginController loginController;
	
	public RegisterListener(LoginController l){
		this.loginController = l;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		final RegisterPopUp popUp = new RegisterPopUp();
		this.loginController.getLoginPage().getLogin().removeClickShortcut();
		popUp.getSubmit().setClickShortcut(KeyCode.ENTER);
		
		popUp.getSubmit().addListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -3249151581808613975L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(popUp.getUsername().getValue().toString().equals("")){
					popUp.getUsername().setComponentError(new UserError("Field Required"));
					return;
				}
				
				if(popUp.getPass().getValue().toString().equals("")){
					popUp.getPass().setComponentError(new UserError("Field Required"));
					return;
				}
				
				if(!popUp.getPass().getValue().toString().equals(popUp.getConfirmPass().getValue().toString())){
					popUp.getConfirmPass().setComponentError(new UserError("Passwords do not match"));
					return;
				}
				
				try {
					if(DBHelper.newUser(popUp.getUsername().getValue().toString(), 
							popUp.getPass().getValue().toString())){
						loginController.getLoginPage().getApplication().getMainWindow().
						showNotification("Registration completed", Notification.TYPE_HUMANIZED_MESSAGE);
						loginController.getLoginPage().getLogin().setClickShortcut(KeyCode.ENTER);
						loginController.getLoginPage().removeWindow(popUp);
					} else {
						popUp.getUsername().setComponentError(new UserError("Email is not valid"));
					}
				} catch (SQLException e) {
					loginController.getLoginPage().getApplication().getMainWindow().
					showNotification("User already exists", Notification.TYPE_ERROR_MESSAGE);
					e.printStackTrace();
				}
				
			}
		});

		loginController.getLoginPage().addWindow(popUp);
		
		
	}
	
}
