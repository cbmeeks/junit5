/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.gen5.engine.junit5;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.engine.discovery.ClassSelector.forClass;
import static org.junit.gen5.launcher.main.TestDiscoveryRequestBuilder.request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.gen5.api.Assertions;
import org.junit.gen5.api.Dynamic;
import org.junit.gen5.api.DynamicTest;
import org.junit.gen5.api.Test;
import org.junit.gen5.engine.ExecutionEventRecorder;
import org.junit.gen5.engine.TestDescriptor;
import org.junit.gen5.launcher.TestDiscoveryRequest;

class DynamicTestGenerationTests extends AbstractJUnit5TestEngineTests {

	@Test
	public void dynamicTestMethodIsCorrectlyDiscovered() {
		TestDiscoveryRequest request = request().select(forClass(MyDynamicTestCase.class)).build();
		TestDescriptor engineDescriptor = discoverTests(request);
		assertEquals(2, engineDescriptor.allDescendants().size(), "# resolved test descriptors");
	}

	//@Test
	public void dynamicTestsAreExecuted() {
		TestDiscoveryRequest request = request().select(forClass(MyDynamicTestCase.class)).build();

		ExecutionEventRecorder eventRecorder = executeTests(request);

		assertEquals(2L, eventRecorder.getContainerStartedCount(), "# container started");
		assertEquals(2L, eventRecorder.getTestStartedCount(), "# tests started");
		assertEquals(1L, eventRecorder.getTestSuccessfulCount(), "# tests succeeded");
		assertEquals(1L, eventRecorder.getTestFailedCount(), "# tests failed");
		assertEquals(2L, eventRecorder.getContainerFinishedCount(), "# container finished");
	}

	private static class MyDynamicTestCase {

		@Dynamic
		Stream<DynamicTest> myDynamicTest() {
			List<DynamicTest> tests = new ArrayList<>();

			tests.add(new DynamicTest("succeedingTest", () -> Assertions.assertTrue(true, "succeeding")));
			tests.add(new DynamicTest("failingTest", () -> Assertions.assertTrue(false, "failing")));

			return tests.stream();
		}

	}

}