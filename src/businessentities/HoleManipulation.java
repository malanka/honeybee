package businessentities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="hole_action")
public class HoleManipulation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3896898759905875441L;

	private String assigned_pattern_id;
	
	private String action;

	public String getAssigned_pattern_id() {
		return assigned_pattern_id;
	}

	public void setAssigned_pattern_id(String assigned_pattern_id) {
		this.assigned_pattern_id = assigned_pattern_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((assigned_pattern_id == null) ? 0 : assigned_pattern_id.hashCode());
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
		HoleManipulation other = (HoleManipulation) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (assigned_pattern_id == null) {
			if (other.assigned_pattern_id != null)
				return false;
		} else if (!assigned_pattern_id.equals(other.assigned_pattern_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HoleManipulation [assigned_pattern_id=" + assigned_pattern_id + ", action=" + action + "]";
	}

	public HoleManipulation(String assigned_pattern_id, String action) {
		super();
		this.assigned_pattern_id = assigned_pattern_id;
		this.action = action;
	}

	public HoleManipulation() {
		super();
	}
	
}
