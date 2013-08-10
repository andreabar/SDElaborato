package view.views;

import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class RegisterPopUp extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7015811923284371310L;
	private TextField username;
	private PasswordField pass, confirmPass;
	private Button submit;
	
	public RegisterPopUp(){
		super("Register a new account");
		initViewComponents();
	}
	
	private void initViewComponents(){
		this.setModal(true);
		this.setWidth("30%");
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		
		this.username = new TextField("Username: ");
		this.pass = new PasswordField("Password: ");
		this.confirmPass = new PasswordField("Confirm Password: ");
		this.submit = new Button("Submit");
		
		vl.addComponent(username);
		vl.addComponent(pass);
		vl.addComponent(confirmPass);
		vl.addComponent(submit);
		
		this.setContent(vl);
		
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

	public PasswordField getConfirmPass() {
		return confirmPass;
	}

	public void setConfirmPass(PasswordField confirmPass) {
		this.confirmPass = confirmPass;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

}
