package view.controllers;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ProgressIndicator;

public class DownloadThread extends Thread {

	private ProgressIndicator progressBar;
	int taskId;
	ResultViewController view;
	private boolean refresh;

	public DownloadThread(ResultViewController resultViewController,
			ProgressIndicator p) {

		refresh = false;
		view = resultViewController;
		progressBar = p;
		((AbstractComponent) progressBar).setImmediate(true);

	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	@Override
	public void run() {

		while (true) {
//			System.out.println("in");
//			if (isRefresh()) {
//
//				try {
//					sleep(progressBar.getPollingInterval());
//					
//					view.refresh();
//
//					synchronized (view.getResultView()) {
//						if (view.getResultView().isVisible()) {
//							synchronized (view.getResultView().getApplication()) {
//								view.getProgressIndicator().setValue(
//										Math.random());
//							}
//						}
//					}
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//
//			}
//		}
			
			try {
				Thread.sleep(progressBar.getPollingInterval());
//				view.refresh();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			progressBar.setValue(Math.random());
			

	}
		
	}
}
