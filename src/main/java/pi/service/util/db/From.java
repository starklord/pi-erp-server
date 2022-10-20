package pi.service.util.db;

import java.util.ArrayList;
import java.util.List;

public class From {
	
	private StringBuilder from = new StringBuilder();
	public From() { 
	}
	
	public void add(ObjectDB obj){
		from.append("from ").append(obj.tableName).append(" as ").append(obj.alias).append("\n");
	}
        
        public void add(String from){
		this.from.append(from).append("\n");
	}
        
        public void set(String from) {
		this.from = new StringBuilder(from);
	}

	public StringBuilder getFrom() {
		return from;
	}
	
}
