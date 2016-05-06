package services;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "error")
public class WebServiseError implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4272094477158904455L;

	private String message;
	
	public String getMessage() {
		return message;
	}
	@XmlElement
	public void setMessage(String message) {
		this.message = message;
	}

	public WebServiseError() {
		super();
	}

	@Override
	public String toString() {
		return "WebServiseError [message=" + message + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		WebServiseError other = (WebServiseError) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	public WebServiseError(String message) {
		super();
		this.message = message;
	}

}
