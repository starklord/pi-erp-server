package pi.service.util.exporter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RowData implements Serializable {

	public Data[] data;
	private int pos = 0;
	
	public RowData() {
	}
	
	public RowData(int size) {
		data = new Data[size];
	}
	
	public RowData add(Object value) {
		if (value instanceof BigDecimal) {
            return add((BigDecimal) value);
        }

        if (value instanceof Boolean) {
        	return add((Boolean) value);
        }
        if (value instanceof Date) {
        	return add((Date) value);
        }
        if (value instanceof Double) {
        	return add((Double) value);
        }
        if (value instanceof String) {
        	return add((String) value);
        }
        if (value instanceof Integer) {
        	return add((Integer) value);
        }
        if (value instanceof Float) {
        	return add((Float) value);
        }
        return this;
	}
	
	public RowData add(BigDecimal value) {
		data[pos] = new BigDecimalData(value);
		pos++;
		return this;
	}
	
	public RowData add(Boolean value) {
		data[pos] = new BooleanData(value);
		pos++;
		return this;
	}
	
	public RowData add(Date value) {
		data[pos] = new DateData(value);
		pos++;
		return this;
	}
	
	public RowData add(Double value) {
		data[pos] = new DoubleData(value);
		pos++;
		return this;
	}
	
	public RowData add(Integer value) {
		data[pos] = new IntegerData(value);
		pos++;
		return this;
	}
	
	public RowData add(Float value) {
		data[pos] = new FloatData(value);
		pos++;
		return this;
	}
	
	public RowData add(String value) {
		data[pos] = new StringData(value);
		pos++;
		return this;
	}


}
