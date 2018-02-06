package com.zc.zredis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisTest
{
    private Jedis jedis;
    private static final String serverHost = "192.168.144.129";
    private static final int serverPort = 6379; 
    @Before
    public void setup()
    {
        jedis = new Jedis(serverHost, serverPort);
        //jedis.auth("");
    }
    
    @Test
    public void testString()
    {
        //1.增
        //1)
        jedis.set("k1", "v1");
        System.out.println(jedis.get("k1"));
        //2)设置多个键值对k1,v1,k2,v2...
        jedis.mset("name","zc","age","23","qq","112");
        //加1操作
        jedis.incr("age");
        System.out.println(jedis.get("name")+"->"+jedis.get("age")+"->"+jedis.get("qq"));
        //拼接
        jedis.append("k1", "2345");
        System.out.println(jedis.get("k1"));
        //删
        jedis.del("k1");
        System.out.println(jedis.get("k1"));
    }
    
    @Test
    public void testMap()
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("name", "zc");
        map.put("age", "22");
        map.put("qq", "123");
        //1.增
        jedis.hmset("userMap", map);
        //取出userMap中的 可变个数的key 的values
        //2.查
        List<String> result = jedis.hmget("userMap", "name","age","qq");
        System.out.println(result);
        //返回map中的记录数
        System.out.println(jedis.hlen("userMap"));
        //map中是否有该key
        System.out.println(jedis.exists("name"));
        //返回map中的所有key
        System.out.println(jedis.hkeys("userMap"));
        //返回map中的所有value
        System.out.println(jedis.hvals("userMap"));
        //遍历keySet
        Iterator<String> it=jedis.hkeys("userMap").iterator();
        while(it.hasNext())
        {
            String key = it.next();
            System.out.println(key+":"+jedis.hmget("userMap",key));
        }
        //3.删
        jedis.hdel("user", "age");
        System.out.println(jedis.hmget("user", "age"));
    }
    
    @Test
    public void testList()
    {
        //1.增
        //lpush向左添加,新添加的元素在最左面
        jedis.lpush("myList", "a", "b", "c", "d");
        jedis.lpush("myList", "e");
        //rpush向右添加,新添加的元素在最右面
        jedis.rpush("myList", "f","g","h");
        //2.查
        //-1表示取出所有
        System.out.println(jedis.lrange("myList", 0, -1));//[e, d, c, b, a, f, g, h]
        //3.删
        //根据list名删除该list(list已不存在)
        jedis.del("myList");
        System.out.println(jedis.lrange("myList", 0, -1));
    }
    
    @Test
    public void testSet()
    {
        //1.增
        jedis.sadd("mySet", "a","b","c","d","e");
        //2.查
        //smembers获取所有元素
        System.out.println(jedis.smembers("mySet"));
        //sismember判断是否是set中的元素
        System.out.println(jedis.sismember("mySet", "a"));
        //随机返回set中的某个元素
        System.out.println(jedis.srandmember("mySet"));
        //返回集合的元素 个数
        System.out.println(jedis.scard("mySet"));
        //3.删
        //移除一个元素
        jedis.srem("mySet", "a");
    }
    
    /** <一句话功能简述>从JedisPool中获取Jedis实例
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @Test
    public void testRedisPool()
    {
        Jedis oneJedis = RedisUtil.getJedis();
        System.out.println(oneJedis+"获取成功");
    }
}
