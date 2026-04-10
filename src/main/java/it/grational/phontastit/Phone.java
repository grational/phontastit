package it.grational.phontastit;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * PhontastIT
 * This class recognize the type of Italian phone numbers using a heuristic
 * @author grational
 * @date 30-08-2018 20.34
 */
public class Phone {
	private final String phone;
	private final boolean fax;

	private static final String IT_PREFIX = "+39";
	private static final String IT_18N = "((00|\\+)39)|(39(?!\\d{7,9}$))";
	private static final String LANDLINE_TWO_DIGIT_PREFIXES = "(?:02|06)";
	private static final String LANDLINE_THREE_DIGIT_PREFIXES = String.join("|",
		"010", "011", "015", "019", "030", "031", "035", "039", "040", "041", "045", "049",
		"050", "051", "055", "059", "070", "071", "075", "079", "080", "081", "085", "089",
		"090", "091", "095", "099"
	);
	private static final String LANDLINE_FOUR_DIGIT_PREFIXES = String.join("|",
		"0121", "0122", "0123", "0124", "0125", "0131", "0141", "0142", "0143", "0144", "0161",
		"0163", "0165", "0166", "0171", "0172", "0173", "0174", "0175", "0182", "0183", "0184",
		"0185", "0187", "0321", "0322", "0323", "0324", "0331", "0332", "0341", "0342", "0343",
		"0344", "0345", "0346", "0362", "0363", "0364", "0365", "0371", "0372", "0373", "0374",
		"0375", "0376", "0377", "0381", "0382", "0383", "0384", "0385", "0386", "0421", "0422",
		"0423", "0424", "0425", "0426", "0427", "0428", "0429", "0431", "0432", "0433", "0434",
		"0435", "0436", "0437", "0438", "0439", "0442", "0444", "0445", "0461", "0462", "0463",
		"0464", "0465", "0471", "0472", "0473", "0474", "0481", "0521", "0522", "0523", "0524",
		"0525", "0532", "0533", "0534", "0535", "0536", "0541", "0542", "0543", "0544", "0545",
		"0546", "0547", "0549", "0564", "0565", "0566", "0571", "0572", "0573", "0574", "0575",
		"0577", "0578", "0583", "0584", "0585", "0586", "0587", "0588", "0721", "0722", "0731",
		"0732", "0733", "0734", "0735", "0736", "0737", "0742", "0743", "0744", "0746", "0761",
		"0763", "0765", "0766", "0771", "0773", "0774", "0775", "0776", "0781", "0782", "0783",
		"0784", "0785", "0789", "0823", "0824", "0825", "0827", "0828", "0831", "0832", "0833",
		"0835", "0836", "0861", "0862", "0863", "0864", "0865", "0871", "0872", "0873", "0874",
		"0875", "0881", "0882", "0883", "0884", "0885", "0921", "0922", "0923", "0924", "0925",
		"0931", "0932", "0933", "0934", "0935", "0941", "0942", "0961", "0962", "0963", "0964",
		"0965", "0966", "0967", "0968", "0971", "0972", "0973", "0974", "0975", "0976", "0981",
		"0982", "0983", "0984", "0985"
	);
	private static final String LANDLINE = String.join("|",
		"(?:" + LANDLINE_TWO_DIGIT_PREFIXES + "\\d{4,9})",
		"(?:(?:" + LANDLINE_THREE_DIGIT_PREFIXES + ")\\d{4,8})",
		"(?:(?:" + LANDLINE_FOUR_DIGIT_PREFIXES + ")\\d{4,7})"
	);

	private static final String MOBILE_PREFIXES = String.join("|",
		"320", "322", "323", "324", "327", "328", "329", "330", "331", "332", "333", "334",
		"335", "336", "337", "338", "339", "340", "342", "343", "344", "345", "346", "347",
		"348", "349", "350", "351", "352", "353", "360", "362", "363", "366", "368", "370",
		"371", "373", "375", "376", "377", "378", "379", "380", "383", "388", "389", "390",
		"391", "392", "393", "398"
	);
	private static final String MOBILE = "(?:(?:" + MOBILE_PREFIXES + ")\\d{6,7})";

