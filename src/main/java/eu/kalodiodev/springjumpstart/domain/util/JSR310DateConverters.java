package eu.kalodiodev.springjumpstart.domain.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * JSR310 Date Converters
 * 
 * @author Athanasios Raptodimos
 */
public final class JSR310DateConverters {
	
	private JSR310DateConverters() {
		// Prevent instantiation
	}
	
	/**
	 * Converts ZonedDateTime to Date
	 */
	public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
		/** Converter instance **/
		public static final ZonedDateTimeToDateConverter INSTANCE = new ZonedDateTimeToDateConverter();
		
		private ZonedDateTimeToDateConverter() {
			// Prevent instantiation
		}
		
		@Override
		public Date convert(ZonedDateTime source) {
			return source == null ? null : Date.from(source.toInstant());
		}
	}
	
	/**
	 * Converts Date to ZonedDateTime
	 */
	public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
		/** Converter instance **/
		public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();
		
		private DateToZonedDateTimeConverter() {
			// Prevent instantiation
		}
		
		@Override
		public ZonedDateTime convert(Date source) {
			return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
		}
	}
}