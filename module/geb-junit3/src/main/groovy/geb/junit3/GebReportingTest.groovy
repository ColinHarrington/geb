/* Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geb.junit3

import geb.Browser
import geb.report.*
import org.openqa.selenium.WebDriver

class GebReportingTest extends GebTest {

	static private GEB_REPORTING_TEST_COUNTERS = [:]
	static private GEB_REPORTING_TEST_REPORTERS = [:]

	void report(String label) {
		getTestReporter(this)?.writeReport(label, getBrowser())
	}
	
	void tearDown() {
		report(getNextTestCounterValue(this).toString())
		super.tearDown()
	}

	/**
	 * Subclasses can override this to use a different reporter
	 */
	Reporter createReporter() {
		def reportDir = getReportDir()
		reportDir ? new ScreenshotAndPageSourceReporter(reportDir, this.class, true) : null
	}
	
	/**
	 * Subclasses override this to determine where the reports are written
	 */
	File getReportDir() {
		browser.config.reportsDir
	}
	
	static private getTestReporter(test) {
		def key = test.class.name
		if (!GEB_REPORTING_TEST_REPORTERS.containsKey(key)) {
			GEB_REPORTING_TEST_REPORTERS[key] = test.createReporter()
		}
		GEB_REPORTING_TEST_REPORTERS[key]
	}
	
	static private getNextTestCounterValue(test) {
		def key = test.class.name
		def value = GEB_REPORTING_TEST_COUNTERS[key] ?: 0
		GEB_REPORTING_TEST_COUNTERS[key] = ++value
		value
	}
}