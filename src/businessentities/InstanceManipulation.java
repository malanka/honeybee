package businessentities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "instanceAction")
public class InstanceManipulation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3663800541050961636L;

	private InstanceState state;

	public InstanceState getState() {
		return state;
	}

	public void setState(InstanceState state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		InstanceManipulation other = (InstanceManipulation) obj;
		if (state != other.state)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InstanceManipulation [state=" + state + "]";
	}

	public InstanceManipulation(InstanceState state) {
		super();
		this.state = state;
	}

	public InstanceManipulation() {
		super();
	}
	
}
