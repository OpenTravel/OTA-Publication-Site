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

/**
 * Identity of a resource for which a read/write lock can be obtained.
 * 
 * @param <T>  the type of object for which a lock can be obtained
 * @author S. Livezey
 */
public abstract class LockableResource<T> {
	
    private T resource;
    private String identity;
    
    /**
     * Constructor that assigns resource for which locks can be obtained.
     * 
     * @param resource  the resource for which locks can be obtained
     */
    protected LockableResource(T resource) {
    	String _identity = buildIdentity( resource );
    	
        this.resource = resource;
        this.identity = (_identity == null) ? null : _identity.intern();
    }

    /**
     * Returns the underlying resource for which locks can be obtained
     * 
     * @return T
     */
    public T getResource() {
        return resource;
    }
    
    /**
     * Returns the unique identity string for the resource.
     * 
     * <p>Note that because identity strings are intern'ed, they are safe for
     * use as synchronization objects and may be compared via the '==' operator.
     * 
     * @return String
     */
    public String getIdentity() {
    	return identity;
    }
    
    /**
     * Returns a unique identity string for the resource.  In addition to
     * being unique, the identity of a resource must not change over the
     * lifetime of the resource.
     * 
     * @param resource  the resource for which to return an identity string
     * @return String
     */
    protected abstract String buildIdentity(T resource);

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        boolean result;

        if (obj instanceof LockableResource) {
        	// The '==' works here becaused we intern'ed the identity string
            result = (this.identity == ((LockableResource<?>) obj).identity);
            
        } else {
            result = false;
        }
        return result;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (identity == null) ? 0 : identity.hashCode();
    }

}
