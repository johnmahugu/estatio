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
package org.estatio.integtests.lease;

import javax.inject.Inject;
import org.estatio.dom.lease.*;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.asset.PropertyForKal;
import org.estatio.fixture.asset.PropertyForOxf;
import org.estatio.fixture.lease.*;
import org.estatio.fixture.party.*;
import org.estatio.integtests.EstatioIntegrationTest;
import org.estatio.integtests.VT;
import org.junit.Before;
import org.junit.Test;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class LeaseTermTest_approve extends EstatioIntegrationTest {

    @Before
    public void setupData() {
        scenarioExecution().install(new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                execute(new EstatioBaseLineFixture(), executionContext);

                execute(new PersonForJohnDoe(), executionContext);
                execute(new PersonForLinusTorvalds(), executionContext);

                execute(new OrganisationForHelloWorld(), executionContext);
                execute(new PropertyForOxf(), executionContext);

                execute(new OrganisationForAcme(), executionContext);
                execute(new PropertyForKal(), executionContext);

                execute(new OrganisationForTopModel(), executionContext);
                execute(new LeasesEtcForOxfTopModel001(), executionContext);

                execute(new OrganisationForMediaX(), executionContext);
                execute(new LeasesEtcForOxfMediax002(), executionContext);

                execute(new OrganisationForPoison(), executionContext);
                execute(new LeasesEtcForOxfPoison003(), executionContext);
                execute(new LeasesEtcForKalPoison001(), executionContext);

                execute(new OrganisationForPret(), executionContext);
                execute(new LeasesEtcForOxfPret004(), executionContext);

                execute(new OrganisationForMiracle(), executionContext);
                execute(new LeasesEtcForOxfMiracl005(), executionContext);
            }
        });
    }

    @Inject
    private Leases leases;

    private Lease lease;
    private LeaseItem leaseTopModelRentItem;

    @Before
    public void setup() {
        lease = leases.findLeaseByReference("OXF-TOPMODEL-001");
        leaseTopModelRentItem = lease.findItem(LeaseItemType.RENT, VT.ld(2010, 7, 15), VT.bi(1));
        assertNotNull(leaseTopModelRentItem);
    }

    @Test
    public void happyCase() throws Exception {

        // given
        lease.verifyUntil(VT.ld(2014, 1, 1));

        LeaseTerm term0 = leaseTopModelRentItem.findTerm(VT.ld(2010, 7, 15));
        LeaseTerm term2 = leaseTopModelRentItem.findTerm(VT.ld(2012, 7, 15));
        assertThat(term2, is(not(sameInstance(term0))));

        assertThat(term0.getStatus(), is(LeaseTermStatus.NEW));
        assertThat(term2.getStatus(), is(LeaseTermStatus.NEW));

        // when
        term0.approve();

        // then only the term that is approved has a changed status
        assertThat(term0.getStatus(), is(LeaseTermStatus.APPROVED));
        assertThat(term2.getStatus(), is(LeaseTermStatus.NEW));
    }

}