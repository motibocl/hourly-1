package com.elector;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.*;
import com.elector.Utils.ConfigUtils;
import com.elector.Utils.Utils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.elector.Utils.Definitions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sigal on 5/20/2016.
 */
@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private SessionFactory sessionFactory;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public List<String> getDbClasses () {
        return new ArrayList<>(sessionFactory.getAllClassMetadata().keySet());
    }

    public void saveAll(List<? extends BaseEntity> objects) {
        for (BaseEntity object : objects) {
            save(object);
        }
    }

    public void save(Object o) {
        sessionFactory.getCurrentSession().saveOrUpdate(o);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return (T) getQuerySession().get(clazz, oid);
    }

    public <T> List<T> getList(Class<T> clazz) {
        return (List<T>) getQuerySession().createQuery(String.format("FROM %s WHERE deleted=FALSE", clazz.getSimpleName())).list();
    }

    public Object load(Class clazz, long id) {
        return getQuerySession().get(clazz, id);
    }

    public <T> List<T> loadList(Class<T> clazz) {
        return getQuerySession().createCriteria(clazz).list();
    }

    public <T> void delete(Class<T> clazz) {
        getQuerySession().createQuery(String.format("DELETE FROM %s", clazz.getSimpleName())).executeUpdate();
    }

    public <T> void setDeleted(Class<T> clazz, int oid) {
        getQuerySession().createQuery(String.format("UPDATE %s SET deleted=TRUE WHERE oid=:oid", clazz.getSimpleName())).setInteger(PARAM_OID, oid).executeUpdate();
    }


    public <T> void delete(Class<T> clazz, int oid) {
        getQuerySession().createQuery(String.format("DELETE FROM %s WHERE oid=%s", clazz.getSimpleName(), oid)).executeUpdate();
    }

    public <T> void delete(Class<T> clazz, List<Integer> oidsList) {
        getQuerySession().createQuery(String.format("DELETE FROM %s WHERE oid IN (:oidsList)", clazz.getSimpleName())).setParameterList(PARAM_OIDS_LIST, oidsList).executeUpdate();
    }


    public <T> void deleteByAdminOid(Class<T> clazz, int adminOid) {
        Query query = getQuerySession().createQuery(String.format("DELETE FROM %s WHERE adminUserObject.oid=:adminUserOid", clazz.getSimpleName()));
        query.setInteger(PARAM_ADMIN_USER_OID, adminOid);
        query.executeUpdate();
    }

    public Map<Integer, Integer> votersSupportStatusMap() {
        Map<Integer, Integer> votersSupportStatusMap = new HashMap<>();
        boolean error = false;
        List<Object[]> votersData = (getQuerySession().createSQLQuery("SELECT v.oid, v.support_status FROM voters v WHERE v.deleted=FALSE").list());
        for (Object[] data : votersData) {
            if (data.length == 2) {
                votersSupportStatusMap.put(Integer.valueOf(data[0].toString()), Integer.valueOf(data[1].toString()));
            } else {
                error = true;
            }
        }
        if (error) {
            LOGGER.warn("votersSupportStatusMap, some data was not retrieved");
        }
        return votersSupportStatusMap;
    }

    public void removeActivistVoterMapObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE ActivistVoterMapObject a SET deleted=TRUE WHERE a.voter.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeCallerVoterMapObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE CallerVoterMapObject c SET deleted=TRUE WHERE c.voterObject.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeVoterCustomGroupMappingObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE VoterCustomGroupMappingObject v SET deleted=TRUE WHERE v.voterObject.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeVoterElectionDayCallObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE VoterElectionDayCallObject v SET deleted=TRUE WHERE v.voterObject.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeDriveObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE DriveObject d SET deleted=TRUE WHERE d.voterObject.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeSupporterBirthdayObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE SupporterBirthdayObject s SET deleted=TRUE WHERE s.supporter.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public <T> List<T> getListByAdminOid(Class<T> clazz, int adminOid, Integer limit, boolean reverse) {
        Query query = getQuerySession().createQuery(
                String.format("FROM %s WHERE adminUserObject.oid=:adminUserOid AND deleted=FALSE %s",
                        clazz.getSimpleName(),
                        reverse ? " ORDER BY oid DESC" : EMPTY));
        query.setInteger(PARAM_ADMIN_USER_OID, adminOid);
        if (limit != null) {
            query.setMaxResults(limit);
        }
        return (List<T>) query.list();
    }

    public Map<Integer, Integer> calculateCandidateSupportStats(int adminOid) {
        Map<Integer, Integer> stats = new HashMap<>();
        Integer unknown = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM voters WHERE admin_user_oid=:adminUserOid AND support_status=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_UNKNOWN).setInteger(PARAM_ADMIN_USER_OID, adminOid).uniqueResult()).intValue();
        Integer supporting = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM voters WHERE admin_user_oid=:adminUserOid AND support_status=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_SUPPORTING).setInteger(PARAM_ADMIN_USER_OID, adminOid).uniqueResult()).intValue();
        Integer notSupporting = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM voters WHERE admin_user_oid=:adminUserOid AND support_status=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_NOT_SUPPORTING).setInteger(PARAM_ADMIN_USER_OID, adminOid).uniqueResult()).intValue();
        Integer unverifiedSupporting = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM voters WHERE admin_user_oid=:adminUserOid AND support_status=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING).setInteger(PARAM_ADMIN_USER_OID, adminOid).uniqueResult()).intValue();
        stats.put(PARAM_SUPPORT_STATUS_UNKNOWN, unknown);
        stats.put(PARAM_SUPPORT_STATUS_SUPPORTING, supporting);
        stats.put(PARAM_SUPPORT_STATUS_NOT_SUPPORTING, notSupporting);
        stats.put(PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING, unverifiedSupporting);
        return stats;
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>>  getActivistsDataMap (boolean today) {
        Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataMap = new LinkedHashMap<>();
        List<Object[]> rawData = (getQuerySession().createSQLQuery(
                String.format("SELECT " +
                        "  a.admin_user_oid, " +
                        "  activist_oid, " +
                        "  COUNT(activist_oid) " +
                        "FROM activists a " +
                        "INNER JOIN activists_voters_map avm ON a.oid = avm.activist_oid " +
                        "INNER JOIN admin_users au ON a.admin_user_oid = au.oid " +
                        " WHERE avm.deleted=FALSE AND au.deleted=FALSE AND au.active=TRUE %s" +
                        "GROUP BY activist_oid;", today ? " AND avm.date > TIMESTAMP(current_date) " : EMPTY)).list());
        for (Object[] data : rawData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int supportersCount = Integer.valueOf(data[2].toString());
            LinkedHashMap<Integer, Integer> activists = activistsDataMap.computeIfAbsent(adminOid, k -> new LinkedHashMap<>());
            activists.put(activistOid, supportersCount);
        }
        return activistsDataMap;
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>>  activistsDataFromYesterday () {
        Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataMap = new LinkedHashMap<>();
        List<Object[]> rawData = (getQuerySession().createSQLQuery("SELECT " +
                "  admin_user_oid, " +
                "  activist_oid, " +
                "  COUNT(activist_oid) " +
                "FROM activists_voters_map avm " +
                "WHERE DATE(date) = DATE(NOW() - INTERVAL 1 DAY) AND avm.deleted = FALSE " +
                "GROUP BY activist_oid;").list());
        for (Object[] data : rawData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int supportersCount = Integer.valueOf(data[2].toString());
            LinkedHashMap<Integer, Integer> activists = activistsDataMap.computeIfAbsent(adminOid, k -> new LinkedHashMap<>());
            activists.put(activistOid, supportersCount);
        }
        return activistsDataMap;
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>>  activistsSupportStats (boolean onlyYesterday) {
        Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataMap = new LinkedHashMap<>();
        List<Object[]> rawData = (getQuerySession().createSQLQuery(
                String.format(
                        "SELECT " +
                                "  a.admin_user_oid, " +
                                "  activist_oid, " +
                                "  COUNT(activist_oid) " +
                                "FROM activists_voters_map avm " +
                                "  INNER JOIN activists a ON avm.activist_oid = a.oid " +
                                "WHERE " +
                                " avm.deleted = FALSE " +
                                " AND a.deleted = FALSE " +
                                " %s " +
                                "GROUP BY activist_oid;", onlyYesterday ? " AND DATE(date) = DATE(NOW() - INTERVAL 1 DAY) " : EMPTY
                )).list());
        for (Object[] data : rawData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int supportersCount = Integer.valueOf(data[2].toString());
            LinkedHashMap<Integer, Integer> activists = activistsDataMap.computeIfAbsent(adminOid, k -> new LinkedHashMap<>());
            activists.put(activistOid, supportersCount);
        }
        return activistsDataMap;
    }

    public Long countClients(int campaignOid) {
        return ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(oid) FROM admin_users a WHERE a.campaign_oid=:campaignOid AND a.deleted=FALSE")
                .setInteger(PARAM_CAMPAIGN_OID, campaignOid)
                .uniqueResult()).longValue();
    }

    public Integer getActivistPerformanceByDate(int activistOid, Timestamp date) {
        return (Integer) getQuerySession().createQuery(
                "SELECT supportersCount FROM ActivistDataDailyObject a " +
                        "WHERE a.activistObject.oid=:activistOid AND a.date=:date")
                .setInteger(PARAM_ACTIVIST_OID, activistOid)
                .setTimestamp(PARAM_DATE, date)
                .uniqueResult();
    }

    public Map<String, Integer> getDatabaseConnectionsInfo() {
        Map<String, Integer> dbInfoMap = new HashMap<>();
        try {
            List<Object> rawData = getQuerySession().createSQLQuery("SELECT user, COUNT(user) FROM INFORMATION_SCHEMA.PROCESSLIST GROUP BY (user);").list();
            for (Object row : rawData) {
                Object[] data = (Object[]) row;
                dbInfoMap.put(String.valueOf(data[0]), Integer.valueOf(String.valueOf(data[1])));
            }
        } catch (Exception e) {
            LOGGER.error("getDatabaseConnectionsInfo", e);
        }
        return dbInfoMap;
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>> activistsSupportStatsDaysAgo(int days) {
        Calendar date = GregorianCalendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date.add(Calendar.DAY_OF_MONTH, days * (-1));
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        String endDate = simpleDateFormat.format(date.getTime());
        Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataMap = new LinkedHashMap<>();
        List<Object[]> rawData = (getQuerySession().createSQLQuery(
                String.format(
                        "SELECT " +
                                "  a.admin_user_oid, " +
                                "  activist_oid, " +
                                "  COUNT(activist_oid) " +
                                "FROM activists_voters_map avm " +
                                "  INNER JOIN activists a ON avm.activist_oid = a.oid " +
                                "WHERE " +
                                " avm.deleted = FALSE " +
                                " AND a.deleted = FALSE " +
                                " AND date <= '%s' " +
                                "GROUP BY activist_oid;", endDate
                )).list());
        for (Object[] data : rawData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int supportersCount = Integer.valueOf(data[2].toString());
            LinkedHashMap<Integer, Integer> activists = activistsDataMap.computeIfAbsent(adminOid, k -> new LinkedHashMap<>());
            activists.put(activistOid, supportersCount);
        }
        return activistsDataMap;
    }

    public int countRecentActivistsVotersMappings() {
        return ((BigInteger) (getQuerySession().createSQLQuery(
                String.format(
                        "SELECT DISTINCT COUNT(*) " +
                                "FROM activists_voters_map " +
                                "WHERE date > (NOW() - INTERVAL %s MINUTE) " +
                                "ORDER BY oid DESC; ",
                        ConfigUtils.getConfig(ConfigEnum.minutes_to_get_recent_activists_voters_mappings, 5))).uniqueResult())).intValue();
    }

    public int countRecentCallers() {
        return ((BigInteger) (getQuerySession().createSQLQuery(
                String.format(
                        "SELECT COUNT(DISTINCT (caller_oid)) " +
                                "FROM voters_calls " +
                                "WHERE time > (NOW() - INTERVAL %s MINUTE); ",
                        ConfigUtils.getConfig(ConfigEnum.minutes_to_get_recent_callers, 30))).uniqueResult())).intValue();
    }

    public double activistsRegistrationTokenPercent() {
        return ((BigDecimal) (getQuerySession().createSQLQuery(
                "SELECT ((SELECT COUNT(*)" +
                        " FROM activists" +
                        " WHERE mobile_registration_token IS NOT NULL) / (SELECT COUNT(*)" +
                        " FROM activists)); "
        ).uniqueResult())).doubleValue();
    }

    public int recentRequestsData() {
        return ((BigInteger) (getQuerySession().createSQLQuery(
                String.format("SELECT COUNT(*) FROM requests_data WHERE time > (NOW() - INTERVAL %s MINUTE);",
                        ConfigUtils.getConfig(ConfigEnum.minutes_recent_requests, 5))
        ).uniqueResult())).intValue();
    }

    public int countRecentElectionDayCallers() {
        return ((BigInteger) (getQuerySession().createSQLQuery(
                String.format(
                        "SELECT COUNT(DISTINCT (caller_oid)) " +
                                "FROM voters_election_day_calls " +
                                "WHERE time > (NOW() - INTERVAL %s MINUTE); ",
                        ConfigUtils.getConfig(ConfigEnum.minutes_to_get_recent_callers, 2))).uniqueResult())).intValue();
    }

    public int countRecentElectionDayCalls() {
        return ((BigInteger) (getQuerySession().createSQLQuery(
                String.format(
                        "SELECT COUNT(*) " +
                                "FROM voters_election_day_calls " +
                                "WHERE time > (NOW() - INTERVAL %s MINUTE); ",
                        ConfigUtils.getConfig(ConfigEnum.minutes_to_get_recent_callers, 2))).uniqueResult())).intValue();
    }

    public int countRecentVoteStatusChanges() {
        return ((BigInteger) (getQuerySession().createSQLQuery(
                String.format(
                        "SELECT COUNT(*) " +
                                "FROM voting_status_changes " +
                                "WHERE date > (NOW() - INTERVAL %s MINUTE); ",
                        ConfigUtils.getConfig(ConfigEnum.minutes_to_get_recent_callers, 2))).uniqueResult())).intValue();
    }

    public int totalCheckedInObservers() {
        return ((BigInteger) (getQuerySession().createSQLQuery(
                "SELECT COUNT(*) FROM observers_ballot_boxes_map WHERE checked_in=TRUE AND deleted=FALSE").uniqueResult())).intValue();
    }

    public int totalSuperObservers() {
        return ((BigInteger) (getQuerySession().createSQLQuery(
                "SELECT COUNT(*) FROM observers WHERE super_observer=TRUE AND deleted=FALSE").uniqueResult())).intValue();
    }

    public boolean isPhoneNumberExist(String phone) {
        boolean exists = false;
        List<String> tables = Arrays.asList("admin_users", "activists", "callers", "observers", "drivers");
        for (String table : tables) {
            int count = ((BigInteger) (
                    getQuerySession().createSQLQuery(
                    String.format("SELECT COUNT(*) FROM %s WHERE phone=:phone", table))
                            .setString(PARAM_PHONE, phone)
                            .uniqueResult()))
                    .intValue();
            if (count > 0) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public List<String> getVotersIds (int oid, boolean voters) {
        Query query = null;
        if (voters) {
            query = getQuerySession().createQuery("SELECT v.voterId FROM VoterObject v WHERE v.adminUserObject.oid=:oid AND deleted=FALSE").setInteger(PARAM_OID, oid);
        } else {
            query = getQuerySession().createQuery("SELECT v.voterId FROM CampaignVoterObject v WHERE v.campaignObject.oid=:oid AND deleted=FALSE").setInteger(PARAM_OID, oid);
        }
        return query.list();
    }




}