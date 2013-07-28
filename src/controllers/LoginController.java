package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.AppData;
import view.controllers.TabViewController;
import views.LoginPage;
import views.MainView;
import views.TabSheetView;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import dbutil.DBHelper;

public class LoginController {
	
	private LoginPage loginPage;
	
	public LoginController(LoginPage p){
		this.setLoginPage(p);
		this.getLoginPage().getLogin().addListener(new LoginButtonListener(this));
	}

	public static int handleLogin(String u, String p) {

		String q = "SELECT id FROM user WHERE username = ? and password = ?";
		
		ResultSet set = DBHelper.executePreparedStatement(q, new String[]{u,p});
		
		if(set == null)
			return -1;
		
		try {
			if(set.next())
				return set.getInt("id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
		
		
		
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
			int loginResult = LoginController.handleLogin(loginController.getLoginPage().getUsername().getValue().toString(), 
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
