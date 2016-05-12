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

	public GeneralCase generateInstance (String instanceId) throws InternalErrorException {
		return null;
	}

}