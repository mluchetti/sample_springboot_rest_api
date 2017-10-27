package com.luchetti.springboot.firstApplication.model;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Component
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Person {
	private static final Logger log = LogManager.getLogger(Person.class);

	@JsonProperty(access=Access.WRITE_ONLY,required=true)
	private String id;
	@JsonProperty(access=Access.READ_ONLY)
	private int idValue;
	
	private String fName, lName;
	private int age;
	private LocalDate birthDate;
	@JsonProperty(access=Access.READ_ONLY)
	private String ageMessage;

	@JsonProperty(required=false)
	private String esrt;
	//	@JsonProperty(access=Access.READ_ONLY)
	private Timestamp esrtTimestamp;
	@JsonProperty(access=Access.READ_ONLY)
	private ZonedDateTime esrtZoned;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	private ZonedDateTime lastUpdated;

	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
		LocalDate today = LocalDate.now(ZoneId.of("America/Chicago"));

		Period p = Period.between(birthDate, today);
		long p2 = ChronoUnit.DAYS.between(birthDate, today);
		
		this.ageMessage = String.format("%s is %d years, %d months, and %d days old. (%d days total)", this.getfName(),p.getYears(),p.getMonths(),p.getDays(),p2);
		this.age = p.getYears();
	}
	
	public String getId() {
		return id == null ? "0" : id;
	}
	public void setId(String id) {
		this.id = id;
		this.idValue = Integer.parseUnsignedInt(id);
	}

	@JsonFormat(timezone="America/Chicago", pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	public ZonedDateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(ZonedDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getEsrt() {
		 return esrtTimestamp == null ? (esrt != null ? esrt : null) : esrtTimestamp.toString();
	}

	/**
	 * 
	 * @return ISO-8601 format
	 */
	public String getEsrtZoned() {
		return esrtZoned == null ? null : esrtZoned.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("America/Chicago")));
	}
	public void setEsrtZoned(ZonedDateTime zDate) {
		this.esrtZoned = zDate;
	}
	public String getEsrtUTC() {
		return esrtZoned == null ? null : esrtZoned.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("UTC")));
	}

	/**
	 * DateTimeFormatter Pattern: <br/>
	 * 	Value(YearOfEra,4,19,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)['T'][' ']Value(HourOfDay,2)':'Value(MinuteOfHour,2)':'Value(SecondOfMinute,2)[Fraction(NanoOfSecond,0,6,DecimalPoint)][ZoneOrOffsetId()]
	 * <p/>
	 * Examples of allowed formats:<br/>
	 * <ul>
	 * <li>2017-11-05 02:00:00</li>
	 * <li>2017-11-05 02:00:00Z</li>
	 * <li>2017-11-05 02:00:00-05:00</li>
	 * <li>2017-11-05 02:00:00-06:00</li>
	 * <li>2017-11-05T02:00:00</li>
	 * <li>2017-11-05T02:00:00Z</li>
	 * <li>2017-11-05T02:00:00-05:00</li>
	 * <li>2017-11-05T02:00:00-06:00</li>
	 * </ul>
	 * @param esrt Estimated Safe Restoration Time
	 * @throws Exception 
	 */
	public void setEsrt(String esrt) {
		
		if (esrt != null && esrt.length() > 0) {
			DateTimeFormatter df = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
			.optionalStart().appendPattern("'T'").optionalEnd()
			.optionalStart().appendPattern("' '").optionalEnd()
			.appendPattern("HH:mm:ss") 
	        .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 6, true).optionalEnd()
	        .optionalStart().appendZoneOrOffsetId().optionalEnd()
	        .toFormatter()
	        .withZone(ZoneId.of("America/Chicago"))
	        ;
	
			this.setEsrtZoned(convertStringToZonedDateTime(esrt, df));
			this.esrtTimestamp = Timestamp.from(this.esrtZoned.toInstant());
			this.esrt = esrt;
        
			log.debug(String.format("zonedDateToTimestamp: %s",this.esrtTimestamp.toString()));
		}
	}

	protected ZonedDateTime convertStringToZonedDateTime(String strDate, DateTimeFormatter formatter) {
        String input = (strDate != null ? strDate : ZonedDateTime.now(ZoneId.of("America/Chicago")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        log.debug(String.format("DateTime string to parse: %s",input));
        log.debug(String.format("System Default TimeZone: %s", ZoneId.systemDefault().getId()));
        log.debug(String.format("DateTimeFormatter Pattern: %s", formatter.toString()));

        ZonedDateTime zDate;
        try {
            zDate = ZonedDateTime.parse(input, formatter);
            log.debug(String.format("zonedDateFormatted(Local): %s",zDate.format(formatter)));
            log.debug(String.format("zonedDateFormatted(%s): %s",ZoneId.of("America/Chicago").getId(),zDate.format(formatter.withZone(ZoneId.of("America/Chicago")))));
            log.debug(String.format("zonedDateFormatted(%s): %s",ZoneId.of("UTC").getId(),zDate.format(formatter.withZone(ZoneId.of("UTC")))));
        }
        catch (DateTimeParseException exc) {
        	String err = String.format("Provided Timestamp '%s' is not parsable!", input);
       	 	log.error(err);
            throw exc;
        }
        // 'strDate' has been successfully parsed
        return zDate;
	}

}