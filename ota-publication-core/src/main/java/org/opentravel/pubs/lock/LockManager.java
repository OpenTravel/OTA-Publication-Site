/**
 * Copyright (C) 2015 OpenTravel Alliance (info@opentravel.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opentravel.pubs.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Maintains a central registry of read and write locks for named resources in a
 * multi-threaded environment.
 * 
 * @author S. Livezey
 */
public class LockManager {
	
	/** Default timeout for lock acquisition is 5 sec. */
	public static final long DEFAULT_TIMEOUT = 5000;
	
	private Map<LockableResource<?>,ReentrantReadWriteLock> lockRegistry = new HashMap<>();
	private long timeout = DEFAULT_TIMEOUT;
	
	/**
	 * Default constructor.
	 */
	public LockManager() {
	}
	
	/**
	 * Returns the current timeout value (in milliseconds) for lock acquisition.
	 * 
	 * @return long
	 */
	public long getTimeout() {
		return timeout;
	}
	
	/**
	 * Assigns the timeout value (in milliseconds) for lock acquisition.
	 * 
	 * @param timeout the timeout value to assign
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Acquires a read lock on the requested resource. If another thread owns
	 * (or has requested) a write lock on the same resource, this method will
	 * block until the write lock has been released.
	 * 
	 * <p>If the lock is successfully acquired, this method will return without
	 * error.
	 * 
	 * @param resource  the resource for which to acquire the read lock
	 * @throws LockException  thrown if the timeout expires before the lock can be established
	 */
	public void acquireReadLock(LockableResource<?> resource) throws LockException {
		try {
			ReadWriteLock lock = getReadWriteLock(resource);
			
			lock.readLock().tryLock(timeout, TimeUnit.MILLISECONDS);
			
		} catch (InterruptedException e) {
			throw new LockException("Timed out waiting for read lock for resource: " +
					resource.getIdentity(), e);
		}
	}
	
	/**
	 * Releases the lock previously obtained by a call to 'acquireReadLock()'.
	 * 
	 * @param resource  the resource for which to release the read lock
	 * @throws LockException  thrown if the current thread does not own a read lock on the resource
	 */
	public void releaseReadLock(LockableResource<?> resource) throws LockException {
		try {
			getReadWriteLock(resource).readLock().unlock();
			
		} catch (IllegalMonitorStateException e) {
			throw new LockException("Unable to release read lock for resource: " +
					resource.getIdentity(), e);
		}
	}
	
	/**
	 * Acquires a write lock on the requested resource. If one or more threads
	 * own a read or write lock on the same resource, this method will block
	 * until the lock(s) have been released.
	 * 
	 * <p>If the lock is successfully acquired, this method will return without
	 * error.
	 * 
	 * @param resource  the resource for which to acquire the write lock
	 * @throws LockException  thrown if the timeout expires before the lock can be established
	 */
	public void acquireWriteLock(LockableResource<?> resource) throws LockException {
		try {
			ReadWriteLock lock = getReadWriteLock(resource);
			
			lock.writeLock().tryLock(timeout, TimeUnit.MILLISECONDS);
			
		} catch (InterruptedException e) {
			throw new LockException("Timed out waiting for write lock for resource: " +
					resource.getIdentity(), e);
		}
	}
	
	/**
	 * Downgrades a previously obtained write lock to a read lock.  The lock owner must
	 * call 'releaseReadLock()' to release the lock after calling this method.
	 * 
	 * @param resource  the resource for which the write lock should be downgraded
	 * @throws LockException  thrown if the current thread does not own a write lock on the resource
	 */
	public void downgradeWriteLock(LockableResource<?> resource) throws LockException {
		ReentrantReadWriteLock lock = getReadWriteLock( resource );
		
		if (!lock.writeLock().isHeldByCurrentThread()) {
			throw new LockException(
					"The current thread does not own a write lock on the requested resource: " +
							resource.getIdentity());
		}
		lock.readLock().lock();
		lock.writeLock().unlock();
	}
	
	/**
	 * Releases the lock previously obtained by a call to 'acquireWriteLock()'.
	 * 
	 * @param resource  the resource for which to release the write lock
	 * @throws LockException  thrown if the current thread does not own a write lock on the resource
	 */
	public void releaseWriteLock(LockableResource<?> resource) throws LockException {
		try {
			getReadWriteLock(resource).writeLock().unlock();
			
		} catch (IllegalMonitorStateException e) {
			throw new LockException("Unable to release write lock for resource: " + resource.getIdentity(), e);
		}
	}
	
	/**
	 * Obtains a read/write lock from the registry. If a lock instance for the
	 * resource does not already exist, it is created automatically.
	 * 
	 * @param resource the resource for which the lock should be retrieved
	 * @return ReentrantReadWriteLock
	 */
	private synchronized ReentrantReadWriteLock getReadWriteLock(LockableResource<?> resource) {
		ReentrantReadWriteLock lock = lockRegistry.get(resource);
		
		if (lock == null) {
			lock = new ReentrantReadWriteLock(true);
			lockRegistry.put(resource, lock);
		}
		return lock;
	}
	
}
