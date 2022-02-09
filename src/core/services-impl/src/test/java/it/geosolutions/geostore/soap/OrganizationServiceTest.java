package it.geosolutions.geostore.soap;

import it.geosolutions.geostore.core.model.UserGroup;
import it.geosolutions.geostore.services.UserGroupService;
import it.geosolutions.geostore.services.UserGroupServiceImpl;
import it.geosolutions.geostore.services.exception.BadRequestServiceEx;
import it.geosolutions.geostore.soap.model.*;
import it.geosolutions.geostore.soap.service.OrganizationService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrganizationServiceTest {

    OrganizationService organizationService;

    UserGroupService userGroupService = mock(UserGroupServiceImpl.class);

    List<UserGroup> userGroups = new ArrayList<>();

    @Before
    public void setUp() {
        organizationService = new OrganizationService(userGroupService);

        UserGroup userGroup1 = new UserGroup();
        userGroup1.setId(1L);
        userGroup1.setGroupName("testGroup");
        userGroup1.setDescription("test group description");
        userGroups.add(userGroup1);

        UserGroup userGroup2 = new UserGroup();
        userGroup2.setId(2L);
        userGroup2.setGroupName("demoGroup");
        userGroup2.setDescription("demo group description");
        userGroups.add(userGroup2);

        UserGroup userGroup3 = new UserGroup();
        userGroup3.setId(Long.parseLong("0419052173"));
        userGroup3.setGroupName("abcd");
        userGroup3.setDescription("description");
        userGroups.add(userGroup3);

        try {
            when(userGroupService.getAll(null, null)).thenReturn(userGroups);
        } catch (BadRequestServiceEx e) {
            throw new RuntimeException("Unable to get UserGroups", e);
        }

    }

    /**
     * Checks that UserGroup is retrieved and mapped correctly in the response for given Organization ID
     */
    @Test
    public void testUserGroupsFound() {

        // Prepare the request
        QuerySingleFilterBeschikbareEntitlementRequest request = new QuerySingleFilterBeschikbareEntitlementRequest();
        WhereQueryListType whereQueryListType = new WhereQueryListType();
        WhereQueryType queryType = new WhereQueryType();
        WhereFilterType whereFilterType = new WhereFilterType();
        whereFilterType.setArtefactName("organization");
        whereFilterType.setFilterValue("2");
        queryType.setSingleWhereFilterQuery(whereFilterType);
        whereQueryListType.getWhereQuery().add(queryType);
        request.setWhereQueryList(whereQueryListType);

        // Execute the request
        QuerySingleFilterBeschikbareEntitlementStringResponse response = organizationService.getResponse(request);

        List<ResultColumnType> resultColumnTypes =
                response.getQueryResult().get(0).getQueryResultSet().getResultRow().get(0).getArtefact();

        assertEquals(2, resultColumnTypes.size());
        assertEquals("context", resultColumnTypes.get(0).getArtefactName());
        assertEquals("demoGroup", resultColumnTypes.get(0).getArtefactValue());
        assertEquals("contextNaam", resultColumnTypes.get(1).getArtefactName());
        assertEquals("demo group description", resultColumnTypes.get(1).getArtefactValue());

    }

    /**
     * Checks for exception when UserGroup is not found
     */
    @Test(expected = RuntimeException.class)
    public void testUserGroupsNotFound() {

        // Prepare the request
        QuerySingleFilterBeschikbareEntitlementRequest request = new QuerySingleFilterBeschikbareEntitlementRequest();
        WhereQueryListType whereQueryListType = new WhereQueryListType();
        WhereQueryType queryType = new WhereQueryType();
        WhereFilterType whereFilterType = new WhereFilterType();
        whereFilterType.setArtefactName("organization");
        whereFilterType.setFilterValue("4");
        queryType.setSingleWhereFilterQuery(whereFilterType);
        whereQueryListType.getWhereQuery().add(queryType);
        request.setWhereQueryList(whereQueryListType);

        // Execute the request
        organizationService.getResponse(request);
    }

    /**
     * Checks for Exception when Invalid SOAP Request is provided
     */
    @Test(expected = RuntimeException.class)
    public void testInvalidRequest() {

        // Prepare the request
        QuerySingleFilterBeschikbareEntitlementRequest request = new QuerySingleFilterBeschikbareEntitlementRequest();
        WhereQueryListType whereQueryListType = new WhereQueryListType();

        // Avoid setting and adding the WhereQueryType to WhereQueryListType

        request.setWhereQueryList(whereQueryListType);

        // Execute the request
        organizationService.getResponse(request);
    }

}
