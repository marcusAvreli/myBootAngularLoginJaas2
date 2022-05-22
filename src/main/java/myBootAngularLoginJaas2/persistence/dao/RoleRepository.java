package myBootAngularLoginJaas2.persistence.dao;

import java.util.List;
import java.util.Set;


import myBootAngularLoginJaas2.persistence.model.Role;

public interface RoleRepository  {
    Role findByRole(String role);
    Role findByRoleId(Integer id);
    Set<Role> findAll();
}