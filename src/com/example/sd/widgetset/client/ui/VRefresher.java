package com.example.sd.widgetset.client.ui;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VRefresher extends Widget implements Paintable {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-refresher";

	public static final String CLICK_EVENT_IDENTIFIER = "click";
	
	public static Integer refreshTime = 10*1000;

	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	protected ApplicationConnection client;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VRefresher() {
		// TODO This example code is extending the GWT Widget class so it must set a root element.
		// Change to a proper element or remove this line if extending another widget.
		setElement(Document.get().createDivElement());
		
		// This method call of the Paintable interface sets the component
		// style name in DOM tree
		setStyleName(CLASSNAME);
		
		
		
	}

    /**
     * Called whenever an update is received from the server 
     */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		// This call should be made first. 
		// It handles sizes, captions, tooltips, etc. automatically.
		if (client.updateComponent(this, uidl, true)) {
		    // If client.updateComponent returns true there has been no changes and we
		    // do not need to update anything.
			return;
		}

		// Save reference to server connection object to be able to send
		// user interaction later
		this.client = client;

		// Save the client side identifier (paintable id) for the widget
		paintableId = uidl.getId();
		
		Timer t = new MyTimer(this);
	    // Schedule the timer to run once in 5 seconds.
	    t.scheduleRepeating(20000);

		// Process attributes/variables from the server
		// The attribute names are the same as we used in 
		// paintContent on the server-side
//		int clicks = uidl.getIntAttribute("clicks");
//		String message = uidl.getStringAttribute("message");
//		
//		getElement().setInnerHTML("After <b>"+clicks+"</b> mouse clicks:\n" + message);
		
	}


     
     public static class MyTimer extends Timer {
    	 VRefresher _ref = null;
    	 private int _counter = 0;
    	 public MyTimer(VRefresher ref) {
    		 _ref = ref;
    	 }
		@Override
		public void run() {
			_ref.client.updateVariable(_ref.paintableId, "refresh", _counter++, true);
		}
    	 
     }
}