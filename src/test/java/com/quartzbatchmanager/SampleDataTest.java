package com.quartzbatchmanager;

import com.quartzbatchmanager.batch.entity.BatchEntity;
import com.quartzbatchmanager.batch.repository.SampleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SampleDataTest {

    @Autowired
    private SampleRepository sampleRepository;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testInsertData() {
        int expectedCount = 100;

        for (int i = 0; i < expectedCount; i++) {
            sampleRepository.save(BatchEntity.builder().name("A").build());
        }

        long actualCount = sampleRepository.count();
        //assertEquals(expectedCount, actualCount, "Inserted data count should be 100");
    }
}