	private static final String TOLLFREE = String.join("|",
		"(?:800\\d{6})",
		"(?:803\\d{3})"
	);

	private static final String SHARED_COST = String.join("|",
		"(?:840\\d{6})",
		"(?:841\\d{3})",
		"(?:847\\d{3})",
		"(?:848\\d{6})"
	);

	private static final String PREMIUM = String.join("|",
		"(?:892\\d{3})",
		"(?:8930\\d{3})",
		"(?:8934\\d{4})",
		"(?:8938\\d{6})",
		"(?:894[0-4]\\d{2})",
		"(?:894[5-9]\\d{4})",
		"(?:895[0-2]\\d{2})",
		"(?:8953\\d{3})",
		"(?:8954\\d{4})",
		"(?:895[5-9]\\d{6})",
		"(?:899\\d{6})",
		"(?:199\\d{6})",
		"(?:178\\d{7})"
	);

	private static final Pattern VALID_PATTERN = Pattern.compile(
		"^(?:" + IT_18N + ")?(?:(?:" + LANDLINE + ")|(?:" + MOBILE + ")|(?:" + TOLLFREE + ")|(?:" + SHARED_COST + ")|(?:" + PREMIUM + "))$"
	);
	private static final Pattern PREMIUM_PATTERN = Pattern.compile("^" + PREMIUM + "$");
	private static final Pattern SHARED_COST_PATTERN = Pattern.compile("^" + SHARED_COST + "$");
	private static final Pattern TOLLFREE_PATTERN = Pattern.compile("^" + TOLLFREE + "$");
	private static final Pattern MOBILE_PATTERN = Pattern.compile("^" + MOBILE + "$");
	private static final Pattern LANDLINE_PATTERN = Pattern.compile("^" + LANDLINE + "$");

	/**
	 * Primary constructor
	 *
	 * @param ph  the Italian phone number to be represented by the object
	 */
	public Phone(String ph) {
		this(ph, false);
	}

	/**
	 * Primary constructor
	 *
	 * @param ph  the Italian phone number to be represented by the object
	 * @param fx  a boolean flag to force the type to FAX
	 */
	public Phone(String ph, Boolean fx) {
		String sanitized = sanitize(ph);
		String localized = localize(sanitized);
		if (VALID_PATTERN.matcher(sanitized).matches() && VALID_PATTERN.matcher(localized).matches()) {
			this.phone = localized;
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
	 * @return Type  return an enum Type indicating the possible types
	 */
	public Type type() {
		return this.fax ? Type.FAX : heuristic(this.phone);
	}

	/**
	 * This method decides according to the starting digits of
	 * an Italian Phone Number without the i18n IT prefix
	 */
	private Type heuristic(String phone) {
		if (PREMIUM_PATTERN.matcher(phone).matches()) {
			return Type.PREMIUM;
		}
		if (TOLLFREE_PATTERN.matcher(phone).matches()) {
			return Type.TOLLFREE;
		}
		if (SHARED_COST_PATTERN.matcher(phone).matches()) {
			return Type.SHARED_COST;
		}
		if (MOBILE_PATTERN.matcher(phone).matches()) {
			return Type.MOBILE;
		}
		if (LANDLINE_PATTERN.matcher(phone).matches()) {
			return Type.LANDLINE;
		}
		throw new IllegalStateException(
			"Cannot determine the phone number type of '" + phone + "'"
		);
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
		Phone that = (Phone) o;
		return fax == that.fax && Objects.equals(phone, that.phone);
	}

	@Override
	public int hashCode() {
		return Objects.hash(phone, fax);
	}
}
