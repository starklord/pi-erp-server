package pi.service.util.exporter;

import java.io.Serializable;

public class ColumnData implements Serializable{
	
	public String name;
	public int width;
	
	public ColumnData() {
	}
	
	public ColumnData(String name, int width) {
		this.name = name;
		this.width = width;
	}
	

}
