package il.co.ilrd.vendingmachine;

interface Notifier {
	public void write(String str);
}

class Print implements Notifier {
	@Override
	public void write(String str) {
		System.out.println(str);
	}
}