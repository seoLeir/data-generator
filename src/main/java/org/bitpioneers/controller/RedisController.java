package org.bitpioneers.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisTemplate<String, String>  redisTemplate;


    @PostMapping("/strings")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> setString(@RequestParam("key") String key, @RequestParam("value") String val) {
        redisTemplate.opsForValue().set(key, val);
        return Map.of(key, val);
    }

    @GetMapping("/strings")
    public ResponseEntity<String> getString(@RequestParam("key") String key){
        return ResponseEntity.ok()
                .body(redisTemplate.opsForValue().get(key));
    }
}
