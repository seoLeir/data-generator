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

@Slf4j
@Service
public class DepartmentLoadService {
    private final RedisTemplate<String, String> redisTemplate;
    private final List<DepartmentInfo> departmentInfoList;
    private final Random random;

    public DepartmentLoadService(RedisTemplate<String, String> redisTemplate, DepartmentService departmentService) {
        this.redisTemplate = redisTemplate;
        departmentInfoList = departmentService.load();
        random = new Random();
    }

    @Scheduled(fixedRate = 10,
            initialDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(1, 30)}",
            timeUnit = TimeUnit.MINUTES)
    public synchronized void addLegalEntityTicket(){
        departmentInfoList.forEach(departmentInfo -> {
            Long id = departmentInfo.getId();
            try {
                Thread.sleep((random.nextInt(1, 60) * 1000L));
                String redisKey = id + ":" + PersonType.LEGAL_ENTITY.ordinal();
//                redisTemplate.opsForValue().setIfPresent();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*@Scheduled
    public void loadLegalEntityAllTicket(){

    }

    @Scheduled
    public void addIndividualTicket(){

    }

    @Scheduled
    public void loadIndividualAllTicket(){

    }*/


}
