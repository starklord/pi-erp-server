package pi.service.util.db.client;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Record implements Serializable {

    Map<String, Object> map = new HashMap<>();
    
    //pattern data
    public Boolean active() {
    	return (Boolean) map.get("activo");
    }
    
    public String creator() {
    	return(String) map.get("creador");
    }
    
    public Integer id() {
    	return (Integer)map.get("id");
    }
    
    public String name() {
    	return (String)map.get("name");
    }
    
    //close pattern data

    public Integer integer(String key) {
        return (Integer) map.get(key);
    }

    public BigDecimal bigdecimal(String key) {
        return (BigDecimal) map.get(key);
    }

    public Date date(String key) {
        return (Date) map.get(key);
    }

    public String string(String key) {
        return (String) map.get(key);
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }
    
    @Override
	public String toString() {
		return string("name");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id() == null) ? 0 : id().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (id() == null) {
			if (other.id() != null)
				return false;
		} else if (!id().equals(other.id()))
			return false;
		return true;
	}
    
    
}
