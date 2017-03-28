
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {
	
    // shared response for post data
    static String sharedResponse = "";
    static boolean gotMessageFlag = false;
    static ArrayList<Employee> employee = new ArrayList<Employee>();
    public static void main(String[] args) throws Exception {

        // HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Displays results
        server.createContext("/displayresults", new DisplayHandler());

        // Gets requests
        server.createContext("/sendresults",new PostHandler());
        
        // Default executer
        server.setExecutor(null);
        
        // Start server
        System.out.println("Starting Server.");
        server.start();

    }
    
    static class DisplayHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
			Gson g = new Gson();
			t.getResponseHeaders().set("Content-Type", "text/html");
			String response = "";
			String css = "<Style>"
					+ "table {border-collapse: collapse; width: 100%;max-width: 1000px; margin-left:auto; margin-right:auto;}"
					+ "th {text-align: left; background-color: #228B22; color: white;}"
					+ "th, td {text-align: left; padding: 12px;}"
					+ "tr:nth-child(even) {background-color: #D3D3D3;}"
					+ "</Style>";
			
			try {
				if (!sharedResponse.isEmpty()) {
					ArrayList<Employee> fromJson = g.fromJson(sharedResponse, new TypeToken<Collection<Employee>>() {}.getType());
					Collections.sort(fromJson);
			    	response = css;
			        response += "<table><tbody><tr><th>Title</th> <th>First Name</th> <th>Last Name</th> <th>Department</th> <th>Phone Number</th> <th>Gender</th></tr>";
			        for(Employee e : fromJson) {
			        	response += "<tr>";
			        	response += "<td>" + e.title + "</td><td>" + e.firstName + "</td><td>" + e.lastName + "</td><td>" + e.dept + "</td><td>" + e.phone + "</td><td>" + e.gender + "</td>\n";
			        	response += "</tr>";
			        }
			        response += "</tbody></table>";
				}
			}catch (JsonSyntaxException e) {e.printStackTrace();}
            
            // write response
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class PostHandler implements HttpHandler {
        public void handle(HttpExchange transmission) throws IOException {

            sharedResponse = "";

            // read request
            InputStream inputStr = transmission.getRequestBody();

            // write response
            OutputStream outputStream = transmission.getResponseBody();

            // holds result
            StringBuilder sb = new StringBuilder();

            //  build shared response
            int nextChar = inputStr.read();
            while (nextChar > -1) {
                sb=sb.append((char)nextChar);
                nextChar=inputStr.read();
            }

            sharedResponse = sharedResponse+sb.toString();

            // respond to POST
            String postResponse = "JSON RECEIVED";

            System.out.println("response: " + sharedResponse);

            transmission.sendResponseHeaders(300, postResponse.length());

            outputStream.write(postResponse.getBytes());

            outputStream.close();
        }
        
    }
}