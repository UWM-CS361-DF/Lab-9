import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;

import com.google.gson.Gson;

public class UI extends JFrame {

	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UI a = new UI();
				a.setTitle("Employee Input");
				a.setSize(300,300);
				a.setVisible(true);
			}
		});
	}
	
	ArrayList<JTextField> textField;
	ArrayList<Employee> employee;
	public UI(){	
		String[] labels = {"Title: ", "First Name: ", "Last Name: ", "Department: ", "Phone: ", "Gender: "};
		int numPairs = labels.length;
		textField = new ArrayList<JTextField>();
		employee = new ArrayList<Employee>();
		//Create and populate the panel.
		JPanel p = new JPanel(new SpringLayout());
		for (int i = 0; i < numPairs; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			p.add(l);
			textField.add(new JTextField(10));
			l.setLabelFor(textField.get(i));
			p.add(textField.get(i));
		}
				
		JButton submit = new JButton("Submit");
		submit.setForeground(Color.BLUE);
		submit.addActionListener(new SubmitListener());
			
		JButton exit = new JButton("Exit");
		exit.setForeground(Color.RED);
		exit.addActionListener(new ExitListener());
		p.add(submit);
		p.add(exit);
		
		//Lay out the panel.
		SpringUtilities.makeCompactGrid(p, numPairs + 1, 2, 6, 6, 6, 6);  

		this.setContentPane(p);
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private class ExitListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			 System.exit(0);
		}
	}
	
	private class SubmitListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				System.out.println("in the client");
				String[] emp=new String[6];
				for (int i = 0; i < 6; i++) {
					emp[i]=textField.get(i).getText();
				}
				System.out.println(emp[0]+emp[1]+emp[2]+emp[3]+emp[4]+emp[5]);
				employee.add(new Employee(emp[0],emp[1],emp[2],emp[3],emp[4],emp[5]));
				
				// Client will connect to this location
				URL site = new URL("http://localhost:8000/sendresults");
				HttpURLConnection conn = (HttpURLConnection) site.openConnection();

				// now create a POST request
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				
				Gson g = new Gson();
				String json = g.toJson(employee);

				// write out string to output buffer for message
				out.writeBytes(json);
				out.flush();
				out.close();

				System.out.println("Done sent to server.");

				InputStreamReader inputStr = new InputStreamReader(conn.getInputStream());

				// string to hold the result of reading in the response
				StringBuilder sb = new StringBuilder();

				// read the characters from the request byte by byte and build up
				// the Response
				int nextChar;
				while ((nextChar = inputStr.read()) > -1) {
					sb = sb.append((char) nextChar);
				}
				System.out.println("Return String: " + sb);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
}


