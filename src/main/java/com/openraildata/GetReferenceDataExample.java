package com.openraildata;

import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken;
import com.thalesgroup.rtti._2015_05_14.ldbsv_ref.types.TOCName;
import com.thalesgroup.rtti._2021_11_01.ldbsv_ref.GetTOCListResponseType;
import com.thalesgroup.rtti._2021_11_01.ldbsv_ref.GetVersionedRefDataRequestParams;
import com.thalesgroup.rtti._2021_11_01.ldbsv_ref.LDBSVRefServiceSoap;
import com.thalesgroup.rtti._2021_11_01.ldbsv.Ldbsv;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ConfigurationException;
import java.util.List;

/**
 * Open Live Departure Boards Staff Version Web Service (OpenLDBSVWS) API Demonstrator
 * Copyright (C)2018-2022 OpenTrainTimes Ltd.
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

    private static final Logger logger = LoggerFactory.getLogger(GetReferenceDataExample.class);

    private static final String LDB_TOKEN = "";
    private static final boolean DEBUG = false;

    public static void main(String[] args) throws ConfigurationException {

        if (LDB_TOKEN.isEmpty()) {
            throw new ConfigurationException("Please configure your OpenLDBSVWS token in GetReferenceDataExample!");
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

        logger.info("All defined train operators");
        logger.info("===============================================================================");

        List<TOCName> tocs = tocList.getGetTOCListResult().getTOCList().getTOC();

        for (TOCName toc : tocs) {

            logger.info("{} - {}", toc.getToc(), toc.getValue());

        }

    }

}
