package android.provider;

import android.net.Uri;

public class ContactsContract {
	public static final String AUTHORITY = "com.android.contacts";
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
	public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";
	public static final String DIRECTORY_PARAM_KEY = "directory";
	public static final String LIMIT_PARAM_KEY = "limit";
	public static final String PRIMARY_ACCOUNT_NAME = "name_for_primary_account";
	public static final String PRIMARY_ACCOUNT_TYPE = "type_for_primary_account";
	public static final String REMOVE_DUPLICATE_ENTRIES = "remove_duplicate_entries";

	protected interface BaseSyncColumns {
		String SYNC1 = "sync1";
		String SYNC2 = "sync2";
		String SYNC3 = "sync3";
		String SYNC4 = "sync4";
	}

	protected interface ContactsColumns {
		String DISPLAY_NAME = "display_name";
		String PHOTO_ID = "photo_id";
		String PHOTO_FILE_ID = "photo_file_id";
		String PHOTO_URI = "photo_uri";
		String PHOTO_THUMBNAIL_URI = "photo_thumb_uri";
		String IN_DEFAULT_DIRECTORY = "in_default_directory";
		String IN_VISIBLE_GROUP = "in_visible_group";
		String IS_USER_PROFILE = "is_user_profile";
		String LOOKUP_KEY = "lookup";
		String HAS_PHONE_NUMBER = "has_phone_number";
		String NAME_RAW_CONTACT_ID = "name_raw_contact_id";
		String CONTACT_LAST_UPDATED_TIMESTAMP = "contact_last_updated_timestamp";
	}

	protected interface ContactOptionsColumns {
		String TIMES_CONTACTED = "times_contacted";
		String LAST_TIME_CONTACTED = "last_time_contacted";
		String STARRED = "starred";
		String PINNED = "pinned";
		String CUSTOM_RINGTONE = "custom_ringtone";
		String SEND_TO_VOICEMAIL = "send_to_voicemail";
	}

	protected interface ContactNameColumns {
		String DISPLAY_NAME_SOURCE = "display_name_source";
		String DISPLAY_NAME_PRIMARY = "display_name";
		String DISPLAY_NAME_ALTERNATIVE = "display_name_alt";
		String PHONETIC_NAME_STYLE = "phonetic_name_style";
		String PHONETIC_NAME = "phonetic_name";
		String SORT_KEY_PRIMARY = "sort_key";
		String SORT_KEY_ALTERNATIVE = "sort_key_alt";
	}

	protected interface ContactStatusColumns {
		String CONTACT_PRESENCE = "contact_presence";
		String CONTACT_CHAT_CAPABILITY = "contact_chat_capability";
		String CONTACT_STATUS = "contact_status";
		String CONTACT_STATUS_TIMESTAMP = "contact_status_ts";
		String CONTACT_STATUS_RES_PACKAGE = "contact_status_res_package";
		String CONTACT_STATUS_LABEL = "contact_status_label";
		String CONTACT_STATUS_ICON = "contact_status_icon";
	}

	protected interface RawContactsColumns {
		String CONTACT_ID = "contact_id";
		String DATA_SET = "data_set";
		String ACCOUNT_TYPE_AND_DATA_SET = "account_type_and_data_set";
		String AGGREGATION_MODE = "aggregation_mode";
		String DELETED = "deleted";
		String RAW_CONTACT_IS_READ_ONLY = "raw_contact_is_read_only";
		String RAW_CONTACT_IS_USER_PROFILE = "raw_contact_is_user_profile";
		String METADATA_DIRTY = "metadata_dirty";
	}

	protected interface SyncColumns extends BaseSyncColumns {
		String ACCOUNT_NAME = "account_name";
		String ACCOUNT_TYPE = "account_type";
		String SOURCE_ID = "sourceid";
		String VERSION = "version";
		String DIRTY = "dirty";
	}

	protected interface DataColumns {
		String MIMETYPE = "mimetype";
		String RAW_CONTACT_ID = "raw_contact_id";
		String IS_PRIMARY = "is_primary";
		String IS_SUPER_PRIMARY = "is_super_primary";
		String IS_READ_ONLY = "is_read_only";
		String DATA_VERSION = "data_version";
		String DATA1 = "data1";
		String DATA2 = "data2";
		String DATA3 = "data3";
		String DATA4 = "data4";
		String DATA5 = "data5";
		String DATA6 = "data6";
		String DATA7 = "data7";
		String DATA8 = "data8";
		String DATA9 = "data9";
		String DATA10 = "data10";
		String DATA11 = "data11";
		String DATA12 = "data12";
		String DATA13 = "data13";
		String DATA14 = "data14";
		String DATA15 = "data15";
		String SYNC1 = "data_sync1";
		String SYNC2 = "data_sync2";
		String SYNC3 = "data_sync3";
		String SYNC4 = "data_sync4";
	}

