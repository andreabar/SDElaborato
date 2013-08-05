package view.controllers;

import java.sql.SQLException;

import javax.swing.ButtonModel;

import util.AppData;
import views.LoginPage;
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
		//System.out.print(loginController.getLoginPage().getUsername().getValue().toString());
		if(null == loginController.getLoginPage().getUsername().getValue()){
			loginController.getLoginPage().getUsername().setComponentError(new UserError("Field Required"));
			return;
		}
		
		if(null == loginController.getLoginPage().getPass().getValue()){
			loginController.getLoginPage().getPass().setComponentError(new UserError("Field Required"));
			return;
		}
		
		try {
			if(DBHelper.newUser(loginController.getLoginPage().getUsername().getValue().toString(), 
					loginController.getLoginPage().getPass().getValue().toString())){
				loginController.getLoginPage().getApplication().getMainWindow().
				showNotification("Registration completed", Notification.TYPE_HUMANIZED_MESSAGE);
			} else {
				loginController.getLoginPage().getUsername().setComponentError(new UserError("Email is not valid"));
			}
		} catch (SQLException e) {
			loginController.getLoginPage().getApplication().getMainWindow().
			showNotification("User already exists", Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
}
