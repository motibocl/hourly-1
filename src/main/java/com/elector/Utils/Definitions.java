package com.elector.Utils;

import com.elector.Enums.ConfigEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sigal on 5/27/2016.
 */
public class Definitions {
    public static final String ELECTION_DAY_IN_MEMORY_APP_URL = "http://localhost:8788/";

    public static final int PARAM_LANG_DEFAULT = 0;
    public static final int PARAM_LANG_EN = 1;
    public static final String PARAM_ACTIVIST_OID = "activistOid";
    public static final String PARAM_CANDIDATE_OID = "candidateOid";
    public static final String PARAM_LIST = "list";
    public static final String PARAM_ERROR = "error";
    public static final String PARAM_OID = "oid";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_ACTIVE = "active";
    public static final String PARAM_DONOR_ID = "donorId";
    public static final String PARAM_ACTIVISTS = "activists";
    public static final String PARAM_SITE_NAME = "siteName";
    public static final String PARAM_SITE_URL = "siteUrl";
    public static final String PARAM_TEMPLATE_UTILS = "templateUtils";
    public static final String PARAM_PAGE_NAME = "pageName";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_VOTER_ID = "voterId";
    public static final String PARAM_FULL_NAME = "fullName";
    public static final String PARAM_MESSAGES = "messages";
    public static final String PARAM_VOTERS = "voters";
    public static final String PARAM_BIRTHDAYS_TODAY = "birthdaysToday";
    public static final String PARAM_BIRTHDAYS_MAP = "birthdaysMap";
    public static final int PARAM_COMPARE_EQUALS = 0;
    public static final int PARAM_COMPARE_BIGGER = 1;
    public static final int PARAM_COMPARE_SMALLER = -1;
    public static final String PARAM_DONORS = "donors";
    public static final String PARAM_TABLE_ID = "tableId";
    public static final String PARAM_CLIENTS_COUNTER = "clientsCounter";
    public static final String PARAM_CONFIG_KEY = "configKey";
    public static final String PARAM_CONFIG_VALUE = "configValue";
    public static final String PARAM_CONFIG = "config";
    public static final String PARAM_OBSERVERS = "observers";
    public static final String PARAM_CALLERS = "callers";
    public static final String PARAM_CALLER = "caller";
    public static final String PARAM_CALLS = "calls";
    public static final String PARAM_VOTER_NAME = "voterName";
    public static final String PARAM_PREVIOUS_STATUS = "previousStatus";
    public static final String PARAM_NEW_STATUS = "newStatus";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_ANSWERED = "answered";
    public static final String PARAM_SENDER = "sender";
    public static final String PARAM_CODE = "code";
    public static final String PARAM_CANDIDATE_STATS = "candidateStats";
    public static final String PARAM_ADMIN_OID = "adminOid";
    public static final String PARAM_CANDIDATE_CHART_STATS = "candidateChartStats";
    public static final String PARAM_CALLER_STATS = "callerStats";
    public static final String PARAM_VOTED = "voted";
    public static final String PARAM_ALLOW_REPORT = "allowReport";
    public static final String PARAM_ZOOM = "zoom";
    public static final String PARAM_LAT = "lat";
    public static final String PARAM_LNG = "lng";
    public static final String PARAM_CALLER_NAME = "callerName";
    public static final String PARAM_NOT_VOTED_SUPPORTERS = "notVotedSupporters";
    public static final String PARAM_NOT_VOTED_UNKNOWN_SUPPORT = "notVotedUnknownSupport";
    public static final String PARAM_TOTAL_VOTED = "totalVoted";
    public static final String PARAM_TOTAL_NOT_VOTED = "totalNotVoted";
    public static final String PARAM_VOTED_PERCENT = "votedPercent";
    public static final String PARAM_LAST_CALL_TIME = "lastCallTime";
    public static final String PARAM_CALLS_CHARTS_STATS = "callsChartStats";
    public static final String PARAM_COUNT = "count";
    public static final String PARAM_ACTIVISTS_CHARTS_STATS = "activistsChartStats";
    public static final String PARAM_UNANSWERED_REASON = "unansweredReason";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_HISTORY = "history";
    public static final String PARAM_TOTAL_PURCHASED_SMS = "purchasedSms";
    public static final String PARAM_TOTAL_REMAINING_SMS = "remainingSms";
    public static final String PARAM_TOTAL_USED_SMS = "usedSms";
    public static final String PARAM_SMS_DATA = "smsData";
    public static final String PARAM_HANDLED = "handled";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_REQUESTED_TIME = "requestedTime";
    public static final String PARAM_CREATION_TIME = "creationTime";
    public static final String PARAM_TIME_TO_COME = "timeToCome";
    public static final String PARAM_OPEN_TASKS = "openTasks";
    public static final String PARAM_ID = "id";
    public static final String PARAM_DESIRED_DATE = "desiredDate";
    public static final String PARAM_ESTIMATED_PARTICIPANTS_COUNT = "estimatedParticipantsCount";
    public static final String PARAM_EVENTS = "events";
    public static final String PARAM_DESCRIPTION = "description";
    public static final String PARAM_CUSTOM_GROUPS = "customGroups";
    public static final String PARAM_GROUP_NAME = "groupName";
    public static final String PARAM_GROUP_SIZE = "groupSize";
    public static final String PARAM_NAME_IN_SERVICE = "nameInService";
    public static final String PARAM_CALLS_HISTORY = "callsHistory";
    public static final String PARAM_VOTERS_BOOK = "votersBook";
    public static final String PARAM_BIRTHDAYS = "birthdays";
    public static final String PARAM_TASK = "task";
    public static final String PARAM_INITIATOR_FULL_NAME = "initiatorFullName";
    public static final String PARAM_INITIATOR_TYPE = "initiatorType";
    public static final String PARAM_MOBILE = "mobile";
    public static final String PARAM_ROLE = "role";
    public static final String PARAM_USERS_COUNT = "usersCount";
    public static final String PARAM_ADMIN_ID = "adminId";
    public static final String PARAM_OIDS_LIST = "oidsList";
    public static final String PARAM_CITY_NAME = "cityName";
    public static final String PARAM_ALLOW_EXCEL_UPLOAD = "allowExcelUpload";
    public static final String PARAM_TASKS = "tasks";
    public static final String PARAM_DONE = "done";
    public static final String PARAM_CURRENT_SUPPORT_STATS = "currentSupportStats";
    public static final String PARAM_ACTIVISTS_NAMES = "activistsNames";
    public static final String PARAM_MASTER_OID = "masterOid";
    public static final String PARAM_SLAVES_OIDS = "slavesOids";
    public static final String PARAM_ACTIVIST_NAME = "activistName";
    public static final String PARAM_ACTIVIST_MAPPING_DATA = "activistMappingData";
    public static final String PARAM_CALLS_DATA = "callsData";
    public static final String PARAM_REFERRERS_NAMES = "referrersNames";
    public static final String PARAM_CURRENT_SUPPORT_STATUS = "currentSupportStatus";
    public static final String PARAM_BALLOT_BOX_BLOCK = "ballotBoxBlock";
    public static final String PARAM_BALLOT_BOXES_BLOCKS = "ballotBoxesBlocks";
    public static final String PARAM_CUSTOM_PROPERTIES = "customProperties";
    public static final String PARAM_TRANSLATION_KEY = "translationKey";
    public static final String PARAM_JSON_KEY = "jsonKey";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_SMS_SENDER_NAME = "smsSenderName";
    public static final String PARAM_SEND_SMS = "sendSms";
    public static final String PARAM_CUSTOM_PROPERTIES_DATA = "customPropertiesData";
    public static final String PARAM_HEBREW = "hebrew";
    public static final String PARAM_ENGLISH = "english";
    public static final String PARAM_BALLOT_BOX_DESCRIPTION = "ballotBoxDescription";
    public static final String PARAM_SLAVES = "slaves";
    public static final String PARAM_SEND_LOGOUT = "sendLogout";
    public static final String PARAM_CHECKED_IN = "checkedIn";
    public static final String PARAM_ACTIVISTS_RANKING_TODAY = "activistsRankingToday";
    public static final String PARAM_RANK = "rank";
    public static final String PARAM_TOTAL_ACTIVISTS = "totalActivists";
    public static final String PARAM_GENERAL_CALLS_STATS = "generalCallsStats";
    public static final String PARAM_SEND_SMS_TO_ACTIVIST_WHEN_SUPPORTER_DECLINED = "sendSmsToActivistWhenSupporterDeclined";
    public static final String PARAM_SEND_NOTIFICATION_TO_ACTIVIST_WHEN_SUPPORTER_DECLINED = "sendNotificationToActivistWhenSupporterDeclined";
    public static final String PARAM_SEND_SUPPORTER_VERIFIED_SMS = "sendSupporterVerifiedSms";
    public static final String PARAM_SEND_SUPPORTER_VERIFIED_NOTIFICATION = "sendSupporterVerifiedNotification";
    public static final String PARAM_SUPER_OBSERVER = "superObserver";
    public static final String PARAM_OBSERVER_LOGS = "observerLogs";
    public static final String PARAM_SENDER_TYPE = "senderType";
    public static final String PARAM_ALERT = "alert";
    public static final String PARAM_OBSERVERS_LOGS = "observersLogs";
    public static final String PARAM_START_ADDRESS = "startAddress";
    public static final String PARAM_ADDRESSES = "addresses";
    public static final String PARAM_DURATION = "duration";
    public static final String PARAM_CONFIG_OID = "configOid";
    public static final String PARAM_REDIRECT = "redirect";
    public static final String PARAM_ANDROID = "android";
    public static final String PARAM_IOS = "ios";
    public static final String PARAM_SCREENS = "screens";

