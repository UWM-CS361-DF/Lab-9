
public class Employee implements Comparable<Object> {
	
	protected String title, firstName, lastName, dept, phone, gender;
	
	public Employee() {}
	
	public String toString() {
		return title + " " + firstName + " " + lastName + " " + dept + " " + phone + " " + gender;
	} 
	
	public Employee(String t, String fn, String ln, String d, String ph, String g){
		title = t;
		firstName = fn;
		lastName = ln;
		dept = d;
		phone = ph;
		gender = g;
	}
	
	@Override
	public int compareTo(Object o) {
		if (o instanceof Employee) {
			Employee other = (Employee) o;
			return lastName.compareTo(other.lastName);
		}
		return 0;
	}
	
}
