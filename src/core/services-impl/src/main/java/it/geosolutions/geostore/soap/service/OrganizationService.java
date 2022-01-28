package it.geosolutions.geostore.soap.service;

import it.geosolutions.geostore.core.model.UserGroup;
import it.geosolutions.geostore.services.UserGroupService;
import it.geosolutions.geostore.services.UserGroupServiceImpl;
import it.geosolutions.geostore.services.exception.BadRequestServiceEx;
import it.geosolutions.geostore.soap.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class OrganizationService {
    private static final String NAMESPACE_URI = "http://wse.vlaanderen.be/BeschikbareEntitlementArtefactService/common/DataTypes/v100";

    private static final String CONTEXT = "context";
    private static final String CONTEXT_NAAM = "contextNaam";
    private static final String ORGANIZATION = "Organization";

    private static final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    UserGroupService userGroupService;

    public OrganizationService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "QuerySingleFilterBeschikbareEntitlementRequest")
    @ResponsePayload
    public QuerySingleFilterBeschikbareEntitlementStringResponse getResponse(
            @RequestPayload QuerySingleFilterBeschikbareEntitlementRequest request) {
        log.info("Organization Service: Request Organization Details");

        QuerySingleFilterBeschikbareEntitlementStringResponse response = new QuerySingleFilterBeschikbareEntitlementStringResponse();

        WhereFilterType whereFilterType = getSingleWhereFilterType(request);

        UserGroup userGroup = getUserGroupByOrganizationId(whereFilterType);
        if (userGroup == null) {
            log.error("No UserGroup found for given organization Id");
            throw new RuntimeException("No UserGroup found for given organization Id");
        }

        QueryResultType resultType = new QueryResultType();

        //set SingleWhereFilterQuery
        resultType.setSingleWhereFilterQuery(whereFilterType);

        // set QueryResultSet
        resultType.setQueryResultSet(convertToResultSetType(userGroup));

        // Add Query result to response
        response.getQueryResult().add(resultType);

        log.info("Organization Service: Organization Response returned successfully");
        return response;
    }

    /**
     * Convert User Group to QueryResultSetType
     *
     * @param userGroup the UserGroup to set
     * @return QueryResultSetType
     */
    private QueryResultSetType convertToResultSetType(UserGroup userGroup) {
        log.info("Organization Service: Convert UserGroup to SOAP result set type");

        QueryResultSetType resultSetType = new QueryResultSetType();
        ResultRowType rowType = new ResultRowType();

        ResultColumnType contextColumnType = new ResultColumnType();
        contextColumnType.setArtefactName(CONTEXT);
        contextColumnType.setArtefactValue(userGroup.getGroupName());
        rowType.getArtefact().add(contextColumnType);

        ResultColumnType contextNaamColumnType = new ResultColumnType();
        contextNaamColumnType.setArtefactName(CONTEXT_NAAM);
        contextNaamColumnType.setArtefactValue(userGroup.getDescription());
        rowType.getArtefact().add(contextNaamColumnType);

        resultSetType.getResultRow().add(rowType);

        return resultSetType;
    }

    private WhereFilterType getSingleWhereFilterType(QuerySingleFilterBeschikbareEntitlementRequest request) {

        List<WhereQueryType> queryType = request.getWhereQueryList().getWhereQuery();

        if (queryType.size() == 0) {
            log.error("No Query filter found");
            throw new RuntimeException("No Query filter found");
        }

        return queryType.get(0).getSingleWhereFilterQuery();
    }

    /**
     * Returns UserGroup with requested organization id
     *
     * @param whereFilterType
     * @return
     */
    private UserGroup getUserGroupByOrganizationId(WhereFilterType whereFilterType) {
        log.info("Organization Service: Get UserGroup by organization Id");

        UserGroup userGroup;
        if (StringUtils.equalsIgnoreCase(ORGANIZATION, whereFilterType.getArtefactName())) {
            userGroup = getUserGroupById(whereFilterType.getFilterValue());
        } else {
            log.error("Organization attribute not found in request");
            throw new RuntimeException("Organization attribute not found in request");
        }
        return userGroup;
    }

    private UserGroup getUserGroupById(String filterValue) {

        Long organizationId = Long.parseLong(filterValue);

        List<UserGroup> userGroups;
        try {
            userGroups = userGroupService.getAll(null, null);
        } catch (BadRequestServiceEx e) {
            log.error("Error fetching user Groups", e);
            throw new RuntimeException("Error fetching user Groups", e);
        }

        return userGroups
                .stream()
                .filter(userGroup -> userGroup.getId().equals(organizationId))
                .findFirst()
                .orElse(null);
    }

    List<UserGroup> getMockUserGroups() {

        List<UserGroup> userGroups = new ArrayList<>();
        UserGroup userGroup1 = new UserGroup();
        userGroup1.setId(1L);
        userGroup1.setGroupName("a");
        userGroup1.setDescription("a desc");
        userGroups.add(userGroup1);

        UserGroup userGroup2 = new UserGroup();
        userGroup2.setId(2L);
        userGroup2.setGroupName("b");
        userGroup2.setDescription("b desc");
        userGroups.add(userGroup2);

        UserGroup userGroup3 = new UserGroup();
        userGroup3.setId(Long.parseLong("0419052173"));
        userGroup3.setGroupName("C");
        userGroup3.setDescription("C desc");
        userGroups.add(userGroup3);

        return userGroups;
    }

}