    public static final int USER_TYPE_CANDIDATE = 0;
    public static final int USER_TYPE_ACTIVIST = 1;

    public static final String EMPTY = "";
    public static final String DOT = ".";
    public static final int MESSAGE_TYPE_DANGER = 1;
    public static final int MESSAGE_TYPE_INFO = 2;
    public static final int MESSAGE_TYPE_WARNING = 3;
    public static final int MESSAGE_TYPE_SUCCESS = 4;

    public static final List<String> MONEY_MANAGEMENT_SECTIONS = Arrays.asList("donors", "incomes.sources", "salaries");
    public static final List<String> MONEY_MANAGEMENT_INCOMES_SECTIONS = Arrays.asList("donors", "incomes.sources");
    public static final List<String> MONEY_MANAGEMENT_OUTCOMES_SECTIONS = Arrays.asList("salaries");

    public static final String PARAM_MONEY_MANAGEMENT_SECTIONS = "moneyManagementSections";
    public static final String PARAM_MONEY_MANAGEMENT_INCOMES_SECTIONS = "moneyManagementIncomesSections";
    public static final String PARAM_MONEY_MANAGEMENT_OUTCOMES_SECTIONS = "moneyManagementOutcomesSections";
    public static final String PARAM_CANDIDATE = "candidate";
    public static final String PARAM_MARKERS = "markers";
    public static final String PARAM_TOTAL_DONATIONS = "totalDonations";
    public static final String PARAM_DONORS_CHART_DATA = "donorsChartData";
    public static final String PARAM_LATEST_CALLS = "latestCalls";

    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String COLON = ":";
    public static final String SEMI_COLON = ";";
    public static final String UNDERSCORE = "_";
    public static final String EQUALS = "=";
    public static final String BREAK = "\n";
    public static final String AND = "&";

