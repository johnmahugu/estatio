/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.incode.module.communications.dom.impl.commchannel;

import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.incode.module.country.dom.impl.Country;

import org.estatio.dom.UdoDomainRepositoryAndFactory;

/**
 * Domain service acting as repository for finding existing {@link PostalAddress postal address}es.
 */
@DomainService(repositoryFor = PostalAddress.class, nature = NatureOfService.DOMAIN)
public class PostalAddressRepository extends UdoDomainRepositoryAndFactory<PostalAddress> {

    public String getId() {
        return "estatio.PostalAddressRepository";
    }

    public PostalAddressRepository() {
        super(PostalAddressRepository.class, PostalAddress.class);
    }

    // //////////////////////////////////////

    @Programmatic
    public PostalAddress findByAddress(
            final CommunicationChannelOwner owner, 
            final String address1, 
            final String postalCode, 
            final String city, 
            final Country country) {

        final List<CommunicationChannelOwnerLink> links =
                communicationChannelOwnerLinkRepository.findByOwnerAndCommunicationChannelType(owner, CommunicationChannelType.POSTAL_ADDRESS);
        final Iterable<PostalAddress> postalAddresses =
                Iterables.transform(
                        links,
                        CommunicationChannelOwnerLink.Functions.communicationChannel(PostalAddress.class));
        final Optional<PostalAddress> postalAddressIfFound =
                Iterables.tryFind(postalAddresses, PostalAddress.Predicates.equalTo(address1, postalCode, city, country));
        return postalAddressIfFound.orNull();
    }

    @Programmatic
    public PostalAddress findByAddress(
            final CommunicationChannelOwner owner,
            final String address1,
            final String address2,
            final String address3,
            final String postalCode,
            final String city,
            final Country country) {

        final List<CommunicationChannelOwnerLink> links =
                communicationChannelOwnerLinkRepository.findByOwnerAndCommunicationChannelType(owner, CommunicationChannelType.POSTAL_ADDRESS);
        final Iterable<PostalAddress> postalAddresses =
                Iterables.transform(
                        links,
                        CommunicationChannelOwnerLink.Functions.communicationChannel(PostalAddress.class));
        final Optional<PostalAddress> postalAddressIfFound =
                Iterables.tryFind(postalAddresses, PostalAddress.Predicates.equalTo(address1, address2, address3, postalCode, city, country));
        return postalAddressIfFound.orNull();
    }


    @Inject
    CommunicationChannelOwnerLinkRepository communicationChannelOwnerLinkRepository;

}
