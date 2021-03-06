package businessentities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hole")
public class Hole implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String data_in;
	private String data_out;
	private String event_start;
	private String event_end;

	public Hole() {
		super();
	}

	public Hole(String name, String data_in, String data_out, String event_start, String event_end) {
		super();
		this.name = name;
		this.data_in = data_in;
		this.data_out = data_out;
		this.event_start = event_start;
		this.event_end = event_end;
	}

	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
	public String getData_in() {
		return data_in;
	}
	@XmlElement
	public void setData_in(String data_in) {
		this.data_in = data_in;
	}
	public String getData_out() {
		return data_out;
	}
	@XmlElement
	public void setData_out(String data_out) {
		this.data_out = data_out;
	}
	public String getEvent_start() {
		return event_start;
	}
	@XmlElement
	public void setEvent_start(String event_start) {
		this.event_start = event_start;
	}
	public String getEvent_end() {
		return event_end;
	}
	@XmlElement
	public void setEvent_end(String event_end) {
		this.event_end = event_end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data_in == null) ? 0 : data_in.hashCode());
		result = prime * result + ((data_out == null) ? 0 : data_out.hashCode());
		result = prime * result + ((event_end == null) ? 0 : event_end.hashCode());
		result = prime * result + ((event_start == null) ? 0 : event_start.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Hole other = (Hole) obj;
		if (data_in == null) {
			if (other.data_in != null)
				return false;
		} else if (!data_in.equals(other.data_in))
			return false;
		if (data_out == null) {
			if (other.data_out != null)
				return false;
		} else if (!data_out.equals(other.data_out))
			return false;
		if (event_end == null) {
			if (other.event_end != null)
				return false;
		} else if (!event_end.equals(other.event_end))
			return false;
		if (event_start == null) {
			if (other.event_start != null)
				return false;
		} else if (!event_start.equals(other.event_start))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Hole [name=" + name + ", data_in=" + data_in + ", data_out=" + data_out + ", event_start=" + event_start
				+ ", event_end=" + event_end + "]";
	}


}