-- 设置k-v
-- 返回值是JSON需要JSON序列化支持
--return redis.call('set', KEYS[1], ARGV[1])
return cjson.encode(redis.call('mset', KEYS[1], ARGV[1], KEYS[2], ARGV[2]))