    public static final String PARAM_CAMPAIGNS = "campaigns";
    public static final String PARAM_TABLE_TYPE = "tableType";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_PHONE = "phone";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_CAMPAIGN = "campaign";
    public static final String PARAM_FIRST_NAME = "firstName";
    public static final String PARAM_LAST_NAME = "lastName";
    public static final String PARAM_BIRTH_DATE = "birthDate";
    public static final String PARAM_ACTIVIST = "activist";
    public static final String PARAM_ADDRESS = "address";
    public static final String PARAM_SUPPORTERS = "supporters";
    public static final String PARAM_SUPPORTER = "supporter";
    public static final String PARAM_TOTAL_DONATION = "totalDonation";
    public static final String PARAM_RELATION = "relation";
    public static final String PARAM_DONATION_DATE = "donationDate";
    public static final String PARAM_CAMPAIGN_NAME = "campaignName";
    public static final String PARAM_SUPPORTING = "supporting";
    public static final String PARAM_NOT_SUPPORTING = "notSupporting";
    public static final String PARAM_SUPPORT_UNKNOWN = "supportUnknown";
    public static final String PARAM_UNVERIFIED_SUPPORTING = "unverifiedSupporting";
    public static final String PARAM_BALLOT_BOX = "ballotBox";
    public static final String PARAM_OBSERVER = "observer";
    public static final String PARAM_OBSERVER_OID = "observerOid";
    public static final String PARAM_BALLOT_BOX_OID = "ballotBoxOid";
    public static final String PARAM_USER_TYPE = "userType";
    public static final String PARAM_VOTERS_COUNT = "votersCount";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_BODY = "body";
    public static final String PARAM_RECIPIENT_TYPE = "recipientType";
    public static final String PARAM_RECIPIENT_OID = "recipientOid";
    public static final String PARAM_INITIAL_LOGIN = "initialLogin";
    public static final String PARAM_MAX_USERS = "maxUsers";
    public static final String PARAM_BUSINESS_ID = "businessId";
    public static final String PARAM_BUSINESS_NAME = "businessName";
    public static final String PARAM_EXPIRATION_DATE = "expirationDate";
    public static final String PARAM_MAIN_OFFICE_ADDRESS = "mainOfficeAddress";
    public static final String PARAM_MAIN_OFFICE_PHONE = "mainOfficePhone";
    public static final String PARAM_COST = "cost";
    public static final String PARAM_SUPPORTERS_COUNT = "supportersCount";
    public static final String PARAM_ACTIVISTS_COUNT = "activistsCount";
    public static final String PARAM_CALLERS_COUNT = "callersCount";
    public static final String PARAM_OBSERVERS_COUNT = "observersCount";
    public static final String PARAM_DRIVERS_COUNT = "driversCount";
    public static final String PARAM_UNVERIFIED_SUPPORTING_COUNT = "unverifiedSupportingCount";
    public static final String PARAM_UNKNOWN_SUPPORT_VOTERS_COUNT = "unknownSupportVotersCount";
    public static final String PARAM_NOT_SUPPORTING_VOTERS_COUNT = "notSupportingVotersCount";
    public static final String PARAM_USER_OID = "userOid";
    public static final String PARAM_MAPPING_OBJECT_OID = "mapOid";
    public static final String PARAM_VOTER_OID = "voterOid";
    public static final String PARAM_TOTAL_HANDLING_ACTIVISTS = "totalHandlingActivists";
    public static final String PARAM_SESSION_ID = "sessionId";
    public static final String PARAM_ANSWERED_CALLS_COUNT = "answeredCallsCount";
    public static final String PARAM_UNANSWERED_CALLS_COUNT = "unansweredCallsCount";
    public static final String PARAM_REMAINING_CALLS_COUNT = "remainingCallsCount";
    public static final String PARAM_UNKNOWN_SUPPORT_VOTERS = "unknownSupportVoters";
    public static final String PARAM_MESSAGE = "message";
    public static final String PARAM_DEST = "dest";
    public static final String PARAM_USER = "user";
    public static final String PARAM_LAST_LOGIN_DATE = "lastLoginDate";
    public static final String PARAM_USERS = "users";
    public static final String PARAM_NOT_VOTED_SUPPORTERS_COUNT = "notVotedSupportersCount";
    public static final String PARAM_ELECTION_DAY_STATS = "electionDayStats";
    public static final String PARAM_NOT_VOTED_VOTERS_COUNT = "notVotedVotersCount";
    public static final String PARAM_VOTED_SUPPORTERS_COUNT = "votedSupportersCount";
    public static final String PARAM_VOTED_VOTERS_COUNT = "votedVotersCount";
    public static final String PARAM_ELECTION_DAY_ALERTS = "electionDayAlerts";
    public static final String PARAM_DRIVES = "drives";
    public static final String PARAM_VOTED_NON_SUPPORTERS_COUNT = "votedNonSupportersCount";
    public static final String PARAM_VOTED_UNKNOWN_SUPPORT_COUNT = "votedUnknownSupportCount";
    public static final String PARAM_VOTED_UNVERIFIED_SUPPORTERS_COUNT = "votedUnverifiedSupportersCount";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_SUPPORT_STATUS = "supportStatus";
    public static final String PARAM_BALLOT_BOX_ADDRESS = "ballotBoxAddress";
    public static final String PARAM_GENDER = "gender";
    public static final String PARAM_CAN_CALL = "canCall";
    public static final String PARAM_DONATIONS = "donations";
    public static final String PARAM_SUM = "sum";
    public static final String PARAM_DRIVERS = "drivers";
    public static final String PARAM_DONOR_FIRST_NAME = "donorFirstName";
    public static final String PARAM_DONOR_LAST_NAME = "donorLastName";
    public static final String PARAM_DONOR_PHONE = "donorPhone";
    public static final String PARAM_RELATION_TO_DONOR = "relationToDonor";
    public static final String PARAM_DUPLICATE_VOTERS = "duplicateVoters";
    public static final String PARAM_KEY = "key";
    public static final String PARAM_VALUE = "value";
    public static final String PARAM_VOTER = "voter";
    public static final String PARAM_PENDING_VOTERS = "pendingVoters";
    public static final String PARAM_SUPPORT_SIGN = "supportSign";
    public static final String PARAM_DONATIONS_SUM = "donationsSum";
    public static final String PARAM_PAYMENT_CONFIRMATION_IMAGE = "paymentConfirmationImage";
    public static final String PARAM_VOTER_NUMBER = "voterNumber";
    public static final String PARAM_COMMENT = "comment";
    public static final String PARAM_CITY = "city";
    public static final String PARAM_INVALID_ADDRESSES = "invalidAddresses";
    public static final String PARAM_HAS_MORE = "hasMore";
    public static final String PARAM_SENDER_PHONE = "senderPhone";
    public static final String PARAM_CITY_OID = "cityOid";
    public static final String PARAM_CITIES_OIDS = "citiesOids";
    public static final String PARAM_STREETS = "streets";
    public static final String PARAM_FATHER_NAME = "fatherName";
    public static final String PARAM_OWN_VOTERS_COUNT = "ownVotersCount";
    public static final String PARAM_CAMPAIGN_VOTERS_COUNT = "campaignVotersCount";
    public static final String PARAM_VOTERS_BOOK_COPIED = "votersBookCopied";
    public static final String PARAM_STREET = "street";
    public static final String PARAM_FROM_HOUSE = "fromHouse";
    public static final String PARAM_TO_HOUSE = "toHouse";
    public static final String PARAM_FROM_LETTER = "fromLetter";
    public static final String PARAM_TO_LETTER = "toLetter";
    public static final String PARAM_HOME_PHONE = "homePhone";
    public static final String PARAM_EXTRA_PHONE = "extraPhone";
    public static final String PARAM_SUPPORTERS_LIST_STATUS = "supportersListStatus";
    public static final String PARAM_SUPPORTER_OID = "supporterOid";
    public static final String PARAM_SUPPORTER_FROM_LIST_OID = "supporterFromListOid";
    public static final String PARAM_ROOMS = "rooms";
    public static final String PARAM_IN_CALLER_LIST = "inCallerList";
    public static final String PARAM_IN_ACTIVIST_LIST = "inActivistList";
    public static final String PARAM_IS_MANAGER = "isManager";
    public static final String PARAM_UPCOMING_CALLER = "upcomingCaller";
    public static final String PARAM_SUPPORTERS_LIST = "supportersList";
    public static final String PARAM_BALLOT_BOX_BLOCK_OID = "ballotBoxBlockOid";
    public static final String PARAM_CUSTOM_PROPERTY_OID = "customPropertyOid";
    public static final String PARAM_FAMILY = "family";
    public static final String PARAM_FAMILY_MEMBER_OID = "familyMemberOid";
    public static final String PARAM_FAMILY_MEMBERS = "familyMembers";
    public static final String PARAM_CLOSED = "closed";
    public static final String PARAM_BILLBOARD = "billboard";
    public static final String PARAM_ELECTION_ASSEMBLY = "electionAssembly";
    public static final String PARAM_VOLUNTEER = "volunteer";
    public static final String PARAM_CAR_STICKER = "carSticker";
    public static final String PARAM_ELECTION_ASSEMBLY_PARTICIPATION = "electionAssemblyParticipation";
    public static final String PARAM_TASKS_STATUSES = "tasksStatuses";
    public static final String PARAM_ACCESS_ACTIVISTS = "accessActivists";
    public static final String PARAM_POTENTIAL_FAMILY_MEMBERS = "potentialFamilyMembers";
    public static final String PARAM_NOTIFICATIONS = "notifications";
    public static final String PARAM_NOTIFICATION = "notification";
    public static final String PARAM_TOTAL_RECIPIENTS = "totalRecipients";
    public static final String PARAM_NOTIFICATION_OID = "notificationOid";
    public static final String PARAM_AUTO_VERIFY_SUPPORTERS = "autoVerifySupporters";
    public static final String PARAM_ACTIVIST_STATS = "activistStats";
    public static final String PARAM_SHOW_ACTIVISTS_RANK = "showActivistsRank";
    public static final String PARAM_SUPPORT_STATUS_CHANGES = "supportStatusChanges";
    public static final String PARAM_NEEDS_RIDE = "needsRide";
    public static final String PARAM_ACTIVISTS_VOTING_RATE = "activistsVotingRate";
    public static final String PARAM_UNIQUE_SUPPORTERS_COUNT = "uniqueSupportersCount";
    public static final String PARAM_OWN_SUPPORTERS_COUNT = "ownSupportersCount";
    public static final String PARAM_HEAD_ACTIVISTS = "headActivists";
    public static final String PARAM_USER_TYPE_CODE = "userTypeCode";
    public static final String PARAM_ELECTION_DAY_WORKERS = "electionDayWorkers";
    public static final String PARAM_WORKS_ON_ELECTION_DAY = "workOnElectionDay";
    public static final String PARAM_ELECTION_DAY_MORNING_SHIFT = "electionDayMorningShift";
    public static final String PARAM_ELECTION_DAY_EVENING_SHIFT = "electionDayEveningShift";
    public static final String PARAM_CHECK_INS_MAP = "checkInsMap";
    public static final String PARAM_SUPPORTING_COUNT = "supportingCount";
    public static final String PARAM_NOT_SUPPORTING_COUNT = "notSupportingCount";
    public static final String PARAM_UNKNOWN_SUPPORT_COUNT = "unknownSupportCount";
    public static final String PARAM_TRANSLATIONS = "translations";
    public static final String PARAM_BLOCKING_STATUS = "blockingStatus";
    public static final String PARAM_HAS = "has";
    public static final String PARAM_REQUESTS_COUNT = "requestsCount";
    public static final String PARAM_RECENT_ACTIVISTS_VOTERS_MAPPINGS = "recentActivistsVotersMappings";
    public static final String PARAM_RECENT_CALLERS = "recentCallers";
    public static final String PARAM_ACTIVISTS_REGISTRATION_TOKENS_PERCENT = "activistsRegistrationTokensPercent";
    public static final String PARAM_RECENT_REQUESTS_DATA = "recentRequestsData";
    public static final String PARAM_JOBS_SUMMARIES = "jobsSummaries";
    public static final String PARAM_RECENT_ELECTION_DAY_CALLERS = "recentElectionDayCallers";
    public static final String PARAM_RECENT_ELECTION_DAY_CALLS = "recentElectionDayCalls";
    public static final String PARAM_RECENT_VOTE_STATUS_CHANGES = "recentVoteStatusChanges";
    public static final String PARAM_TOTAL_CHECKED_IN_OBSERVERS = "totalCheckedInObservers";
    public static final String PARAM_TOTAL_SUPER_OBSERVERS = "totalSuperObservers";


