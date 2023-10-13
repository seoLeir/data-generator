package org.bitpioneers.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StringLoader {
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "*/5 * * * * *")
    public void getInfo() {
        log.info(String.valueOf(redisTemplate.opsForValue().get("abc-key")));
    }

}
