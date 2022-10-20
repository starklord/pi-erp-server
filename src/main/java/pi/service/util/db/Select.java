package pi.service.util.db;

import java.util.ArrayList;
import java.util.List;

public class Select {
	
	private String selectBeginning;
	private Query query;
	private StringBuilder select = new StringBuilder();
	public final List<ObjectDB> listObjectsDB = new ArrayList<>();
	
	public Select(Query query){
		this(null,query); 
	}
	
	public Select(String selectBeginning,Query query) {
		this.query = query;
		this.selectBeginning = selectBeginning;
		select.append(this.selectBeginning==null?"select ":this.selectBeginning);
	}
	
	public Select add(String txt, int nFields){
		this.query.index +=nFields;
		
		select.append(txt);
		return this;
	}
	
        public Select add(ObjectDB... objectDBs) {
            for (ObjectDB objectDB : objectDBs) {
                this.add(objectDB);
            }
            return this;
        }
        
	public Select add(ObjectDB objectDB) {
		Class c = objectDB.c;
		String alias = objectDB.alias;
		String[] fields = objectDB.fields;
		for (String field : fields) {
			select.append(alias).append(".").append(field).append(",");
		}
		objectDB.beginIndex = this.query.index;
		this.query.index = objectDB.beginIndex + fields.length;
		objectDB.endIndex = this.query.index - 1;
		
		listObjectsDB.add(objectDB);
		return this;
	}
        
        public void set(String select){
		this.select = new StringBuilder(select);
	}
	
	
	public StringBuilder getSelect() {
		if(select.charAt(select.length()-1) == ','){
			select.deleteCharAt(select.length()-1);
		}
		return select;
	}

}
