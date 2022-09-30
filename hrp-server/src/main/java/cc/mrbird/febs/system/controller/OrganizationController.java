package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.Organization;
import cc.mrbird.febs.system.service.IOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/organization")
public class OrganizationController extends BaseController {

    @Autowired
    IOrganizationService iOrganizationService;

    @GetMapping
    public List<Organization> findOrganization(QueryRequest request) {
        return iOrganizationService.findOrganizations();
    }
}
