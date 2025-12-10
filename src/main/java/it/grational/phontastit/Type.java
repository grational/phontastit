package it.grational.phontastit;

/**
 * Possible types of phone numbers
 */
public enum Type {
    /** Traditional house phone numbers */
    LANDLINE,
    /** Mobile/cell phone numbers */
    MOBILE,
    /** A phone number for which the phone call is free */
    TOLLFREE,
    /** A service number, an extra fee is required for the phone call */
    PREMIUM,
    /** A phone number connected to a fax device */
    FAX
}