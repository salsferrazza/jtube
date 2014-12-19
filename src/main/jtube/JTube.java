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
	
	private GoogleCredential credential;
	private BigQuery bigQuery;
	
	public JTube(String serviceAccountId, String pathToP12File) {
		try {
			this.credential = new GoogleCredential.Builder().setTransport(TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId(serviceAccountId)
				.setServiceAccountScopes(SCOPE)
				.setServiceAccountPrivateKeyFromP12File(new File(pathToP12File).build()));
				
			this.bigQuery =	new Bigquery.Builder(TRANSPORT, JSON_FACTORY, credential)
				 	.setApplicationName("JTube")
				 	.setHttpRequestInitializer(this.credential).build();

		} catch (Exception ex) {
			System.err.println("JTube: error encountered in constructor was \"" + ex.getMessage() = "\"");
			ex.printStackTrace();
			throw(ex);
		}
		
	}

	public Object stream(Object o) throws Exception {
        
		try {
			
			Class persistable = Class.forName(o.getClass().getName());
			String destination = persistable.getSimpleName().toUpperCase();
			Field[] fields = persistable.getFields();
			TableRow row = new TableRow();

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

			TableDataInsertAllRequest content = new TableDataInsertAllRequest().setRows(rowList);
			TableDataInsertAllResponse response = this.bigQuery.tabledata().insertAll(GCE_PROJECT_ID, GCE_DATASET, tableName, content).execute();        
		
			return o; // back at you for fluency
			
		} catch (Exception ex) {
			System.err.println("Exception streaming " + o.toString() + " to BigQuery table " + destination + ": " + ex.getMessage());			
			ex.printStackTrace();
			throw(ex);
		}

	}

}
