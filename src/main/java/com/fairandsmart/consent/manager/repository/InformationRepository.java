package com.fairandsmart.consent.manager.repository;

import com.fairandsmart.consent.manager.entity.Information;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class InformationRepository implements PanacheRepositoryBase<Information, UUID> {

    public List<Information> findHead() {
        return list("type", Information.Type.HEAD);
    }

}
