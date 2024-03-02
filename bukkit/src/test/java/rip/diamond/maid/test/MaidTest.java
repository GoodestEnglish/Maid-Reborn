package rip.diamond.maid.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.*;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;

public class MaidTest {

    private ServerMock server;
    private Maid plugin;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Maid.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test if Maid is initialized")
    public void testMaidInitialization() {
        Assertions.assertNotNull(Maid.INSTANCE);
    }

    @Test
    @DisplayName("Test if MaidAPI is initialized")
    public void testMaidAPIInitialization() {
        Assertions.assertNotNull(MaidAPI.INSTANCE);
    }

}
