package services;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import sun.security.x509.IssuerAlternativeNameExtension;

@XmlRootElement(name = "engine")
public class Engine implements Serializable{

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
	public Engine() {
		super();
	}

	public Engine(EngineBP name, String processId, String baseURI) {
		super();
		this.name = name;
		this.processId = processId;
		this.baseURI = baseURI;
	}

	public Boolean isSet() {
		if ( this.name == null || this.processId == null || baseURI == null )
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Engine [name=" + name + ", processId=" + processId + ", baseURI=" + baseURI + "]";
	}
	
	
}
