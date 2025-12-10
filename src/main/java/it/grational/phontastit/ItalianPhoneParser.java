package it.grational.phontastit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parser for extracting Italian phone numbers from text
 * This class can scan through text and identify all valid Italian phone numbers
 * @author grational
 */
public class ItalianPhoneParser {

	/**
	 * Pattern to match potential Italian phone numbers in text
	 * Matches sequences that could be phone numbers with various separators
	 * The actual validation is delegated to ItalianPhoneNumber class
	 */
	private static final Pattern PHONE_PATTERN = Pattern.compile (
		"(?:(?:\\+|00)?39[\\s\\-.()]?)?[0-9](?:[\\s\\-./()]{0,2}[0-9]){5,14}"
	);
	
	/**
	 * Parse text and extract all valid Italian phone numbers
	 *
	 * @param text  the text to parse for phone numbers
	 * @return List<ItalianPhoneNumber>  a list of all valid Italian phone numbers found
	 */
	public List<ItalianPhoneNumber> parse(String text) { // {{{
		if (text == null || text.trim().isEmpty()) {
			return Collections.emptyList();
		}

		Set<ItalianPhoneNumber> phoneNumbers = new LinkedHashSet<>();

		Matcher matcher = PHONE_PATTERN.matcher(text);

		while (matcher.find()) {
			String candidate = matcher.group().trim();
			try {
				phoneNumbers.add (
					new ItalianPhoneNumber(candidate)
				);
			} catch (IllegalArgumentException e) {}
		}
		return new ArrayList<>(phoneNumbers);
	} // }}}

}
// vim: fdm=marker
