package rip.diamond.maid;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.*;

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
    @DisplayName("Check if Maid is initialized")
    void testMaidInitialization() {
        Assertions.assertNotNull(Maid.INSTANCE);
    }

    @Test
    @DisplayName("Check if MaidAPI is initialized")
    void testMaidAPIInitialization() {
        Assertions.assertNotNull(MaidAPI.INSTANCE);
    }

}