    public static final String MINUS = "-";

    public static final int DATA_TABLE_RENDER_REGULAR = 0;
    public static final String EMAIL_REPLY_TO = "info@elector.co.il";

    public static final String PARAM_ADMIN_USER_OID = "adminUserOid";
    public static final String PARAM_CAMPAIGN_OID = "campaignOid";
    public static final String PARAM_NUMBER = "number";
    public static final String PARAM_BALLOT_BOXES = "ballotBoxes";
    public static final String PARAM_CALLER_OID = "callerOid";
    public static final String PARAM_ELECTION_TIME = "electionTime";
    public static final String PARAM_ELECTION_OPEN = "electionOpen";
    public static final String PARAM_COORDINATES = "coordinates";
    public static final String PARAM_ELECTION_END_TIME = "electionEndTime";
    public static final String PARAM_CITIES = "cities";
    public static final String PARAM_SYMBOL = "symbol";
    public static final String PARAM_LAST_OID = "lastOid";
    public static final String PARAM_PLACE = "place";
    public static final String PARAM_BALLOT_BOXES_STRING = "ballotBoxesString";
    public static final String PARAM_BLOCK_OID = "blockOid";
    public static final String PARAM_SIMULATION = "simulation";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_CALL_SCENARIO = "callScenario";
    public static final String PARAM_MESSAGES_PAGE = "messagesPage";
    public static final String PARAM_CAMPAIGN_MANAGER_NAME = "campaignManagerName";
    public static final String PARAM_ACCESS_TASKS = "accessTasks";
    public static final String PARAM_SMS = "sms";
    public static final String PARAM_CONTACT_TYPE = "contactType";
    public static final String PARAM_DATABASE_CONNECTION = "databaseConnection";

    public static final int PARAM_ADMIN_USER_TYPE_SUPER = 1;
    public static final int PARAM_ADMIN_USER_TYPE_CANDIDATE = 2;
    public static final int PARAM_ADMIN_USER_TYPE_ACTIVIST = 3;
    public static final int PARAM_ADMIN_USER_TYPE_CALLER = 4;
    public static final int PARAM_ADMIN_USER_TYPE_OBSERVER = 5;
    public static final int PARAM_ADMIN_USER_TYPE_DRIVER = 6;

    public static final int PARAM_TABLE_SUPPORTERS = 0;
    public static final int PARAM_TABLE_ACTIVISTS = 1;


    public static final int PARAM_SUPPORT_STATUS_ALL = -1;
    public static final int PARAM_SUPPORT_STATUS_UNKNOWN = 0;
    public static final int PARAM_SUPPORT_STATUS_SUPPORTING = 1;
    public static final int PARAM_SUPPORT_STATUS_NOT_SUPPORTING = 2;
    public static final int PARAM_SUPPORT_STATUS_UNDECIDED = 3;
    public static final int PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING = 4;

    public static final int PARAM_MESSAGE_TYPE_EMAIL = 1;
    public static final int PARAM_MESSAGE_TYPE_SMS = 2;
    public static final int PARAM_MESSAGE_TYPE_NOTIFICATION = 3;
    public static final int PARAM_MESSAGE_TYPE_SYSTEM = 4;

    public static final int PARAM_CALL_UNANSWERED_REASON_UNANSWERED = 0;
    public static final int PARAM_CALL_UNANSWERED_REASON_PHONE_OFF = 1;
    public static final int PARAM_CALL_UNANSWERED_REASON_LINE_NOT_CONNECTED = 2;
    public static final int PARAM_CALL_UNANSWERED_REASON_CALL_REJECTED = 3;
    public static final int PARAM_CALL_UNANSWERED_REASON_OTHER = 4;

    public static final String URL_SMS_SERVICE = "https://sms.deals/ws.php?service=send_sms";
    public static final String UTF_8 = "UTF-8";
    public static final String HTTP_REQUEST_GET = "GET";
    public static final String HTTP_REQUEST_POST = "POST";
    public static final String SERVER_SMS_SENDER_NUMBER = "0012";

    public static final int RECIPIENT_TYPE_SUPPORTER = 1;
    public static final int RECIPIENT_TYPE_VOTER = 2;
    public static final int RECIPIENT_TYPE_ACTIVIST = 3;
    public static final int RECIPIENT_TYPE_CALLER = 4;
    public static final int RECIPIENT_TYPE_OBSERVER = 5;
    public static final int RECIPIENT_TYPE_DRIVER = 6;

    public static final int DEFAULT_TOTAL_SMS_FOR_ADMIN_USER = 50;

    public static final int MAX_BARS_PER_ADMIN_CHART = 4;
    public static final int MAX_ACTIVISTS_DATA_MAIN_PAGE = 5;
    public static final int MAX_ALERTS_MAIN_PAGE = 3;

    public static final int SUPPORTER_ACTION_TYPE_UNKNOWN = -1;
    public static final int SUPPORTER_ACTION_TYPE_ALL = 0;
    public static final int SUPPORTER_ACTION_TYPE_BILLBOARD = 1;
    public static final int SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY = 2;
    public static final int SUPPORTER_ACTION_TYPE_VOLUNTEER = 3;
    public static final int SUPPORTER_ACTION_TYPE_SUPPORTERS_LIST = 4;
    public static final int SUPPORTER_ACTION_TYPE_CAR_STICKER = 5;
    public static final int SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY_PARTICIPATION = 6;

    public static final int EVENT_ELECTION_ASSEMBLY = 1;
    public static final int EVENT_SUPPORTER_BIRTHDAY = 2;


    public static final int PARAM_GENDER_UNKNOWN = 0;
    public static final int PARAM_GENDER_MALE = 1;
    public static final int PARAM_GENDER_FEMALE = 2;


