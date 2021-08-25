package org.acme.quartz;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeTaskResourceIT extends TaskResourceTest {

    // Execute the same tests but in native mode.
}