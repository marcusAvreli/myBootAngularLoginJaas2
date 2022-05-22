package myBootAngularLoginJaas2.myCustomJaas;



/*-
* #%L
* thinkbig-security-api
* %%
* Copyright (C) 2017 ThinkBig Analytics
* %%
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* #L%
*/

import java.security.Principal;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* A principal representing a user group.  This is an immutable implementation
* of the {@link Group} principal.
*/
public class GroupPrincipal extends BasePrincipal implements Group {

   private static final long serialVersionUID = 1L;
   private static final Logger logger = LoggerFactory.getLogger(GroupPrincipal.class);
   private final Set<Principal> members;
   private final int hash; // Since this is immutable it only has to be calculated once.
   
   public GroupPrincipal(String name) {
	  
       this(name, Collections.<Principal>emptySet());
       logger.info("[GroupPrincipal]:1");
   }

   public GroupPrincipal(String name, Principal... members) {
	   
       this(name, members.length > 0 ? new HashSet<>(Arrays.asList(members)) : Collections.<Principal>emptySet());
       logger.info("[GroupPrincipal]:2");
   }

   public GroupPrincipal(String name, Set<Principal> members) {
       super(name);
       logger.info("[GroupPrincipal]:3");
       this.members = members.size() > 0 ? Collections.unmodifiableSet(new HashSet<>(members)) : Collections.<Principal>emptySet();
       this.hash = super.hashCode() ^ members.hashCode();
   }

   /* (non-Javadoc)
    * @see java.security.acl.Group#addMember(java.security.Principal)
    */
   @Override
   public boolean addMember(Principal user) {
	   logger.info("[GroupPrincipal]:4");
       throw new UnsupportedOperationException("Group principals of this type are immutable");
   }

   /* (non-Javadoc)
    * @see java.security.acl.Group#removeMember(java.security.Principal)
    */
   @Override
   public boolean removeMember(Principal user) {
	   logger.info("[GroupPrincipal]:5");
       throw new UnsupportedOperationException("Group principals of this type are immutable");
   }

   /* (non-Javadoc)
    * @see java.security.acl.Group#isMember(java.security.Principal)
    */
   @Override
   public boolean isMember(Principal member) {
	   logger.info("[GroupPrincipal]:6");
       return this.members.contains(member);
   }

   /* (non-Javadoc)
    * @see java.security.acl.Group#members()
    */
   @Override
   public Enumeration<? extends Principal> members() {
	   logger.info("[GroupPrincipal]:7");
       return Collections.enumeration(this.members);
   }

   
   @Override
   public int hashCode() {
	   logger.info("[GroupPrincipal]:8");
       return this.hash;
   }

   
   @Override
   public boolean equals(Object obj) {
	   logger.info("[GroupPrincipal]:9");
       if (obj instanceof Group && super.equals(obj)) {
           Group that = (Group) obj;
           Enumeration<? extends Principal> thatEnum = that.members();
           int count = 0;
           
           while (thatEnum.hasMoreElements()) {
               Principal thatPrinc = thatEnum.nextElement();
               count++;
               if (! this.members.contains(thatPrinc)) {
                   return false;
               }
           }
           
           return count == this.members.size();
       } else {
           return false;
       }
   }

}
