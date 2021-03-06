package qs.config.redis;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import qs.config.SlowLogCenter;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class BinaryRedisAspect {
    @Pointcut("execution(* redis.clients.jedis.BinaryJedis.*(..))")
    public void point() {

    }

    @Around("point()")
    public Object printAnnotationJointPointAround(ProceedingJoinPoint joinpoint) throws Throwable {

        if (METHODS.contains(joinpoint.getSignature().getName())) {
            Stopwatch stopwatch = Stopwatch.createStarted();

            Object object = joinpoint.proceed(joinpoint.getArgs());

            stopwatch.stop();
            long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            log.info("{}({}) costs {} ms", joinpoint.getSignature().getName(), getArgs(joinpoint), duration);

            if (!skipRedisKey(joinpoint, SlowLogCenter.class.getSimpleName()))
                SlowLogCenter.publishEvent(SlowLogCenter.LogType.REDIS, String.format("%s()", joinpoint.getSignature().getName()), duration);
            return object;
        } else {
            return joinpoint.proceed(joinpoint.getArgs());
        }


    }

    //very important ,avoid redis recursion
    private boolean skipRedisKey(ProceedingJoinPoint joinpoint, String redisKey) {
        try {
            return joinpoint.getArgs() != null && joinpoint.getArgs().length > 0
                    && new String((byte[]) joinpoint.getArgs()[0], "utf-8").startsWith(redisKey.toLowerCase());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private String getArgs(ProceedingJoinPoint joinpoint) {
        StringBuilder stringBuilder = new StringBuilder();
        Signature signature = joinpoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            String[] names = methodSignature.getParameterNames();
            int len = methodSignature.getParameterTypes().length;
            for (int i = 0; i < len; i++) {
                Object p = joinpoint.getArgs()[i];
                String pv = "";
                if (p instanceof byte[]) {
                    try {
                        pv = new String((byte[]) p, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        pv = p.toString();
                    }
                } else {
                    pv = p.toString();
                }

                if (pv != null && pv.length() > 10) {
                    pv = pv.substring(0, 10);
                }

                stringBuilder.append(names[i] + "=" + pv).append(",");
            }

        }
        if (stringBuilder.length() > 0)
            return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        return "";
    }


    private final HashSet<String> METHODS = new HashSet(Arrays.asList("zcount", "sunionstore", "zunionstore"
            , "del", "zinterstore", "echo"
            , "hscan", "psubscribe", "type"
            , "sinterstore", "setex", "zlexcount"
            , "brpoplpush", "bitcount", "llen"
            , "zscan", "lpushx", "bitpos"
            , "setnx", "hvals", "evalsha"
            , "substr", "geodist", "zrangeByLex"
            , "geoadd", "expire", "bitop"
            , "zrangeByScore", "smove", "lset"
            , "decrBy", "pttl", "scan"
            , "zrank", "blpop", "rpoplpush"
            , "zremrangeByLex", "get", "lpop"
            , "persist", "scriptExists", "georadius"
            , "set", "srandmember", "incr", "setbit"
            , "hexists", "expireAt", "pexpire", "zcard"
            , "bitfield", "zrevrangeByLex", "sinter", "srem"
            , "getrange", "rename", "zrevrank", "exists"
            , "setrange", "zremrangeByRank", "sadd", "sdiff"
            , "zrevrange", "getbit", "scard", "sdiffstore"
            , "zrevrangeByScore", "zincrby", "rpushx", "psetex"
            , "zrevrangeWithScores", "strlen", "hdel", "zremrangeByScore"
            , "geohash", "brpop", "lrem", "hlen", "decr"
            , "scriptLoad", "lpush", "lindex", "zrange", "incrBy"
            , "getSet", "ltrim", "incrByFloat", "rpop", "sort"
            , "zrevrangeByScoreWithScores", "pfadd", "eval", "linsert"
            , "pfcount", "hkeys", "hsetnx", "hincrBy", "hgetAll"
            , "hset", "spop", "zrangeWithScores", "hincrByFloat"
            , "hmset", "renamenx", "zrem", "msetnx", "hmget"
            , "sunion", "hget", "zadd", "move", "subscribe"
            , "geopos", "mset", "zrangeByScoreWithScores", "zscore"
            , "pexpireAt", "georadiusByMember", "ttl", "lrange"
            , "smembers", "pfmerge", "rpush", "publish"
            , "mget", "sscan", "append", "sismember"));

}