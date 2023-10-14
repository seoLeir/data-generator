package org.bitpioneers.service;

import lombok.extern.slf4j.Slf4j;
import org.bitpioneers.data.DepartmentInfo;
import org.bitpioneers.types.PersonType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class DepartmentLoadService {
    private final RedisTemplate<String, String> redisTemplate;
    private final List<DepartmentInfo> departmentInfoList;
    private final Random random;
    private final DateTimeService dateTimeService;

    public DepartmentLoadService(RedisTemplate<String, String> redisTemplate,
                                 DepartmentService departmentService, DateTimeService dateTimeService) {
        this.redisTemplate = redisTemplate;
        this.departmentInfoList = departmentService.load();
        this.dateTimeService = dateTimeService;
        random = new Random();
    }

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
                if (!dateTimeService.isAllowedByDay() || !dateTimeService.isAllowedByTime(timeLine)) {
                    return;
                }
                long timeToLive = dateTimeService.getTimeToLive(timeLine);
                if (redisTemplate.opsForValue().get(redisKey) == null) {
                    redisTemplate.opsForValue().set(redisKey, "1", timeToLive, TimeUnit.MINUTES);
                } else {
                    int totalTickets = Integer.parseInt(
                            Objects.requireNonNull(redisTemplate
                                    .opsForValue().get(id + ":" + PersonType.JURIDICAL.getValue() + ":total")));
                    int oldValue = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(redisKey)));
                    if (oldValue < totalTickets) {
                        redisTemplate.opsForValue().set(redisKey, String.valueOf(++oldValue), timeToLive, TimeUnit.MINUTES);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

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
                if (!dateTimeService.isAllowedByDay() || !dateTimeService.isAllowedByTime(timeLine)) {
                    return;
                }
                long timeToLive = dateTimeService.getTimeToLive(timeLine);

                if (redisTemplate.opsForValue().get(redisKey) == null) {
                    redisTemplate.opsForValue().set(redisKey, "1", timeToLive, TimeUnit.MINUTES);
                } else {
                    int oldValue = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(redisKey)));
                    int newValue = new Random().nextInt(oldValue, oldValue + 10);
                    redisTemplate.opsForValue().set(redisKey, String.valueOf(newValue), timeToLive, TimeUnit.MINUTES);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

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
                if (!dateTimeService.isAllowedByDay() || !dateTimeService.isAllowedByTime(timeLine)) {
                    return;
                }
                long timeToLive = dateTimeService.getTimeToLive(timeLine);
                if (redisTemplate.opsForValue().get(redisKey) == null) {
                    redisTemplate.opsForValue().set(redisKey, "1", timeToLive, TimeUnit.MINUTES);
                } else {
                    int totalPhysicalTickets = Integer.parseInt(Objects.requireNonNull(
                            redisTemplate.opsForValue().get(id + ":" + PersonType.PHYSICAL.getValue() + ":total")));
                    int oldValue = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(redisKey)));
                    if (oldValue < totalPhysicalTickets) {
                        redisTemplate.opsForValue().set(redisKey, String.valueOf(++oldValue), timeToLive, TimeUnit.MINUTES);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

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
                if (!dateTimeService.isAllowedByDay() || !dateTimeService.isAllowedByTime(timeLine)) {
                    return;
                }
                long timeToLive = dateTimeService.getTimeToLive(timeLine);
                if (redisTemplate.opsForValue().get(redisKey) == null) {
                    redisTemplate.opsForValue().set(redisKey, "1", timeToLive, TimeUnit.MINUTES);
                } else {
                    int oldValue = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(redisKey)));
                    int newValue = new Random().nextInt(oldValue, oldValue + 20);
                    redisTemplate.opsForValue().set(redisKey, String.valueOf(newValue), timeToLive, TimeUnit.MINUTES);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
