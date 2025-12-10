package it.grational.phontastit;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * PhontastIT
 * This class recognize the type of Italian phone numbers using a heuristic
 * @author grational
 * @date 30-08-2018 20.34
 */
public class ItalianPhoneNumber {
	private final String phone;
	private final boolean fax;

	private static final String IT_PREFIX = "+39";
	private static final String IT_18N = "((00|\\+)39)|(39(?!\\d{7,9}$))";
	private static final String LANDLINE = "0[1-9]\\d{4,9}";
	private static final String MOBILE = "(3[1-9]\\d)\\d{6,9}";
	private static final String TOLLFREE = "(?:800\\d{6})|(?:803\\d{3})";
	private static final String PREMIUM = String.join("|",
		"(?:892(?:\\d{3}|\\d{6}))", // Caso specifico 892 (6 o 9 cifre tot)
		"(?:(178|199|840|848|893|894|895|899)\\d{6})" // Altri casi (9 cifre tot)
	);
	
	// Combined regex pattern
	private static final Pattern VALID_PATTERN = Pattern.compile (
		"^(" + IT_18N + ")?((" + LANDLINE + ")|(" + MOBILE + ")|(" + TOLLFREE + ")|(" + PREMIUM + "))$"
	);

	private static final Pattern LANDLINE_PATTERN = Pattern.compile("^" + LANDLINE + "$");
	private static final Pattern MOBILE_PATTERN = Pattern.compile("^" + MOBILE + "$");
	private static final Pattern TOLLFREE_PATTERN = Pattern.compile("^" + TOLLFREE + "$");
	private static final Pattern PREMIUM_PATTERN = Pattern.compile("^" + PREMIUM + "$");

	/**
	 * Primary constructor
	 *
	 * @param ph  the Italian phone number to be represented by the object
	 */
	public ItalianPhoneNumber(String ph) {
		this(ph, false);
	}

	/**
	 * Primary constructor
	 *
	 * @param ph  the Italian phone number to be represented by the object
	 * @param fx  a boolean flag to force the type to FAX
	 */
	public ItalianPhoneNumber(String ph, Boolean fx) {
		String sanitized = sanitize(ph);
		if (VALID_PATTERN.matcher(sanitized).matches()) {
			this.phone = localize(sanitized);
		} else {
			throw new IllegalArgumentException(
				"Cannot determine the phone number type of '" + ph + "'"
			);
		}
		this.fax = fx != null ? fx : false;
	}

	private String sanitize(String input) {
		if (input == null) {
			return "";
		}
		return input.replaceAll("[^+0-9]", "");
	}

	private String localize(String input) {
		if (input == null) {
			return null;
		}
		return input.replaceFirst("^(" + IT_18N + ")", "");
	}

	/**
	 * Return the type of the phone number represented by the class
	 *
	 * The supported types are: LANDLINE, MOBILE, TOLLFREE, PREMIUM, FAX
	 * Since the FAX phone type is not recognizable the external boolean
	 * parameter fax is used to force this type
	 *
	 * @return PhoneNumberType  return an enum PhoneNumberType indicating the possible types
	 */
	public PhoneNumberType type() {
		return this.fax ? PhoneNumberType.FAX : heuristic(this.phone);
	}

	/**
	 * This method decides according to the starting digits of
	 * an Italian Phone Number without the i18n IT prefix
	 */
	private PhoneNumberType heuristic(String phone) {
		if (LANDLINE_PATTERN.matcher(phone).matches()) {
			return PhoneNumberType.LANDLINE;
		} else if (MOBILE_PATTERN.matcher(phone).matches()) {
			return PhoneNumberType.MOBILE;
		} else if (TOLLFREE_PATTERN.matcher(phone).matches()) {
			return PhoneNumberType.TOLLFREE;
		} else if (PREMIUM_PATTERN.matcher(phone).matches()) {
			return PhoneNumberType.PREMIUM;
		} else {
			throw new IllegalStateException (
				String.format (
					"This should never happen: cannot determine the phone number type of '{}'",
					phone
				)
			);
		}
	}

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean local) {
		return (local ? "" : IT_PREFIX) + this.phone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ItalianPhoneNumber that = (ItalianPhoneNumber) o;
		return fax == that.fax && Objects.equals(phone, that.phone);
	}

	@Override
	public int hashCode() {
		return Objects.hash(phone, fax);
	}
}