    public static final int PARAM_CANDIDATE_MESSAGE_TYPE_GENERAL = 0;
    public static final int PARAM_CANDIDATE_MESSAGE_TYPE_DUPLICATE_DATA = 1;
    public static final int PARAM_CANDIDATE_MESSAGE_TYPE_ACTIVIST_SUPPORTER_COMMENT = 2;


    public static final int PARAM_SUPPORT_SIGN_NONE = 0;
    public static final int PARAM_SUPPORT_SIGN_CANDIDATE = 1;
    public static final int PARAM_SUPPORT_SIGN_RIVAL = 2;

    public static final int PARAM_MARKER_TYPE_NONE = 0;
    public static final int PARAM_MARKER_TYPE_SUPPORTER = 1;
    public static final int PARAM_MARKER_TYPE_SUPPORT_SIGN = 2;

    public static final int PARAM_ERROR_EXCEEDED_MAX_USERS = 1;
    public static final int PARAM_ERROR_PHONE_NUMBER_EXISTS = 2;
    public static final int PARAM_ERROR_ID_EXISTS = 3;
    public static final int PARAM_ERROR_NO_SUCH_PHONE = 4;
    public static final int PARAM_ERROR_NO_SUCH_CITY = 5;
    public static final int PARAM_ERROR_USER_NOT_ACTIVE = 6;
    public static final int PARAM_ERROR_CANNOT_LOAD_USER = 7;
    public static final int PARAM_ERROR_PHONE_WAS_NOT_FOUND_IN_MAP = 8;
    public static final int PARAM_ERROR_EXPIRATION_DAY_PASSED = 9;
    public static final int PARAM_ERROR_GENERAL = 10;
    public static final int PARAM_ERROR_ACTIVIST_NOT_DELETED = 11;
    public static final int PARAM_ERROR_USER_NOT_DELETED = 12;
    public static final int PARAM_ERROR_NOT_ENOUGH_SMS = 13;
    public static final int PARAM_ERROR_NOT_ALLOWED_ACCESS_TO_ADMIN_USER_DATA = 14;
    public static final int PARAM_ERROR_NOT_USER_OWNER = 15;
    public static final int PARAM_ERROR_NO_SUCH_ACTIVIST = 16;
    public static final int PARAM_ERROR_BLOCK_ACTIVISTS_CONFIG = 17;
    public static final int PARAM_ERROR_CANNOT_LOAD_MASTER = 18;
    public static final int PARAM_ERROR_NOT_USER_OWNER_OR_NOT_ALLOWED_TO_ACCESS_ADMIN_USER_DATA = 19;
    public static final int PARAM_ERROR_NO_SUCH_CAMPAIGN = 20;
    public static final int PARAM_ERROR_NOT_SUPER_ADMIN = 21;
    public static final int PARAM_ERROR_CONFIG_ENUM_IS_NULL = 22;
    public static final int PARAM_ERROR_NOT_PRIVATE_CODE_OR_NO_CONFIG_ENUM = 23;
    public static final int PARAM_ERROR_CUSTOM_PROPERTY_IS_NULL = 24;
    public static final int PARAM_ERROR_NO_SUCH_ADMIN_USER = 25;
    public static final int PARAM_ERROR_NO_SUCH_ADMIN_USER_OR_CAMPAIGN = 26;
    public static final int PARAM_ERROR_NO_SUCH_ENCRYPTION_KEY = 27;
    public static final int PARAM_ERROR_DATA_NOT_ENCRYPTED = 28;
    public static final int PARAM_ERROR_NO_SUCH_BALLOT_BOX_OR_BALLOT_BOX_BLOCK = 29;
    public static final int PARAM_ERROR_NO_SUCH_BALLOT_BOX = 30;
    public static final int PARAM_ERROR_NOT_SENT_BIRTHDAY_SMS = 31;
    public static final int PARAM_ERROR_MESSAGES_JSON_IS_NULL = 32;
    public static final int PARAM_ERROR_NO_SUCH_CALLER = 33;
    public static final int PARAM_ERROR_VOTER_NO_SUCH_VOTER = 34;
    public static final int PARAM_ERROR_VOTER_JSON_IS_NULL = 35;
    public static final int PARAM_ERROR_ELECTION_DAY_NOT_ALLOWED = 36;
    public static final int PARAM_ERROR_CALLS_JSON_IS_NULL = 37;
    public static final int PARAM_ERROR_ACTIVIST_VOTING_RATES_IS_EMPTY = 38;
    public static final int PARAM_ERROR_NO_SUCH_DONATION = 39;
    public static final int PARAM_ERROR_WRONG_FILE_TYPE = 40;
    public static final int PARAM_ERROR_NO_SUCH_DRIVER = 41;
    public static final int PARAM_ERROR_DRIVE_DONE_OR_DELETED_OR_HANDLED = 42;
    public static final int PARAM_ERROR_NO_SUCH_DRIVE = 43;
    public static final int PARAM_ERROR_DATE_BEFORE_CURRENT_TIME = 44;
    public static final int PARAM_ERROR_DRIVE_DONE_OR_DELETED = 45;
    public static final int PARAM_ERROR_MESSAGE_NO_TITLE_OR_BODY = 46;
    public static final int PARAM_ERROR_CANNOT_LOGIN_AS_MASTER_AND_SLAVE_USER = 47;
    public static final int PARAM_ERROR_NO_SUCH_NOTIFICATION = 48;
    public static final int PARAM_ERROR_REGISTRATION_TOKEN_IS_EMPTY = 49;
    public static final int PARAM_ERROR_NOTIFICATION_NO_TITLE_OR_BODY = 50;
    public static final int PARAM_ERROR_NO_SUCH_OBSERVER_OR_BALLOT_BOX = 51;
    public static final int PARAM_ERROR_NO_SUCH_OBSERVER = 52;
    public static final int PARAM_ERROR_NO_SUCH_OBSERVER_OR_ELECTION_NOT_ALLOWED = 53;
    public static final int PARAM_ERROR_NO_SUCH_VOTER = 54;
    public static final int PARAM_ERROR_NO_SUCH_OBSERVER_OR_ELECTION_NOT_ALLOWED_OR_NOT_SUPER_OBSERVER = 55;
    public static final int PARAM_ERROR_NO_SUCH_OBSERVER_OR_IS_SUPER_OBSERVER = 56;
    public static final int PARAM_ERROR_OBSERVERS_JSON_IS_NULL = 57;
    public static final int PARAM_ERROR_SMS_CONTENT_EXCEEDS_MAX_CHARS = 58;
    public static final int PARAM_ERROR_NO_SUCH_SENT_SMS = 59;
    public static final int PARAM_ERROR_NO_AUTHORIZATION = 60;
    public static final int PARAM_ERROR_SUPPORTERS_JSON_IS_NULL = 61;
    public static final int PARAM_ERROR_COORDINATES_OBJECT_IS_NULL = 62;
    public static final int PARAM_ERROR_NEW_COORDINATES_OBJECT_IS_NULL = 63;
    public static final int PARAM_ERROR_SUPPORTER_ACTION_OBJECT_IS_NULL = 64;
    public static final int PARAM_ERROR_VOTERS_JSON_IS_NULL = 65;
    public static final int PARAM_ERROR_IP_IS_BLOCKED = 66;
    public static final int PARAM_ERROR_NO_SUCH_VOTER_ID = 67;
    public static final int PARAM_ERROR_NO_SUCH_VOTER_ID_OR_OLD_ID_NOT_EQUAL_NEW_ID = 68;
    public static final int PARAM_ERROR_NO_SUCH_ADMIN_OID = 69;
    public static final int PARAM_ERROR_NO_VOTER_ID_OR_FIRST_NAME_AND_LAST_NAME = 70;
    public static final int PARAM_ERROR_CALLERS_JSON_IS_NULL = 71;
    public static final int PARAM_ERROR_COULD_NOT_FETCH_VOTER_FROM_SERVICE = 72;
    public static final int PARAM_ERROR_FILE_NOT_FOUND = 73;



