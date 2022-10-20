package pi.service.util.db;

import java.lang.reflect.Field;

public class Join {
	
	private final StringBuilder join = new StringBuilder();
	
	public Join() {
		
	}
	 
	public Join add(String txt){
		join.append(txt).append("\n");
		return this;
	}
	
	/**
	 * 
	 * @param obj1 the new table
	 * @param obj2 the table which owns obj1
	 * @return the this object
	 * @throws Exception
	 */
	public Join add(ObjectDB obj1, ObjectDB obj2,String field) throws Exception {
		Field f1 = obj1.c.getDeclaredField("id");
		String id1 = ObjectDB.getFieldNameOrAnnotation(f1);
		Field f2 = obj2.c.getDeclaredField(field);
		String id2 = ObjectDB.getFieldNameOrAnnotation(f2);
		join.append("left join ").append(obj1.tableName).append(" as ").append(obj1.alias)
			.append(" on ").append(obj1.alias).append(".").append(id1)
			.append(" = ").append(obj2.alias).append(".").append(id2).append("\n");
		
		return this;
	}
	
	public StringBuilder getJoin() {
		return join;
	}

}
