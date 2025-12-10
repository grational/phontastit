package it.grational.phontastit

import spock.lang.*

/**
 * Unit tests for ItalianPhoneParser
 * Testing the ability to extract Italian phone numbers from text
 * @author grational
 */
class ItalianPhoneParserUSpec extends Specification {

	def "Should return an empty list when the text is null or empty"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
		expect:
			parser.parse(text).isEmpty()
		where:
			text << [null, '', '   ']
	} // }}}

	def "Should return an empty list when the text contains no phone numbers"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "This is just some text without any phone numbers"
		when:
			List result = parser.parse(text)
		then:
			result.isEmpty()
	} // }}}

	def "Should find a single mobile phone number in text"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "Call me at 348 9018484 for more info"
		when:
			List result = parser.parse(text)
		then:
			result.size() == 1
			result[0].type() == PhoneNumberType.MOBILE
			result[0].toString() == '3489018484'
	} // }}}

	def "Should find a single landline phone number in text"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "Our office number is 02 8193736"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 1
			result[0].type() == PhoneNumberType.LANDLINE
			result[0].toString() == '028193736'
	} // }}}

	def "Should find phone numbers with international prefix"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "Contact us at +39 335 5856641 or +39 02 8193736"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 2
			result[0].type() == PhoneNumberType.MOBILE
			result[0].toString() == '3355856641'
			result[1].type() == PhoneNumberType.LANDLINE
			result[1].toString() == '028193736'
	} // }}}

	def "Should find multiple phone numbers of different types in text"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = """
				For support call our toll free number 800 200227.
				For sales contact 335 5856641 or our office at 011 8193736.
				Premium line available at 899 200227.
			"""
		when:
			def result = parser.parse(text)
		then:
			result.size() == 4
			result[0].type() == PhoneNumberType.TOLLFREE
			result[1].type() == PhoneNumberType.MOBILE
			result[2].type() == PhoneNumberType.LANDLINE
			result[3].type() == PhoneNumberType.PREMIUM
	} // }}}

	def "Should find phone numbers with various formatting"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "Call 348-901-8484 or (335) 5856641 or 02.8193736"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 3
			result[0].toString() == '3489018484'
			result[1].toString() == '3355856641'
			result[2].toString() == '028193736'
	} // }}}

	def "Should handle phone numbers with 0039 prefix"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "International format: 0039 335 5856641 and 0039 02 8193736"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 2
			result[0].type() == PhoneNumberType.MOBILE
			result[1].type() == PhoneNumberType.LANDLINE
	} // }}}

	def "Should not duplicate phone numbers that appear multiple times"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "Call 335 5856641. Yes, please call 335 5856641 today!"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 2
			result[0].toString() == '3355856641'
			result[1].toString() == '3355856641'
	} // }}}

	def "Should find phone numbers at the beginning and end of text"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "3355856641 is my mobile, office is 028193736"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 2
			result[0].toString() == '3355856641'
			result[1].toString() == '028193736'
	} // }}}

	def "Should find phone numbers separated by various delimiters"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "Numbers: 3355856641, 028193736; 800200227 - 899200227"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 4
	} // }}}

	def "Should handle edge cases with shortest and longest valid numbers"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			// shortest LANDLINE: 022881, longest LANDLINE: 09977933544
			// shortest MOBILE: 324611069, longest MOBILE: 33124165852
			def text = "Short: 022881 and 324611069. Long: 09977933544 and 33124165852"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 4
			result[0].toString() == '022881'
			result[1].toString() == '324611069'
			result[2].toString() == '09977933544'
			result[3].toString() == '33124165852'
	} // }}}

	def "Should not match invalid sequences that look like phone numbers"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "My PIN is 12345678 and year 2023 and code 999888777"
		when:
			def result = parser.parse(text)
		then:
			result.isEmpty()
	} // }}}

	def "Should find phone numbers in multi-line text"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = """
				Customer Service
				Phone: +39 335 5856641
				Fax: 02 8193736
			"""
		when:
			def result = parser.parse(text)
		then:
			result.size() == 2
	} // }}}

	def "Should handle incomplete international prefix (39 only)"() { // {{{
		given:
			def parser = new ItalianPhoneParser()
			def text = "Numbers from database: 390239435196 and 393271117978"
		when:
			def result = parser.parse(text)
		then:
			result.size() == 2
			result[0].toString() == '0239435196'
			result[1].toString() == '3271117978'
	} // }}}

}
// vim: fdm=marker
