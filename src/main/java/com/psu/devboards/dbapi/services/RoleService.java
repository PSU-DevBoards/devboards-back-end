package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.repositories.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Singleton service for interacting with roles.
 */
@Service
public class RoleService extends CrudService<Integer, Role, Object> {

    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
    }

    /**
     * Gets a role by its name.
     * @param name The name to get the role by.
     * @return The retrieved role.
     * @throws ResponseStatusException Throws not found if the role does not exist.
     */
    public Role getByName(String name) throws ResponseStatusException {
        return ((RoleRepository) repository).findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested resource not found."));
    }

    @Override
    protected Role updateEntityFromRequest(Object request, Role entity) {
        return null;
    }

    @Override
    protected Role createEntityFromRequest(Object request) {
        return null;
    }
}
