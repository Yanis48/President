package president;

public enum Mode {
	SIMPLE(1),
	DOUBLE(2),
	TRIPLE(3);
	
	private int id;
	
	private Mode(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}
