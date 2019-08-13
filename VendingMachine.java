package il.co.ilrd.vendingmachine;

import java.util.ArrayList;
import java.lang.Thread;


public class VendingMachine {
	private StateVendingMachine stateVM = StateVendingMachine.START;
	private ArrayList<Product> arrProducts;
	private Notifier notePrint; 
	private int secondsToPay = 0;
	private Thread timerThread;

	enum StateVendingMachine {

		START {
			@Override
			protected void turnOn(VendingMachine vm) {
				try {
					vm.timerThread.start();
				} catch (Exception e) {
				}
				vm.notePrint.write("**Was on START, moving to WFS**");
				
				vm.stateVM = WAITING_FOR_SELECTION;
			}
			
			@Override
			protected void cancel(VendingMachine vm) {
				vm.stateVM = START;
			}
		},
		WAITING_FOR_SELECTION {
			@Override
			protected void select(VendingMachine vm, String productName) {
				for (Product p: vm.arrProducts) {
					if (productName.equals(p.getName())) {
						myProd = p;
						vm.notePrint.write(productName + " was found");
						break;
					}
				}
				System.out.println("**Waiting for money**");
				vm.secondsToPay = 10;
				vm.stateVM = WAITING_FOR_MONEY;
			}
			
			@Override
			protected void inputMoney(VendingMachine vm, double money) {
				vm.notePrint.write("Please insert selection before money. Here's your money back");
				vm.stateVM = WAITING_FOR_SELECTION;
			}
			
			@Override
			protected void cancel(VendingMachine vm) {
				vm.notePrint.write("**Waiting for selection**");
				vm.stateVM = WAITING_FOR_SELECTION;
			}
		},
		WAITING_FOR_MONEY {
			@Override
			protected void inputMoney(VendingMachine vm, double money) {
				if (money == myProd.getPrice()) {
					vm.notePrint.write("You've entered the exact price! Here's your product");
				} else if (money > myProd.getPrice()) {
					vm.notePrint.write("Your change: " + (money - myProd.getPrice()) + ". Here's your product");
				} else {
					vm.notePrint.write("You didn't insert enough money");
				}
				vm.notePrint.write("**Waiting for selection**");
				vm.stateVM = WAITING_FOR_SELECTION;
			}
			
			@Override
			protected void cancel(VendingMachine vm) {
				vm.notePrint.write("**Waiting for selection**");
				vm.stateVM = WAITING_FOR_SELECTION;
			}
			
			@Override
			protected void timeOut(VendingMachine vm) {
				if (0 == vm.secondsToPay) {
					vm.notePrint.write("You didn't pay for more than 10 seconds, reseting");
					cancel(vm);
				}
				--vm.secondsToPay;
			}
		};

		private static Product myProd;
		
		//4 functions that make it possible for the enum to override them
		protected void turnOn(VendingMachine vm) { };
		
		protected void select(VendingMachine vm, String productName) { };
		
		protected void inputMoney(VendingMachine vm, double money) { };
		
		//since it's abstract we'll need to override it in each enum constant
		protected abstract void cancel(VendingMachine vm);
		
		protected void timeOut(VendingMachine vm){ };
	}

	public VendingMachine(ArrayList<Product> productList, Notifier note) {
		arrProducts = productList;
		notePrint = note;
		TimerThread timerThreadClassInstance = new TimerThread();
		timerThread = new Thread(timerThreadClassInstance);
	}
	
	//4 functions that the user can invoke
	public void turnOn() {
		notePrint.write("You've pressed turn-on");
		stateVM.turnOn(this);
	}
	
	public void select(String productName) {
		notePrint.write("A product was selected: " + productName);
		stateVM.select(this, productName);
	}
	
	public void inputMoney(double price) {
		notePrint.write("A money was inserted: " + price);
		stateVM.inputMoney(this, price);
	}
	
	public void cancel() {
		notePrint.write("You've pressed cancel");
		stateVM.cancel(this);
	}
	
	public void turnOff() {
		notePrint.write("You've pressed turn-off");
		stateVM = StateVendingMachine.START;
	}
	
	class TimerThread implements Runnable {
		@Override
		public void run() {
			while (stateVM != StateVendingMachine.START) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				stateVM.timeOut(VendingMachine.this);
			}
		}
	}
}