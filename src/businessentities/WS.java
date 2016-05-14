package businessentities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ws")
public class WS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1086234448083750719L;

	private String uri = null;
	
	private String method = null;
	
	private String requestDocument = null;

	public String getRequestDocument() {
		return requestDocument;
	}
	@XmlElement
	public void setRequestDocument(String requestDocument) {
		this.requestDocument = requestDocument;
	}
	public String getUri() {
		return this.uri;
	}
	@XmlElement
	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}
	@XmlElement
	public void setMethod(String method) {
		this.method = method;
	}

	public WS(String uri, String method, String requestDocument) {
		super();
		this.uri = uri;
		this.method = method;
		this.requestDocument = requestDocument;
	}

	public WS() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		WS other = (WS) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "WS [uri=" + this.uri + ", method=" + this.method + ", requestDocument=" + this.requestDocument + "]";
	}
	
}
