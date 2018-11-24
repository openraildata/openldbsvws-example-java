package com.openraildata;

import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken;
import com.thalesgroup.rtti._2015_05_14.ldbsv_ref.GetTOCListResponseType;
import com.thalesgroup.rtti._2015_05_14.ldbsv_ref.GetVersionedRefDataRequestParams;
import com.thalesgroup.rtti._2015_05_14.ldbsv_ref.LDBSVRefServiceSoap;
import com.thalesgroup.rtti._2015_05_14.ldbsv_ref.types.TOCName;
import com.thalesgroup.rtti._2017_10_01.ldbsv.Ldbsv;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.List;

/**
 * Open Live Departure Boards Staff Version Web Service (OpenLDBWS) API Demonstrator
 * Copyright (C)2018 OpenTrainTimes Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
public class GetReferenceDataExample {

    private static final String LDB_TOKEN = "";
    private static final boolean DEBUG = false;

    public static void main(String[] args) throws DatatypeConfigurationException {

        if (LDB_TOKEN.isEmpty()) {
            throw new RuntimeException("Please configure your OpenLDBWS token in GetDepartureBoardExample!");
        }

        AccessToken accessToken = new AccessToken();
        accessToken.setTokenValue(LDB_TOKEN);

        Ldbsv soap = new Ldbsv();
        LDBSVRefServiceSoap soapService = soap.getLDBSVRefServiceSoap12();

        /*
         * To examine the request and responses sent to the service, set DEBUG to true above
         */
        if (DEBUG) {
            Client client = ClientProxy.getClient(soapService);
            client.getInInterceptors().add(new LoggingInInterceptor());
            client.getOutInterceptors().add(new LoggingOutInterceptor());
        }

        GetVersionedRefDataRequestParams params = new GetVersionedRefDataRequestParams();
        params.setCurrentVersion(null);

        GetTOCListResponseType tocList = soapService.getTOCList(params, accessToken);

        System.out.println("All defined train operators");
        System.out.println("===============================================================================");

        List<TOCName> tocs = tocList.getGetTOCListResult().getTOCList().getTOC();

        for (TOCName toc : tocs) {

            System.out.println(toc.getToc() + " - " + toc.getValue());

        }

    }

}
