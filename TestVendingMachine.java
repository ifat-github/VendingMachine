package il.co.ilrd.vendingmachine;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TestVendingMachine {
	public static void main(String[] args) {
		Product cola = new Product("cola", 5.0);
		Product sprite = new Product("sprite", 10.0);
		Product water = new Product("water", 3.0);
		
		Notifier note = new Print();
		
		ArrayList<Product> productList = new ArrayList<Product>();
		
		productList.add(cola);
		productList.add(sprite);
		productList.add(water);
		
		VendingMachine vm = new VendingMachine(productList, note);
		vm.turnOn();
		vm.inputMoney(12.0);
		vm.select("water");
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vm.inputMoney(12.0);
		//vm.cancel();
		//vm.turnOn();
		//vm.select("cola");
		vm.turnOff();
		vm.turnOn();
		vm.inputMoney(12.0);
		vm.select("water");
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//vm.inputMoney(12.0);
		//vm.cancel();
		//vm.turnOn();
		//vm.select("cola");
	}
}