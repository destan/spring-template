package dev.destan.template.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface RoleRepository extends JpaRepository<Role, Long> {


    @Query("SELECT r FROM Role r WHERE r.name IN :allowedRoles")
    List<Role> registrableRoles(List<String> allowedRoles);

    Optional<Role> findByName(String name);
}