    public static final int FILE_TYPE_IMAGE = 1;
    public static final int FILE_TYPE_SPREADSHEET = 2;
    public static final int FILE_TYPE_TEXT = 3;

    public static final int GROUP_TYPE_NONE = -1;
    public static final int SPECIFIC_NUMBER = 0;
    public static final int GROUP_TYPE_ACTIVISTS = 3;
    public static final int GROUP_TYPE_CALLERS = 4;
    public static final int GROUP_TYPE_OBSERVERS = 5;
    public static final int GROUP_TYPE_VOTERS = 6;
    public static final int GROUP_TYPE_SUPPORTERS = 7;
    public static final int GROUP_TYPE_DRIVERS = 8;
    public static final int BY_SUPPORT_STATUS = 9;
    public static final int GROUP_TYPE_UNVERIFIED_SUPPORTERS = 10;
    public static final int GROUP_TYPE_UNKNOWN_SUPPORT_STATUS = 11;
    public static final int GROUP_TYPE_NOT_SUPPORTING = 12;

    public static final List<Integer> GROUPS_TYPES = Arrays.asList(
            GROUP_TYPE_ACTIVISTS, GROUP_TYPE_CALLERS, GROUP_TYPE_OBSERVERS, GROUP_TYPE_VOTERS, GROUP_TYPE_SUPPORTERS,
            GROUP_TYPE_DRIVERS, GROUP_TYPE_UNVERIFIED_SUPPORTERS, GROUP_TYPE_UNKNOWN_SUPPORT_STATUS, GROUP_TYPE_NOT_SUPPORTING
    );

    public static final int NO_CHANGE = 0;
    public static final int ADD_TO_SUPPORTERS_GROUP = 1;
    public static final int REMOVE_FROM_SUPPORTERS_GROUP = 2;

    public static final int SECOND = 1000;
    public static final int MINUTE = SECOND * 60;
    public static final int HOUR = MINUTE * 60;

    public static final int ENTITY_VOTER = 1;
    public static final int ENTITY_ACTIVIST = 2;
    public static final int ENTITY_CALLER = 3;
    public static final int ENTITY_OBSERVER = 4;
    public static final int ENTITY_DRIVER = 5;
    public static final int ENTITY_DONATION = 6;
    public static final int ENTITY_ADMIN = 7;
    public static final int ENTITY_BLOCK = 8;

    public static final int SUPER_ADMIN_OID = 69;

    public static final int PARAM_DEFAULT_ZOOM_GOOGLE_MAPS = 14;

    public static final int PARAM_MAX_CANDIDATE_DASHBOARD_MESSAGES = 5;

    public static final String TECHNICAL_ISSUES_EMAIL = "shaigivati464@gmail.com";

    public static final int MAX_VOTERS_TO_LOAD = 500000;
    public static final int MAX_ENTITIES_TO_SAVE = 1000;
    public static final int MAX_SMS_HISTORY_TO_FETCH = 1000;
    public static final int MAX_SIZE_OF_CONTACTS_QUEUE_TO_SYNC = 5000;
    public static final int MAX_TASKS_IN_DASHBOARD = 10;


    public static final int SUPPORTERS_LIST_STATUS_NOT_ASKED_YET = 0;
    public static final int SUPPORTERS_LIST_STATUS_NO = 1;
    public static final int SUPPORTERS_LIST_STATUS_WANTS_TO_GIVE = 2;
    public static final int SUPPORTERS_LIST_STATUS_GAVE = 3;
    public static final int SUPPORTERS_LIST_STATUS_WANTS_TO_GIVE_MORE = 4;

    public static final int VOTERS_BOOK_VOTER_ID = 0;
    public static final int VOTERS_BOOK_LAST_NAME = 1;
    public static final int VOTERS_BOOK_FIRST_NAME = 2;
    public static final int VOTERS_BOOK_FATHER_NAME = 3;
    public static final int VOTERS_BOOK_CITY_SYMBOL = 4;
    public static final int VOTERS_BOOK_BALLOT_BOX_NUMBER = 5;
    public static final int VOTERS_BOOK_ZIP_CODE_OLD = 6;
    public static final int VOTERS_BOOK_CITY_SYMBOL_2 = 7;
    public static final int VOTERS_BOOK_CITY_NAME = 8;
    public static final int VOTERS_BOOK_EMPTY_1 = 9;
    public static final int VOTERS_BOOK_STREET_CODE = 10;
    public static final int VOTERS_BOOK_STREET = 11;
    public static final int VOTERS_BOOK_HOUSE_NUMBER = 12;
    public static final int VOTERS_BOOK_HOUSE_ENTRY = 13;
    public static final int VOTERS_BOOK_APARTMENT_NUMBER = 14;
    public static final int VOTERS_BOOK_LETTER = 15;
    public static final int VOTERS_BOOK_VOTER_NUMBER = 16;
    public static final int VOTERS_BOOK_EMPTY_2 = 17;
    public static final int VOTERS_BOOK_EMPTY_3 = 18;
    public static final int VOTERS_BOOK_ZIP_CODE_NEW = 19;
    public static final int VOTERS_BOOK_EMPTY_4 = 20;


    public static final int BALLOT_BOXES_DATA_CITY_NAME = 0;
    public static final int BALLOT_BOXES_DATA_CITY_SYMBOL = 1;
    public static final int BALLOT_BOXES_DATA_NUMBER = 1;
    public static final int BALLOT_BOXES_DATA_PLACE = 3;
    public static final int BALLOT_BOXES_DATA_ADDRESS = 2;
    public static final int BALLOT_BOXES_DATA_STREET = 5;
    public static final int BALLOT_BOXES_DATA_TYPE = 6;
    public static final int BALLOT_BOXES_DATA_FROM_HOUSE = 7;
    public static final int BALLOT_BOXES_DATA_TO_HOUSE = 8;
    public static final int BALLOT_BOXES_DATA_FROM_LETTER = 9;
    public static final int BALLOT_BOXES_DATA_TO_LETTER = 10;
    public static final int BALLOT_BOXES_DATA_DISABLED_ACCESS = 4;
    public static final int BALLOT_BOXES_DATA_DISABLES_VOTING_ARRANGEMENTS = 5;
    public static final int BALLOT_BOXES_DATA_DISTRICT = 6;


    public static final int REGIONAL_BALLOT_BOXES_DATA_REGION_NAME = 0;
    public static final int REGIONAL_BALLOT_BOXES_DATA_CITY_NAME = 1;
    public static final int REGIONAL_BALLOT_BOXES_DATA_NUMBER = 2;
    public static final int REGIONAL_BALLOT_BOXES_DATA_ADDRESS = 3;
    public static final int REGIONAL_BALLOT_BOXES_DATA_PLACE = 4;
    public static final int REGIONAL_BALLOT_BOXES_DATA_DISABLED_ACCESS = 5;
    public static final int REGIONAL_BALLOT_BOXES_DATA_DISTRICT = 6;


