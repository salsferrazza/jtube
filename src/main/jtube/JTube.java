package jtube;

import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
import com.google.api.services.bigquery.*;
import com.google.api.services.bigquery.model.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.bigquery.Bigquery.Datasets;

public class JTube {

	public static List<TableDataInsertAllRequest.Rows> stream(Object o) throws Exception {
        
		try {

			TableRow row = new TableRow();
			Class thisClass = Class.forName(o.getClass().getName());
			Field[] fields = thisClass.getFields();

			for (Field f: fields) {

				Class fieldType = f.getType();

				switch(fieldType.getName()) {
				case "long":
					long longVal = f.getLong(o);
					row.set(f.getName(), longVal);
					break;
				case "int":
					int intVal = f.getInt(o);
					row.set(f.getName(), intVal);
					break;
				case "double":
					double dubVal = f.getDouble(o);
					row.set(f.getName(), dubVal);
					break;
				default:
					String strVal = (String) f.get(o);
					row.set(f.getName(), strVal);
					break;
				}
                
			}

			TableDataInsertAllRequest.Rows rows = new TableDataInsertAllRequest.Rows();
			rows.setInsertId("" + System.currentTimeMillis());
			rows.setJson(row);
        
			List<TableDataInsertAllRequest.Rows> rowList = new ArrayList<TableDataInsertAllRequest.Rows>();
			rowList.add(rows);

			return rowList;
        
		} catch (Exception ex) {
			System.err.println("Exception caught streaming object: " + ex.getMessage());
			ex.printStackTrace();
			throw(ex);
		}

	}

}
