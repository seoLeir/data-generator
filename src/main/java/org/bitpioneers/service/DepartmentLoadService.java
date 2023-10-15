package org.bitpioneers.service;

import lombok.extern.slf4j.Slf4j;
import org.bitpioneers.data.DepartmentInfo;
import org.bitpioneers.types.PersonType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * The TicketManagementSystem class is a core component of a software system designed to manage the issuance, tracking,
 * and processing of tickets for both physical and juridical entities. It contains several scheduled methods that
 * periodically update and record data about issued tickets and the current status of individuals and entities being
 * serviced in a banking or customer service environment
 * The TicketManagementSystem class serves the following purposes:
 *
 * <ul>
 *    <li>
 *       It provides a structured and organized environment for managing the issuance and tracking of tickets for both
 *       physical and juridical entities in a dynamic operational setting.
 *    </li>
 *    <li>
 *        It incorporates scheduled tasks that ensure the accurate and real-time tracking of ticket data, aligning with
 *        department schedules and operational constraints.
 *    </li>
 *    <li>
 *        It offers methods for time-based validation to verify whether ticket issuance should occur at the current day
 *        and time, as well as time-to-live calculations to ensure data validity.
 *    </li>
 * </ul>
 *
 * @since 1.0
 * @author Mirolim Mirzayev
 *
*/
@Slf4j
@Service
public class DepartmentLoadService {
    private final RedisTemplate<String, String> redisTemplate;
    private final List<DepartmentInfo> departmentInfoList;
    private final Random random;
    private final DateTimeService dateTimeService;


     /**
     * Constructor initializes and sets up the DepartmentLoadService class by configuring its dependencies
     * and preparing it for use in a larger software system. This document provides an overview of the
     * constructor's purpose, its parameters, and the actions it performs during instantiation
     * @param redisTemplate  An instance of the RedisTemplate class specialized for working with key-value
      *                      pairs of type String. This parameter represents the Redis data store where
      *                      department-related information will be stored and retrieved.
      * @param departmentService  An instance of the DepartmentService class that provides access to
      *                           department-specific data. This parameter is used to load a list of
      *                           department information, which will be processed by the DepartmentLoadService.
      * @param dateTimeService  An instance of the DateTimeService class or a related service responsible for date
      *                         and time-related operations. This parameter is utilized to handle time-based
      *                         validations and calculations within the DepartmentLoadService.
     */
    public DepartmentLoadService(RedisTemplate<String, String> redisTemplate,
                                 DepartmentService departmentService, DateTimeService dateTimeService) {
        this.redisTemplate = redisTemplate;
        this.departmentInfoList = departmentService.load();
        this.dateTimeService = dateTimeService;
        random = new Random();
    }