	protected interface PhoneLookupColumns {
		String _ID = "_id";
		String CONTACT_ID = "contact_id";
		String DATA_ID = "data_id";
		String NUMBER = "number";
		String TYPE = "type";
		String LABEL = "label";
		String NORMALIZED_NUMBER = "normalized_number";
	}

	public static final class Contacts implements BaseColumns, ContactsColumns, ContactOptionsColumns, ContactNameColumns, ContactStatusColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "contacts");
		public static final Uri CONTENT_LOOKUP_URI = Uri.withAppendedPath(CONTENT_URI, "lookup");
		public static final Uri CONTENT_VCARD_URI = Uri.withAppendedPath(CONTENT_URI, "as_vcard");
		public static final Uri CONTENT_MULTI_VCARD_URI = Uri.withAppendedPath(CONTENT_URI, "as_multi_vcard");
		public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
		public static final Uri CONTENT_STREQUENT_URI = Uri.withAppendedPath(CONTENT_URI, "strequent");
		public static final Uri CONTENT_FREQUENT_URI = Uri.withAppendedPath(CONTENT_URI, "frequent");
		public static final Uri CONTENT_GROUP_URI = Uri.withAppendedPath(CONTENT_URI, "group");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contact";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact";
		public static final String CONTENT_VCARD_TYPE = "text/x-vcard";

		public static Uri getLookupUri(long contactId, String lookupKey) {
			return Uri.withAppendedPath(Uri.withAppendedPath(CONTENT_LOOKUP_URI, lookupKey), String.valueOf(contactId));
		}

		public static final class Photo implements BaseColumns {
			public static final String CONTENT_DIRECTORY = "photo";
			public static final String DISPLAY_PHOTO = "display_photo";
			public static final String PHOTO_FILE_ID = "data14";
			public static final String PHOTO = "data15";
		}

		public static final class Data implements BaseColumns, DataColumns {
			public static final String CONTENT_DIRECTORY = "data";
		}
	
	public static java.io.InputStream openContactPhotoInputStream(android.content.ContentResolver arg0, android.net.Uri arg1) { return null; } // ATL-autostub
	public static java.io.InputStream openContactPhotoInputStream(android.content.ContentResolver arg0, android.net.Uri arg1, boolean arg2) { return null; } // ATL-autostub
}

	public static final class Profile implements BaseColumns, ContactsColumns, ContactOptionsColumns, ContactNameColumns, ContactStatusColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "profile");
		public static final Uri CONTENT_VCARD_URI = Uri.withAppendedPath(CONTENT_URI, "as_vcard");
		public static final Uri CONTENT_RAW_CONTACTS_URI = Uri.withAppendedPath(CONTENT_URI, "raw_contacts");
		public static final long MIN_ID = Long.MAX_VALUE - (long)Integer.MAX_VALUE;
	}

	public static final class RawContacts implements BaseColumns, RawContactsColumns, ContactOptionsColumns, ContactNameColumns, SyncColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "raw_contacts");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/raw_contact";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/raw_contact";
		public static final int AGGREGATION_MODE_DEFAULT = 0;
		public static final int AGGREGATION_MODE_IMMEDIATE = 1;
		public static final int AGGREGATION_MODE_SUSPENDED = 2;
		public static final int AGGREGATION_MODE_DISABLED = 3;

		public static final class Data implements BaseColumns, DataColumns {
			public static final String CONTENT_DIRECTORY = "data";
		}

		public static final class Entity implements BaseColumns, DataColumns {
			public static final String CONTENT_DIRECTORY = "entity";
			public static final String DATA_ID = "data_id";
		}
	
	public static android.net.Uri getContactLookupUri(android.content.ContentResolver arg0, android.net.Uri arg1) { return null; } // ATL-autostub
}

	public static final class Data implements BaseColumns, DataColumns, RawContactsColumns, ContactsColumns, ContactNameColumns, ContactOptionsColumns, ContactStatusColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "data");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/data";
		public static final String VISIBLE_CONTACTS_ONLY = "visible_contacts_only";
	}

	public static final class PhoneLookup implements BaseColumns, PhoneLookupColumns, ContactsColumns, ContactOptionsColumns, ContactNameColumns {
		public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(AUTHORITY_URI, "phone_lookup");
		public static final Uri ENTERPRISE_CONTENT_FILTER_URI = Uri.withAppendedPath(AUTHORITY_URI, "phone_lookup_enterprise");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/phone_lookup";
		public static final String QUERY_PARAMETER_SIP_ADDRESS = "sip";
	}

	public static final class CommonDataKinds {
		protected interface CommonColumns {
			String DATA = "data1";
			String TYPE = "data2";
			String LABEL = "data3";
		}

		public static final class Phone implements BaseColumns, DataColumns, CommonColumns, ContactsColumns, ContactNameColumns {
			public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/phone_v2";
			public static final String CONTENT_TYPE = "vnd.android.cursor.dir/phone_v2";
			public static final Uri CONTENT_URI = Uri.withAppendedPath(Data.CONTENT_URI, "phones");
			public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
			public static final String NUMBER = "data1";
			public static final String NORMALIZED_NUMBER = "data4";
			public static final String SEARCH_DISPLAY_NAME_KEY = "search_display_name";
			public static final String SEARCH_PHONE_NUMBER_KEY = "search_phone_number";
			public static final int TYPE_CUSTOM = 0;
			public static final int TYPE_HOME = 1;
			public static final int TYPE_MOBILE = 2;
			public static final int TYPE_WORK = 3;
			public static final int TYPE_FAX_WORK = 4;
			public static final int TYPE_FAX_HOME = 5;
			public static final int TYPE_PAGER = 6;
			public static final int TYPE_OTHER = 7;
			public static final int TYPE_MAIN = 12;

			public static CharSequence getTypeLabel(android.content.res.Resources res, int type, CharSequence label) {
				switch (type) {
					case TYPE_HOME: return "Home";
					case TYPE_MOBILE: return "Mobile";
					case TYPE_WORK: return "Work";
					case TYPE_MAIN: return "Main";
					case TYPE_CUSTOM: return label != null ? label : "";
					default: return "Other";
				}
			}
		}

		public static final class Email implements BaseColumns, DataColumns, CommonColumns, ContactsColumns, ContactNameColumns {
			public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/email_v2";
			public static final String CONTENT_TYPE = "vnd.android.cursor.dir/email_v2";
			public static final Uri CONTENT_URI = Uri.withAppendedPath(Data.CONTENT_URI, "emails");
			public static final Uri CONTENT_LOOKUP_URI = Uri.withAppendedPath(CONTENT_URI, "lookup");
			public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
			public static final String ADDRESS = "data1";
			public static final String DISPLAY_NAME = "data4";
			public static final int TYPE_HOME = 1;
			public static final int TYPE_WORK = 2;
			public static final int TYPE_OTHER = 3;
			public static final int TYPE_MOBILE = 4;
		
	public static int getTypeLabelResource(int arg0) { return 0; } // ATL-autostub
	public static java.lang.CharSequence getTypeLabel(android.content.res.Resources arg0, int arg1, java.lang.CharSequence arg2) { return null; } // ATL-autostub
}

		public static final class Im implements BaseColumns, DataColumns, CommonColumns {
			public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/im";
			public static final String PROTOCOL = "data5";
			public static final String CUSTOM_PROTOCOL = "data6";
			public static final int PROTOCOL_CUSTOM = -1;
			public static final int TYPE_HOME = 1;
			public static final int TYPE_WORK = 2;
			public static final int TYPE_OTHER = 3;
		
	public static int getProtocolLabelResource(int arg0) { return 0; } // ATL-autostub
}

		public static final class StructuredName implements BaseColumns, DataColumns {
			public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/name";
			public static final String DISPLAY_NAME = "data1";
			public static final String GIVEN_NAME = "data2";
			public static final String FAMILY_NAME = "data3";
			public static final String PREFIX = "data4";
			public static final String MIDDLE_NAME = "data5";
			public static final String SUFFIX = "data6";
		}

		public static final class StructuredPostal implements BaseColumns, DataColumns, CommonColumns {
			public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/postal-address_v2";
			public static final String CONTENT_TYPE = "vnd.android.cursor.dir/postal-address_v2";
			public static final Uri CONTENT_URI = Uri.withAppendedPath(Data.CONTENT_URI, "postals");
			public static final String FORMATTED_ADDRESS = "data1";
			public static final String STREET = "data4";
			public static final String POBOX = "data5";
			public static final String NEIGHBORHOOD = "data6";
			public static final String CITY = "data7";
			public static final String REGION = "data8";
			public static final String POSTCODE = "data9";
			public static final String COUNTRY = "data10";
			public static final int TYPE_HOME = 1;
			public static final int TYPE_WORK = 2;
			public static final int TYPE_OTHER = 3;
		}

		public static final class Organization implements BaseColumns, DataColumns, CommonColumns {
			public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/organization";
			public static final String COMPANY = "data1";
			public static final String TITLE = "data4";
		}

		public static final class Photo implements BaseColumns, DataColumns {
			public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/photo";
			public static final String PHOTO_FILE_ID = "data14";
			public static final String PHOTO = "data15";
		}
	}
}
