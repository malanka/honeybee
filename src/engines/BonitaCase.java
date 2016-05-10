package engines;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/* As we need just few information, we will skip some fields
 * This is the expected output according the documentation bonita 7.2
{
    "end_date": "",
    "processDefinitionId": "6301330702208387181",
    "start": "2016-05-10 21:47:14.713",
    "rootCaseId": "4020",
    "id": "4020",
    "state": "started",
    "started_by": "4",
    "last_update_date": "2016-05-10 21:47:14.713",
    "startedBySubstitute": "4"
}
*/
@JsonIgnoreProperties({"end_date", "processDefinitionId", "rootCaseId", "started_by", "last_update_date", "startedBySubstitute"})
public class BonitaCase {
	
	// The class is used to parse output from the bonita responses
	// Dates are returned in this strange format: 2016-05-10 23:03:07.082
	private String startDate = null;
	
	private String id = null;
	
	private String state = null;

	@JsonIgnore
	public Date startDateCorrect() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			return format.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@JsonGetter("start")
	public String getStartDate() {
		return startDate;
	}

	@JsonSetter("start")
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BonitaCase other = (BonitaCase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BonitaCase [startDate=" + startDate + ", id=" + id + ", state=" + state + "]";
	}

	public BonitaCase() {
		super();
	}

	@JsonCreator
	public BonitaCase(
			@JsonProperty("start") String startDate,
			@JsonProperty("id") String id,
			@JsonProperty("state") String state) {
		super();
		this.startDate = startDate;
		this.id = id;
		this.state = state;
	}

}