    public static final int BALLOT_BOX_TYPE_ALL = 1;
    public static final int BALLOT_BOX_TYPE_CONTINUOUS = 2;
    public static final int BALLOT_BOX_TYPE_EVEN = 3;
    public static final int BALLOT_BOX_TYPE_ODD = 4;
    public static final int BALLOT_BOX_TYPE_ONE_LAST_NAME = 5;
    public static final int BALLOT_BOX_TYPE_MINUS = 5;
    public static final int BALLOT_BOX_TWO_LAST_NAME = 6;
    public static final int BALLOT_BOX_ONE_FIRST_ONE_LAST = 7;
    public static final int BALLOT_BOX_TWO_FIRST_TWO_LAST = 8;

    public static final int KB = 1024;
    public static final int MB = 1024 * KB;
    public static final int GB = 1024 * MB;

    public static final int CUSTOM_PROPERTY_TYPE_TEXT = 0;
    public static final int CUSTOM_PROPERTY_TYPE_SELECT = 1;
    public static final int CUSTOM_PROPERTY_TYPE_CHECKBOX = 2;

    public static final int PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING_AND_UNKNOWN_SUPPORT = 2;

    public static final int FAMILY_MEMBER_RELATION_OTHER = 0;
    public static final int FAMILY_MEMBER_RELATION_PARENT = 1;
    public static final int FAMILY_MEMBER_RELATION_SIBLING = 2;
    public static final int FAMILY_MEMBER_RELATION_CHILD = 3;

    public static final int EXCEL_ROW_VOTER_ID = 0;
    public static final int EXCEL_ROW_VOTER_SUPPORT_STATUS = 1;
    public static final int EXCEL_ROW_VOTER_BIRTH_DATE = 2;
    public static final int EXCEL_ROW_VOTER_GENDER = 3;
    public static final int EXCEL_ROW_VOTER_CELLULAR_PHONE = 4;
    public static final int EXCEL_ROW_VOTER_HOME_PHONE = 5;
    public static final int EXCEL_ROW_VOTER_EXTRA_PHONE = 6;
    public static final int EXCEL_ROW_VOTER_EMAIL = 7;

    public static final int PARAM_MAX_REQUESTS_PER_IP_FOR_MINUTE = 20;

    public static final boolean PARAM_INCREMENT_ACTIVISTS_COUNT = true;
    public static final boolean PARAM_DECREMENT_ACTIVISTS_COUNT = false;

    public static final int PARAM_MAX_ACTIVISTS_STATS = 20;
    public static final int PARAM_MAX_CHARS_IN_SMS = 133;

    public static final int PARAM_TASK_STATUS_ALL = 0;
    public static final int PARAM_TASK_STATUS_DONE = 1;
    public static final int PARAM_TASK_STATUS_PENDING = 2;
    public static final int PARAM_TASK_STATUS_NONE = 3;

    public static final int PARAM_LANGUAGE_UNDEFINED = 0;
    public static final int PARAM_LANGUAGE_HEBREW = 1;
    public static final int PARAM_LANGUAGE_RUSSIAN = 2;
    public static final int PARAM_LANGUAGE_AMHARIC = 3;
    public static final int PARAM_LANGUAGE_ENGLISH = 4;
    public static final int PARAM_LANGUAGE_FRENCH = 5;
    public static final int PARAM_LANGUAGE_SPANISH = 6;
    public static final int PARAM_LANGUAGE_ARAB = 7;
    public static final int PARAM_LANGUAGE_OTHER = 8;

    public static final int PARAM_REPORT_TYPE_SUPPORTERS_WITH_NO_ACTIVISTS = 1;
    public static final int PARAM_REPORT_TYPE_CALLS = 2;
    public static final int PARAM_REPORT_TYPE_NOT_YET_VOTED = 3;
    public static final int PARAM_REPORT_TYPE_NOT_YET_VOTED_BY_ACTIVIST = 4;
    public static final int PARAM_REPORT_TYPE_NOT_YET_VOTED_NO_ACTIVIST = 5;
    public static final int PARAM_REPORT_TYPE_ACTIVISTS = 6;
    public static final int PARAM_REPORT_TYPE_CALLERS = 7;
    public static final int PARAM_REPORT_TYPE_VOTERS_POLING_STATIONS = 8;
    public static final int PARAM_REPORT_TYPE_CALLS_MESSAGES = 9;
    public static final int PARAM_REPORT_TYPE_VOTERS_BY_HOUSE_NUMBER = 10;
    public static final int PARAM_REPORT_TYPE_SUPPORTERS = 11;
    public static final int PARAM_REPORT_TYPE_VOTERS = 12;
    public static final int PARAM_REPORT_TYPE_TASKS = 13;
    public static final int PARAM_REPORT_TYPE_ACTIVISTS_VOTERS = 14;
    public static final int PARAM_REPORT_TYPE_BALLOT_BOXES = 15;
    public static final int PARAM_ACTIVIST_ATTACHED_SUPPORTERS = 16;
    public static final int PARAM_ALL_ACTIVISTS_ATTACHED_SUPPORTERS = 17;
    public static final int PARAM_REPORT_TYPE_VOTERS_BY_CITY = 18;
    public static final int PARAM_REPORT_TYPE_SUPPORT_STATUS_COUNT_CHANGES = 19;
    public static final int PARAM_REPORT_TYPE_VOTERS_ADVANCED_SEARCH = 20;
    public static final int PARAM_REPORT_TYPE_NEEDS_RIDE = 21;
    public static final int PARAM_REPORT_TYPE_NEEDS_RIDE_BY_BLOCKS = 22;
    public static final int PARAM_REPORT_TYPE_NEEDS_RIDE_BY_BALLOT_BOXES = 23;
    public static final int PARAM_REPORT_TYPE_NOT_YET_VOTED_VOTERS = 24;
    public static final int PARAM_REPORT_TYPE_HEAD_ACTIVISTS = 25;
    public static final int PARAM_REPORT_TYPE_HEAD_ACTIVISTS_WITH_SUPPORTERS = 26;

    public static final int PARAM_NEED_RIDE_STATUS_UNKNOWN = 0;
    public static final int PARAM_NEED_RIDE_STATUS_YES = 1;
    public static final int PARAM_NEED_RIDE_STATUS_NO = 2;

    public static final int PARAM_OBSERVER_LOG_TYPE_VOTING_STATUS_CHANGE_UNVOTED = 0;
    public static final int PARAM_OBSERVER_LOG_TYPE_VOTING_STATUS_CHANGE_VOTED = 1;
    public static final int PARAM_OBSERVER_LOG_TYPE_CHECK_IN = 2;
    public static final int PARAM_OBSERVER_LOG_TYPE_CHECK_OUT = 3;
    public static final int PARAM_OBSERVER_SENT_MESSAGE = 4;

    public static final int PARAM_VOTE_STATUS_NOT_YET_VOTED = -1;
    public static final int PARAM_VOTE_STATUS_UNKNOWN = 0;
    public static final int PARAM_VOTE_STATUS_VOTED_FOR = 1;
    public static final int PARAM_VOTE_STATUS_VOTED_AGAINST = 2;
    public static final int PARAM_VOTE_STATUS_WONT_VOTE = 3;

    public static final int SIMULATION_CHECK_LIST_CALLER_PARTICIPATED = 1;
    public static final int SIMULATION_CHECK_LIST_ACTIVIST_PARTICIPATED = 2;
    public static final int SIMULATION_CHECK_LIST_OBSERVER_PARTICIPATED = 3;
    public static final int SIMULATION_CHECK_LIST_DRIVER_PARTICIPATED = 4;
    public static final int SIMULATION_CHECK_LIST_SENT_ELECTION_DAY_ALERT = 5;