    /**
    * The addJuridicalTicket method is a scheduled task within a software system designed to manage the creation and
     * tracking of tickets for juridical entities in a banking or customer service environment. This method is executed
     * periodically to record and update information about which juridical entities are currently being serviced and
     * with which tickets. It manages this data in a Redis data store.
    */
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(1, 60)}",
            timeUnit = TimeUnit.SECONDS)
    public void addJuridicalTicket() {
        log.info("Ticket was created for juridical person");
        departmentInfoList.forEach(departmentInfo -> {
            Long id = departmentInfo.getId();
            String timeLine = departmentInfo.getScheduleJurL();
            try {
                Thread.sleep(random.nextInt(1, 60));
                String redisKey = id + ":" + PersonType.JURIDICAL.getValue() + ":current";
                if (!dateTimeService.isAllowedByDay() || !dateTimeService.isAllowedByTime(timeLine)) return;
                long timeToLive = dateTimeService.getTimeToLive(timeLine);
                if (redisTemplate.opsForValue().get(redisKey) == null)
                    redisTemplate.opsForValue().set(redisKey, "1", timeToLive, TimeUnit.MINUTES);
                else {
                    String tempvalue = redisTemplate.opsForValue().get(id + ":" + PersonType.JURIDICAL.getValue() + ":total");
                    if (tempvalue == null) return;
                    int totalTickets = Integer.parseInt(tempvalue);
                    String value = redisTemplate.opsForValue().get(redisKey);
                    if (value == null) return;
                    int oldValue = Integer.parseInt(value);
                    if (oldValue < totalTickets) {
                        redisTemplate.opsForValue().set(redisKey, String.valueOf(++oldValue), timeToLive, TimeUnit.MINUTES);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
    * The loadJuridicalAllTicket method is a scheduled task within a software system designed to manage the issuance
    * and tracking of tickets for juridical entities. This method operates by periodically updating and recording the
    * number of juridical entities that have taken a ticket. Additionally, it increments the count in a random manner
    * within a specified range while managing this information in a Redis data store.
    */
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(1, 50)}",
            timeUnit = TimeUnit.SECONDS)
    public void loadJuridicalAllTicket() {
        log.info("Load juridical ticket to all tickets");
        departmentInfoList.forEach(departmentInfo -> {
            Long id = departmentInfo.getId();
            String timeLine = departmentInfo.getScheduleJurL();
            try {
                Thread.sleep(random.nextInt(1, 60));
                String redisKey = id + ":" + PersonType.JURIDICAL.getValue() + ":total";
                if (!dateTimeService.isAllowedByDay() || !dateTimeService.isAllowedByTime(timeLine)) return;
                long timeToLive = dateTimeService.getTimeToLive(timeLine);
                if (redisTemplate.opsForValue().get(redisKey) == null)
                    redisTemplate.opsForValue().set(redisKey, "1", timeToLive, TimeUnit.MINUTES);
                else {
                    String value = redisTemplate.opsForValue().get(redisKey);
                    if (value == null) return;
                    int oldValue = Integer.parseInt(value);
                    redisTemplate.opsForValue().set(redisKey,
                            String.valueOf(random.nextInt(oldValue, oldValue + 10)), timeToLive, TimeUnit.MINUTES);
                }
            } catch (Exception e) {
                log.error(e.toString());
            }
        });
    }

    /**
    * The addIndividualTicket method is a scheduled task designed for managing the creation and tracking of tickets for
     * physical individuals in a dynamic environment, such as a bank or customer service center. This method is executed
     * periodically between 1 and 15 units of time and interacts with a list of department information, recording the creation of
     * tickets and their real-time status in a Redis data store.
    **/
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(1, 15)}",
            timeUnit = TimeUnit.SECONDS)
    public void addIndividualTicket() {
        log.info("Ticket was created for physical person");
        departmentInfoList.forEach(departmentInfo -> {
            Long id = departmentInfo.getId();
            String timeLine = departmentInfo.getScheduleJurL();
            try {
                Thread.sleep(random.nextInt(1, 60));
                String redisKey = id + ":" + PersonType.PHYSICAL.getValue() + ":current";
                if (!dateTimeService.isAllowedByDay() || !dateTimeService.isAllowedByTime(timeLine)) return;
                long timeToLive = dateTimeService.getTimeToLive(timeLine);
                if (redisTemplate.opsForValue().get(redisKey) == null)
                    redisTemplate.opsForValue().set(redisKey, "1", timeToLive, TimeUnit.MINUTES);
                else {
                    String value = redisTemplate.opsForValue().get(id + ":" + PersonType.PHYSICAL.getValue() + ":total");
                    String valueToCheck = redisTemplate.opsForValue().get(redisKey);
                    if (value == null || valueToCheck == null) return;
                    int totalPhysicalTickets = Integer.parseInt(value);
                    int oldValue = Integer.parseInt(valueToCheck);
                    if (oldValue < totalPhysicalTickets) {
                        redisTemplate.opsForValue().set(redisKey, String.valueOf(++oldValue), timeToLive, TimeUnit.MINUTES);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
    *  The loadIndividualAllTicket method is designed to be executed periodically as a scheduled task within a larger
     *  software system that manages the issuance and tracking of physical individual tickets. By invoking this method
     *  at fixed intervals between 1 and 10 units of time, the system can ensure that ticket counts are incremented in
     *  a manner that aligns with department schedules and operational constraints.
    */
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(1, 10)}",
            timeUnit = TimeUnit.SECONDS)
    public void loadIndividualAllTicket() {
        log.info("Load physical ticket to all tickets");
        departmentInfoList.forEach(departmentInfo -> {
            Long id = departmentInfo.getId();
            String timeLine = departmentInfo.getScheduleJurL();
            try {
                Thread.sleep(random.nextInt(1, 60));
                String redisKey = id + ":" + PersonType.PHYSICAL.getValue() + ":total";
                if (!dateTimeService.isAllowedByDay() || !dateTimeService.isAllowedByTime(timeLine)) return;
                long timeToLive = dateTimeService.getTimeToLive(timeLine);
                if (redisTemplate.opsForValue().get(redisKey) == null)
                    redisTemplate.opsForValue().set(redisKey, "1", timeToLive, TimeUnit.MINUTES);
                else {
                    String value = redisTemplate.opsForValue().get(redisKey);
                    if (value == null) return;
                    int oldValue = Integer.parseInt(value);
                    redisTemplate.opsForValue().set(redisKey,
                            String.valueOf(random.nextInt(oldValue, oldValue + 20)), timeToLive, TimeUnit.MINUTES);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
