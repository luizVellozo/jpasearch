/*
 * Copyright 2014, jpasearch
 *
 * This file is part of jpasearch.
 *
 * jpasearch is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * jpasearch is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jpasearch. If not, see <http://www.gnu.org/licenses/>.
 */
package jpasearch.repository;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.EntityA;
import jpasearch.domain.EntityA_;
import jpasearch.repository.query.SearchBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author speralta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@Transactional
public class EntityARepositoryIT {

    @Inject
    private EntityARepository entityARepository;

    @Test
    @Rollback
    public void test() {
        EntityA entityA = new EntityA();
        entityA.setValue("test");
        entityARepository.save(entityA);

        entityARepository.find(new SearchBuilder<EntityA>() //
                .on(EntityA_.value).equalsTo("test") //
                .build());
    }

}
