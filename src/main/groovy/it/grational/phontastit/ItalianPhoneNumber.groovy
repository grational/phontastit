package it.grational.phontastit

import groovy.transform.EqualsAndHashCode

/**
 * PhontastIT
 * This class recognize the type of Italian phone numbers using a heuristic
 * @return The type of italiaon phone passed as a parameter
 * @author grational
 * @date 30-08-2018 20.34
 */
@EqualsAndHashCode(includeFields = true, includes='phone')
class ItalianPhoneNumber {
	private final String  phone
	private final Boolean fax

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
		this.phone = ph?.replaceAll(/\s+/,'') ?: { throw new IllegalArgumentException("Empty or null phone number") }()
		this.fax = fx
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
		switch(localPhoneNumber(phone)) {
			case ~/^0\d{5,10}$/:
				type = PhoneNumberType.LANDLINE
				break
			case ~/^3[1-9]\d\d{6,8}$/:
				type = PhoneNumberType.MOBILE
				break
			case ~/^80[03]\d+$/:
				type = PhoneNumberType.TOLLFREE
				break
			case ~/^(178|199|840|848|892|893|894|895|899)\d+$/:
				type = PhoneNumberType.PREMIUM
				break
			default:
				throw new IllegalStateException("Cannot determine the number '${phone}' correctly" as String)
		}
		return type
	}

	private localPhoneNumber(String phone) {
		return phone.replaceFirst(/^(?:00|\+)39/,'')
	}

	@Override
	String toString(boolean local = true) {
		local
			? this.localPhoneNumber(this.phone)
			: this.phone
	}

}
