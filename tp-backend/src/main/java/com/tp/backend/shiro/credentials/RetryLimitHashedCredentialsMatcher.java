package com.tp.backend.shiro.credentials;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import com.tp.backend.shiro.filter.ShiroUPToken;

/**
 * <p>User: king shang
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

	//缓存对象
    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    	ShiroUPToken t = (ShiroUPToken) token;
        String username = (String)t.getUsername();
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if(retryCount.incrementAndGet() > 5) {
            //如果登出失败次数大于5次,抛出异常
            throw new ExcessiveAttemptsException("登录次数失败超过5次,请在5分钟后重试");
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //从缓存中将用户登录次数记录删除掉
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
