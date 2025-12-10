package it.grational.phontastit

import spock.lang.*

/**
 * Unit tests for Parser
 * Testing the ability to extract Italian phone numbers from text
 * @author grational
 */
class ParserUSpec extends Specification {

	def "Should return an empty list when the text is null or empty"() { // {{{
		expect:
			Parser.parse(text).isEmpty()
		where:
			text << [
				null,
				'',
				'   ',
				"This is just some text without any phone numbers"
			]
	} // }}}

	def "Should find a single mobile phone number in text"() { // {{{
		given:
			def text = "Call me at 348 9018484 for more info"
		when:
			List result = Parser.parse(text)
		then:
			result.size() == 1
			result[0].type() == Type.MOBILE
			result[0].toString() == '3489018484'
	} // }}}

	def "Should find a single landline phone number in text"() { // {{{
		given:
			def text = "Our office number is 02 81.93.736"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 1
			result[0].type() == Type.LANDLINE
			result[0].toString() == '028193736'
	} // }}}

	def "Should find phone numbers with international prefix"() { // {{{
		given:
			def text = "Contact us at +39 335 5856641 or +39 02-81.93.736"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 2
			result[0].type() == Type.MOBILE
			result[0].toString() == '3355856641'
			result[1].type() == Type.LANDLINE
			result[1].toString() == '028193736'
	} // }}}

	def "Should find multiple phone numbers of different types in text"() { // {{{
		given:
			def text = """
				For support call our toll free number 800 200 227.
				For sales contact 335 5856641 or our office at 011 8193736.
				Premium line available at 899 200227.
			"""
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 4
			result[0].type() == Type.TOLLFREE
			result[1].type() == Type.MOBILE
			result[2].type() == Type.LANDLINE
			result[3].type() == Type.PREMIUM
	} // }}}

	def "Should find phone numbers with various formatting"() { // {{{
		given:
			def text = "Call 348-901-8484 or (335) 5856641 or 02.8193736 or 800/200227"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 4
			result[0].toString() == '3489018484'
			result[1].toString() == '3355856641'
			result[2].toString() == '028193736'
			result[3].toString() == '800200227'
	} // }}}

	def "Should handle phone numbers with 0039 prefix"() { // {{{
		given:
			def text = "International format: 0039 335/5856641 and 0039 02-81.93.736"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 2
			result[0].type() == Type.MOBILE
			result[1].type() == Type.LANDLINE
	} // }}}

	def "Should not duplicate phone numbers that appear multiple times"() { // {{{
		given:
			def text = "Call 335 5856641. Yes, please call 335 5856641 today!"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 1
			result[0].toString() == '3355856641'
	} // }}}

	def "Should find phone numbers at the beginning and end of text"() { // {{{
		given:
			def text = "3355856641 is my mobile, office is 028193736"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 2
			result[0].toString() == '3355856641'
			result[1].toString() == '028193736'
	} // }}}

	def "Should find phone numbers separated by various delimiters"() { // {{{
		given:
			def text = "Numbers: 3355856641, 028193736; 800200227 - 899200227"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 4
	} // }}}

	def "Should handle edge cases with shortest and longest valid numbers"() { // {{{
		given:
			// shortest LANDLINE: 022881, longest LANDLINE: 09977933544
			// shortest MOBILE: 324611069, longest MOBILE: 33124165852
			def text = "Short: 022881 and 324611069. Long: 09977933544 and 33124165852"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 4
			result[0].toString() == '022881'
			result[1].toString() == '324611069'
			result[2].toString() == '09977933544'
			result[3].toString() == '33124165852'
	} // }}}

	def "Should not match invalid sequences that look like phone numbers"() { // {{{
		given:
			def text = "My PIN is 12345678 and year 2023 and code 999888777"
		when:
			def result = Parser.parse(text)
		then:
			result.isEmpty()
	} // }}}

	def "Should find phone numbers in multi-line text"() { // {{{
		given:
			def text = """
				Customer Service
				Phone: +39 335 5856641
				Fax: 02 8193736
			"""
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 2
	} // }}}

	def "Should handle incomplete international prefix (39 only)"() { // {{{
		given:
			def text = "Numbers from database: 390239435196 and 393271117978"
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 2
			result[0].toString() == '0239435196'
			result[1].toString() == '3271117978'
	} // }}}

	def "Should correctly handle a series of edge cases from the report"() { // {{{
		given:
			def text = """
				Numbers from the powerlisting report
				00000000000;0558954159
				0000000000;0464519688
				00158170670;086166404
				000000000;0362284445
				000000000;3206393915
				00140840406;05411836580
				00140840406;0541974135
				000000000;3299420528
				000000000;3342929395
				000000000;3920544313;0331724600
				000000000;0456304777;3295304376
				000000000;029747004
				000000000;0250043638;3473062946
				000000000;08119723547;08119723546;0815922409
			"""
		when:
			def result = Parser.parse(text)
		then:
			result.size() == 19
	} // }}}
}
// vim: fdm=marker