package eu.kalodiodev.springjumpstart.domain.util;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import eu.kalodiodev.springjumpstart.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter;
import eu.kalodiodev.springjumpstart.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter;

/**
 * JSR310 Persistence Converters used for auditing
 * 
 * @author Athanasios Raptodimos
 */
public final class JSR310PersistenceConverters {
	
	private JSR310PersistenceConverters() {
		// Prevent instantiation
	}
	
	/**
	 * ZonedDateTime Converter
	 * 
	 * <p>Database column is converted to Date, Entity attribute is converted to ZonedDateTime</p>
	 */
	@Converter(autoApply = true)
	public static class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Date> {
		
		@Override
		public Date convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
			return ZonedDateTimeToDateConverter.INSTANCE.convert(zonedDateTime);
		}
		
		@Override
		public ZonedDateTime convertToEntityAttribute(Date date) {
			return DateToZonedDateTimeConverter.INSTANCE.convert(date);
		}
	}
}