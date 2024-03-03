package rip.diamond.maid.mongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.util.MaidTestEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MongoManagerTest extends MaidTestEnvironment {

    @BeforeEach
    void setUp() {
        loadManagersAndListeners();
    }

    @Test
    @DisplayName("Test if mongo collections are available")
    void testMongoCollection() {
        assertNotNull(mongoManager.getUsers());
        assertNotNull(mongoManager.getRanks());
        assertNotNull(mongoManager.getPunishments());

        assertEquals(mongoManager.getUsers().countDocuments(), 0);
        assertEquals(mongoManager.getRanks().countDocuments(), 1); //Default Rank
        assertEquals(mongoManager.getPunishments().countDocuments(), 0);
    }

}
