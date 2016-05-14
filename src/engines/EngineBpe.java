package engines;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import serviceerrors.InternalErrorException;
import services.WS;

@XmlRootElement(name = "engine")
public class EngineBpe implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1924340617886310855L;

	private EngineBP name = null;
	
	private String processId = null;
	
	private String baseURI = null;

	public EngineBP getName() {
		return name;
	}
	@XmlElement
	public void setName(EngineBP name) {
		this.name = name;
	}

	public String getProcessId() {
		return processId;
	}
	@XmlElement
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getBaseURI() {
		return baseURI;
	}
	@XmlElement
	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}
	// must have for XML
	public EngineBpe() {
		super();
	}

	public EngineBpe(EngineBP name, String processId, String baseURI) {
		super();
		this.name = name;
		this.processId = processId;
		this.baseURI = baseURI;
	}
	@JsonIgnore
	@XmlTransient
	public Boolean isSet() {
		if ( this.name == null || this.processId == null || baseURI == null )
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Engine [name=" + name + ", processId=" + processId + ", baseURI=" + baseURI + "]";
	}

	@JsonIgnore
	public WS getProcessResource() {
		return null;
	}

	public GeneralCase generateInstance (String instanceUrl) throws InternalErrorException {
		return null;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseURI == null) ? 0 : baseURI.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((processId == null) ? 0 : processId.hashCode());
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
		EngineBpe other = (EngineBpe) obj;
		if (baseURI == null) {
			if (other.baseURI != null)
				return false;
		} else if (!baseURI.equals(other.baseURI))
			return false;
		if (name != other.name)
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		return true;
	}

}