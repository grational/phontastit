package it.grational.phontastit

import java.util.regex.Pattern
import groovy.transform.EqualsAndHashCode

/**
 * PhontastIT
 * This class recognize the type of Italian phone numbers using a heuristic
 * @return The type of italiaon phone passed as a parameter
 * @author grational
 * @date 30-08-2018 20.34
 */
@EqualsAndHashCode (
	includeFields = true,
	includes='phone'
)
class ItalianPhoneNumber {
	private final String  input
	private final String  phone
	private final Boolean fax
	private final String  itPrefix = '+39'
	private final String  it18n    = /((00|\+)39)|(39(?!\d{7,9}$))/
	private final String  landline = /0\d{5,10}/
	private final String  mobile   = /(3[1-9]\d)\d{6,8}/
	private final String  tollfree = /80[03]\d+/
	private final String  premium  = /(178|199|840|848|892|893|894|895|899)\d+/
	private final Pattern valid    = ~/^(${it18n})?((${landline})|(${mobile})|(${tollfree})|(${premium}))$/

	/**
	 * Primary constructor
	 *
	 * @param ph  the Italian phone number to be rapresented by the object
	 * @param fx  a boolean flag to force the type to FAX
	 */
	ItalianPhoneNumber (
		String ph,
		Boolean fx = false
	) {
		phone = sanitize(ph)
		phone = (phone ==~ valid) ? localize(phone) : {
			throw new IllegalArgumentException (
				"Cannot determine the phone number type of '${ph}'"
			)
		}()
		this.fax = fx
	}

	private String sanitize(String input) {
		input?.replaceAll(/[^+0-9]/,'')
	}

	private String localize(String input) {
		input?.replaceFirst(/^(${it18n})/,'')
	}

	/**
	 * Return the type of the phone number rapresented by the class
	 *
	 * The supported types are: LANDLINE, MOBILE, TOLLFREE, PREMIUM, FAX
	 * Since the FAX phone type is not recognizable the external boolean
	 * parameter fax is used to force this type
	 *
	 * @return PhoneNumberType  return an enum PhoneNumberType indicating the possible types
	 */
	PhoneNumberType type() {
		this.fax ? PhoneNumberType.FAX : heuristic(this.phone)
	}

	/**
	 * This method decides according to the starting digits of
	 * an Italian Phone Number without the i18n IT prefix
	 */
	private PhoneNumberType heuristic(String phone) {
		PhoneNumberType type
		switch(phone) {
			case ~/^${landline}$/:
				type = PhoneNumberType.LANDLINE
				break
			case ~/^${mobile}$/:
				type = PhoneNumberType.MOBILE
				break
			case ~/^${tollfree}$/:
				type = PhoneNumberType.TOLLFREE
				break
			case ~/^${premium}$/:
				type = PhoneNumberType.PREMIUM
				break
			default:
				throw new IllegalStateException (
					"This should never happen: cannot determine the phone number type of '${phone}'"
				)
		}
		return type
	}

	@Override
	String toString(boolean local = true) {
		return "${local ? '' : itPrefix}${this.phone}"
	}

}
