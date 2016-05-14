package businessentities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import services.NotReadyException;

@XmlRootElement(name = "instance")
public class InstanceBasic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2252042484107815249L;

	private String patternId = null;
	
	@JsonGetter("pattern_id")
	public String getPatternId() {
		return patternId;
	}
	
	@JsonSetter("pattern_id")
	@XmlElement(name="pattern_id")
	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

	@Override
	public String toString() {
		return "InstanceBasic [patternId=" + patternId + "]";
	}

	public InstanceBasic(String patternId) {
		super();
		this.patternId = patternId;
	}

	public InstanceBasic() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((patternId == null) ? 0 : patternId.hashCode());
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
		InstanceBasic other = (InstanceBasic) obj;
		if (patternId == null) {
			if (other.patternId != null)
				return false;
		} else if (!patternId.equals(other.patternId))
			return false;
		return true;
	}

	public void checkIt() throws NotReadyException {
		if ( patternId.isEmpty() )
			throw new NotReadyException("'pattern_id' has to be specified");
	}

}