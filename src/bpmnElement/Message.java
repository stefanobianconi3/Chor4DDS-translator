package bpmnElement;

import java.util.ArrayList;

/**
 * The class represents the Topic on DDS (Choreography message). Used for the conversion from xml
 * @author Stefano Bianconi
 *
 */
public class Message extends Element{
	private QoS qos;
	private ArrayList<Field> fields;

	public Message(String id, String name, String documentation, QoS qos, ArrayList<Field> fields) {
		super(id, name, documentation);
		this.qos = qos; 
		this.fields = fields;
	}
	public ArrayList<Field> getFields() {
		return fields;
	}
	public QoS getQos() {
		return qos;
	}


	@Override
	public String toString() {
		return "Message id: "+this.getId()+" name: "+ this.getName()+ " documentation: "+this.getDocumentation()+ " qos: "+qos.toString()+" fields size: " +this.fields.size();
	}


	//Used for test
	public void  printFields() {
		for (Field field : fields) {
			System.out.println("name-> " + field.getName() + "         type->  "+ field.getType());
		}
	}


	/**
	 * The methods generate the idl structure of the topic in the form:
	 * struct {messageName} { 
	 * field1;
	 * field2;
	 * fieldN;
	 * };
	 * @return
	 */
	public String getIdl() {
		String base = "@topic"+"\n"+"struct "+this.getName()+" {";
		if(this.getDocumentation() != null) {
			base = "//"+this.getDocumentation()+"\n"+base;
		}
		for (Field f : fields) {
			if(!f.getType().equals("enum")) {
				if(f.isKey()) {
					base = base+"\n"+" @key "+f.getType()+" "+f.getName()+";";
					base.substring(0, base.length() - 1);
				}
				else {
					base = base+"\n "+f.getType()+" "+f.getName()+";";
					base.substring(0, base.length() - 1);
				}
			}
			else if (f.getType().equals("enum")) {
				String n = f.getName();
				String[] values = n.split(" ");
				String name = values[0];
				String en = " enum "+name+" {"+"\n ";
				for (int i = 1; i < values.length; i++) {
					en = en + values[i]+", ";
				}
				en = en.substring(0, en.length() - 2)+ "\n };";

				base = en +"\n\n"+base;

				if(f.isKey()) {
					base = base+"\n"+" @key "+f.getType()+" "+name+";";
					base.substring(0, base.length() - 2);
				}
				else {
					base = base+"\n "+f.getType()+" "+name+";";	
					base.substring(0, base.length() - 1);
				}
			}

		}
		base = base + "\n };\n";

		return base;
	}

}
