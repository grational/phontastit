package it.grational.phontastit

import spock.lang.Specification
import java.text.ParseException

/**
 * This Spock specification was auto generated by 'gigawatt'
 * @author d7392
 * @date 30-08-2018 20.34
 */
class ItalianPhoneNumberUSpec extends Specification {

	def "Should raise a ArgumentException if one of the parameters is empty or null"() {
		when:
			new ItalianPhoneNumber(phone, fax).type()
		then:
			def exception = thrown(IllegalArgumentException)
			exception.message == "Empty or null phone number"
		where:
			phone | fax
			null  | null
			null  | false
			''    | null
			''    | false
			null  | true
			''    | true
	}

	def "Should raise a ParseException if the number type cannot be determined"() {
		when:
			new ItalianPhoneNumber(phone).type()
		then:
			def exception = thrown(IllegalStateException)
			exception.message == "Cannot determine the number '${phone.replaceAll(/ +/,'')}' correctly"
		where:
			phone << [
				"+39 11 12345678",
				"ull",
			]
	}

	def "Should always return the correct number type"() {
		expect:
			expected == new ItalianPhoneNumber(phone, fax).type()
		where:
			phone              | fax   || expected
			'0039 800 200227'  | false || PhoneNumberType.TOLLFREE
			'+39 800 200227'   | false || PhoneNumberType.TOLLFREE
			'0039 899 200227'  | false || PhoneNumberType.PREMIUM
			'+39 893 200227'   | false || PhoneNumberType.PREMIUM
			'+39 348 9018484'  | false || PhoneNumberType.MOBILE
			'0039 335 5856641' | false || PhoneNumberType.MOBILE
			'0039 02 8193736'  | false || PhoneNumberType.LANDLINE
			'+39 011 8193736'  | false || PhoneNumberType.LANDLINE
			'+39 011 8193736'  | true  || PhoneNumberType.FAX
			'+39 011 8193736'  | true  || PhoneNumberType.FAX
			// test case from https://jira.italiaonline.it/browse/COL-1739
			'02989442'         | false || PhoneNumberType.LANDLINE
			// shortest LANDLINE
			'022881'           | false || PhoneNumberType.LANDLINE
			// longest  LANDLINE
			'09977933544'      | false || PhoneNumberType.LANDLINE
			// shortest MOBILE
			'324611069'        | false || PhoneNumberType.MOBILE
			// longest  MOBILE
			'33124165852'      | false || PhoneNumberType.MOBILE
			// case from https://aziende.virgilio.it/amministrazioni-immobiliari/chieri-to/amministratoregestioneimmobili_cibiib
			'373 7915844'      | false || PhoneNumberType.MOBILE
	}

}