    public static final int SERVICE_ELECTOR = 0;
    public static final int SERVICE_VOTERS = 1;
    public static final int SERVICE_ELECTION_DAY = 2;
    public static final int SERVICE_SMS = 3;
    public static final int SERVICE_JOBS = 4;

    public static final Map<String, String> JOBS_TO_ENUMS_MAP = new HashMap<String, String>() {{
        put("NotificationsJob", ConfigEnum.active_job_notifications.toString());
        put("DataDailyJob", ConfigEnum.active_job_data_daily.toString());
        put("CountCampaignClientsJob", ConfigEnum.active_job_count_campaign_clients.toString());
        put("ManageBirthdaysJob", ConfigEnum.active_job_manage_birthdays.toString());
        put("DrivesJob", ConfigEnum.active_job_drives.toString());
        put("GoogleMapsJob", ConfigEnum.active_job_google_maps.toString());
        put("ShareObserversDataJob", ConfigEnum.active_job_share_observers_data.toString());
        put("SaveRequestsDataJob", ConfigEnum.active_job_save_requests_data.toString());
        put("VotersManipulationsJobs", ConfigEnum.active_job_voters_manipulations.toString());
        put("ManageBallotBoxesJob", ConfigEnum.active_job_manage_ballot_boxes.toString());
        put("VotingDataJob", ConfigEnum.active_job_vote_status.toString());

    }};


    public static final int PARAM_CAMPAIGN_TYPE_MUNICIPAL = 1;
    public static final int PARAM_CAMPAIGN_TYPE_PRIMARY = 2;
    public static final int PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LABOR = 3;
    public static final int PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LIKUD = 4;
    public static final int PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION = 5;

    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_VOTER_ID = 0;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_FIRST_NAME = 1;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_LAST_NAME = 2;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_PHONE = 3;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_OFFICE_CITY = 4;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_OFFICE_STREET = 5;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_OFFICE_HOUSE_NUMBER = 6;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_OFFICE_ZIP_CODE = 7;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_PARTY_DISTRICT = 8;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_PARTY_BRANCH = 9;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_ACTIVITY_DISTRICT = 11;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_ACTIVITY_BRANCH = 12;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_BIRTH_DATE = 13;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_CITY = 14;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_STREET = 15;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_HOUSE_NUMBER = 16;
    public static final int ISRAEL_LABOR_PARTY_VOTERS_BOOK_ZIP_CODE = 17;

    public static final int LIKUD_PARTY_VOTERS_BOOK_VOTER_ID = 0;
    public static final int LIKUD_PARTY_VOTERS_BOOK_LAST_NAME = 1;
    public static final int LIKUD_PARTY_VOTERS_BOOK_FIRST_NAME = 2;
    public static final int LIKUD_PARTY_VOTERS_BOOK_CITY = 3;
    public static final int LIKUD_PARTY_VOTERS_BOOK_STREET = 4;
    public static final int LIKUD_PARTY_VOTERS_BOOK_HOUSE_NUMBER = 5;
    public static final int LIKUD_PARTY_VOTERS_BOOK_ZIP_CODE = 6;
    public static final int LIKUD_PARTY_VOTERS_BOOK_COLLECTION_CODE = 7;
    public static final int LIKUD_PARTY_VOTERS_BOOK_COLLECTION_DESCRIPTION = 8;
    public static final int LIKUD_PARTY_VOTERS_BOOK_PHONE = 9;
    public static final int LIKUD_PARTY_VOTERS_BOOK_HOME_PHONE = 10;
    public static final int LIKUD_PARTY_VOTERS_BOOK_JOIN_DATE = 11;
    public static final int LIKUD_PARTY_VOTERS_BOOK_BIRTH_COUNTRY = 12;
    public static final int LIKUD_PARTY_VOTERS_BOOK_BRANCH = 13;
    public static final int LIKUD_PARTY_VOTERS_BOOK_PRIMARY_AREA = 14;
    public static final int LIKUD_PARTY_VOTERS_BOOK_MALE = 15;

    public static final String PARAM_OLD_PHONE = "oldPhone";
    public static final String PARAM_PASSWORD_CHANGED = "passwordChanged";
    public static final String PARAM_SMS_LEFT_TO_SELL = "smsLeftToSell";
    public static final String PARAM_SMS_PURCHASED = "smsPurchased";
    public static final String PARAM_SMS_BALANCE = "smsBalance";
    public static final String PARAM_SUCCESS = "success";

    public static final int SCREEN_CAMPAIGN_MANAGER_DASHBOARD = 1;
    public static final int SCREEN_CAMPAIGN_MANAGER_VOTERS_SEARCH = 2;
    public static final int SCREEN_CAMPAIGN_MANAGER_ALL_VOTERS = 3;
    public static final int SCREEN_CAMPAIGN_MANAGER_APPROVED_SUPPORTERS = 4;
    public static final int SCREEN_CAMPAIGN_MANAGER_UNVERIFIED_SUPPORTERS = 5;
    public static final int SCREEN_CAMPAIGN_MANAGER_UNKNOWN_SUPPORT_VOTERS = 6;
    public static final int SCREEN_CAMPAIGN_MANAGER_NOT_SUPPORTING_VOTERS = 7;
    public static final int SCREEN_CAMPAIGN_MANAGER_VOTERS_GROUPS = 8;
    public static final int SCREEN_CAMPAIGN_MANAGER_PUBLIC_INQUIRIES = 9;
    public static final int SCREEN_CAMPAIGN_MANAGER_ACTIVISTS = 10;
    public static final int SCREEN_CAMPAIGN_MANAGER_HEAD_ACTIVISTS = 11;
    public static final int SCREEN_CAMPAIGN_MANAGER_CALLERS = 12;
    public static final int SCREEN_CAMPAIGN_MANAGER_TASKS = 13;
    public static final int SCREEN_CAMPAIGN_MANAGER_SMS = 14;
    public static final int SCREEN_CAMPAIGN_MANAGER_SMS_SURVEYS = 15;
    public static final int SCREEN_CAMPAIGN_MANAGER_MESSAGES = 16;
    public static final int SCREEN_CAMPAIGN_MANAGER_REPORTS = 17;
    public static final int SCREEN_CAMPAIGN_MANAGER_BIRTHDAYS = 18;
    public static final int SCREEN_CAMPAIGN_MANAGER_SETTINGS = 19;
    public static final int SCREEN_CAMPAIGN_MANAGER_SIMULATION = 20;
    public static final int SCREEN_CAMPAIGN_MANAGER_ELECTION_DAY = 21;
    public static final int SCREEN_CAMPAIGN_MANAGER_VOTERS_THAT_CAN_VOTE = 22;
    public static final int SCREEN_CAMPAIGN_MANAGER_NOT_YET_VOTED_SUPPORTERS = 23;
    public static final int SCREEN_CAMPAIGN_MANAGER_VOTERS_NOT_IN_DB = 24;
    public static final int SCREEN_CAMPAIGN_MANAGER_SUPPORT_MAP = 25;
    public static final int SCREEN_CAMPAIGN_MANAGER_BALLOT_BOXES = 26;
    public static final int SCREEN_CAMPAIGN_MANAGER_NOTIFICATIONS = 27;
    public static final int SCREEN_CAMPAIGN_MANAGER_DONATIONS = 28;
    public static final int SCREEN_CAMPAIGN_MANAGER_OBSERVERS = 29;
    public static final int SCREEN_CAMPAIGN_MANAGER_DRIVERS = 30;
    public static final int SCREEN_CAMPAIGN_MANAGER_ELECTION_DAY_WORKERS = 31;

}
