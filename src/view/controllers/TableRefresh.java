package view.controllers;

public class TableRefresh extends Thread {

	private ResultViewController controller;
	
	public TableRefresh(ResultViewController r) {
		controller = r;
	}
	
	
	@Override
	public void run() {
	
		while(true){
		try {
			System.out.println("REFRESHING");

			controller.getResultView().getRefreshButton().click();
			System.out.println("SLEEPING");
			Thread.sleep(10000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		}
		
		
	}
}
